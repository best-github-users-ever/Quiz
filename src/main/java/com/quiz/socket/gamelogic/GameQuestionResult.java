package com.quiz.socket.gamelogic;

import com.quiz.model.Question;

public class GameQuestionResult {
	private String messageName;
	private int questionNumber;
	private int totalNumberOfQuestions;
	private  int questionId = 0;
	private  int topicId = 0;
	private  String question = null;
	private  String option1 = null;
	private  String option2 = null;
	private  String option3 = null;
	private  String option4 = null;
    
    public GameQuestionResult(String messageName, int questionNumber, int totalNumberOfQuestions, Question question) {
    	this.messageName = messageName;
    	this.totalNumberOfQuestions = totalNumberOfQuestions;
    	this.questionNumber = questionNumber;
        this.questionId = question.getQuestionId();
        this.topicId = question.getTopicId();
        this.question = question.getQuestion();
        this.option1 = question.getOption1();
        this.option2 = question.getOption2();
        this.option3 = question.getOption3();
        this.option4 = question.getOption4();
    }

	public String getMessageName() {
		return messageName;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public int getTotalNumberOfQuestions() {
		return totalNumberOfQuestions;
	}

	public int getQuestionId() {
		return questionId;
	}

	public int getTopicId() {
		return topicId;
	}

	public String getQuestion() {
		return question;
	}

	public String getOption1() {
		return option1;
	}

	public String getOption2() {
		return option2;
	}

	public String getOption3() {
		return option3;
	}

	public String getOption4() {
		return option4;
	}

	@Override
	public String toString() {
		return "GameQuestionResult [messageName=" + messageName
				+ ", questionNumber=" + questionNumber
				+ ", totalNumberOfQuestions=" + totalNumberOfQuestions
				+ ", questionId=" + questionId + ", topicId=" + topicId
				+ ", question=" + question + ", option1=" + option1
				+ ", option2=" + option2 + ", option3=" + option3
				+ ", option4=" + option4 + "]";
	}

	
} 