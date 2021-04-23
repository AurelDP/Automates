import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Lanceur {

	public static void main(String[] args) {
		int saisie;

		ArrayList<String> nomsFichiersTxt = trouverNomsDansFichier(new File("./automates"));
		System.out.println("Choisissez l'automate à traiter parmi la liste suivante :");

		for (int u = 1; u <= nomsFichiersTxt.size(); u++)
			System.out.println(u + " - " + nomsFichiersTxt.get(u - 1));

		do {
			System.out.println("\nVotre choix (entrez le numéro de l'automate) : ");
			Scanner sc = new Scanner(System.in);
			saisie = sc.nextInt();
		} while (saisie <= 0 || saisie > nomsFichiersTxt.size());

		System.out.println("Automate choisi : " + nomsFichiersTxt.get(saisie - 1));

		lireFichier(nomsFichiersTxt.get(saisie - 1));
	}

	private static void lireFichier(final String nom) {
		try {
			FileInputStream fichier = new FileInputStream("./automates/" + nom);
			Scanner scanner = new Scanner(fichier);

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
	}

	private static ArrayList<String> trouverNomsDansFichier(File fichier) {
		File[] fichiers = fichier.listFiles();
		ArrayList<String> noms = new ArrayList<String>();
		for (File f : fichiers) {
			if (f.isFile()) {
				noms.add(f.getName());
			}
		}
		return noms;
	}

}
