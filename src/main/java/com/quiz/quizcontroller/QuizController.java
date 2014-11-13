package com.quiz.quizcontroller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.servlet.ModelAndView;

import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.listener.HttpSessionCollector;
import com.quiz.model.Game;
import com.quiz.model.GameToBeginREST;
import com.quiz.model.Question;
import com.quiz.model.Topic;
import com.quiz.model.User;
import com.quiz.model.UserToVerifyREST;
import com.quiz.socket.controller.JoinGameWebSocketController;

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
			@RequestParam("confirmPassword") String confirmPassword,
			HttpServletRequest request) {

		ModelAndView model = new ModelAndView("login");

		// Validate input fields

		if ((user.getUserId() == null) || ("".equals(user.getUserId().trim()))) {
			request.setAttribute("reqErrorMessage",
					"A Username must be entered.");
			model.setViewName("newAccount");
			return model;
		}

		if ((user.getPassword() == null) || ("".equals(user.getPassword().trim()))) {
			request.setAttribute("reqErrorMessage",
					"A Password must be entered.");
			model.setViewName("newAccount");
			return model;
		}

		if (!user.getPassword().equals(confirmPassword)) {
			request.setAttribute("reqErrorMessage",
					"The passwords must match. Please try again.");
			model.setViewName("newAccount");
			return model;
		}

		if ((user.getHint() == null) || ("".equals(user.getHint().trim()))) {
			request.setAttribute("reqErrorMessage",
					"A Password Hint must be entered.");
			model.setViewName("newAccount");
			return model;
		}

		// Access the DB

		IQuizDbAccess dao = DBAccess.getDbAccess();

		if (!dao.addUser(user)) {
			request.setAttribute("reqErrorMessage",
					"Username '" + user.getUserId() + "' already exists.");
			model.setViewName("newAccount");

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
		ModelAndView model = new ModelAndView("login");

		if ((userId == null) || ("".equals(userId.trim()))) {
			request.setAttribute("reqErrorMessage",
					"A Username must be entered.");
			return model;
		}

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

		log.info("in LoginAction");
		log.info(user.toString());

		ModelAndView model = new ModelAndView();

		// Validate input fields

		if ((user.getUserId() == null) || ("".equals(user.getUserId().trim()))) {
			request.setAttribute("reqErrorMessage",
					"A Username must be entered.");
			return model;
		}

		if ((user.getPassword() == null) || ("".equals(user.getPassword().trim()))) {
			request.setAttribute("reqErrorMessage",
					"A Password must be entered.");
			return model;
		}

		// ensure old user removed from session
		session.removeAttribute("user");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		User localUser = dao.getUser(user);

		if (localUser != null) {

			Boolean editUserRequest = (Boolean) session
					.getAttribute("editUserRequest");

			if ((editUserRequest != null) && (editUserRequest == true)) {

				session.removeAttribute("editUserRequest");

				model.setViewName("editUser");
				session.setAttribute("user", localUser);

			} else {
				model.setViewName("topics-u");
				session.setAttribute("user", localUser);
			}

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

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// should be able to find this in the context but w/ the msg broker
		// initialized w/ annotations not sure where the websocket context is
		// defined. it's separate from the context.xml or web.xml one as it
		// can't be
		// found via the context path lookup. so, finding it in the bean
		// factory.
		template = (SimpMessagingTemplate) beanFactory
				.getBean("brokerMessagingTemplate");

	}

	@RequestMapping(value = "/edit-user-req-u.action", method = RequestMethod.GET)
	public ModelAndView editUserReqAction(HttpServletRequest request,
			HttpSession session) {

		ModelAndView model = new ModelAndView("login");

		request.setAttribute("reqPositiveMessage",
				"User must log in again to perform this activity");

		session.setAttribute("editUserRequest", true);

		return model;
	}

	@RequestMapping(value = "/edit-user-u.action", method = RequestMethod.POST)
	public ModelAndView editUserAction(@ModelAttribute("user") User user,
			@RequestParam("confirmPassword") String confirmPassword,
			HttpServletRequest request, HttpSession session) {

		ModelAndView model = new ModelAndView("topics-u");

		// Validate input fields

		if ((user.getUserId() == null) || ("".equals(user.getUserId().trim()))) {
			model.setViewName("editUser");
			request.setAttribute("reqErrorMessage",
					"A Username must be entered.");
			return model;
		}

		if ((user.getPassword() == null) || ("".equals(user.getPassword().trim()))) {
			model.setViewName("editUser");
			request.setAttribute("reqErrorMessage",
					"A Password must be entered.");
			return model;
		}

		if (!user.getPassword().equals(confirmPassword)) {
			model.setViewName("editUser");
			request.setAttribute("reqErrorMessage",
					"The passwords must match. Please try again.");
			return model;
		}

		if ((user.getHint() == null) || ("".equals(user.getHint().trim()))) {
			model.setViewName("editUser");
			request.setAttribute("reqErrorMessage",
					"A Password Hint must be entered.");
			return model;
		}

		User sessionUser = (User) session.getAttribute("user");

		if ((user != null) && user.getUserId().equals(sessionUser.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		IQuizDbAccess dao = DBAccess.getDbAccess();

		User localUser = dao.setUser(user);

		localUser = dao.setUser(user);

		session.setAttribute("user", localUser);
		request.setAttribute("reqPositiveMessage",
				"User '" + localUser.getUserId() + "' updated.");

		return model;
	}

	// ************* Admin actions below *************

	@RequestMapping("/admin.action")
	public ModelAndView adminAction(HttpServletRequest request,
			HttpSession session) {

		User user = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView("admin");

		if ((user != null) & "admin".equals(user.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		IQuizDbAccess dao = DBAccess.getDbAccess();

		request.setAttribute("topicList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
	}

	@RequestMapping(value = "/admin-edit-question-req.action/{questionId}", method = RequestMethod.GET)
	public ModelAndView adminEditQuestionReqAction(
			@PathVariable(value = "questionId") int questionId,
			HttpServletRequest request, HttpSession session) {

		User user = (User) session.getAttribute("user");

		ModelAndView model = new ModelAndView("admin");

		if ((user != null) & "admin".equals(user.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		IQuizDbAccess dao = DBAccess.getDbAccess();

		Question question = dao.getQuestionFromQuestionId(questionId);

		if (question == null) {
			request.setAttribute("reqErrorMessage", "Question '" + questionId
					+ "' doesn't exist.");

		} else {
			model.setViewName("adminEditQuestion");

			request.setAttribute("question", question);

		}

		return model;
	}

	@RequestMapping(value = "/admin-edit-question.action", method = RequestMethod.POST)
	public ModelAndView adminEditQuestionAction(
			@ModelAttribute(value = "question") Question question,
			HttpServletRequest request, HttpSession session) {

		User user = (User) session.getAttribute("user");

		ModelAndView model = new ModelAndView("admin");

		if ((user != null) & user.getUserId().equals("admin")) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		// Validate input fields

		if ((question.getQuestion() == null)
				|| ("".equals(question.getQuestion().trim()))) {
			model.setViewName("adminEditQuestion");
			request.setAttribute("reqErrorMessage",
					"A Question must be entered.");
			return model;
		}

		if ((question.getOption1() == null)
				|| ("".equals(question.getOption1().trim()))) {
			model.setViewName("adminEditQuestion");
			request.setAttribute("reqErrorMessage", "Option 1 must be entered.");
			return model;
		}

		if ((question.getOption2() == null)
				|| ("".equals(question.getOption2().trim()))) {
			model.setViewName("adminEditQuestion");
			request.setAttribute("reqErrorMessage", "Option 2 must be entered.");
			return model;
		}

		if ((question.getOption3() == null)
				|| ("".equals(question.getOption3().trim()))) {
			model.setViewName("adminEditQuestion");
			request.setAttribute("reqErrorMessage", "Option 3 must be entered.");
			return model;
		}

		if ((question.getOption4() == null)
				|| ("".equals(question.getOption3().trim()))) {
			model.setViewName("adminEditQuestion");
			request.setAttribute("reqErrorMessage", "Option 4 must be entered.");
			return model;
		}

		// Access the DB

		IQuizDbAccess dao = DBAccess.getDbAccess();

		Question localQuestion = dao.getQuestionFromQuestionId(question
				.getQuestionId());

		if (localQuestion == null) {
			request.setAttribute("reqErrorMessage",
					"Question '" + question.getQuestionId()
							+ "' doesn't exist.");
		} else {
			localQuestion = dao.setQuestion(question);
			request.setAttribute("reqPositiveMessage", "Question '"
					+ localQuestion.getQuestionId() + "' updated.");
		}

		request.setAttribute("topicList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
	}

	@RequestMapping(value = "/admin-edit-topic-req.action/{topicId}", method = RequestMethod.GET)
	public ModelAndView adminEditTopicReqAction(
			@PathVariable(value = "topicId") int topicId,
			HttpServletRequest request, HttpSession session) {

		User user = (User) session.getAttribute("user");

		ModelAndView model = new ModelAndView("admin");

		if ((user != null) & "admin".equals(user.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		IQuizDbAccess dao = DBAccess.getDbAccess();

		Topic topic = dao.getTopicFromTopicId(topicId);
		if (topic == null) {
			request.setAttribute("reqErrorMessage", "Topic '" + topicId
					+ "' doesn't exist.");

		} else {
			model.setViewName("adminEditTopic");

			request.setAttribute("topic", topic);

		}

		return model;
	}

	@RequestMapping(value = "/admin-edit-topic.action", method = RequestMethod.POST)
	public ModelAndView adminEditTopicAction(
			@ModelAttribute(value = "topic") Topic topic,
			HttpServletRequest request, HttpSession session) {

		User user = (User) session.getAttribute("user");

		ModelAndView model = new ModelAndView("admin");

		if ((user != null) & "admin".equals(user.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		// Validate the input field

		if ((topic.getName() == null) || ("".equals(topic.getName().trim()))) {
			model.setViewName("adminEditTopic");
			request.setAttribute("reqErrorMessage",
					"A Topic Name must be entered.");
			return model;
		}

		IQuizDbAccess dao = DBAccess.getDbAccess();

		Topic localTopic = dao.getTopicFromTopicId(topic.getTopicId());

		if (localTopic == null) {
			request.setAttribute("reqErrorMessage", "Topic '" + localTopic
					+ "' doesn't exist.");
		} else {
			localTopic = dao.setTopic(topic);
			request.setAttribute("reqPositiveMessage",
					"Topic '" + localTopic.getName() + "' updated.");
		}

		request.setAttribute("topicList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
	}

	@RequestMapping(value = "/admin-new-topic.action", method = RequestMethod.POST)
	public ModelAndView newTopicAction(@ModelAttribute("topic") Topic topic,
			HttpServletRequest request, HttpSession session) {

		User user = (User) session.getAttribute("user");

		ModelAndView model = new ModelAndView("admin");

		if ((user != null) & "admin".equals(user.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		// Validate the input field

		if ((topic.getName() == null) || ("".equals(topic.getName().trim()))) {
			request.setAttribute("reqErrorMessage",
					"A Topic Name must be entered.");
			return model;
		}

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

		request.setAttribute("topicList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
	}

	@RequestMapping(value = "/admin-new-question.action", method = RequestMethod.POST)
	public ModelAndView newTopicAction(
			@ModelAttribute("question") Question question,
			HttpServletRequest request, HttpSession session) {

		ModelAndView model = new ModelAndView("admin");

		User user = (User) session.getAttribute("user");

		if ((user != null) & "admin".equals(user.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		// Validate input fields

		if ((question.getQuestion() == null)
				|| ("".equals(question.getQuestion().trim()))) {
			request.setAttribute("reqErrorMessage",
					"A Question must be entered.");
			return model;
		}

		if ((question.getOption1() == null)
				|| ("".equals(question.getOption1().trim()))) {
			request.setAttribute("reqErrorMessage", "Option 1 must be entered.");
			return model;
		}

		if ((question.getOption2() == null)
				|| ("".equals(question.getOption2().trim()))) {
			request.setAttribute("reqErrorMessage", "Option 2 must be entered.");
			return model;
		}

		if ((question.getOption3() == null)
				|| ("".equals(question.getOption3().trim()))) {
			request.setAttribute("reqErrorMessage", "Option 3 must be entered.");
			return model;
		}

		if ((question.getOption4() == null)
				|| ("".equals(question.getOption4().trim()))) {
			request.setAttribute("reqErrorMessage", "Option 4 must be entered.");
			return model;
		}

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

		request.setAttribute("topicList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
	}

	@RequestMapping(value = "/admin-delete-question.action/{questionId}", method = RequestMethod.GET)
	public ModelAndView deleteQuestionAction(
			@PathVariable(value = "questionId") int questionId,
			HttpServletRequest request, HttpSession session) {
		IQuizDbAccess dao = DBAccess.getDbAccess();

		ModelAndView model = new ModelAndView("admin");

		User user = (User) session.getAttribute("user");

		if ((user != null) & "admin".equals(user.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		dao.deleteQuestion(questionId);

		request.setAttribute("reqPositiveMessage", "Question " + questionId
				+ " was deleted.");

		request.setAttribute("topicList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
	}

	@RequestMapping(value = "/admin-delete-topic.action/{topicId}", method = RequestMethod.GET)
	public ModelAndView deleteTopicAction(
			@PathVariable(value = "topicId") int topicId,
			HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView("admin");

		User user = (User) session.getAttribute("user");

		if ((user != null) & "admin".equals(user.getUserId())) {
			// do nothing. continue.
		} else {
			// send back to login screen.
			model.setViewName("login");
			return model;
		}

		IQuizDbAccess dao = DBAccess.getDbAccess();

		if (dao.deleteTopic(topicId)) {
			request.setAttribute("reqPositiveMessage", "Topic " + topicId
					+ " was deleted.");

		} else {
			request.setAttribute(
					"reqErrorMessage",
					"Topic "
							+ topicId
							+ " can't be deleted until all questions with that topic are deleted.");

		}

		request.setAttribute("topicList", dao.getTopics());
		request.setAttribute("adminQuestionsList", dao.getQuestions());

		return model;
	}

	// ************* REST Methods below *************

	// create a new user
	@RequestMapping(value = "/users", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<String> newAccountRest(@RequestBody UserToVerifyREST userToVerify,
			HttpServletRequest request) {

		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		if ((userToVerify.getUser().getUserId() == null) || ("".equals(userToVerify.getUser().getUserId().trim()))) {
			message = "A Username must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((userToVerify.getUser().getPassword() == null) || ("".equals(userToVerify.getUser().getPassword().trim()))) {
			message = "A Password must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if (!userToVerify.getUser().getPassword().equals(userToVerify.getConfirmPassword())) {
			message = "Password and Conirmation Password do not match.";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((userToVerify.getUser().getHint() == null) || ("".equals(userToVerify.getUser().getHint().trim()))) {
			message = "A Password Hint must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if (!dao.addUser(userToVerify.getUser())) {
			message = "Username " + userToVerify.getUser().getUserId() + " already exists.";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CONFLICT);

		} else {
			message = "User '" + userToVerify.getUser().getUserId()
					+ "' was successfully created!";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CREATED);

		}

	}

	// update a user
	@RequestMapping(value = "/users", method = RequestMethod.PUT, headers = "content-type=application/json")
	ResponseEntity<String> updateAccountRest(@RequestBody User user,
			@RequestParam("confirmPassword") String confirmPassword,
			HttpServletRequest request) {

		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		if ((user.getUserId() == null) || ("".equals(user.getUserId().trim()))) {
			message = "A Username must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((user.getPassword() == null) || ("".equals(user.getPassword().trim()))) {
			message = "A Password must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if (!user.getPassword().equals(confirmPassword)) {
			message = "Password and Conirmation Password do not match.";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((user.getHint() == null) || ("".equals(user.getHint().trim()))) {
			message = "A Password Hint must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

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

	// log a user in
	@RequestMapping(value = "/users/login", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<Map<String, String>> loginRest(@RequestBody User tempUser,
			HttpServletRequest request, HttpSession session) {

		Map<String, String> parameterMap = new LinkedHashMap<String, String>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		// ensure old user removed from session
		session.removeAttribute("user");

		if ((tempUser.getUserId() == null)
				|| ("".equals(tempUser.getUserId().trim()))) {
			parameterMap.put("message", "A Username must be included.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.BAD_REQUEST);
		}

		if ((tempUser.getPassword() == null)
				|| ("".equals(tempUser.getPassword().trim()))) {
			parameterMap.put("message", "A Password must be included.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.BAD_REQUEST);
		}

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
			parameterMap.put("message", "User/Password combination is not valid.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.UNAUTHORIZED);

		}

	}

	// retrieve a user's hint
	@RequestMapping(value = "/users/hint", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<String> hintRest(@RequestBody User tempUser,
			HttpServletRequest request, HttpSession session) {

		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		// ensure old user removed from session
		session.removeAttribute("user");

		if ((tempUser.getUserId() == null)
				|| ("".equals(tempUser.getUserId().trim()))) {
			message = "A Username must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		IQuizDbAccess dao = DBAccess.getDbAccess();
		
		message = dao.showHint(tempUser.getUserId());

		if (message != null) {

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.OK);

		} else {

			message = "User '" + tempUser.getUserId() + "' not found";

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);

		}

	}

	// create a new question
	@RequestMapping(value = "/questions", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<String> newQuestionRest(@RequestBody Question question,
			HttpServletRequest request) {

		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		log.info("********** question: " + question);

		if ((question.getQuestion() == null)
				|| ("".equals(question.getQuestion().trim()))) {
			message = "A Question must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getOption1() == null)
				|| ("".equals(question.getOption1().trim()))) {
			message = "Option 1 must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getOption2() == null)
				|| ("".equals(question.getOption2().trim()))) {
			message = "Option 2 must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getOption3() == null)
				|| ("".equals(question.getOption3().trim()))) {
			message = "Option 3 must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getOption4() == null)
				|| ("".equals(question.getOption4().trim()))) {
			message = "Option 4 must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getAnswerIdx() < 0) || (question.getAnswerIdx() > 3)) {
			message = "Invalid Answer Index.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

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

	// create a new topic
	@RequestMapping(value = "/topics", method = RequestMethod.POST, headers = "content-type=application/json")
	ResponseEntity<String> newTopicRest(@RequestBody Topic topic,
			HttpServletRequest request, HttpSession session) {

		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		if ((topic.getName() == null) || ("".equals(topic.getName().trim()))) {
			message = "Topic Name must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

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

	// retrieve questions
	@RequestMapping(value = "/questions", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Question> queryQuestionsRest() {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		List<Question> questions = dao.getQuestions();
		return questions;
	}

	// retrieve topics
	@RequestMapping(value = "/topics", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Topic> queryTopicsRest() {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		List<Topic> topics = dao.getTopics();
		return topics;
	}

	// delete a question
	@RequestMapping(value = "/questions/{questionId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	ResponseEntity<String> deleteQuestionRest(
			@PathVariable(value = "questionId") int questionId) {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = "";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		dao.deleteQuestion(questionId);

		// ignore any rc from deleteTopic & always return NO CONTENT for
		// completion
		return new ResponseEntity<String>(message, httpHeaders,
				HttpStatus.NO_CONTENT);
	}

	// delete a topic
	@RequestMapping(value = "/topics/{topicId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	ResponseEntity<String> deleteTopicRest(
			@PathVariable(value = "topicId") int topicId) {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = "";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		dao.deleteTopic(topicId);

		// ignore any rc from deleteTopic & always return NO CONTENT for
		// completion
		return new ResponseEntity<String>(message, httpHeaders,
				HttpStatus.NO_CONTENT);
	}

	// update existing question
	@RequestMapping(value = "/questions", method = RequestMethod.PUT, headers = "Accept=application/json")
	ResponseEntity<String> updateQuestionRest(
			@RequestParam(value = "question") Question question) {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = "";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		if ((question.getQuestion() == null)
				|| ("".equals(question.getQuestion().trim()))) {
			message = "A Question must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getOption1() == null)
				|| ("".equals(question.getOption1().trim()))) {
			message = "Option 1 must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getOption2() == null)
				|| ("".equals(question.getOption2().trim()))) {
			message = "Option 2 must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getOption3() == null)
				|| ("".equals(question.getOption3().trim()))) {
			message = "Option 3 must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getOption4() == null)
				|| ("".equals(question.getOption4().trim()))) {
			message = "Option 4 must be included.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		if ((question.getAnswerIdx() < 0) || (question.getAnswerIdx() > 3)) {
			message = "Invalid Answer Index.";
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.BAD_REQUEST);
		}

		Question localQuestion = dao.getQuestionFromQuestionId(question
				.getQuestionId());

		if (localQuestion == null) {
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CONFLICT);

		} else {
			dao.setQuestion(question);

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.OK);

		}
	}

	// update existing topic
	@RequestMapping(value = "/topics", method = RequestMethod.PUT, headers = "Accept=application/json")
	ResponseEntity<String> updateTopicRest(
			@RequestParam(value = "topic") Topic topic) {
		IQuizDbAccess dao = DBAccess.getDbAccess();
		String message = "";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		Topic localTopic = dao.getTopicFromTopicId(topic.getTopicId());

		if (localTopic == null) {
			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.CONFLICT);

		} else {
			dao.setTopic(topic);

			return new ResponseEntity<String>(message, httpHeaders,
					HttpStatus.OK);

		}
	}

	@RequestMapping(value = "/game", method = RequestMethod.POST)
	ResponseEntity<Map<String, String>> joinGameREST(@RequestBody GameToBeginREST gameToBegin,
			HttpSession session) {
		
		String userId = gameToBegin.getUserId();
		int topicId = gameToBegin.getTopicId();
		String password = gameToBegin.getPassword();
		String JSESSIONID = gameToBegin.getJsessionId();
		int numberPlayers = gameToBegin.getNumberPlayers();
		

		Map<String, String> parameterMap = new LinkedHashMap<String, String>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);

		IQuizDbAccess dao = DBAccess.getDbAccess();

		Game game = null;

		log.info("in joinGameREST");
		log.info("topicid:" + topicId);

		if ((userId == null)
				|| ("".equals(userId.trim()))) {
			parameterMap.put("message", "A Username must be included.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.BAD_REQUEST);
		}

		if ((password == null)
				|| ("".equals(password.trim()))) {
			parameterMap.put("message", "A Password must be included.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.BAD_REQUEST);
		}
		
		if ((JSESSIONID == null)
				|| ("".equals(JSESSIONID.trim()))) {
			parameterMap.put("message", "A JSESSIONID parameter must be included.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.BAD_REQUEST);
		}
		
		User myUser = new User();

		myUser.setUserId(userId);
		;
		myUser.setPassword(password);

		myUser = dao.getUser(myUser);

		if (myUser == null) {
			// user didn't pass authentication
			parameterMap.put("message",
					"User/Password combination is not valid.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.UNAUTHORIZED);
		}

		if (session.getId() != JSESSIONID) {
			HttpSession tempSession = HttpSessionCollector.find(JSESSIONID);
			log.info("Session ID and received JSESSIONID are different. Using JSESSIONID if available");
			if (tempSession != null) {
				session = tempSession;
			}

			session.setAttribute("user", myUser);
		}
		
		if ((numberPlayers < 1) || (numberPlayers > Game.MAX_PLAYERS_PER_GAME)) {
			// invalid topicId
			parameterMap.put("message",
					"Invalid numberPlayers.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.BAD_REQUEST);
			}

		Topic testTopic = dao.getTopicFromTopicId(topicId);
		if (testTopic == null) {
			// invalid topicId
			parameterMap.put("message",
					"Invalid topicId.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.BAD_REQUEST);
			}

		String thisUserId = ((User) session.getAttribute("user")).getUserId();

		game = dao.findGameForNewPlayer(topicId, numberPlayers, thisUserId);

		if (game != null) {

			session.setAttribute("game", game);

			session.setAttribute("gameFound", true);

			// Based on this gameId parameter, we're expecting the client to
			// open a Websocket and continue the game using that connection.
			// Client expected to either send a /joinGame/ready if 
			// allPlayersFound is true.  Otherwise, client waits over the
			// socket for the gameReady message.
			
			parameterMap.put("gameId", String.valueOf(game.getGameId()));

			if (game.getTotalPlayers() == game.getNumPlayers()) {

				parameterMap.put("allPlayersFound", "true");

				session.setAttribute("allPlayersFound", true);

				if (game.getTotalPlayers() > 1) {

					List<String> opponentList = dao.getOtherPlayerUserIds(
							game.getGameId(), thisUserId);

					String opponentListString = "";

					for (String opponent : opponentList) {
						opponentListString = opponentListString + " "
								+ opponent;
					}

					parameterMap.put("opponentList", opponentListString);

					// send messages to other players
					for (String recipient : opponentList) {
						JoinGameWebSocketController
								.sendGameReadyMessageToOpponent(template,
										game.getGameId(), recipient);
					}
				}

				Question question = dao.getRandomQuestion(topicId);

				if (question != null) {
					return new ResponseEntity<Map<String, String>>(
							parameterMap, httpHeaders, HttpStatus.OK);
				} else {
					// no questions for topic

					parameterMap.clear();
					parameterMap.put("message",
							"No Questions available for this topic.");
					return new ResponseEntity<Map<String, String>>(
							parameterMap, httpHeaders,
							HttpStatus.SERVICE_UNAVAILABLE);
				}

			} else {

				Question question = dao.getRandomQuestion(topicId);

				if (question != null) {
					parameterMap.put("message",
							"Game Found--Waiting for other players to join.");
					return new ResponseEntity<Map<String, String>>(
							parameterMap, httpHeaders, HttpStatus.OK);

				} else {
					// no questions for topic

					parameterMap.put("message",
							"No Questions available for this topic.");
					return new ResponseEntity<Map<String, String>>(
							parameterMap, httpHeaders,
							HttpStatus.SERVICE_UNAVAILABLE);
				}

			}
		} else {
			parameterMap
					.put("message",
							"Error finding a game for selected Topic and number of players.");
			return new ResponseEntity<Map<String, String>>(parameterMap,
					httpHeaders, HttpStatus.SERVICE_UNAVAILABLE);
		}

	}
	
}
