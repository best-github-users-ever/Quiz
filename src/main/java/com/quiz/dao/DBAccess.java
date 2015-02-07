package com.quiz.dao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.context.support.AbstractApplicationContext;

import com.quiz.model.Game;
import com.quiz.model.Player;
import com.quiz.model.Question;
import com.quiz.model.Topic;
import com.quiz.model.User;

public class DBAccess implements IQuizDbAccess {

	private static IQuizDbAccess dbo;

	private static Logger log = Logger.getLogger("com.quiz.dao.DBAccess");

	private JdbcTemplate jdbcTemplate = null;

	@SuppressWarnings("unused")
	// this is required in order to use Spring JDBC template
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	private DBAccess() {
	}

	// DB Access methods below

	private static final class GameMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Player player;

			Game localGame = new Game();
			localGame.setGameId(rs.getInt("gameId"));
			localGame.setTopicId(rs.getInt("topicid"));
			localGame.setNumPlayers(rs.getInt("numplayers"));
			localGame.setTotalPlayers(rs.getInt("totplayers"));
			localGame.setCurrQIndex(rs.getInt("curr_q_index"));
			localGame.setQ1Id(rs.getInt("Q1ID"));
			localGame.setQ2Id(rs.getInt("Q2ID"));
			localGame.setQ3Id(rs.getInt("Q3ID"));
			localGame.setQ4Id(rs.getInt("Q4ID"));
			localGame.setQ5Id(rs.getInt("Q5ID"));
			localGame.setQ6Id(rs.getInt("Q6ID"));
			localGame.setQ7Id(rs.getInt("Q7ID"));
			localGame.setQ8Id(rs.getInt("Q8ID"));
			localGame.setQ9Id(rs.getInt("Q9ID"));
			localGame.setQ10Id(rs.getInt("Q10ID"));

			player = localGame.getPlayerFromPlayerNumber(1);
			player.setPlayerNumber(1);
			player.setPlayer(rs.getString("player1"));
			player.setPlayerReady(rs.getBoolean("p1_ready"));
			player.setPlayerNumCorrect(rs.getInt("p1_num_correct"));
			player.setPlayerNumWrong(rs.getInt("p1_num_wrong"));
			player.setPlayerNumNoAnswer(rs.getInt("p1_num_no_ans"));
			player.setPlayerPoints(rs.getDouble("p1_points"));
			player.setPlayerCurrQDone(rs.getBoolean("P1_CURR_Q_DONE"));

			player = localGame.getPlayerFromPlayerNumber(2);
			player.setPlayerNumber(2);
			player.setPlayer(rs.getString("player2"));
			player.setPlayerReady(rs.getBoolean("p2_ready"));
			player.setPlayerNumCorrect(rs.getInt("p2_num_correct"));
			player.setPlayerNumWrong(rs.getInt("p2_num_wrong"));
			player.setPlayerNumNoAnswer(rs.getInt("p2_num_no_ans"));
			player.setPlayerPoints(rs.getDouble("p2_points"));
			player.setPlayerCurrQDone(rs.getBoolean("P2_CURR_Q_DONE"));

			player = localGame.getPlayerFromPlayerNumber(3);
			player.setPlayerNumber(3);
			player.setPlayer(rs.getString("player3"));
			player.setPlayerReady(rs.getBoolean("p3_ready"));
			player.setPlayerNumCorrect(rs.getInt("p3_num_correct"));
			player.setPlayerNumWrong(rs.getInt("p3_num_wrong"));
			player.setPlayerNumNoAnswer(rs.getInt("p3_num_no_ans"));
			player.setPlayerPoints(rs.getDouble("p3_points"));
			player.setPlayerCurrQDone(rs.getBoolean("P3_CURR_Q_DONE"));

			player = localGame.getPlayerFromPlayerNumber(4);
			player.setPlayerNumber(4);
			player.setPlayer(rs.getString("player4"));
			player.setPlayerReady(rs.getBoolean("p4_ready"));
			player.setPlayerNumCorrect(rs.getInt("p4_num_correct"));
			player.setPlayerNumWrong(rs.getInt("p4_num_wrong"));
			player.setPlayerNumNoAnswer(rs.getInt("p4_num_no_ans"));
			player.setPlayerPoints(rs.getDouble("p4_points"));
			player.setPlayerCurrQDone(rs.getBoolean("P4_CURR_Q_DONE"));

			player = localGame.getPlayerFromPlayerNumber(5);
			player.setPlayerNumber(5);
			player.setPlayer(rs.getString("player5"));
			player.setPlayerReady(rs.getBoolean("p5_ready"));
			player.setPlayerNumCorrect(rs.getInt("p5_num_correct"));
			player.setPlayerNumWrong(rs.getInt("p5_num_wrong"));
			player.setPlayerNumNoAnswer(rs.getInt("p5_num_no_ans"));
			player.setPlayerPoints(rs.getDouble("p5_points"));
			player.setPlayerCurrQDone(rs.getBoolean("P5_CURR_Q_DONE"));

			for (int i = 1; i <= localGame.getNumPlayers(); i++) {
				localGame.getPlayerFromPlayerNumber(i).setValidPlayer(true);
			}

			return localGame;
		}
	}

	private static class GamePreparedStatementCreator implements
			PreparedStatementCreator {
		private String GAME_INSERT = "INSERT INTO games (topicid, totplayers) VALUES(?,?)";
		private Game game;

		public GamePreparedStatementCreator(Game game, int topicId,
				int totalPlayers) {
			this.game = game;
			this.game.setTopicId(topicId);
			this.game.setTotalPlayers(totalPlayers);
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection conn)
				throws SQLException {
			PreparedStatement ps = conn.prepareStatement(GAME_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, game.getTopicId());
			ps.setInt(2, game.getTotalPlayers());
			return ps;
		}
	}

	private static final class QuestionMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			Question localQuestion = new Question();

			localQuestion.setQuestionId(rs.getInt("questionid"));
			localQuestion.setTopicId(rs.getInt("topicid"));
			localQuestion.setQuestion(rs.getString("question"));
			localQuestion.setOption1(rs.getString("option1"));
			localQuestion.setOption2(rs.getString("option2"));
			localQuestion.setOption3(rs.getString("option3"));
			localQuestion.setOption4(rs.getString("option4"));
			localQuestion.setAnswerIdx(rs.getInt("answeridx"));

			return localQuestion;
		}
	}

	private static final class TopicMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			Topic localTopic = new Topic();
			localTopic.setTopicId(rs.getInt("topicid"));
			localTopic.setName(rs.getString("name"));
			return localTopic;
		}
	}

	private static final class UserMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			User localUser = new User();
			localUser.setUserId(rs.getString("userid"));
			localUser.setEmailAddress(rs.getString("email"));
			localUser.setPassword(rs.getString("password"));
			localUser.setHint(rs.getString("hint"));

			return localUser;
		}
	}

	private static class QuestionPreparedStatementCreator implements
			PreparedStatementCreator {
		private String QUESTION_INSERT = "INSERT INTO questions (question, option1, option2, option3, option4, "
				+ "answerIdx, topicId) VALUES(?, ?,?,?,?,?,?)";
		private Question question;

		public QuestionPreparedStatementCreator(Question question) {
			this.question = question;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection conn)
				throws SQLException {
			PreparedStatement ps = conn.prepareStatement(QUESTION_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, question.getQuestion().trim());
			ps.setString(2, question.getOption1().trim());
			ps.setString(3, question.getOption2().trim());
			ps.setString(4, question.getOption3().trim());
			ps.setString(5, question.getOption4().trim());
			ps.setInt(6, question.getAnswerIdx());
			ps.setInt(7, question.getTopicId());
			return ps;
		}
	}

	public int addQuestion(Question question) {
		QuestionPreparedStatementCreator creator = new QuestionPreparedStatementCreator(
				question);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(creator, keyHolder);
			return keyHolder.getKey().intValue();

		} catch (Exception e) {
			return -1;
		}
	}

	private static class TopicPreparedStatementCreator implements
			PreparedStatementCreator {
		private String TOPIC_INSERT = "INSERT INTO topics (name) VALUES(?)";
		private Topic topic;

		public TopicPreparedStatementCreator(Topic topic) {
			this.topic = topic;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection conn)
				throws SQLException {
			PreparedStatement ps = conn.prepareStatement(TOPIC_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, topic.getName().trim());
			return ps;
		}
	}

	public int addTopic(Topic topic) {
		TopicPreparedStatementCreator creator = new TopicPreparedStatementCreator(
				topic);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(creator, keyHolder);
			return keyHolder.getKey().intValue();

		} catch (Exception e) {
			return -1;
		}
	}

	public static IQuizDbAccess getDbAccess() {
		if (dbo == null) {
			dbo = new DBAccess();

			ApplicationContext ctx = new ClassPathXmlApplicationContext(
					"context.xml");

			dbo = (IQuizDbAccess) ctx.getBean("quizJdbcImplDao");

			((AbstractApplicationContext) ctx).close();

			return dbo;

		} else {
			return dbo;
		}
	}

	@Override
	public synchronized Game addGame(int topicId, int totalPlayers) {
		Game game = new Game();
		GamePreparedStatementCreator creator = new GamePreparedStatementCreator(
				game, topicId, totalPlayers);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(creator, keyHolder);
		if (keyHolder.getKey() != null) {
			return retrieveGamefromId(keyHolder.getKey().intValue());
		} else {
			return null;

		}

	}

	private static class UserPreparedStatementCreator implements
			PreparedStatementCreator {
		private String USER_INSERT = "INSERT INTO users (userid, hint, email, password) VALUES(?,?,?,?)";
		private User user;

		public UserPreparedStatementCreator(User user) {
			this.user = user;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection conn)
				throws SQLException {
			PreparedStatement ps = conn.prepareStatement(USER_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getUserId());
			ps.setString(2, user.getHint());
			ps.setString(3, user.getEmailAddress());
			ps.setString(4, user.getPassword());
			return ps;
		}
	}

	@Override
	public boolean addUser(User user) {
		UserPreparedStatementCreator creator = new UserPreparedStatementCreator(
				user);
		// KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(creator);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean allPlayersFinishedQuestion(int gameId) {

		Game game = retrieveGamefromId(gameId);
		log.info("game value:" + game);
		if (game != null) {

			return game.allPlayersFinishedQuestion();

		} else {
			log.info("couldn't find game based on gameId of:" + gameId);
			return false;
		}
	}

	@Override
	public boolean allPlayersReady(int gameId) {
		Game game = retrieveGamefromId(gameId);

		if (game != null) {
			return game.allPlayersReady();

		} else {
			log.info("couldn't find game based on gameId of:" + gameId);
			return false;
		}
	}

	public boolean deleteQuestion(int questionId) {
		final String DELETE_QUESTION = "DELETE FROM questions WHERE questionid = ?";

		try {
			jdbcTemplate.update(DELETE_QUESTION, new Object[] { questionId });

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean deleteTopic(int topicId) {
		final String DELETE_TOPIC = "DELETE FROM topics WHERE topicid = ?";

		try {
			jdbcTemplate.update(DELETE_TOPIC, new Object[] { topicId });

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public synchronized Game findGameForNewPlayer(int topicId, int totalPlayers,
			String username) {

		Game game = searchForFirstMatchingQueuedGame(topicId, totalPlayers);

		if (game != null) {
			game = joinGame(game.getGameId(), username);

			log.info("joined existing game");
			return game;
		} else {
			game = addGame(topicId, totalPlayers);

			if (game != null) {
				game = joinGame(game.getGameId(), username);

				log.info("added new game");

				return game;
			}

		}

		return null;
	}

	@Override
	public List<String> getAllPlayerUserIds(int gameId) {

		Game game = retrieveGamefromId(gameId);
		log.info(game.toString());

		return game.getAllPlayerUserIds();
	}

	@Override
	public List<String> getOtherPlayerUserIds(int gameId, String userId) {

		Game game = retrieveGamefromId(gameId);
		return game.getOtherPlayerUserIds(userId);

	}

	@Override
	public Question getQuestion(int topicId) {
		final String GET_QUESTION = "SELECT * FROM questions WHERE topicid = ?";
		int questionIndex = 0;

		List<Object> candidateQuestions = jdbcTemplate.query(GET_QUESTION,
				new Object[] { topicId }, new QuestionMapper());

		if (!candidateQuestions.isEmpty()) {
			// just return the first question
			return (Question) candidateQuestions.get(questionIndex);
		} else {
			return null;
		}
	}

	@Override
	public Question getQuestionFromQuestionId(int questionId) {
		final String GET_QUESTION = "SELECT * FROM questions WHERE questionid = ?";

		try {
			return (Question) jdbcTemplate.queryForObject(GET_QUESTION,
					new Object[] { questionId }, new QuestionMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Question getRandomQuestion(int topicId) {

		final String GET_QUESTION = "SELECT * FROM questions WHERE topicid = ?";

		List<Object> candidateQuestions = jdbcTemplate.query(GET_QUESTION,
				new Object[] { topicId }, new QuestionMapper());

		if (!candidateQuestions.isEmpty()) {

			int questionIndex = (int) Math.round(Math.random()
					* (candidateQuestions.size() - 1));

			return (Question) candidateQuestions.get(questionIndex);
		} else {
			return null;
		}
	}

	@Override
	public Question getRandomQuestionWithExclusions(int topicId, int gameId) {
		Game game = retrieveGamefromId(gameId);
		String exclusionText = "";

		List<Integer> questionList = new ArrayList<Integer>();

		if (game.getCurrQIndex() > 0) {
			questionList.add(game.getQ1Id());
		}
		if (game.getCurrQIndex() > 1) {
			questionList.add(game.getQ2Id());
		}
		if (game.getCurrQIndex() > 2) {
			questionList.add(game.getQ3Id());
		}
		if (game.getCurrQIndex() > 3) {
			questionList.add(game.getQ4Id());
		}
		if (game.getCurrQIndex() > 4) {
			questionList.add(game.getQ5Id());
		}
		if (game.getCurrQIndex() > 5) {
			questionList.add(game.getQ6Id());
		}
		if (game.getCurrQIndex() > 6) {
			questionList.add(game.getQ7Id());
		}
		if (game.getCurrQIndex() > 7) {
			questionList.add(game.getQ8Id());
		}
		if (game.getCurrQIndex() > 8) {
			questionList.add(game.getQ9Id());
		}
		if (game.getCurrQIndex() > 9) {
			questionList.add(game.getQ10Id());
		}

		for (Integer question : questionList) {
			exclusionText = exclusionText + " AND QUESTIONID != " + question;
		}
		final String GET_QUESTION = "SELECT * FROM questions WHERE topicid = ?"
				+ exclusionText;

		try {
			List<Object> candidateQuestions = jdbcTemplate.query(GET_QUESTION,
					new Object[] { topicId }, new QuestionMapper());

			if (!candidateQuestions.isEmpty()) {

				int questionIndex = (int) Math.round(Math.random()
						* (candidateQuestions.size() - 1));

				return (Question) candidateQuestions.get(questionIndex);
			} else {
				return null;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Question> getQuestions() {
		final String GET_QUESTIONS = "SELECT * FROM questions ORDER BY questionid";
		List<Question> questions = new ArrayList<Question>();

		List<Object> objects = jdbcTemplate.query(GET_QUESTIONS,
				new Object[] {}, new QuestionMapper());

		for (Object myObject : objects) {
			questions.add((Question) myObject);
		}

		return questions;
	}

	@Override
	public Topic getTopicFromTopicId(int topicId) {
		final String GET_TOPIC = "SELECT * FROM topics WHERE topicid = ?";

		try {
			Topic topic = (Topic) jdbcTemplate.queryForObject(GET_TOPIC,
					new Object[] { topicId }, new TopicMapper());

			return topic;

		} catch (Exception e) {
			// topic not found
			log.info("exception is :" + e.getMessage());
			return null;
		}
	}

	@Override
	public List<Topic> getTopics() {
		final String GET_TOPICS = "SELECT * FROM topics ORDER BY topicid";
		List<Topic> topics = new ArrayList<Topic>();

		List<Object> objects = jdbcTemplate.query(GET_TOPICS, new Object[] {},
				new TopicMapper());

		for (Object myObject : objects) {
			// topics.add( ((Topic) myObject).getTopicId(), (Topic) myObject);
			topics.add((Topic) myObject);
		}

		return topics;
	}

	@Override
	public User getUser(User user) {
		// need to include password below
		final String GET_USER = "SELECT * FROM users WHERE userid = ? AND password = ?";
		try {
			return (User) jdbcTemplate.queryForObject(GET_USER, new Object[] {
					user.getUserId(), user.getPassword() }, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Game joinGame(int gameId, String username) {
		Game game = retrieveGamefromId(gameId);

		if (game != null) {
			if (game.getNumPlayers() < game.getTotalPlayers()) {
				String playerString = null;
				String playerUserId = null;

				int playerNumber = game.getNumPlayers() + 1;

				playerUserId = username;

				switch (playerNumber) {
				case 1:
					playerString = "player1";
					break;
				case 2:
					playerString = "player2";
					break;
				case 3:
					playerString = "player3";
					break;
				case 4:
					playerString = "player4";
					break;
				case 5:
					playerString = "player5";
					break;

				default:
					log.severe("Incorrect player number: " + playerNumber);
					break;
				}

				game.getPlayerFromPlayerNumber(playerNumber)
						.setPlayer(username);
				game.getPlayerFromPlayerNumber(playerNumber).setValidPlayer(
						true);

				game.setNumPlayers(game.getNumPlayers() + 1);
				log.info(game.toString());

				String updateStatement = " UPDATE games"
						+ " SET numplayers= ?," + playerString + " = ?"
						+ " WHERE gameid= ?";

				jdbcTemplate.update(
						updateStatement,
						new Object[] { game.getNumPlayers(), playerUserId,
								game.getGameId() });
			}
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);

		}
		return game;
	}

	@Override
	public Game resetPlayersQDone(Game inputGame) {
		int gameId = inputGame.getGameId();
		Game game = retrieveGamefromId(gameId);

		if (game != null) {

			String updateStatement = " UPDATE games"
					+ " SET P1_CURR_Q_DONE = false, "
					+ " P2_CURR_Q_DONE = false, " + " P3_CURR_Q_DONE = false, "
					+ " P4_CURR_Q_DONE = false, "
					+ " P5_CURR_Q_DONE = false  WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { gameId });

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	public Game retrieveGamefromId(int gameId) {
		final String GET_GAME = "SELECT * FROM games WHERE gameid = ?";
		return (Game) jdbcTemplate.queryForObject(GET_GAME,
				new Object[] { gameId }, new GameMapper());
	}

	@Override
	public Game searchForFirstMatchingQueuedGame(int topicId, int totalPlayers) {
		final String GET_GAME = "SELECT * FROM games WHERE topicid = ? AND totplayers = ? AND numplayers < totplayers";

		List<Object> candidateGames = jdbcTemplate.query(GET_GAME,
				new Object[] { topicId, totalPlayers }, new GameMapper());

		if (!candidateGames.isEmpty()) {
			return (Game) candidateGames.get(0);
		} else {
			return null;
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setNamedJdbcTemplate(
			NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}

	@Override
	public Game setPlayerReady(String username, int gameId) {
		Game game = retrieveGamefromId(gameId);

		if (game != null) {
			String playerReadyString = null;

			int playerNumber = game.findPlayerNumberFromUserId(username);

			if (playerNumber == 0) {
				log.severe("Incorrect username: " + username);
				return null;
			}

			game.getPlayerFromPlayerNumber(playerNumber).setPlayerReady(true);
			playerReadyString = "P" + playerNumber + "_READY";

			String updateStatement = " UPDATE games" + " SET "
					+ playerReadyString + " = true" + " WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { gameId });

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	@Override
	public Game setPlayerCorrectAnswer(String username, double answerTime,
			int gameId) {
		Game game = retrieveGamefromId(gameId);
		int newCorrectCount = 0;

		if (game != null) {
			String playerFinishedQuestionString = null;
			String playerCorrectAnswerString = null;
			String playerTimeString = null;
			double newPointsAmount;

			int playerNumber = game.findPlayerNumberFromUserId(username);

			if (playerNumber == 0) {
				log.severe("Incorrect username: " + username);
				return null;
			}

			Player player = game.getPlayerFromPlayerNumber(playerNumber);

			if (player.isPlayerCurrQDone()) {
				log.info("P" + playerNumber + ": late correct");
				return null;
			}

			player.setPlayerCurrQDone(true);
			newCorrectCount = player.getPlayerNumCorrect() + 1;
			player.setPlayerNumCorrect(newCorrectCount);
			newPointsAmount = player.getPlayerPoints() + answerTime;
			player.setPlayerPoints(newPointsAmount);
			playerTimeString = "P" + playerNumber + "_POINTS";
			playerFinishedQuestionString = "P" + playerNumber + "_CURR_Q_DONE";
			playerCorrectAnswerString = "P" + playerNumber + "_NUM_CORRECT";

			String updateStatement = " UPDATE games" + " SET "
					+ playerFinishedQuestionString + " = true, "
					+ playerTimeString + " = ?, " + playerCorrectAnswerString
					+ " = ?  WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] {
					newPointsAmount, newCorrectCount, gameId });
			game.dump_if_discrepancy();

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	@Override
	public Game setPlayerFinishedQuestion(String username, int gameId) {
		Game game = retrieveGamefromId(gameId);

		if (game != null) {
			String playerFinishedQuestionString = null;

			int playerNumber = game.findPlayerNumberFromUserId(username);

			if (playerNumber == 0) {
				log.severe("Incorrect username: " + username);
				return null;
			}

			Player player = game.getPlayerFromPlayerNumber(playerNumber);

			if (player.isPlayerCurrQDone()) {
				log.info("P" + playerNumber + ": already done");
				return null;
			}

			player.setPlayerCurrQDone(true);

			playerFinishedQuestionString = "P" + playerNumber + "_CURR_Q_DONE";

			String updateStatement = " UPDATE games" + " SET "
					+ playerFinishedQuestionString + " = true WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { gameId });
			game.dump_if_discrepancy();

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	@Override
	public Game setPlayerNoAnswer(String username, int gameId) {
		Game game = retrieveGamefromId(gameId);
		int newNoAnswerCount = 0;

		if (game != null) {
			String playerFinishedQuestionString = null;
			String playerNoAnswerString = null;

			int playerNumber = game.findPlayerNumberFromUserId(username);

			if (playerNumber == 0) {
				log.severe("Incorrect username: " + username);
				return null;
			}

			Player player = game.getPlayerFromPlayerNumber(playerNumber);

			if (player.isPlayerCurrQDone()) {
				log.info("P" + playerNumber + ": No Answer. already done");
				return null;
			}

			player.setPlayerCurrQDone(true);
			newNoAnswerCount = player.getPlayerNumNoAnswer() + 1;

			player.setPlayerNumNoAnswer(newNoAnswerCount);
			playerFinishedQuestionString = "P" + playerNumber + "_CURR_Q_DONE";
			playerNoAnswerString = "P" + playerNumber + "_NUM_NO_ANS";

			String updateStatement = " UPDATE games" + " SET "
					+ playerFinishedQuestionString + " = true, "
					+ playerNoAnswerString + " = ?  WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] {
					newNoAnswerCount, gameId });

			game.dump_if_discrepancy();
			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	@Override
	public Game setPlayerWrongAnswer(String username, int gameId) {
		Game game = retrieveGamefromId(gameId);
		int newWrongCount = 0;

		if (game != null) {
			String playerFinishedQuestionString = null;
			String playerWrongAnswerString = null;

			int playerNumber = game.findPlayerNumberFromUserId(username);

			if (playerNumber == 0) {
				log.severe("Incorrect username: " + username);
				return null;
			}

			Player player = game.getPlayerFromPlayerNumber(playerNumber);

			if (player.isPlayerCurrQDone()) {
				log.info("P" + playerNumber + ": late wrong");
				return null;
			}

			player.setPlayerCurrQDone(true);
			newWrongCount = player.getPlayerNumWrong() + 1;
			player.setPlayerNumWrong(newWrongCount);
			playerFinishedQuestionString = "P" + playerNumber + "_CURR_Q_DONE";
			playerWrongAnswerString = "P" + playerNumber + "_NUM_WRONG";

			String updateStatement = " UPDATE games" + " SET "
					+ playerFinishedQuestionString + " = true, "
					+ playerWrongAnswerString + " = ?  WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { newWrongCount,
					gameId });
			game.dump_if_discrepancy();

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	@Override
	public Question setQuestion(Question question) {
		Question localQuestion = getQuestionFromQuestionId(question
				.getQuestionId());

		if (localQuestion == null) {
			return localQuestion;
		} else {

			String updateStatement = " UPDATE questions SET QUESTION = ?, TOPICID = ?, "
					+ "OPTION1 = ?, OPTION2 = ?, OPTION3 = ?, OPTION4 = ?, ANSWERIDX = ?  WHERE questionId = ?";

			try {
				jdbcTemplate.update(
						updateStatement,
						new Object[] { question.getQuestion(),
								question.getTopicId(), question.getOption1(),
								question.getOption2(), question.getOption3(),
								question.getOption4(), question.getAnswerIdx(),
								question.getQuestionId() });

				return getQuestionFromQuestionId(question.getQuestionId());

			} catch (Exception e) {
				return null;
			}
		}
	}

	@Override
	public Topic setTopic(Topic topic) {
		Topic localTopic = getTopicFromTopicId(topic.getTopicId());

		if (localTopic == null) {
			return localTopic;
		} else {
			String updateStatement = " UPDATE topics SET NAME = ?  WHERE topicId = ?";

			jdbcTemplate.update(updateStatement, new Object[] {
					topic.getName(), topic.getTopicId() });

			return topic;
		}
	}

	@Override
	public User setUser(User user) {
		User localUser;

		String updateStatement = " UPDATE users SET PASSWORD = ?, HINT = ?, EMAIL = ? WHERE userId = ?";

		jdbcTemplate.update(updateStatement, new Object[] { user.getPassword(),
				user.getHint(), user.getEmailAddress(), user.getUserId() });

		localUser = getUser(user);

		return localUser;
	}

	@Override
	public String showHint(String userId) {
		final String GET_HINT = "SELECT * FROM users WHERE userid = ?";
		User user = null;

		try {
			user = (User) jdbcTemplate.queryForObject(GET_HINT,
					new Object[] { userId }, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		if (user != null) {

			if (user.getHint() == null) {
				return "";
			} else {
				return user.getHint();
			}
		} else {
			return null;
		}
	}

	@Override
	public Game setRemainingPlayersNoAnswer(int gameId) {

		Game game = retrieveGamefromId(gameId);

		Player player;

		for (int i = 1; i <= Game.MAX_PLAYERS_PER_GAME; i++) {
			player = game.getPlayerFromPlayerNumber(i);
			if (player.isValidPlayer() && !player.isPlayerCurrQDone()) {
				game = setPlayerNoAnswer(player.getPlayer(), gameId);
			}
		}

		game.dump_if_discrepancy();

		return game;
	}

	@Override
	public Game updateQuestionInfo(int gameId, int questionId) {
		Game game = retrieveGamefromId(gameId);
		String questionString = "";

		if (game != null) {
			int newIndex = game.getCurrQIndex() + 1;

			switch (newIndex) {
			case 1:
				questionString = " Q1ID ";
				break;
			case 2:
				questionString = " Q2ID ";
				break;
			case 3:
				questionString = " Q3ID ";
				break;
			case 4:
				questionString = " Q4ID ";
				break;
			case 5:
				questionString = " Q5ID ";
				break;
			case 6:
				questionString = " Q6ID ";
				break;
			case 7:
				questionString = " Q7ID ";
				break;
			case 8:
				questionString = " Q8ID ";
				break;
			case 9:
				questionString = " Q9ID ";
				break;
			case 10:
				questionString = " Q10ID ";
				break;

			default:
				log.info("invalid question index passed in:" + newIndex);
				return null;
			}

			String updateStatement = " UPDATE games"
					+ " SET CURR_Q_INDEX = ?, " + questionString
					+ "= ? WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { newIndex,
					questionId, gameId });

			return retrieveGamefromId(gameId);

		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	public static String formatForSql(String incomingString) {
		String stringForDB;

		// converts quotes into sql literals \' and \"

		if (incomingString == null) {
			return "";
		}

		stringForDB = incomingString.replaceAll("\'", "\\\\'");
		stringForDB = stringForDB.replaceAll("\"", "\\\\\"");

		return stringForDB;
	}

	public void updateFlatFileWithQuestion(Question question, String filePath) {
		FileOutputStream file = null;
		try {
			file = new FileOutputStream(filePath, true);
		} catch (FileNotFoundException e1) {
			log.info("File not found: " + filePath);
		}

		PrintWriter writer;

		writer = new PrintWriter(file);
		writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', "
				+ question.getTopicId() + ", '"
				+ formatForSql(question.getQuestion()) + "', '"
				+ formatForSql(question.getOption1()) + "', '"
				+ formatForSql(question.getOption2()) + "', '"
				+ formatForSql(question.getOption3()) + "', '"
				+ formatForSql(question.getOption4()) + "',"
				+ question.getAnswerIdx() + ");");

		writer.flush();
		writer.close();
	}

	public void updateFlatFileWithTopic(String topic, String filePath) {
		FileOutputStream file = null;
		try {
			file = new FileOutputStream(filePath, true);
		} catch (FileNotFoundException e1) {
			log.info("File not found: " + filePath);
		}

		PrintWriter writer;

		writer = new PrintWriter(file);
		writer.println(" INSERT INTO TOPICS VALUES ('DEFAULT', '"
				+ formatForSql(topic) + "');");

		writer.flush();
		writer.close();
	}
}
