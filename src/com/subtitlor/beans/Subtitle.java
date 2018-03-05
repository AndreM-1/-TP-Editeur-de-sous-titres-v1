package com.subtitlor.beans;

public class Subtitle {
	private int numeroSousTitre = 0;
	private String sousTitreOriginal = "";
	private String sousTitreTraduit = "";
	private String debutSequence = "";
	private String finSequence = "";

	public int getNumeroSousTitre() {
		return numeroSousTitre;
	}

	public void setNumeroSousTitre(int numeroSousTitre) {
		this.numeroSousTitre = numeroSousTitre;
	}

	public String getSousTitreOriginal() {
		return sousTitreOriginal;
	}

	public void setSousTitreOriginal(String sousTitreOriginal) {
		this.sousTitreOriginal = sousTitreOriginal;
	}

	public String getSousTitreTraduit() {
		return sousTitreTraduit;
	}

	public void setSousTitreTraduit(String sousTitreTraduit) {
		this.sousTitreTraduit = sousTitreTraduit;
	}

	public String getDebutSequence() {
		return debutSequence;
	}

	public void setDebutSequence(String debutSequence) {
		this.debutSequence = debutSequence;
	}

	public String getFinSequence() {
		return finSequence;
	}

	public void setFinSequence(String finSequence) {
		this.finSequence = finSequence;
	}
	
	public String toString() {
		String affichageSousTitre="";
		affichageSousTitre=numeroSousTitre+"\t\t"+ sousTitreOriginal+"\t\t"+sousTitreTraduit+"\t\t"+debutSequence+"\t\t"+finSequence;
		return affichageSousTitre;	
	}

}
