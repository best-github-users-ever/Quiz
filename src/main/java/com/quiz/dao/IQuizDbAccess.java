package com.quiz.dao;

import java.util.List;

import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.Topic;
import com.quiz.model.User;

public interface IQuizDbAccess {

	Game addGame(int topicId, int totalPlayers);

	int addQuestion(Question question);

	int addTopic(Topic topic);

	boolean addUser(User u);

	boolean allPlayersFinishedQuestion(int gameId);

	boolean allPlayersReady(int gameId);
	
	boolean deleteQuestion(int QuestionId);
	
	boolean deleteTopic(int TopicId);

	Game findGameForNewPlayer(int topicId, int totalPlayers, String username);

	List<String> getAllPlayerUserIds(int gameId);

	List<String> getOtherPlayerUserIds(int gameId, String userId);

	Question getQuestion(int topicId);

	Question getQuestionFromQuestionId(int questionId);
	
	List<Question> getQuestions();
	
	Question getRandomQuestion(int topicId);

	Question getRandomQuestionWithExclusions(int topicId, int gameId);
	
	Topic getTopicFromTopicId(int TopicId);
	
	List<Topic> getTopics();

	User getUser(User user);
	
	Game joinGame(int gameId, String username);

	Game resetPlayersQDone(Game inputGame);

	Game retrieveGamefromId(int gameId);

	Game searchForFirstMatchingQueuedGame(int topicId, int totalPlayers);
	
	Game setPlayerCorrectAnswer(String username, double answerTime, int gameId);
	
	Game setPlayerNoAnswer(String username, int gameId);
	
	Game setPlayerReady(String username, int gameId);

	Game setPlayerWrongAnswer(String username, int gameId);
	
	Game setPlayerFinishedQuestion(String username, int gameId);
	
	Question setQuestion(Question question);
	
	Game setRemainingPlayersNoAnswer(int gameId);
	
	Topic setTopic(Topic topic);
	
	User setUser (User user);
	
	String showHint(String userId);
	
	void updateFlatFileWithQuestion(Question question, String filePath);

	void updateFlatFileWithTopic(String topic, String filePath);

	Game updateQuestionInfo(int gameId, int questionId);
}
