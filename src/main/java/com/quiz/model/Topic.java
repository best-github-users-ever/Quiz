package com.quiz.model;

import java.io.Serializable;

public class Topic implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2951718619656031394L;
	
	private int topicId;
	private String name;
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
