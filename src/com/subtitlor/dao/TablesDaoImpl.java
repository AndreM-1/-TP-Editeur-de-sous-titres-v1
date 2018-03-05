package com.subtitlor.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import com.subtitlor.beans.Subtitle;
import com.subtitlor.utilities.SubtitlesHandler;

public class TablesDaoImpl implements TablesDao {
	private DaoFactory daoFactory;

	public TablesDaoImpl(DaoFactory daoFactory) {
		this.daoFactory=daoFactory;

	}

	@Override
	public void createTable(String nomFichier) {
		Connection connexion=null;
		Statement statement=null;
		ResultSet resultat=null;
		String strCreationTable="";
		strCreationTable="CREATE TABLE IF NOT EXISTS "+nomFichier+"("
				+"numero_sous_titre INTEGER NOT NULL,"
				+"sous_titre_original VARCHAR(500) NOT NULL,"
				+"sous_titre_traduit VARCHAR(500),"
				+"debut_sequence VARCHAR(15) NOT NULL,"
				+"fin_sequence VARCHAR(15) NOT NULL,"
				+"CONSTRAINT password_presentation_pk PRIMARY KEY (numero_sous_titre));";

		try {
			connexion=daoFactory.getConnection();
			statement=connexion.createStatement();
			resultat=statement.executeQuery(strCreationTable);

		} catch (SQLException e) {
			e.getMessage();
		}
		finally {
			if(resultat!=null)
				try {
					resultat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(statement!=null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(connexion!=null)
				try {
					connexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public void saveFileSubtitle(SubtitlesHandler subtitles, String nomFichier) {
		createTable(nomFichier);
		Connection connexion=null;
		PreparedStatement preparedStatement=null;
		System.out.println("Appel de la méthode saveFileSubtitle pour sauvegarder dans la BDD !!! ");
		try {
			connexion=daoFactory.getConnection();
			preparedStatement=connexion.prepareStatement("INSERT INTO "+nomFichier+" (numero_sous_titre,sous_titre_original,debut_sequence,fin_sequence)"
					+ " VALUES (?,?,?,?);");

			for(Subtitle subtitle: subtitles.getSubtitles()) {
				preparedStatement.setInt(1, subtitle.getNumeroSousTitre());
				preparedStatement.setString(2, subtitle.getSousTitreOriginal());
				preparedStatement.setString(3, subtitle.getDebutSequence());
				preparedStatement.setString(4, subtitle.getFinSequence());
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(preparedStatement!=null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {	
					e.printStackTrace();
				}
			if(connexion!=null)
				try {
					connexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public void updateFileSubtitle(String traduction,String nomFichier,int ligne) {
		Connection connexion=null;
		PreparedStatement preparedStatement=null;
		System.out.println("Appel de la méthode updateFileSubtitle pour mettre à jour les sous titres dans la BDD !!! ");
		try {
			connexion=daoFactory.getConnection();
			preparedStatement=connexion.prepareStatement("UPDATE "+nomFichier+" SET sous_titre_traduit=? WHERE numero_sous_titre=?");
			preparedStatement.setString(1, traduction);
			preparedStatement.setInt(2, ligne+1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(preparedStatement!=null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {	
					e.printStackTrace();
				}
			if(connexion!=null)
				try {
					connexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}	 
	
	public ArrayList<Subtitle> getSubtitlesBdd(String nomFichier){
		ArrayList <Subtitle> subtitlesBdd =new ArrayList<Subtitle>();
		Connection connexion=null;
		Statement statement=null;
		ResultSet resultat=null;
		try {
			connexion=daoFactory.getConnection();
			statement=connexion.createStatement();
			resultat=statement.executeQuery("SELECT * FROM "+nomFichier+" ORDER BY numero_sous_titre;");
			while(resultat.next()) {
				Subtitle subtitle=new Subtitle();
				subtitle.setNumeroSousTitre(resultat.getInt("numero_sous_titre"));
				subtitle.setSousTitreOriginal(resultat.getString("sous_titre_original"));
				subtitle.setSousTitreTraduit(resultat.getString("sous_titre_traduit"));
				subtitle.setDebutSequence(resultat.getString("debut_sequence"));
				subtitle.setFinSequence(resultat.getString("fin_sequence"));
				subtitlesBdd.add(subtitle);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			if(resultat!=null)
				try {
					resultat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(statement!=null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(connexion!=null)
				try {
					connexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return subtitlesBdd;	
	}
}
