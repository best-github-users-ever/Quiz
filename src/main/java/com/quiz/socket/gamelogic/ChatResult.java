package com.quiz.socket.gamelogic;

import com.quiz.model.PlayerList;

public class ChatResult {
	private String messageName;
	private String senderId;
    private String chatMessage;
    private String chatMsgType;

    public ChatResult(String messageName, String senderId, String chatMessage, String chatMsgType) {
    	this.senderId = senderId;
    	this.messageName = messageName;
    	this.chatMessage = chatMessage;
    	this.chatMsgType = chatMsgType;
    }

	public String getMessageName() {
		return messageName;
	}

	public String getSenderId() {
		return senderId;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public String getChatMsgType() {
		return chatMsgType;
	}
	
} 