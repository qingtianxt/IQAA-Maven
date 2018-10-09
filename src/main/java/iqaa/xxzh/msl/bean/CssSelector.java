package iqaa.xxzh.msl.bean;

public class CssSelector {
	private Long id;
	private String name;
	private String themeSelector;
	private String questionSelector;
	private String answerSelector;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getThemeSelector() {
		return themeSelector;
	}
	public void setThemeSelector(String themeSelector) {
		this.themeSelector = themeSelector;
	}
	public String getQuestionSelector() {
		return questionSelector;
	}
	public void setQuestionSelector(String questionSelector) {
		this.questionSelector = questionSelector;
	}
	public String getAnswerSelector() {
		return answerSelector;
	}
	public void setAnswerSelector(String answerSelector) {
		this.answerSelector = answerSelector;
	}
	@Override
	public String toString() {
		return "CssSelector [id=" + id + ", name=" + name + ", themeSelector=" + themeSelector + ", questionSelector="
				+ questionSelector + ", answerSelector=" + answerSelector + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
