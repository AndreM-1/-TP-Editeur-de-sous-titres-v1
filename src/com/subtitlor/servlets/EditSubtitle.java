package com.subtitlor.servlets;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.TablesDao;
import com.subtitlor.utilities.FilesHandler;
import com.subtitlor.utilities.SubtitlesHandler;

@WebServlet("/EditSubtitle")
public class EditSubtitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String FILE_NAME = "/WEB-INF/password_presentation.srt";
	private static boolean fichierUploade=false;
	private TablesDao tablesDao;
	private SubtitlesHandler subtitles;
	private String nomFichier="";
	private static final String CHEMIN_FICHIERS = "D:/Programmation_D�veloppez_des_sites_web_avec_Java_EE/Enregistrer_dans_une_base_de_donn�es/Activite_Editeur_de_sous_titres_AM/fichiers/";
	private boolean boutonInactif=true;
	
	public void init() throws ServletException{
		//R�cup�ration des param�tres relatifs � la connexion � la base de donn�es
		System.out.println("Initialisation servlet");
		DaoFactory daoFactory=DaoFactory.getInstance();
		this.tablesDao=daoFactory.getTablesDao();	
		//subtitles=new SubtitlesHandler(getServletContext().getRealPath(FILE_NAME));
		//System.out.println(getServletContext().getRealPath(FILE_NAME));
		//nomFichier=getServletContext().getRealPath(FILE_NAME).split(Pattern.quote(File.separator))[getServletContext().getRealPath(FILE_NAME).split(Pattern.quote(File.separator)).length-1];
		//nomFichier=nomFichier.substring(0, nomFichier.length()-4);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//this.tablesDao.saveFileSubtitle(subtitles,nomFichier);
		//request.setAttribute("subtitles", tablesDao.getSubtitlesBdd(nomFichier));
		request.setAttribute("boutonInactif",boutonInactif);
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");	

		System.out.println(request.getParameter("upload"));
		System.out.println(request.getParameter("saveDB"));

		//Gestion du fichier � uploader
		String description="";
		String cheminComplet="";
		FilesHandler file=new FilesHandler();
		description=request.getParameter("description");
		request.setAttribute("description", description);
		Part part=request.getPart("fileToUpload");
		String fileToUploadName=file.getFileName(part);

		if ((fileToUploadName != null && !fileToUploadName.isEmpty())) {
			fichierUploade=true;
			boutonInactif=false;
			request.setAttribute("boutonInactif",boutonInactif);
			//Permet de r�cup�rer le nom de la balise, ici "fileToUpload"
			String nomChamp = part.getName();

			// Correctif d'un bug li� au fonctionnement d'Internet Explorer
			fileToUploadName = fileToUploadName.substring(fileToUploadName.lastIndexOf('/') + 1)
					.substring(fileToUploadName.lastIndexOf('\\') + 1);

			//Ecriture du fichier sur le disque et r�cup�ration du chemin complet
			cheminComplet=file.uploadFile(part, fileToUploadName, CHEMIN_FICHIERS);  
			System.out.println("Chemin complet : "+cheminComplet);

			request.setAttribute(nomChamp,fileToUploadName);
			System.out.println("fileToUploadName :"+fileToUploadName);
			nomFichier=fileToUploadName.substring(0, fileToUploadName.length()-4);
			subtitles=new SubtitlesHandler(cheminComplet);
			this.tablesDao.saveFileSubtitle(subtitles,nomFichier);
			System.out.println("Nom du fichier : "+nomFichier);

		}
		//Mise � jour de la base de donn�es en fonction de la traduction des sous-titres.
		if(request.getParameter("saveDB")!=null&&boutonInactif==false){
			String zoneTexte ="";
			String traduction="";
			for (int i=0;i<subtitles.getNbSsTitres();i++) {	
				zoneTexte ="line"+ i;
				traduction=request.getParameter(zoneTexte);
				if(traduction!=null&&!traduction.isEmpty()) {
					this.tablesDao.updateFileSubtitle(traduction,nomFichier,i);
				}
			}
		}

		request.setAttribute("subtitles", tablesDao.getSubtitlesBdd(nomFichier));

		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}
}
