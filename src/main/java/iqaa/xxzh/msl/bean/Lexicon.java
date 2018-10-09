package iqaa.xxzh.msl.bean;

import java.io.Serializable;

public class Lexicon implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -665365217178503049L;
	private Long id;
	private String word;
	// 出现这个词语的文档篇数
	private int times;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	@Override
	public String toString() {
		return "Lexicon [id=" + id + ", word=" + word + ", times=" + times + "]";
	}
	

}
