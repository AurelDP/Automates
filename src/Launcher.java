import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Launcher {

	public static void main(String[] args) {
		int entry;
		Scanner sc = null;
		boolean run = true;
		
		// We get the name of all the files contained in the "automates" folder
		ArrayList<String> namesTxtFiles = findNamesInFile(new File("./automates"));
				
		if (namesTxtFiles.size() != 0) {
			
			do {
		
				System.out.print("\n\nChoisissez l'automate à traiter parmi la liste suivante :\n");
				
				// We display all the file names and ask the user to choose an automaton from the list
				for (int u = 1; u <= namesTxtFiles.size(); u++)
					System.out.println(u + " - " + namesTxtFiles.get(u - 1));
		
				do {
					System.out.print("\nVotre choix (entrez le numéro de l'automate) : ");
					sc = new Scanner(System.in);
					entry = sc.nextInt();
				} while (entry <= 0 || entry > namesTxtFiles.size());
		
				System.out.println("Automate choisi : " + namesTxtFiles.get(entry - 1) + "\n");
				
				
				// We recover the chosen automaton and we write it in the program memory
				Automaton AF = getAutomatonFromFile(namesTxtFiles.get(entry - 1));
				System.out.println("\n\n--------------------------\n"
						+ "AUTOMATE DE BASE");
				AF.display();
				
				
				Automaton AFDC = null;
				if (AF.isAsynchrone())
					System.out.println("\n\nL'automate est asynchrone et ne pourra pas être traité.");
				else {
					HashMap<Integer, ArrayList<Integer>> links = null;
					
					AFDC = new Automaton(AF);
					if (AF.isDeterminist()) {
						if (!AF.isComplete()) {
							AFDC = new Automaton(AF);
							AFDC.completion();
						}
					} else {
						if (!AF.isComplete()) {
							AFDC = new Automaton(AF);
							AFDC.completion();
							links = AFDC.determinization();
						}
					}
					System.out.println("\n\n--------------------------------------\n"
							+ "AUTOMATE DETERMINISTE COMPLET");
					AFDC.display();
					if (links != null)
						AFDC.displayDeterministOrMinimalistLinks(links);
				}
				
				
				Automaton AFDCM = new Automaton(AFDC);
				System.out.println("\n\n--------------------------\n"
						+ "AUTOMATE PRE-MINIMALISATION");
				AFDCM.display();
				System.out.println("\nAUTOMATE MINIMAL");
				HashMap<Integer, ArrayList<Integer>> links = AFDCM.minimization();
				AFDCM.display();
				AFDCM.displayDeterministOrMinimalistLinks(links);
				
				
				Automaton AFcomp = new Automaton(AFDCM);
				System.out.println("\n\n--------------------------\n"
						+ "AUTOMATE COMPLEMENTAIRE");
				AFcomp.complementaryAutomaton();
				AFcomp.display();
				
				
				
				String[] words = readWords(sc);
				if (words.length != 0) {
					System.out.println("Mots entrés :");
					for (String w : words) {
						if (w.matches("[*]+"))
							System.out.print("Mot vide");
						else
							System.out.print(w);
						if (recognizedWord(AFcomp, w))
							System.out.println(" : Reconnu");
						else
							System.out.println(" : Non reconnu");
					}
				} else {
					System.out.println("Aucun mot n'a été entré...");
				}
				
				
				
				if (AF.isStandard())
					System.out.println("\n\n--------------------------\n"
							+ "AUTOMATE DEJA STANDARD");
				else {
					Automaton AFstd = new Automaton(AF);
					System.out.println("\n\n--------------------------\n"
							+ "AUTOMATE STANDARD");
					AFstd.standardization();
					AFstd.display();
				}				
				
				System.out.print("\n\nQue voulez-vous faire :\n"
						+ "1) Choisir un autre automate\n"
						+ "2) Quitter le programme\n");	
				do {
					System.out.print("\nVotre choix (entrez le numéro du choix) : ");
					sc = new Scanner(System.in);
					entry = sc.nextInt();
				} while (entry < 1 || entry > 2);
				
				if (entry == 2)
					run = false;
			
			} while (run);
			
			sc.close();
			
		} else {
			System.out.println("Le dossier des automates est vide !");
		}
	}

	private static Automaton getAutomatonFromFile(final String nom) {
		// These variables will be used for the constructor of the automaton
		int nbrLettersInLang = 0;
		int nbrStates = 0;
		int nbrInitialStates = 0;
		int nbrFinalStates = 0;
		int nbrTransitions = 0;
		ArrayList<State> states = new ArrayList<State>();
		
		try {
			// We retrieve the contents of the file chosen by the user
			FileInputStream file = new FileInputStream("./automates/" + nom);
			Scanner scanner = new Scanner(file);
			
			int i = 1;
			while (scanner.hasNextLine()) {
				
				Scanner lineScanner = new Scanner(scanner.nextLine());
				
				switch (i) {
					
					// For the first line, we get an integer corresponding to the number of letters of
					// the alphabet composing the recognized language of the automaton
					case 1:
						nbrLettersInLang = lineScanner.nextInt();
						break;
					
					// The second line takes the number of states, then the loop is used 
					// to create states (which are initialized with default values)
					case 2:
						nbrStates = lineScanner.nextInt();
						for (int u = 0; u < nbrStates; u++) {
							states.add(new State(u, false, false, 0, new ArrayList<Transition>()));
						}

						break;
					
					// We get the number of initial states, then we call the "setInitial"
					// method to change the values of states already created
					case 3:
						nbrInitialStates = lineScanner.nextInt();
						while (lineScanner.hasNext()) {
							int num = lineScanner.nextInt();
							for (State s : states) {
								if (s.getName() == num) {
									s.setInitial(true);
									break;
								}
							}
						}
						break;
					
					// Same principle for final states
					case 4:
						nbrFinalStates = lineScanner.nextInt();
						while (lineScanner.hasNext()) {
							int num = lineScanner.nextInt();
							for (State s : states) {
								if (s.getName() == num) {
									s.setFinal(true);
									break;
								}
							}
						}
						break;
					
					case 5:
						nbrTransitions = lineScanner.nextInt();
						break;
					
					// Finally, we recover all the transitions
					default:
						String line = lineScanner.next();
						// Line by line, we divide the content of the line using REGEX rules
						String[] part = line.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
						// The first part of the split line is converted to an int for the name of the pre-transition state
						int name = Integer.parseInt(part[0]);
						// The third part corresponds to the arrival state
						int arrival = Integer.parseInt(part[2]);
						// Then we modify this state to add a transition, using the recovered information
						for (State s : states) {
							if (s.getName() == name) {
								s.incrementNbrTransi();
								s.setTransi(part[1], arrival);
								break;
							}
						}
						break;
				
				}
				
				i ++;
				
				lineScanner.close();
				
			}
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		// With the variables filled in, all that remains is to call the automaton constructor
		return new Automaton(nbrLettersInLang, nbrStates, nbrInitialStates, nbrFinalStates, nbrTransitions, states);
	}

	private static ArrayList<String> findNamesInFile(File file) {
		// We get a list of all the files contained in the folder
		File[] files = file.listFiles();
		ArrayList<String> names = new ArrayList<String>();
		// If the file is a file, then we add it to a list of names
		for (File f : files) {
			if (f.isFile()) {
				names.add(f.getName());
			}
		}
		return names;
	}
	
	private static String[] readWords(Scanner sc) {
		String line;
		String[] words;
		do {
			System.out.print("\nListe des mots à reconnaître (séparés par un espace).\n"
					+ "Le mot vide est noté \"*\".\n"
					+ "Votre liste : ");
			sc.nextLine(); // This line is used to empty the buffer
			line = sc.nextLine();
		} while (line.equals(""));
		words = line.split("\\s+");
		return words;
	}
	
	private static boolean recognizedWord(Automaton a, String word) {
		State currentState = null;
		
		// Recover of the initial state
		for (State s : a.getStates()) {
			if (s.isInitial()) {
				currentState = s;
				break;
			}
		}
		
		// If there is no initial state, then no words can be recognized
		if (currentState == null)
			return false;
		
		// Recognition of void word
		if (word.matches("[*]+") && currentState.isInitial() && currentState.isFinal())
			return true;
		
		// If void symbol is in word, we remove it
		word.replace("*", "");
		
		// Convert word into array of chars
		char[] chars = word.toCharArray();
		
		// Recognition of other words
		for (char c : chars) {
			boolean foundLetter = false;
			for (Transition t : currentState.getTransiList()) {
				if (c == t.getLetter().charAt(0)) {
					foundLetter = true;
					currentState = a.getStateFromName(t.getArrivalStateName());
					break;
				}
			}
			if (!foundLetter)
				return false;
		}
		if (currentState.isFinal())
			return true;
		return false;
	}
	
}
