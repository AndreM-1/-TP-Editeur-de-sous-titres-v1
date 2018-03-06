package com.subtitlor.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.subtitlor.beans.Subtitle;

public class SubtitlesHandler {
	private ArrayList<String> originalSubtitles = null;
	private ArrayList<String> translatedSubtitles = null;
	private ArrayList<Subtitle> subtitles=null;
	private int nbSsTitres;
	
	public SubtitlesHandler(String fileName) {
		originalSubtitles = new ArrayList<String>();
		translatedSubtitles = new ArrayList<String>();
		BufferedReader br;
		try {
			//Il faut bien procéder ainsi pour gérer l'encodage.
			FileInputStream fis = new FileInputStream(fileName);
	        InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				originalSubtitles.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Subtitle> getSubtitles(){
		subtitles=new ArrayList<Subtitle>();
		Subtitle subtitle=new Subtitle();
		String ssTitreOri="";
		boolean ajoutSousTitreOri=false;
		nbSsTitres=0;
		
		for(String str:originalSubtitles) {
	
			if(isNumeric(str)) {
				subtitle.setNumeroSousTitre(Integer.valueOf(str));
				nbSsTitres++;
			}
			else if(str.contains(" --> ")) {
				subtitle.setDebutSequence(str.split(" --> ")[0]);
				subtitle.setFinSequence(str.split(" --> ")[1]);
			}
			else if (!isNumeric(str)&&!str.contains(" --> ")&&!str.isEmpty()) {
				ssTitreOri+=str;
				ajoutSousTitreOri=true;
			}
			else {
				if(ajoutSousTitreOri) {
					subtitle.setSousTitreOriginal(ssTitreOri);
					ajoutSousTitreOri=false;
					ssTitreOri="";
					subtitles.add(subtitle);
					subtitle=new Subtitle();
				}
			}	
		}
		subtitle.setSousTitreOriginal(ssTitreOri);
		ajoutSousTitreOri=false;
		ssTitreOri="";
		subtitles.add(subtitle);
		return subtitles;
	}

	
	public String toString() {
		String affichageFichierSousTitre="";
		for(Subtitle subtitle: subtitles) {
			affichageFichierSousTitre+=subtitle.toString()+"\n";
		}
		return affichageFichierSousTitre;	
	}
	
	private boolean isNumeric (String str){
		try {
			Integer.valueOf(str);
		}
		catch (NumberFormatException e) {
			return false ;
		}
		return true ;

	}

	public ArrayList<String> getOriginalSubtitles() {
		return originalSubtitles;
	}
	
	public int getNbSsTitres() {
		return nbSsTitres;
	}
	
	
	public ArrayList<String> getTranslatedSubtitles() {
		return translatedSubtitles;
	}
}