package com.subtitlor.servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.subtitlor.beans.Subtitle;
import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.TablesDao;
import com.subtitlor.utilities.FilesHandler;
import com.subtitlor.utilities.SubtitlesHandler;

@WebServlet("/EditSubtitle")
public class EditSubtitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String FILE_NAME = "/fichiers/";
	private static String CHEMIN_RELATIF="";
	private boolean fichierUploade=false;
	private boolean boutonInactif=true;
	private TablesDao tablesDao;
	private SubtitlesHandler subtitles;
	private String nomFichier="";
	private String cheminComplet="";
	
	
	public void init() throws ServletException{
		//Récupération des paramètres relatifs à la connexion à la base de données
		System.out.println("Initialisation servlet");
		DaoFactory daoFactory=DaoFactory.getInstance();
		this.tablesDao=daoFactory.getTablesDao();
		CHEMIN_RELATIF=getServletContext().getRealPath(FILE_NAME).replace("WebContent\\", "");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		request.setAttribute("boutonInactif",boutonInactif);
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");	
		ArrayList<Subtitle> subtitlesBdd=new ArrayList<Subtitle>();
		
		//Gestion du fichier à uploader
		String description="";
		FilesHandler file=new FilesHandler();
		description=request.getParameter("description");
		request.setAttribute("description", description);
		Part part=request.getPart("fileToUpload");
		String fileToUploadName=file.getFileName(part);
		if ((fileToUploadName != null && !fileToUploadName.isEmpty())) {
			fichierUploade=true;
			boutonInactif=false;
			request.setAttribute("boutonInactif",boutonInactif);
			//Permet de récupérer le nom de la balise, ici "fileToUpload"
			String nomChamp = part.getName();

			// Correctif d'un bug lié au fonctionnement d'Internet Explorer
			fileToUploadName = fileToUploadName.substring(fileToUploadName.lastIndexOf('/') + 1)
					.substring(fileToUploadName.lastIndexOf('\\') + 1);

			//Ecriture du fichier sur le disque et récupération du chemin complet
			cheminComplet=file.uploadFile(part, fileToUploadName, CHEMIN_RELATIF);  
			
			request.setAttribute(nomChamp,fileToUploadName);
			nomFichier=fileToUploadName.substring(0, fileToUploadName.length()-4).toLowerCase();
			subtitles=new SubtitlesHandler(cheminComplet);
			this.tablesDao.saveFileSubtitle(subtitles,nomFichier);
			request.setAttribute("subtitles", tablesDao.getSubtitlesBdd(nomFichier));

		}
		//Mise à jour de la base de données en fonction de la traduction des sous-titres.
		if((request.getParameter("saveDB")!=null||request.getParameter("exportDB")!=null)&&(request.getParameter("fileToUpload")!=null||fichierUploade==true)){
			String zoneTexte ="";
			String traduction="";
			
			for (int i=0;i<subtitles.getNbSsTitres();i++) {	
				zoneTexte ="line"+ i;
				traduction=request.getParameter(zoneTexte);
				if(traduction!=null&&!traduction.isEmpty()) {
					this.tablesDao.updateFileSubtitle(traduction,nomFichier,i);
				}
			}
			subtitlesBdd=tablesDao.getSubtitlesBdd(nomFichier);
			request.setAttribute("saveDB", request.getParameter("saveDB"));
			request.setAttribute("subtitles", subtitlesBdd);
		}
		
		//Export de la base de données dans un fichier .srt
		if(request.getParameter("exportDB")!=null) {
			FilesHandler exportFile=new FilesHandler();
			cheminComplet=cheminComplet.replace(nomFichier,nomFichier+"_sortie");
			exportFile.exportSubtitlesBdd(subtitlesBdd,cheminComplet);
			request.setAttribute("exportDB", request.getParameter("exportDB"));
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
	}
}
