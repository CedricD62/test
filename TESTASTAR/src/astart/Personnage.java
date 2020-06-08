package astart;

public class Personnage 
{
	Integer niveau = 0;
	int experience;
	int experienceRecquis;
	int pointExperience;
	int experienceRestant;	
	String nom;
	String villeDepart;
	
	public Personnage () 
	{
		nom = "";
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String pNom) {
		this.nom = pNom;
	}
	
	public void afficher() {
		System.out.print("nom :"+nom+", niveau :"+niveau+"experience :"+experience);
	}
	

}