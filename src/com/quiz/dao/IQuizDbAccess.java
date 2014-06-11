package com.quiz.dao;

import com.quiz.model.Question;
import com.quiz.model.User;

public interface IQuizDbAccess {
	
	void addUser(User u);

	User getUser(User user);

	Question getQuestion(int topicId);

}
