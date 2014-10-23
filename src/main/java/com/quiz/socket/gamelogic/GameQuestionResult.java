package com.quiz.socket.gamelogic;

import com.quiz.model.Question;

public class GameQuestionResult {
	private String messageName;
	private int questionNumber;
	private int totalNumberOfQuestions;
	private Question question;
    
    public GameQuestionResult(String messageName, int questionNumber, int totalNumberOfQuestions, Question question) {
    	this.messageName = messageName;
    	this.totalNumberOfQuestions = totalNumberOfQuestions;
    	this.questionNumber = questionNumber;
    	this.question = question;
        this.question.setAnswerIdx(99);
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
	
	

	public Question getQuestion() {
		return question;
	}

	@Override
	public String toString() {
		return "GameQuestionResult [messageName=" + messageName
				+ ", questionNumber=" + questionNumber
				+ ", totalNumberOfQuestions=" + totalNumberOfQuestions
				+ ", question=" + question + "]";
	}


	
} 