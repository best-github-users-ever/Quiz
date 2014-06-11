package com.quiz.model;

import java.io.Serializable;

public class Question implements Serializable {
	
	private static final long serialVersionUID = 4035109883436279265L;
	
	private  int questionId = 0;
	private  int topicId = 0;
	private  String question = null;
	private  String option1 = null;
	private  String option2 = null;
	private  String option3 = null;
	private  String option4 = null;
	private  int answerIdx = 0;
	
	public Question(){
		
	}
	
	
	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public String getOption1() {
		return option1;
	}
	public void setOption1(String option1) {
		this.option1 = option1;
	}
	public String getOption2() {
		return option2;
	}
	public void setOption2(String option2) {
		this.option2 = option2;
	}
	public String getOption3() {
		return option3;
	}
	public void setOption3(String option3) {
		this.option3 = option3;
	}
	public String getOption4() {
		return option4;
	}
	public void setOption4(String option4) {
		this.option4 = option4;
	}
	public int getAnswerIdx() {
		return answerIdx;
	}
	public void setAnswerIdx(int answerIdx) {
		this.answerIdx = answerIdx;
	}


	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", topicId=" + topicId
				+ ", question=" + question + ", option1=" + option1
				+ ", option2=" + option2 + ", option3=" + option3
				+ ", option4=" + option4 + ", answerIdx=" + answerIdx + "]";
	}
	
		

}
