package com.subtitlor.dao;

import java.util.ArrayList;

import com.subtitlor.beans.Subtitle;
import com.subtitlor.utilities.SubtitlesHandler;

public interface TablesDao {

	void createTable (String name);
	void saveFileSubtitle(SubtitlesHandler subtitles,String nomFichier);
	void updateFileSubtitle(String traduction,String nomFichier,int ligne);
	ArrayList<Subtitle> getSubtitlesBdd(String nomFichier);
}
