package com.quiz.socket.gamelogic;

import java.io.Serializable;

import com.quiz.model.Question;

public class GameQuestionResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6873036639072967345L;
	
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