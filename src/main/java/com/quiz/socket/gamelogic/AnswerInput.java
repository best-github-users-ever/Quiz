package com.quiz.socket.gamelogic;

import java.io.Serializable;

public class AnswerInput implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6369539465766173337L;
	
	private int gameId;
    private String userId;
    private int questionId;
    private int guess;
    private double answerTime;
    private String jsessionId;
    
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getGuess() {
		return guess;
	}
	public void setGuess(int guess) {
		this.guess = guess;
	}
	public String getJsessionId() {
		return jsessionId;
	}
	
	public void setJsessionId(String jsessionId){
		this.jsessionId = jsessionId;
	}
	public double getAnswerTime() {
		return answerTime;
	}
	public void setAnswerTime(double answerTime) {
		this.answerTime = answerTime;
	}
	
	
	
	
} 