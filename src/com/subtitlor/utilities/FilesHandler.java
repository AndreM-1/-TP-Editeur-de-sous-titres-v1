package com.subtitlor.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.Part;

import com.subtitlor.beans.Subtitle;

public class FilesHandler {
	private static final int TAILLE_TAMPON = 10240;

	public String getFileName(Part part) {
		for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
			if (contentDisposition.trim().startsWith("filename")) {
				return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	public String uploadFile(Part part, String nomFichier, String chemin) throws IOException {
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;

		try {
			entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
			sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);

			byte[] tampon = new byte[TAILLE_TAMPON];
			int longueur;
			while ((longueur = entree.read(tampon)) > 0) {
				sortie.write(tampon, 0, longueur);
			}
		} finally {
			try {
				sortie.close();
			} catch (IOException ignore) {
			}
			try {
				entree.close();
			} catch (IOException ignore) {
			}
		}
		return chemin + nomFichier.toLowerCase();
	}
	
	public void exportSubtitlesBdd(ArrayList<Subtitle> subtitlesBdd, String cheminComplet) {
		PrintWriter sortie=null; 
		try {
				sortie = new PrintWriter(new BufferedWriter(new FileWriter(cheminComplet)),true);
			for(Subtitle subtitle:subtitlesBdd) {
				if(subtitle.getSousTitreTraduit()!=null) {
					sortie.println(subtitle.getNumeroSousTitre());
					sortie.println(subtitle.getDebutSequence()+" --> "+subtitle.getFinSequence());
					sortie.println(subtitle.getSousTitreTraduit());
					sortie.println("");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally {
			 sortie.close();
		 }
		 
	}
	
}
