package iqaa.xxzh.msl.dao;

import java.util.List;

import iqaa.xxzh.msl.bean.Lexicon;

public interface LexiconDao extends Dao<Lexicon>{
	List<String> getLexicons();
	
	List<Lexicon> getWords();
}
