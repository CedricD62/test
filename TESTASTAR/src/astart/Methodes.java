package astart;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class Methodes {
	
	private Scanner sc = new Scanner(System.in);
	public static final int COUT_DEP_DIAG = 14; 
	public static final int COUT_DEP_H_OU_V = 10; 
	private Case [][] matrice;
	private PriorityQueue<Case> openList;
	private PriorityQueue<Case> openListTampon;
	private PriorityQueue<Case> cheminEndroit;
	private ArrayList<Case> closeList = new  ArrayList<Case>();
	private ArrayList<Case> closeListTampon = new  ArrayList<Case>();
	private int debutI , debutJ;
	private int finI, finJ;
	private static int cpt = 0;
	private int departTamponI, departTamponJ;
	private int finTamponI, finTamponJ;
	private boolean cheminPossible = true;
	private boolean astarFin = false;
	Personnage personnage = new Personnage();;
	
	public Methodes(int ligne, int colonne, int iDepart, int jDepart, int iArrive, int jArrive, int[][] obstacles, int [] niveau) {
		
		matrice = new Case[ligne][colonne];
		openList = new PriorityQueue<Case>((Case c1, Case c2) -> {
			if(c1.coutTotal < c2.coutTotal) {
				return -1;
			}else if (c1.coutTotal > c2.coutTotal) {
				return 1;
			}else {
				return 0;
			}
		});
		openListTampon = new PriorityQueue<Case>((Case c1, Case c2) -> {
			if(c1.coutTotal < c2.coutTotal) {
				return -1;
			}else if (c1.coutTotal > c2.coutTotal) {
				return 1;
			}else {
				return 0;
			}
		});
		
		cheminEndroit = new PriorityQueue<Case>((Case c1, Case c2) -> {
			if(c1.coutTotal < c2.coutTotal) {
				return -1;
			}else if (c1.coutTotal > c2.coutTotal) {
				return 1;
			}else {
				return 0;
			}
		});
		
		caseDepart(iDepart, jDepart);
		departTampon(iDepart, jDepart);
		caseFin(iArrive, jArrive);
		
		// calcul du coût heursitique
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[i].length; j++) {
				matrice[i][j] = new Case(i,j);
				matrice[i][j].heuristique = Math.abs(i - iArrive) + Math.abs(j - jArrive);
				matrice[i][j].niveauMin = niveau[cpt];
				cpt++;
				matrice[i][j].niveauMax = niveau[cpt];
				cpt++;
			}
		}
		
		matrice[debutI][debutJ].coutTotal = 0;
		
		for (int i = 0; i < obstacles.length; i++) {
				ajoutObstacles(obstacles[i][0], obstacles[i][1]);
		}
		
	}
	
	public void ajoutObstacles(int i, int j) {
		matrice[i][j] = null;
	}
	
	public void caseDepart(int i, int j) {
		debutI = i;
		debutJ = j;		
	}
	
	public void departTampon(int i, int j) {
		departTamponI = i;
		departTamponJ = j;
	}
	
	public void finTampon(int i, int j) {
		finTamponI = i;
		finTamponJ = j;
	}
	
	public void caseFin(int i, int j) {
		finI = i;
		finJ = j;	
	}
	
	public void niveauPersonnage(Personnage personnage, Case caseActuelle) {
		
		if(personnage.niveau.equals(caseActuelle.niveauMin) || personnage.niveau.equals(0)) {
			personnage.niveau = caseActuelle.niveauMax;
		}else if (personnage.niveau > caseActuelle.niveauMin && personnage.niveau < caseActuelle.niveauMax) {
			personnage.niveau = caseActuelle.niveauMax;
		}else if (personnage.niveau > caseActuelle.niveauMax || personnage.niveau < caseActuelle.niveauMin) {
			return;
		}
	}
	
	
	public void rechercheChemin() 
	{
		Case caseTampon;
		for(int i = 0; i < matrice.length; i++) {
			for(int j = 0; j < matrice[i].length; j++) {
				if(matrice[i][j] != null) {
					caseTampon = matrice[i][j];
					if(caseTampon.niveauMax.equals(personnage.niveau + 5) && caseTampon.impasse != true && caseTampon != null) {
						finTamponI = matrice[i][j].i;
						finTamponJ = matrice[i][j].i;
						astarTampon();
						if (cheminPossible == true) {
							copieClosedList();
							departTamponI = finTamponI;
							departTamponJ = finTamponJ;
							openListTampon.clear();
							closeListTampon.clear();
						}
						if(cheminPossible == false) {
							matrice[i][j].impasse = true;
							cheminPossible = true;
						}
						if(astarFin == true) {
							copieClosedList();
							break;
						}
					}
				}
			}
		}
		
	}
	
	public void copieClosedList() {
		Case caseTampon;
		for(int i = 0; i < closeListTampon.size(); i++) {
			caseTampon = closeListTampon.get(i);
			closeList.add(caseTampon);
		}
	}
	
	public void astarTampon() {
		
		if(personnage.niveau.equals(0)) {
			openListTampon.add(matrice[departTamponI][departTamponJ]);
		}else {
			openListTampon.add(matrice[departTamponI][departTamponJ]);
		}
		
		
		Case caseActuelle;

		while(openListTampon != null) {
			caseActuelle = openListTampon.poll();
		
			if(caseActuelle == null) {
				break;
			}
			
			niveauPersonnage(personnage, caseActuelle);
			
			closeListTampon.add(caseActuelle);
			
			if(caseActuelle.equals(matrice[finTamponI][finTamponJ])) {
				break;
			}
			
			Case voisin;
			
			if(caseActuelle.i - 1 >= 0 ){
				voisin = matrice[caseActuelle.i-1][caseActuelle.j];
				if(voisin != null) {
					if(personnage.niveau.equals(voisin.niveauMin)) {
					//if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
					calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_H_OU_V);
					}
				}
				if(caseActuelle.j - 1 >= 0){
					
					voisin = matrice[caseActuelle.i-1][caseActuelle.j-1];
					if(voisin != null) {
						if(personnage.niveau.equals(voisin.niveauMin)) {
					//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
						calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_DIAG);
						}
					}
				}
				if(caseActuelle.j + 1 < matrice[0].length){
					
					voisin = matrice[caseActuelle.i-1][caseActuelle.j+1];
					if(voisin != null) {
						if(personnage.niveau.equals(voisin.niveauMin)) {
					//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
						calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_DIAG);
						}
					}
				}
			}
			if(caseActuelle.i + 1 < matrice.length){
				
				voisin = matrice[caseActuelle.i+1][caseActuelle.j];
				if(voisin != null) {
					if(personnage.niveau.equals(voisin.niveauMin)) {
				//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
					calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_H_OU_V);
					}
				}
				
				if(caseActuelle.j - 1 >= 0){
					
					voisin = matrice[caseActuelle.i+1][caseActuelle.j-1];
					if(voisin != null) {
						if(personnage.niveau.equals(voisin.niveauMin)) {
					//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
						calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_DIAG);
						}
					}
					
				}
				if(caseActuelle.j + 1 < matrice[0].length){
					
					voisin = matrice[caseActuelle.i+1][caseActuelle.j+1];
					if(voisin != null) {
						if(personnage.niveau.equals(voisin.niveauMin)) {
				//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
						calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_DIAG);
						}
					}
				}
			}
			if (caseActuelle.j - 1 >= 0){
				
				voisin = matrice[caseActuelle.i][caseActuelle.j-1];
				if(voisin != null) {
					if(personnage.niveau.equals(voisin.niveauMin)) {
				//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
					calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_H_OU_V);
					}
				}
				
			}
			if (caseActuelle.j + 1 < matrice[0].length){
				
				voisin = matrice[caseActuelle.i][caseActuelle.j+1];
				if(voisin != null) {
					if(personnage.niveau.equals(voisin.niveauMin)) {
			//		if(caseActuelle.niveauMax.equals(voisin.niveauMin)){
					calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_H_OU_V);
					}
				}
			}
		}
		if(openListTampon == null) {
			
			if(closeListTampon.contains(matrice[finTamponI][finTamponJ])) {
				if( matrice[finTamponI][finTamponJ] != matrice[finI][finJ]) {
					cheminPossible = true;
					return;
				}else if (matrice[finTamponI][finTamponJ] == matrice[finI][finJ]) {
					astarFin = true;
					return;
				}
			}
			else  {
				cheminPossible = false;
			}
		}
	}
	
	
	
	/*public void algorithme() {
		
		
		
		openList.add(matrice[debutI][debutJ]);
		
		Case caseActuelle;

		while(openList != null) {
			caseActuelle = openList.poll();
		
		niveauPersonnage(personnage, caseActuelle);
		
			if(caseActuelle == null) {
				break;
			}
			
			closeList.add(caseActuelle);
			
			if(caseActuelle.equals(matrice[finI][finJ])) {
				break;
			}
			
			Case voisin;
			
			if(caseActuelle.i - 1 >= 0 ){
				voisin = matrice[caseActuelle.i-1][caseActuelle.j];
				if(voisin != null) {
					if(personnage.niveau.equals(voisin.niveauMin)) {
					//if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
					calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_H_OU_V);
					}
				}
				if(caseActuelle.j - 1 >= 0){
					
					voisin = matrice[caseActuelle.i-1][caseActuelle.j-1];
					if(voisin != null) {
						if(personnage.niveau.equals(voisin.niveauMin)) {
					//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
						calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_DIAG);
						}
					}
				}
				if(caseActuelle.j + 1 < matrice[0].length){
					
					voisin = matrice[caseActuelle.i-1][caseActuelle.j+1];
					if(voisin != null) {
						if(personnage.niveau.equals(voisin.niveauMin)) {
					//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
						calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_DIAG);
						}
					}
				}
			}
			if(caseActuelle.i + 1 < matrice.length){
				
				voisin = matrice[caseActuelle.i+1][caseActuelle.j];
				if(voisin != null) {
					if(personnage.niveau.equals(voisin.niveauMin)) {
				//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
					calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_H_OU_V);
					}
				}
				
				if(caseActuelle.j - 1 >= 0){
					
					voisin = matrice[caseActuelle.i+1][caseActuelle.j-1];
					if(voisin != null) {
						if(personnage.niveau.equals(voisin.niveauMin)) {
					//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
						calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_DIAG);
						}
					}
					
				}
				if(caseActuelle.j + 1 < matrice[0].length){
					
					voisin = matrice[caseActuelle.i+1][caseActuelle.j+1];
					if(voisin != null) {
						if(personnage.niveau.equals(voisin.niveauMin)) {
				//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
						calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_DIAG);
						}
					}
				}
			}
			if (caseActuelle.j - 1 >= 0){
				
				voisin = matrice[caseActuelle.i][caseActuelle.j-1];
				if(voisin != null) {
					if(personnage.niveau.equals(voisin.niveauMin)) {
				//	if(caseActuelle.niveauMax.equals(voisin.niveauMin)) {
					calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_H_OU_V);
					}
				}
				
			}
			if (caseActuelle.j + 1 < matrice[0].length){
				
				voisin = matrice[caseActuelle.i][caseActuelle.j+1];
				if(voisin != null) {
					if(personnage.niveau.equals(voisin.niveauMin)) {
			//		if(caseActuelle.niveauMax.equals(voisin.niveauMin)){
					calculCoutVoisin(caseActuelle, voisin, caseActuelle.coutTotal + COUT_DEP_H_OU_V);
					}
				}
			}
		}
	}*/
	
	public void calculCoutVoisin(Case caseActuelle, Case voisin, int coutCaseActuelle) {
		
		boolean voisinPresent = closeListTampon.contains(voisin);
		
		if(voisin == null || voisinPresent == true) {
			return;
		}
		
		int voisinCoutTotal = voisin.heuristique + coutCaseActuelle;
		voisinPresent = openListTampon.contains(voisin);
		
		if(voisinPresent == false || voisin.coutTotal > voisinCoutTotal) {
			voisin.coutTotal = voisinCoutTotal;
			voisin.origine = caseActuelle;
			
			if(voisinPresent == false) {
				openListTampon.add(voisin);
			}
		}
	}
	
	public void affichageMatrice() {
		System.out.println(" ");
		System.out.println("Matrice : ");
		
		for (int i = 0; i < matrice.length; i++) {
			System.out.println(" ");
			for (int j = 0; j < matrice[i].length; j++) {
				if(matrice[i][j] == matrice[debutI][debutJ]) {
					System.out.print("  [DEP]");
				}else if (matrice[i][j] == matrice[finI][finJ]) {
					System.out.print("  [ARR]");
				}else if(matrice[i][j] == null) {
					System.out.print("  [OBS]");
				}else {
					System.out.print("  [ 0 ]");
				}
			}
			System.out.println(" ");
		}
		System.out.println(" ");
	}
	
	public void affichageCoutCase() {
		System.out.println(" ");
		System.out.println("Poids des cases : ");
		
		for (int i = 0; i < matrice.length; i++) {
			System.out.println(" ");
			for (int j = 0; j < matrice[i].length; j++) {
				if(matrice[i][j] == matrice[debutI][debutJ]) {
					System.out.print("  [  "+matrice[i][j].coutTotal+" ]");
				}else if (matrice[i][j] != null) {
					System.out.print("  [ "+matrice[i][j].coutTotal+" ]");
				}else{
					System.out.print("  [ BL ]");
				}
			}
			System.out.println(" ");
		}
		System.out.println(" ");
	}
	
	public void affichageChemin() {
		if(closeList.contains(matrice[finI][finJ])) {
			System.out.println(" ");
			System.out.println("chemin arrivé -> départ : ");
			System.out.println(" ");
			Case cheminInverse = matrice[finI][finJ];
			System.out.print(cheminInverse);
		
			while(cheminInverse.origine != null) {
				cheminEndroit.add(cheminInverse);
				System.out.print(cheminInverse.origine);
				cheminInverse = matrice[cheminInverse.origine.i][cheminInverse.origine.j];
			}
			
			System.out.println(" ");
			System.out.println(" ");
			System.out.println("chemin départ -> arrivé : ");
			System.out.println(" ");
			System.out.print(matrice[debutI][debutJ]);
			
			for(Case chemin : cheminEndroit) {
				System.out.print(chemin);
			}

			System.out.println(" ");
			System.out.println(" ");
			for (int i = 0; i < matrice.length; i++) {
				System.out.println(" ");
				for (int j = 0; j < matrice[i].length; j++) {
					if(matrice[i][j] == matrice[debutI][debutJ]) {
						System.out.print("  [DEP]");
					}else if (matrice[i][j] == matrice[finI][finJ]) {
						System.out.print("  [ARR]");
					}else if(matrice[i][j] == null) {
						System.out.print("  [OBS]");
					}else {
						if(cheminEndroit.contains(matrice[i][j]))		
							System.out.print("  [ X ]");
						else {
							System.out.print("  [ 0 ]");
						}
					}
				}
				System.out.println(" ");
			}	
		System.out.println(" ");
		}else {
			System.out.println(" Pas de chemin possible jusqu'à l'arrivée");
		}	
	sc.close();
	}
}
	