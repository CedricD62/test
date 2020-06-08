package astart;

public class Case 
{
	
	public int i, j; // coordonn�es dans la matrice
	public int heuristique; // calcul� avec l'algorithme de manathan (case[i] - caseFin[i]) + (case[j] - caseFin[j])
	public int coutTotal; // cout heuristique + cout de d�placement
	public Case origine; // case avant d�placement
	public Integer niveauMin;
	public Integer niveauMax;
	public String nom; 
	public boolean impasse = false;
	
	public Case(int i, int j) 
	{
		this.i = i;
		this.j = j;
	}

	/*public void afficher() {
		System.out.print("[ Zone : ["+i+" , "+i+"] , "+nom+" , "+"coutTotal :"+coutTotal+", heuristique : "+heuristique+"]");
	}*/
	
	@Override
	public String toString() {
		return "[" + i + "," + j + "]";
	}
}
 