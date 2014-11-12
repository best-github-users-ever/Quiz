package com.quiz.socket.gamelogic;

import java.io.Serializable;

public class JoinInput implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3392343763726837602L;
	
	private int gameId;
    private String userId;
    private String jsessionId;

	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getJsessionId() {
		return jsessionId;
	}
	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
} 