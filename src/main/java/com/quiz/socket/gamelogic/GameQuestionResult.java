package com.quiz.socket.gamelogic;

import com.quiz.model.Question;

public class GameQuestionResult {
	private String messageName;
	private int questionNumber;
	private Question question;
    
    public GameQuestionResult(String messageName, int questionNumber, Question question) {
    	this.messageName = messageName;
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

	public Question getQuestion() {
		return question;
	}

} 