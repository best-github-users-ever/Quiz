package com.quiz.quizcontroller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.opensymphony.xwork2.ActionContext;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.Topic;
import com.quiz.model.User;
import com.quiz.socket.controller.JoinGameWebSocketController;
import com.quiz.socket.gamelogic.GameResult;

@Controller
public class QuizController implements Serializable, BeanFactoryAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6321821026030843446L;
	private static Logger log = Logger.getLogger(QuizController.class);
	private SimpMessagingTemplate template;

	@RequestMapping("/")
	public ModelAndView welcomeAction() {

		ModelAndView model = new ModelAndView("login");

		return model;
	}

	@RequestMapping("/request-topics-u.action")
	public ModelAndView goToTopicsAction() {

		ModelAndView model = new ModelAndView("topics-u");

		return model;
	}

	@RequestMapping("/login-again.action")
	public ModelAndView loginAgainAction() {

		ModelAndView model = new ModelAndView("login");

		return model;
	}

	@RequestMapping("/admin-u.action")
	public ModelAndView adminAction(HttpServletRequest request) {

		IQuizDbAccess dao = DBAccess.getDbAccess();

		ModelAndView model = new ModelAndView("admin");
		
		request.setAttribute("adminTopicsList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
	}

	@RequestMapping("/logout-u.action")
	public ModelAndView logoutAction(HttpSession session) {

		session.removeAttribute("user");

		ModelAndView model = new ModelAndView("login");

		return model;
	}

	@RequestMapping(value = "/request-new-account.action")
	public ModelAndView requestNewAccountAction() {

		ModelAndView model = new ModelAndView("newAccount");

		return model;
	}

	@RequestMapping(value = "/new-account.action", method = RequestMethod.POST)
	public ModelAndView newAccountAction(@ModelAttribute("user") User user,
			HttpServletRequest request) {

		ModelAndView model = new ModelAndView("login");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		if (!dao.addUser(user)) {
			request.setAttribute("reqErrorMessage",
					"Username '" + user.getUserId() + "' already exists.");

		} else {
			request.setAttribute("reqPositiveMessage",
					"User " + user.getUserId() + " added! Please login.");
		}

		// @ModelAttribute added user to model. remove it so user will login
		model.getModelMap().remove("user");

		return model;
	}

	@RequestMapping(value = "/show-hint.action", method = RequestMethod.GET)
	public ModelAndView showHintAction(HttpServletRequest request,
			@RequestParam("userId") String userId) {

		log.info("in showHintAction.");
		log.info("userid:" + userId);
		ModelAndView model = new ModelAndView();
		model.setViewName("/login");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		String hint = dao.showHint(userId);

		if ("".equals(hint)) {
			request.setAttribute("reqErrorMessage",
					"No hint available for Username of '" + userId
							+ "'. This may not be a valid user.");

		} else {
			request.setAttribute("reqPositiveMessage", "Hint: '" + hint + "'");
			model.addObject("userName", userId.trim());

		}

		return model;
	}

	@RequestMapping(value = "/login.action", method = RequestMethod.POST)
	public ModelAndView loginAction(@ModelAttribute("user") User user,
			HttpServletRequest request, HttpSession session) {

		log.info("in LoginAction::in execute.");
		log.info(user.toString());

		ModelAndView model = new ModelAndView();

		// ensure old user removed from session
		session.removeAttribute("user");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		User localUser = dao.getUser(user);

		if (localUser != null) {

			model.setViewName("topics-u");
			session.setAttribute("user", localUser);

		} else {
			model.setViewName("login");
			model.addObject("userName", user.getUserId().trim());

			request.setAttribute("reqErrorMessage",
					"Username Password combination is not correct");
			// @ModelAttribute added user to model. remove it so user will login
			model.getModelMap().remove("user");
		}

		// remove from request to prevent jsp from using it.
		request.removeAttribute("user");

		return model;
	}

	@RequestMapping(value = "/chooseQuizTopic-u.action", method = RequestMethod.POST)
	public ModelAndView chooseQuizTopicAction(
			@RequestParam("topicId") int topicId,
			@RequestParam("numberPlayers") int numberPlayers,
			HttpServletRequest request, HttpSession session) {

		ModelAndView model = new ModelAndView();

		log.info("in chooseQuizTopicAction");
		log.info("topicid:" + topicId);

		IQuizDbAccess dao = DBAccess.getDbAccess();
		log.info("value of dao is null:" + (dao == null));

		session.removeAttribute("question");
		model.addObject("numberPlayers", numberPlayers);

		Game game = null;

		String thisUserId = ((User) session.getAttribute("user")).getUserId();

		game = dao.findGameForNewPlayer(topicId, numberPlayers, thisUserId);

		if (game != null) {

			session.setAttribute("game", game);

			request.setAttribute("gameFound", true);

			session.setAttribute("JSESSIONID", session.getId());

			if (game.getTotalPlayers() == game.getNumPlayers()) {

				request.setAttribute("allPlayersFound", true);

				if (game.getTotalPlayers() > 1) {

					String message = null;
					String opponentNameList = "";

					List<String> opponentList = dao.getOtherPlayerUserIds(
							game.getGameId(), thisUserId);

					if (opponentList != null) {
						if (opponentList.size() > 1) {
							message = "Game with users ";

							for (String user : opponentList) {
								opponentNameList = opponentNameList + "'"
										+ user + "' ";
							}

							message = message + opponentNameList
									+ "can now begin!";

						} else {
							message = "Game with user '" + opponentList.get(0)
									+ "' can now begin!";
						}

						request.setAttribute("reqPositiveMessage", message);

					}

					// send messages to other players
					for (String recipient : opponentList) {
						JoinGameWebSocketController
								.sendGameReadyMessageToOpponent(template,
										game.getGameId(), recipient);
					}
				}

				Question question = dao.getRandomQuestion(topicId);

				if (question != null) {
					model.setViewName("quiz-u");

					return model;
				} else {
					// no questions for topic

					model.setViewName("topics-u");
					request.setAttribute("reqErrorMessage",
							"No Questions available for this topic");
					return model;
				}

			} else {

				Question question = dao.getRandomQuestion(topicId);

				if (question != null) {
					model.setViewName("quiz-u");

				} else {
					// no questions for topic

					model.setViewName("topics-u");
					request.setAttribute("reqErrorMessage",
							"No Questions available for this topic");
					return model;
				}

				request.setAttribute("reqPositiveMessage",
						"Game Found! Please wait until other players join.");
				return model;
			}
		} else {
			model.setViewName("topics-u");
			request.setAttribute("reqErrorMessage",
					"Error finding a game for selected Topic and number of players");
			return model;
		}

	}

	@RequestMapping(value = "/ready-u.action", method = RequestMethod.POST)
	public ModelAndView startQuizAction(HttpServletRequest request,
			HttpSession session) {

		ModelAndView model = new ModelAndView();

		log.info("in ready action");
		IQuizDbAccess dao = DBAccess.getDbAccess();

		Question question = null;

		String user = ((User) session.getAttribute("user")).getUserId();
		Game game = (Game) session.getAttribute("game");

		game = dao.setPlayerReady(user, game.getGameId());

		if (game != null) {
			// update game with ready indication
			session.setAttribute("game", game);
		} else {
			model.setViewName("topics-u");
			request.setAttribute("reqErrorMessage",
					"Error updating ready status. Please choose topic again.");
			return model;
		}

		if (dao.allPlayersReady(game.getGameId())) {
			question = dao.getRandomQuestion(game.getTopicId());
		} else {
			model.setViewName("quiz-u");

			return model;

		}

		if (question != null) {
			model.setViewName("quiz-u");

			session.setAttribute("questionNumber", 1);
			session.setAttribute("totalNumberOfQuestions",
					Game.NUMBER_QUESTIONS_PER_GAME);

			// JoinGameWebSocketController.sendQuestion(template,
			// game.getGameId(), 1, Game.NUMBER_QUESTIONS_PER_GAME, question);

			return model;
		} else {
			// no questions for topic

			model.setViewName("topics-u");
			request.setAttribute("reqErrorMessage",
					"No Questions available for this topic");
			return model;
		}
	}

	@RequestMapping(value = "/answerQuestion-u.action", method = RequestMethod.POST)
	public ModelAndView answerQuestionAction(
			@RequestParam("option") int option, HttpServletRequest request,
			HttpSession session) {

		ModelAndView model = new ModelAndView();
		model.setViewName("quiz-u");

		log.info("in answerQuestionAction");
		log.info("option:" + option);

		Question question = (Question) session.getAttribute("question");

		if (question != null) {

			IQuizDbAccess dao = DBAccess.getDbAccess();
			String thisUserId = ((User) session.getAttribute("user"))
					.getUserId();
			Game game = (Game) session.getAttribute("game");

			List<String> opponentList = dao.getOtherPlayerUserIds(
					game.getGameId(), thisUserId);

			if (question.getAnswerIdx() == option) {
				request.setAttribute("reqPositiveMessage", "Correct!");

				// 'randomly' get a question from the topic.
				// should really get one we're sure that hasn't been asked yet.
				// for now just picks one at random & may be the same one
				// repeatedly
				Question nextQuestion = dao.getRandomQuestion(question
						.getTopicId());

				if (nextQuestion != null) {
					model.setViewName("quiz-u");
					session.setAttribute("question", nextQuestion);

					return model;
				} else {
					// no questions for topic

					model.setViewName("topics-u");
					request.setAttribute("reqErrorMessage",
							"No Questions available for this topic");
					return model;
				}

			} else {

				request.setAttribute("reqErrorMessage", "Wrong!");
			}

		} else {
			// no questions for topic

			request.setAttribute("reqErrorMessage", "Question Not Found");
		}

		return model;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// kludge. should be able to find this in the context but w/ the msg
		// broker
		// initialized w/ annotations not sure where the websocket context is
		// defined.
		// it's separate from the context.xml or web.xml one as it can't be
		// found
		// via the context path lookup. so, finding it in the bean factory.
		template = (SimpMessagingTemplate) beanFactory
				.getBean("brokerMessagingTemplate");

	}

	@RequestMapping(value = "/new-topic.action", method = RequestMethod.POST)
	public ModelAndView newTopicAction(@ModelAttribute("topic") Topic topic,
			HttpServletRequest request, HttpSession session) {

		ModelAndView model = new ModelAndView("admin");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		int newTopic = dao.addTopic(topic);
		if (newTopic < 0) {
			request.setAttribute("reqErrorMessage", "Topic '" + topic.getName()
					+ "' already exists.");

		} else {
			session.setAttribute("topicList", dao.getTopics());

			request.setAttribute("reqPositiveMessage",
					"Topic '" + topic.getName() + "' added with topic ID of "
							+ newTopic + ".");

			String path = session.getServletContext().getRealPath(
					"/WEB-INF/db_additions.sql");
			dao.updateFlatFileWithTopic(topic.getName(), path);
		}

		return model;
	}

	@RequestMapping(value = "/new-question.action", method = RequestMethod.POST)
	public ModelAndView newTopicAction(
			@ModelAttribute("question") Question question,
			HttpServletRequest request, HttpSession session) {

		ModelAndView model = new ModelAndView("admin");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		if (!question.getQuestion().trim().endsWith("?")) {
			question.setQuestion(question.getQuestion() + "?");
		}

		int newQuestion = dao.addQuestion(question);
		if (newQuestion < 0) {
			request.setAttribute("reqErrorMessage",
					"Question '" + question.getQuestion() + "' already exists.");

		} else {

			request.setAttribute("reqPositiveMessage",
					"Question '" + question.getQuestion()
							+ "' added with Question ID of " + newQuestion
							+ ".");

			String path = session.getServletContext().getRealPath(
					"/WEB-INF/db_additions.sql");
			dao.updateFlatFileWithQuestion(question, path);
		}

		return model;
	}

	@RequestMapping(value = "/delete-question.action/{questionId}", method = RequestMethod.GET)
	public ModelAndView deleteQuestionAction(@PathVariable(value = "questionId") int questionId, HttpServletRequest request){
		IQuizDbAccess dao = DBAccess.getDbAccess();
		
		ModelAndView model = new ModelAndView("admin");
		
		dao.deleteQuestion(questionId);
		
		request.setAttribute("reqPositiveMessage",
				"Question " + questionId + " was deleted.");
		
		request.setAttribute("adminTopicsList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
}
	

	@RequestMapping(value = "/delete-topic.action/{topicId}", method = RequestMethod.GET)
	public ModelAndView deleteTopicAction(@PathVariable(value = "topicId") int topicId, HttpServletRequest request){
		IQuizDbAccess dao = DBAccess.getDbAccess();
		
		ModelAndView model = new ModelAndView("admin");
		
		if (dao.deleteTopic(topicId)){
			request.setAttribute("reqPositiveMessage",
					"Topic " + topicId + " was deleted.");
						
		} else {
			request.setAttribute("reqErrorMessage",
					"Topic " + topicId + " can't be deleted until all questions with that topic are deleted.");
			

		}
		
		request.setAttribute("adminTopicsList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
}
	
	
	// ************* REST Methods below *************

	@RequestMapping(value = "/users", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<String> newAccountRest(@RequestBody User user,
			HttpServletRequest request) {

		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		if (!dao.addUser(user)) {
			message = "Username " + user.getUserId() + " already exists.";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CONFLICT);

		} else {
			message = "User '" + user.getUserId()
					+ "' was successfully created!";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CREATED);

		}

	}

	@RequestMapping(value = "/users/login", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<Map<String, String>> loginRest(@RequestBody User tempUser,
			HttpServletRequest request, HttpSession session) {

		String sessionId = "";
		Map<String, String> parameterMap = new LinkedHashMap<String, String>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		// ensure old user removed from session
		session.removeAttribute("user");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		User localUser = dao.getUser(tempUser);

		if (localUser != null) {

			session.setAttribute("user", localUser);

			parameterMap.put("JSESSIONID", session.getId());
			parameterMap.put("maxPlayers", "5");

			List<Topic> topics = dao.getTopics();
			for (Topic topic : topics) {
				parameterMap.put("topic" + topic.getTopicId(), topic.getName());
			}

			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.OK);

		} else {

			parameterMap.put("", "");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.UNAUTHORIZED);

		}

	}

	@RequestMapping(value = "/users/hint", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<String> hintRest(@RequestBody User tempUser,
			HttpServletRequest request, HttpSession session) {

		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		// ensure old user removed from session
		session.removeAttribute("user");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		message = dao.showHint(tempUser.getUserId());

		if (!"".equals(message)) {

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.OK);

		} else {

			message = "User " + tempUser.getUserId() + " not found";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.UNAUTHORIZED);

		}

	}

	@RequestMapping(value = "/topics", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<String> newTopicRest(@RequestBody Topic topic,
			HttpServletRequest request, HttpSession session) {

		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		int newTopicId = dao.addTopic(topic);

		if (newTopicId < 0) {
			message = "Topic '" + topic.getName() + "' already exists.";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CONFLICT);

		} else {
			message = "/topics/" + newTopicId;

			session.setAttribute("topicList", dao.getTopics());

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CREATED);

		}

	}

	@RequestMapping(value = "/questions", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<String> newQuestionRest(@RequestBody Question question,
			HttpServletRequest request) {

		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		int newQuestionId = dao.addQuestion(question);

		if (newQuestionId < 0) {
			message = "Question '" + question.getQuestion()
					+ "' already exists.";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CONFLICT);

		} else {
			message = "/questions/" + newQuestionId;

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CREATED);

		}

	}

	@RequestMapping(value = "/questions", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Question> queryQuestionsRest() {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		List<Question> questions = dao.getQuestions();
		return questions;
	}

	@RequestMapping(value = "/topics", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Topic> queryTopicsRest() {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		List<Topic> topics = dao.getTopics();
		return topics;
	}

	@RequestMapping(value = "/topics/{topicId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	ResponseEntity<String> deleteTopicRest(
			@PathVariable(value = "topicId") int topicId) {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = "";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		dao.deleteTopic(topicId);
		
		//ignore any rc from deleteTopic & always return NO CONTENT for completion
		return new ResponseEntity<String>(message, httpHeaders,
				HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/questions/{questionId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	ResponseEntity<String> deleteQuestionRest(
			@PathVariable(value = "questionId") int questionId) {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = "";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		dao.deleteQuestion(questionId);
		
		//ignore any rc from deleteTopic & always return NO CONTENT for completion
		return new ResponseEntity<String>(message, httpHeaders,
				HttpStatus.NO_CONTENT);
	}

}
