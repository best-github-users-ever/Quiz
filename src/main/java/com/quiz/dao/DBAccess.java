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

import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.Topic;
import com.quiz.model.User;

public class DBAccess implements IQuizDbAccess {
	private static final class GameMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

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
			localGame.setPlayer1(rs.getString("player1"));
			localGame.setP1Ready(rs.getBoolean("p1_ready"));
			localGame.setP1NumCorrect(rs.getInt("p1_num_correct"));
			localGame.setP1NumWrong(rs.getInt("p1_num_wrong"));
			localGame.setP1NumNoAnswer(rs.getInt("p1_num_no_ans"));
			localGame.setP1Time(rs.getDouble("p1_time"));
			localGame.setP1CurrQDone(rs.getBoolean("P1_CURR_Q_DONE"));
			localGame.setPlayer2(rs.getString("player2"));
			localGame.setP2Ready(rs.getBoolean("p2_ready"));
			localGame.setP2NumCorrect(rs.getInt("p2_num_correct"));
			localGame.setP2NumWrong(rs.getInt("p2_num_wrong"));
			localGame.setP2NumNoAnswer(rs.getInt("p2_num_no_ans"));
			localGame.setP2Time(rs.getDouble("p2_time"));
			localGame.setP2CurrQDone(rs.getBoolean("P2_CURR_Q_DONE"));
			localGame.setPlayer3(rs.getString("player3"));
			localGame.setP3Ready(rs.getBoolean("p3_ready"));
			localGame.setP3NumCorrect(rs.getInt("p3_num_correct"));
			localGame.setP3NumWrong(rs.getInt("p3_num_wrong"));
			localGame.setP3NumNoAnswer(rs.getInt("p3_num_no_ans"));
			localGame.setP3Time(rs.getDouble("p3_time"));
			localGame.setP3CurrQDone(rs.getBoolean("P3_CURR_Q_DONE"));
			localGame.setPlayer4(rs.getString("player4"));
			localGame.setP4Ready(rs.getBoolean("p4_ready"));
			localGame.setP4NumCorrect(rs.getInt("p4_num_correct"));
			localGame.setP4NumWrong(rs.getInt("p4_num_wrong"));
			localGame.setP4NumNoAnswer(rs.getInt("p4_num_no_ans"));
			localGame.setP4Time(rs.getDouble("p4_time"));
			localGame.setP4CurrQDone(rs.getBoolean("P4_CURR_Q_DONE"));
			localGame.setPlayer5(rs.getString("player5"));
			localGame.setP5Ready(rs.getBoolean("p5_ready"));
			localGame.setP5NumCorrect(rs.getInt("p5_num_correct"));
			localGame.setP5NumWrong(rs.getInt("p5_num_wrong"));
			localGame.setP5NumNoAnswer(rs.getInt("p5_num_no_ans"));
			localGame.setP5Time(rs.getDouble("p5_time"));
			localGame.setP5CurrQDone(rs.getBoolean("P5_CURR_Q_DONE"));
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
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
			return dbo;
		} else {
			return dbo;
		}
	}

	private static IQuizDbAccess dbo;

	private static Logger log = Logger.getLogger("com.quiz.dao.DBAccess");

	private JdbcTemplate jdbcTemplate = null;

	// New
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	private DBAccess() {
	}

	// DB Access methods below

	@Override
	public Game addGame(int topicId, int totalPlayers) {
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

			if (game.getTotalPlayers() == 2) {
				return game.isP1CurrQDone() & game.isP2CurrQDone();
			} else if (game.getTotalPlayers() == 3) {
				return game.isP1CurrQDone() & game.isP2CurrQDone()
						& game.isP3CurrQDone();
			} else if (game.getTotalPlayers() == 4) {
				return game.isP1CurrQDone() & game.isP2CurrQDone()
						& game.isP3CurrQDone() & game.isP4CurrQDone();
			} else if (game.getTotalPlayers() == 5) {
				return game.isP1CurrQDone() & game.isP2CurrQDone()
						& game.isP3CurrQDone() & game.isP4CurrQDone()
						& game.isP5CurrQDone();
			} else if (game.getTotalPlayers() == 1) {
				return game.isP1CurrQDone();
			} else {
				log.info("value of total players is invalid in gameId: "
						+ gameId);
				return false;
			}

		} else {
			log.info("couldn't find game based on gameId of:" + gameId);
			return false;
		}
	}

	@Override
	public boolean allPlayersReady(int gameId) {
		Game game = retrieveGamefromId(gameId);

		if (game != null) {

			if (game.getTotalPlayers() == 2) {
				return (game.isP1Ready() & game.isP2Ready());
			} else if (game.getTotalPlayers() == 3) {
				return game.isP1Ready() & game.isP2Ready() & game.isP3Ready();
			} else if (game.getTotalPlayers() == 4) {
				return game.isP1Ready() & game.isP2Ready() & game.isP3Ready()
						& game.isP4Ready();
			} else if (game.getTotalPlayers() == 5) {
				return game.isP1Ready() & game.isP2Ready() & game.isP3Ready()
						& game.isP4Ready() & game.isP5Ready();
			} else if (game.getTotalPlayers() == 1) {
				return game.isP1Ready();
			} else {
				log.info("value of total players is invalid in gameId: "
						+ gameId);
				return false;
			}

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
	public Game findGameForNewPlayer(int topicId, int totalPlayers,
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
		List<String> playerList = new ArrayList<String>();

		log.info(game.toString());
		if ((game.getPlayer1() != null) & (game.getPlayer1() != "")) {
			playerList.add(game.getPlayer1());
		}
		if ((game.getPlayer2() != null) & (game.getPlayer2() != "")) {
			playerList.add(game.getPlayer2());
		}
		if ((game.getPlayer3() != null) & (game.getPlayer3() != "")) {
			playerList.add(game.getPlayer3());
		}
		if ((game.getPlayer4() != null) & (game.getPlayer4() != "")) {
			playerList.add(game.getPlayer4());
		}
		if ((game.getPlayer5() != null) & (game.getPlayer5() != "")) {
			playerList.add(game.getPlayer5());
		}
		return playerList;
	}

	@Override
	public List<String> getOtherPlayerUserIds(int gameId, String userId) {

		Game game = retrieveGamefromId(gameId);
		List<String> opponentList = new ArrayList<String>();

		log.info(game.toString());
		if ((game.getPlayer1() != null) & (game.getPlayer1() != "")
				& (!userId.equals(game.getPlayer1()))) {
			opponentList.add(game.getPlayer1());
		}
		if ((game.getPlayer2() != null) & (game.getPlayer2() != "")
				& (!userId.equals(game.getPlayer2()))) {
			opponentList.add(game.getPlayer2());
		}
		if ((game.getPlayer3() != null) & (game.getPlayer3() != "")
				& (!userId.equals(game.getPlayer3()))) {
			opponentList.add(game.getPlayer3());
		}
		if ((game.getPlayer4() != null) & (game.getPlayer4() != "")
				& (!userId.equals(game.getPlayer4()))) {
			opponentList.add(game.getPlayer4());
		}
		if ((game.getPlayer5() != null) & (game.getPlayer5() != "")
				& (!userId.equals(game.getPlayer5()))) {
			opponentList.add(game.getPlayer5());
		}
		return opponentList;
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
		;

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
					game.setPlayer1(username);
					playerString = "player1";
					break;
				case 2:
					game.setPlayer2(username);
					playerString = "player2";
					break;
				case 3:
					game.setPlayer3(username);
					playerString = "player3";
					break;
				case 4:
					game.setPlayer4(username);
					playerString = "player4";
					break;
				case 5:
					game.setPlayer5(username);
					playerString = "player5";
					break;

				default:
					log.severe("Incorrect player number: " + playerNumber);
					break;
				}

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

			if (username.equals(game.getPlayer1())) {
				game.setP1Ready(true);
				playerReadyString = "p1_ready";
			} else if (username.equals(game.getPlayer2())) {
				game.setP2Ready(true);
				playerReadyString = "p2_ready";
			} else if (username.equals(game.getPlayer3())) {
				game.setP3Ready(true);
				playerReadyString = "p3_ready";
			} else if (username.equals(game.getPlayer4())) {
				game.setP4Ready(true);
				playerReadyString = "p4_ready";
			} else if (username.equals(game.getPlayer5())) {
				game.setP5Ready(true);
				playerReadyString = "p5_ready";
			}

			else {
				log.severe("Incorrect username: " + username);
				return null;
			}

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
			double newTimeAmount;

			if (username.equals(game.getPlayer1())) {
				if (game.isP1CurrQDone()) {
					log.info("P1: late correct");
					return null;
				}
				game.setP1CurrQDone(true);
				newCorrectCount = game.getP1NumCorrect() + 1;
				game.setP1NumCorrect(newCorrectCount);
				newTimeAmount = game.getP1Time() + answerTime;
				game.setP1Time(newTimeAmount);
				playerTimeString = "P1_TIME";
				playerFinishedQuestionString = "P1_CURR_Q_DONE";
				playerCorrectAnswerString = "P1_NUM_CORRECT";
			} else if (username.equals(game.getPlayer2())) {
				if (game.isP2CurrQDone()) {
					log.info("P2: late correct");
					return null;
				}
				newCorrectCount = game.getP2NumCorrect() + 1;
				game.setP2NumCorrect(newCorrectCount);
				game.setP2CurrQDone(true);
				newTimeAmount = game.getP2Time() + answerTime;
				game.setP2Time(newTimeAmount);
				playerTimeString = "P2_TIME";
				playerFinishedQuestionString = "P2_CURR_Q_DONE";
				playerCorrectAnswerString = "P2_NUM_CORRECT";
			} else if (username.equals(game.getPlayer3())) {
				if (game.isP3CurrQDone()) {
					log.info("P3: late correct");
					return null;
				}
				newCorrectCount = game.getP3NumCorrect() + 1;
				game.setP3NumCorrect(newCorrectCount);
				game.setP3CurrQDone(true);
				newTimeAmount = game.getP3Time() + answerTime;
				game.setP3Time(newTimeAmount);
				playerTimeString = "P3_TIME";
				playerFinishedQuestionString = "P3_CURR_Q_DONE";
				playerCorrectAnswerString = "P3_NUM_CORRECT";
			} else if (username.equals(game.getPlayer4())) {
				if (game.isP4CurrQDone()) {
					log.info("P4: late correct");
					return null;
				}
				newCorrectCount = game.getP4NumCorrect() + 1;
				game.setP4NumCorrect(newCorrectCount);
				game.setP4CurrQDone(true);
				newTimeAmount = game.getP4Time() + answerTime;
				game.setP4Time(newTimeAmount);
				playerTimeString = "P4_TIME";
				playerFinishedQuestionString = "P4_CURR_Q_DONE";
				playerCorrectAnswerString = "P4_NUM_CORRECT";
			} else if (username.equals(game.getPlayer5())) {
				if (game.isP5CurrQDone()) {
					log.info("P5: late correct");
					return null;
				}
				newCorrectCount = game.getP5NumCorrect() + 1;
				game.setP5NumCorrect(newCorrectCount);
				game.setP5CurrQDone(true);
				newTimeAmount = game.getP5Time() + answerTime;
				game.setP5Time(newTimeAmount);
				playerTimeString = "P5_TIME";
				playerFinishedQuestionString = "P5_CURR_Q_DONE";
				playerCorrectAnswerString = "P5_NUM_CORRECT";
			}

			else {
				log.severe("Incorrect username: " + username);
				return null;
			}

			String updateStatement = " UPDATE games" + " SET "
					+ playerFinishedQuestionString + " = true, "
					+ playerTimeString + " = ?, " + playerCorrectAnswerString
					+ " = ?  WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { newTimeAmount,
					newCorrectCount, gameId });
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

			if (username.equals(game.getPlayer1())) {
				game.setP1CurrQDone(true);
				playerFinishedQuestionString = "P1_CURR_Q_DONE";
			} else if (username.equals(game.getPlayer2())) {
				game.setP2CurrQDone(true);
				playerFinishedQuestionString = "P2_CURR_Q_DONE";
			} else if (username.equals(game.getPlayer3())) {
				game.setP3CurrQDone(true);
				playerFinishedQuestionString = "P3_CURR_Q_DONE";
			} else if (username.equals(game.getPlayer4())) {
				game.setP4CurrQDone(true);
				playerFinishedQuestionString = "P4_CURR_Q_DONE";
			} else if (username.equals(game.getPlayer5())) {
				game.setP5CurrQDone(true);
				playerFinishedQuestionString = "P5_CURR_Q_DONE";
			}

			else {
				log.severe("Incorrect username: " + username);
				return null;
			}

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

			if (username.equals(game.getPlayer1())) {
				if (game.isP1CurrQDone()) {
					log.info("P1: late no answer");
					return null;
				}
				game.setP1CurrQDone(true);
				newNoAnswerCount = game.getP1NumNoAnswer() + 1;
				game.setP1NumNoAnswer(newNoAnswerCount);
				playerFinishedQuestionString = "P1_CURR_Q_DONE";
				playerNoAnswerString = "P1_NUM_NO_ANS";
			} else if (username.equals(game.getPlayer2())) {
				if (game.isP2CurrQDone()) {
					log.info("P2: late no answer");
					return null;
				}
				newNoAnswerCount = game.getP2NumNoAnswer() + 1;
				game.setP2NumNoAnswer(newNoAnswerCount);
				game.setP2CurrQDone(true);
				playerFinishedQuestionString = "P2_CURR_Q_DONE";
				playerNoAnswerString = "P2_NUM_NO_ANS";
			} else if (username.equals(game.getPlayer3())) {
				if (game.isP3CurrQDone()) {
					log.info("P3: late no answer");
					return null;
				}
				newNoAnswerCount = game.getP3NumNoAnswer() + 1;
				game.setP3NumNoAnswer(newNoAnswerCount);
				game.setP3CurrQDone(true);
				playerFinishedQuestionString = "P3_CURR_Q_DONE";
				playerNoAnswerString = "P3_NUM_NO_ANS";
			} else if (username.equals(game.getPlayer4())) {
				if (game.isP4CurrQDone()) {
					log.info("P4: late no answer");
					return null;
				}
				newNoAnswerCount = game.getP4NumNoAnswer() + 1;
				game.setP4NumNoAnswer(newNoAnswerCount);
				game.setP4CurrQDone(true);
				playerFinishedQuestionString = "P4_CURR_Q_DONE";
				playerNoAnswerString = "P4_NUM_NO_ANS";
			} else if (username.equals(game.getPlayer5())) {
				if (game.isP5CurrQDone()) {
					log.info("P5: late no answer");
					return null;
				}
				newNoAnswerCount = game.getP5NumNoAnswer() + 1;
				game.setP5NumNoAnswer(newNoAnswerCount);
				game.setP5CurrQDone(true);
				playerFinishedQuestionString = "P5_CURR_Q_DONE";
				playerNoAnswerString = "P5_NUM_NO_ANS";
			}

			else {
				log.severe("Incorrect username: " + username);
				return null;
			}

			String updateStatement = " UPDATE games" + " SET "
					+ playerFinishedQuestionString + " = true, "
					+ playerNoAnswerString + " = ?  WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] {
					newNoAnswerCount, gameId });
			log.info("******GAME DAO" + game.toString());
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

			if (username.equals(game.getPlayer1())) {
				if (game.isP1CurrQDone()) {
					log.info("P1: late wrong");
					return null;
				}
				game.setP1CurrQDone(true);
				newWrongCount = game.getP1NumWrong() + 1;
				game.setP1NumWrong(newWrongCount);
				playerFinishedQuestionString = "P1_CURR_Q_DONE";
				playerWrongAnswerString = "P1_NUM_WRONG";
			} else if (username.equals(game.getPlayer2())) {
				if (game.isP2CurrQDone()) {
					log.info("P2: late wrong");
					return null;
				}
				newWrongCount = game.getP2NumWrong() + 1;
				game.setP2NumWrong(newWrongCount);
				game.setP2CurrQDone(true);
				playerFinishedQuestionString = "P2_CURR_Q_DONE";
				playerWrongAnswerString = "P2_NUM_WRONG";
			} else if (username.equals(game.getPlayer3())) {
				if (game.isP3CurrQDone()) {
					log.info("P3: late wrong");
					return null;
				}
				newWrongCount = game.getP3NumWrong() + 1;
				game.setP3NumWrong(newWrongCount);
				game.setP3CurrQDone(true);
				playerFinishedQuestionString = "P3_CURR_Q_DONE";
				playerWrongAnswerString = "P3_NUM_WRONG";
			} else if (username.equals(game.getPlayer4())) {
				if (game.isP4CurrQDone()) {
					log.info("P4: late wrong");
					return null;
				}
				newWrongCount = game.getP4NumWrong() + 1;
				game.setP4NumWrong(newWrongCount);
				game.setP4CurrQDone(true);
				playerFinishedQuestionString = "P4_CURR_Q_DONE";
				playerWrongAnswerString = "P4_NUM_WRONG";
			} else if (username.equals(game.getPlayer5())) {
				if (game.isP5CurrQDone()) {
					log.info("P5: late wrong");
					return null;
				}
				newWrongCount = game.getP5NumWrong() + 1;
				game.setP5NumWrong(newWrongCount);
				game.setP5CurrQDone(true);
				playerFinishedQuestionString = "P5_CURR_Q_DONE";
				playerWrongAnswerString = "P5_NUM_WRONG";
			}

			else {
				log.severe("Incorrect username: " + username);
				return null;
			}

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
log.info("in set question:" + question);
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

			jdbcTemplate.update(updateStatement,
					new Object[] { topic.getName(), topic.getTopicId() });

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
		log.info("******GAME BEFORE DAO" + game.toString());

		if (game.isP1Ready() & !game.isP1CurrQDone()) {
			game = setPlayerNoAnswer(game.getPlayer1(), gameId);
		}
		if (game.isP2Ready() & !game.isP2CurrQDone()) {
			game = setPlayerNoAnswer(game.getPlayer2(), gameId);
		}
		if (game.isP3Ready() & !game.isP3CurrQDone()) {
			game = setPlayerNoAnswer(game.getPlayer3(), gameId);
		}
		if (game.isP4Ready() & !game.isP4CurrQDone()) {
			game = setPlayerNoAnswer(game.getPlayer4(), gameId);
		}
		if (game.isP5Ready() & !game.isP5CurrQDone()) {
			game = setPlayerNoAnswer(game.getPlayer5(), gameId);
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
