package com.quiz.socket.gamelogic;

import java.io.Serializable;
import java.util.List;

public class ChatInput implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -960845571820192242L;
	
	private String msgType;
    private int gameId;
    private String userId;
    private String jsessionId;
    private String chatMessage;
    private List<String> recipients;

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
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getChatMessage() {
		return chatMessage;
	}
	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}
	public List<String> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}
	@Override
	public String toString() {
		return "ChatInput [msgType=" + msgType + ", gameId=" + gameId
				+ ", userId=" + userId + ", jsessionId=" + jsessionId
				+ ", chatMessage=" + chatMessage + ", recipients=" + recipients
				+ "]";
	}
	
	
		
} 