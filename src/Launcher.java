import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Launcher {

	public static void main(String[] args) {
		int entry;
		
		
		/*
		 * METTRE ICI POUR TESTER, NE PAS S'OCCUPER DE TOUT CE QU'IL Y A EN DESSOUS
		 * (Je devrais le finir dans la soirée du 23 (Aurel))
		 */
		
		
		ArrayList<String> namesTxtFiles = findNamesInFile(new File("./automates"));
				
		if (namesTxtFiles.size() != 0) {
		
			System.out.println("Choisissez l'automate à traiter parmi la liste suivante :");
	
			for (int u = 1; u <= namesTxtFiles.size(); u++)
				System.out.println(u + " - " + namesTxtFiles.get(u - 1));
	
			do {
				System.out.println("\nVotre choix (entrez le numéro de l'automate) : ");
				Scanner sc = new Scanner(System.in);
				entry = sc.nextInt();
			} while (entry <= 0 || entry > namesTxtFiles.size());
	
			System.out.println("Automate choisi : " + namesTxtFiles.get(entry - 1));
			
			
	
			Automaton automaton = getAutomatonFromFile(namesTxtFiles.get(entry - 1));
			
			
			
		} else {
			System.out.println("Le dossier des automates est vide !");
		}
	}

	private static Automaton getAutomatonFromFile(final String nom) {
		int nbrLettersInLang = 0;
		int nbrStates = 0;
		int nbrInitialStates = 0;
		int nbrFinalStates = 0;
		int nbrTransitions = 0;
		ArrayList<State> states = new ArrayList<State>();
		
		try {
			FileInputStream file = new FileInputStream("./automates/" + nom);
			Scanner scanner = new Scanner(file);
			
			int i = 1;
			while (scanner.hasNextLine()) {
				
				Scanner lineScanner = new Scanner(scanner.nextLine());
				
				switch (i) {
					
					case 1:
						nbrLettersInLang = lineScanner.nextInt();
						System.out.println("Nombre de lettres : " + nbrLettersInLang);
						break;
					
					case 2:
						nbrStates = lineScanner.nextInt();
						for (int u = 0; u < nbrStates; u++) {
							states.add(new State(u, false, false, 0, new ArrayList<Transition>()));
						}
						System.out.println("Nombre d'états : " + nbrStates);
						System.out.println("Liste des états :");
						for (State s : states) {
							System.out.println(s.getName());
						}
						break;
						
					case 3:
						nbrInitialStates = lineScanner.nextInt();
						System.out.println("Nombre d'états initiaux : " + nbrInitialStates);
						while (lineScanner.hasNext()) {
							int num = lineScanner.nextInt();
							for (State s : states) {
								if (s.getName() == num) {
									s.setInitial(true);
									System.out.println("Etat initial : " + s.getName());
								}
							}
						}
						break;
					
					case 4:
						nbrFinalStates = lineScanner.nextInt();
						System.out.println("Nombre d'états finaux : " + nbrFinalStates);
						while (lineScanner.hasNext()) {
							int num = lineScanner.nextInt();
							for (State s : states) {
								if (s.getName() == num) {
									s.setFinal(true);
									System.out.println("Etat final : " + s.getName());
								}
							}
						}
						break;
					
					case 5:
						nbrTransitions = lineScanner.nextInt();
						System.out.println("Nombre de transitions : " + nbrTransitions);
						System.out.println("Transitions :");
						break;
					
					default:
						String line = lineScanner.next();
						String[] part = line.split("(?=\\d)(?<=\\D)(?=\\d)");
						System.out.println(part[0] + " " + part[1]/* + " " + part[2]*/);
						int name = Integer.parseInt(part[0]);
						int a = Integer.parseInt(part[2]);
						State arrival = null;
						for (State s : states) {
							if (s.getName() == a)
								arrival = s;
						}
						for (State s : states) {
							if (s.getName() == name) {
								s.incrementNbrTransi();
								s.setTransi(part[1], arrival);
								System.out.println(s.getName() + part[1] + arrival);
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
		return new Automaton(nbrLettersInLang, nbrStates, nbrInitialStates, nbrFinalStates, nbrTransitions, states);
	}

	private static ArrayList<String> findNamesInFile(File file) {
		File[] files = file.listFiles();
		ArrayList<String> names = new ArrayList<String>();
		for (File f : files) {
			if (f.isFile()) {
				names.add(f.getName());
			}
		}
		return names;
	}

}
