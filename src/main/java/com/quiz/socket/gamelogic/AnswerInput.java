package com.quiz.socket.gamelogic;
public class AnswerInput {
    private int gameId;
    private String userId;
    private int questionId;
    private int guess;
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
	
	
} 