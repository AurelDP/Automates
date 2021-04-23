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
		int nbrLettersInLang;
		int nbrStates;
		int nbrInitialStates;
		int nbrFinalStates;
		int nbrTransitions;
		ArrayList<State> states = new ArrayList<State>();
		
		try {
			FileInputStream file = new FileInputStream("./automates/" + nom);
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				if (scanner.findInLine("a") != null)
					System.out.println("Yes");
				else {
					System.out.println("No");
					break;
				}
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
