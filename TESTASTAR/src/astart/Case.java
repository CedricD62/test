package astart;

public class Case 
{
	
	public int i, j; // coordonnées dans la matrice
	public int heuristique; // calculé avec l'algorithme de manathan (case[i] - caseFin[i]) + (case[j] - caseFin[j])
	public int coutTotal; // cout heuristique + cout de déplacement
	public Case origine; // case avant déplacement
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
 