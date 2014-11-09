$(document).ready(function(){
	init();
})

var totalNumberOfQuestions = 10;
var stompClient = null;
var myUserId = null;
var myGameId = null;
var currentQuestionIdx = 0;
var guessedMessage; // used for temp storage of message when other user guesses
var remainingTime;
var greenTimBox = true;
var screenMsg;
var errorMsg;
var opponentSelectList;
var updateMsg;
var chatMsgField;
var allOptions;

var answerTimer = null;
var questionDisplayTimer = null;
var incrementTime;
var questionDisplayIncrementTime;
var startingTime;
var currentTime; 
var currentQuestionDisplayTime; 

var progressBar1 = null;
var progressBar2 = null;
var progressBar3 = null;
var progressBar4 = null;
var progressBar5 = null;

var userProgressMap = {};

function makeSelection(gameId, userId, guessIdx) {
	var answerTime = stopGetRemaining();
	var button;

	switch (guessIdx) {
	case 0:
		button = $("#option-a-button");
		button.prop("src", "resources/images/quizsmallselected.png");
		break;
	case 1:
		button = $("#option-b-button");
		button.prop("src", "resources/images/quizsmallselected.png");
		break;
	case 2:
		button = $("#option-c-button");
		button.prop("src", "resources/images/quizsmallselected.png");
		break;
	case 3:
		button = $("#option-d-button");
		button.prop("src", "resources/images/quizsmallselected.png");
		break;
	}
	button.effect( "shake", { direction: "up", times: 5, distance: 4}, 500 );

	stompClient.send("/gameApp/joinGame/answer", {}, JSON.stringify({
		'gameId' : gameId,
		'userId' : userId,
		'questionId' : $('#questionId').text(),
		'guess' : guessIdx,
		'answerTime' : answerTime,
		'jsessionId' : $("#JSESSIONID").text()
	}));
	setDisableOfAnswerButtons(true, false);
	// $("#submitButton").prop("disabled", true);

}

$(document).keypress(function(e) {
	if (e.which == 13) {

		if ($.trim($("#chatbox").val()) !== "") {
			sendPressed(myGameId);
		}
		e.preventDefault();
	}
});

function sendPressed(gameId) {
	var filters = [];
	var chatMessage = "Sent";

	$('#opponentSelectList option').each(function() {
		if ($(this).is(':selected')) {
			filters.push(this.value);
		}
	});

	if (filters.length !== 0) {

		chatMessage = chatMessage + " Private Msg to";

		$.each(filters, function(index, value) {
			chatMessage = chatMessage + " " + value;
		});

		stompClient.send("/gameApp/joinGame/chat", {}, JSON.stringify({
			'msgType' : "private",
			'gameId' : gameId,
			'userId' : myUserId,
			'jsessionId' : $("#JSESSIONID").text(),
			'chatMessage' : $("#chatbox").val(),
			'recipients' : filters
		}));
	} else {

		stompClient.send("/gameApp/joinGame/chat", {}, JSON.stringify({
			'msgType' : "public",
			'gameId' : gameId,
			'userId' : myUserId,
			'jsessionId' : $("#JSESSIONID").text(),
			'chatMessage' : $("#chatbox").val()
		}));
	}

	chatMessage = chatMessage + ": ";

	chatMessage = chatMessage + $("#chatbox").val();

	$("#chatbox").val("");

	addTextAfterNode(chatMessage, chatMsgField);

	chatMsgField.scrollTop(chatMsgField[0].scrollHeight);

}

function formatSeconds(milliseconds) {
	var timeInSec = milliseconds / 1000.00;
	return (timeInSec.toFixed(1));
}

function stopGetRemaining() {
	var elapsed = startingTime - currentTime;
	answerTimer.stop();

	// return time took to make guess
	return elapsed;

}

function updateTimer() {

	// remainingTime.text(formatSeconds(currentTime));
	remainingTime.html("Time: " + formatSeconds(currentTime));

	if (greenTimBox && currentTime <= 3000) {
		greenTimBox = false;
		$("#countdown").css("color", "red");
		$("#countdown").css("border", "1px solid red");

	}

	// If answerTimer is complete, trigger alert
	if (currentTime <= 0) {
		// Example2.Timer.stop();
		// Setup the answerTimer
		stopGetRemaining();
		setDisableOfAnswerButtons(true, true);
		// $("#submitButton").prop("disabled", true);

	} else {

		// Increment answerTimer position
		currentTime -= incrementTime;
		if (currentTime < 0) {
			currentTime = 0;
		}
	}
}

function updateQuestionDisplayTimer() {

	// If answerTimer is complete, trigger alert
	if (currentQuestionDisplayTime <= 0) {
		// Setup the answerTimer
		questionDisplayTimer.stop();
		allOptions.show();
		setDisableOfAnswerButtons(false, true);

		remainingTime.show();
		answerTimer.play(true);

	} else {

		// Increment answerTimer position
		currentQuestionDisplayTime -= questionDisplayIncrementTime;
		if (currentQuestionDisplayTime < 0) {
			currentQuestionDisplayTime = 0;
		}
	}
}


var create_progressbar = function(id) {

	// ### 
	// Create Success Process
	// ###

	var progressBarItem1 = {};
	progressBarItem1[ProgressBar.OPTION_NAME.ITEM_ID] = "successData";
	progressBarItem1[ProgressBar.OPTION_NAME.COLOR_ID] = ProgressBar.OPTION_VALUE.COLOR_ID.GREEN;
	progressBarItem1[ProgressBar.OPTION_NAME.POSITION] = "relative";

	// ### 
	// Create Error Process
	// ###
	var progressBarItem2 = {};
	progressBarItem2[ProgressBar.OPTION_NAME.ITEM_ID] = "errorData";
	progressBarItem2[ProgressBar.OPTION_NAME.COLOR_ID] = ProgressBar.OPTION_VALUE.COLOR_ID.RED;
	progressBarItem2[ProgressBar.OPTION_NAME.ALIGN] = ProgressBar.OPTION_VALUE.ALIGN.RIGHT;
	progressBarItem2[ProgressBar.OPTION_NAME.POSITION] = "relative";

	// ### 
	// Create No Response Process
	// ###
	var progressBarItem3 = {};
	progressBarItem3[ProgressBar.OPTION_NAME.ITEM_ID] = "noResponseData";
	progressBarItem3[ProgressBar.OPTION_NAME.COLOR_ID] = ProgressBar.OPTION_VALUE.COLOR_ID.YELLOW;
	progressBarItem3[ProgressBar.OPTION_NAME.ALIGN] = ProgressBar.OPTION_VALUE.ALIGN.RIGHT;
	progressBarItem3[ProgressBar.OPTION_NAME.POSITION] = "relative";

	// ### 
	// Create New ProgressBar
	// ###

	switch (id) {
	case "my-progressbar1":
		progressBar1 = new ProgressBar(id, {
			'width' : '350px',
			'height' : '6px'
		});

		progressBar1.createItem(progressBarItem1);
		progressBar1.createItem(progressBarItem2);
		progressBar1.createItem(progressBarItem3);
		progressBar1.setMaxValue(10);
		progressBar1.setPercent(progressBar1
				.getPercentByValue(0, "successData"), "successData");
		progressBar1.setPercent(progressBar1.getPercentByValue(0, "errorData"),
				"errorData");
		progressBar1.setPercent(progressBar1.getPercentByValue(0,
				"noResponseData"), "noResponseData");

		break;

	case "my-progressbar2":
		progressBar2 = new ProgressBar(id, {
			'width' : '350px',
			'height' : '6px'
		});

		progressBar2.createItem(progressBarItem1);
		progressBar2.createItem(progressBarItem2);
		progressBar2.createItem(progressBarItem3);
		progressBar2.setMaxValue(10);
		progressBar2.setPercent(progressBar2
				.getPercentByValue(0, "successData"), "successData");
		progressBar2.setPercent(progressBar2.getPercentByValue(0, "errorData"),
				"errorData");
		progressBar2.setPercent(progressBar2.getPercentByValue(0,
				"noResponseData"), "noResponseData");

		break;

	case "my-progressbar3":
		progressBar3 = new ProgressBar(id, {
			'width' : '350px',
			'height' : '6px'
		});

		progressBar3.createItem(progressBarItem1);
		progressBar3.createItem(progressBarItem2);
		progressBar3.createItem(progressBarItem3);
		progressBar3.setMaxValue(10);
		progressBar3.setPercent(progressBar3
				.getPercentByValue(0, "successData"), "successData");
		progressBar3.setPercent(progressBar3.getPercentByValue(0, "errorData"),
				"errorData");
		progressBar3.setPercent(progressBar3.getPercentByValue(0,
				"noResponseData"), "noResponseData");

		break;

	case "my-progressbar4":
		progressBar4 = new ProgressBar(id, {
			'width' : '350px',
			'height' : '6px'
		});

		progressBar4.createItem(progressBarItem1);
		progressBar4.createItem(progressBarItem2);
		progressBar4.createItem(progressBarItem3);
		progressBar4.setMaxValue(10);
		progressBar4.setPercent(progressBar4
				.getPercentByValue(0, "successData"), "successData");
		progressBar4.setPercent(progressBar4.getPercentByValue(0, "errorData"),
				"errorData");
		progressBar4.setPercent(progressBar4.getPercentByValue(0,
				"noResponseData"), "noResponseData");

		break;

	case "my-progressbar5":
		progressBar5 = new ProgressBar(id, {
			'width' : '350px',
			'height' : '6px'
		});

		progressBar5.createItem(progressBarItem1);
		progressBar5.createItem(progressBarItem2);
		progressBar5.createItem(progressBarItem3);
		progressBar5.setMaxValue(10);
		progressBar5.setPercent(progressBar5
				.getPercentByValue(0, "successData"), "successData");
		progressBar5.setPercent(progressBar5.getPercentByValue(0, "errorData"),
				"errorData");
		progressBar5.setPercent(progressBar5.getPercentByValue(0,
				"noResponseData"), "noResponseData");

		break;

	default:
		break;
	}

};

function readyPressed(gameId, userId) {

	myUserId = userId;
	myGameId = gameId;
	stompClient.send("/gameApp/joinGame/ready", {}, JSON.stringify({
		'gameId' : gameId,
		'userId' : userId,
		'jsessionId' : $("#JSESSIONID").text()
	}));

}

function answerSubmitted(gameId, userId) {
	var answerTime = stopGetRemaining();
	stompClient.send("/gameApp/joinGame/answer", {}, JSON.stringify({
		'gameId' : gameId,
		'userId' : userId,
		'questionId' : $('#questionId').text(),
		'guess' : $("input[name=option]:checked").val(),
		'answerTime' : answerTime,
		'jsessionId' : $("#JSESSIONID").text()
	}));
	setDisableOfAnswerButtons(true, false);

	// $("#submitButton").prop("disabled", true);

}

function disconnect() {
	stompClient.disconnect();
	setConnected(false);
	console.log("Disconnected");
}

function setReadyButtonVisiblity(visible) {
	if (visible) {
		$('#readyButton').show();
	} else {
		$('#readyButton').hide();

	}
}

function addTextBeforeNode(message, node) {
	var p = $("<p></p>");
	p.text(message);

	node.children().first().css("color", "gray");
	node.prepend(p);
}

function addTextAfterNode(message, node) {
	var p = $("<p></p>");
	p.text(message);

	node.children().last().css("color", "gray");
	node.append(p);
}

function addOpponentToSelectList(message, node) {
	var option = $("<option value='" + message + "'>" + message + "</option>");
	node.append(option);
}

function addOpponentToList(user) {
	if (userProgressMap.myMapAssigned === true) {
		if (userProgressMap.count === 1) {
			// userProgressMap[user] = progressBar2;
			create_progressbar("my-progressbar2");
			userProgressMap[user] = 2;
			$("#my-progressbar-text2").text(user);
		} else if (userProgressMap.count === 2) {
			// userProgressMap[user] = progressBar3;
			create_progressbar("my-progressbar3");
			userProgressMap[user] = 3;
			$("#my-progressbar-text3").text(user);
		} else if (userProgressMap.count === 3) {
			// userProgressMap[user] = progressBar4;
			create_progressbar("my-progressbar4");
			userProgressMap[user] = 4;
			$("#my-progressbar-text4").text(user);
		} else if (userProgressMap.count === 4) {
			// userProgressMap[user] = progressBar5;
			create_progressbar("my-progressbar5");
			userProgressMap[user] = 5;
			$("#my-progressbar-text5").text(user);
		}
	} else {
		if (userProgressMap.count === 0) {
			// userProgressMap[user] = progressBar2;
			create_progressbar("my-progressbar2");
			userProgressMap[user] = 2;
			$("#my-progressbar-text2").text(user);
		} else if (userProgressMap.count === 1) {
			// userProgressMap[user] = progressBar3;
			create_progressbar("my-progressbar3");
			userProgressMap[user] = 3;
			$("#my-progressbar-text3").text(user);
		} else if (userProgressMap.count === 2) {
			// userProgressMap[user] = progressBar4;
			create_progressbar("my-progressbar4");
			userProgressMap[user] = 4;
			$("#my-progressbar-text4").text(user);
		} else if (userProgressMap.count === 3) {
			// userProgressMap[user] = progressBar5;
			create_progressbar("my-progressbar5");
			userProgressMap[user] = 5;
			$("#my-progressbar-text5").text(user);

		}
	}
	userProgressMap.count = userProgressMap.count + 1;
}

function addMyselfToList(inUserId) {
	create_progressbar("my-progressbar1");
	$("#my-progressbar-text1").text(inUserId);
	userProgressMap[inUserId] = 1;
	userProgressMap.myMapAssigned = true;
	userProgressMap.count = userProgressMap.count + 1;
}

var setDisableOfAnswerButtons = function(disabled, force) {
	$("#option-a-button").prop("disabled", disabled);
	$("#option-b-button").prop("disabled", disabled);
	$("#option-c-button").prop("disabled", disabled);
	$("#option-d-button").prop("disabled", disabled);

	if (disabled === false) {
		$("#option-a-button").prop("src", "resources/images/quizsmall.png");
		$("#option-b-button").prop("src", "resources/images/quizsmall.png");
		$("#option-c-button").prop("src", "resources/images/quizsmall.png");
		$("#option-d-button").prop("src", "resources/images/quizsmall.png");
	} else {
		if (force) {
			$("#option-a-button").prop("src", "resources/images/quizsmalldisabled.png");
			$("#option-b-button").prop("src", "resources/images/quizsmalldisabled.png");
			$("#option-c-button").prop("src", "resources/images/quizsmalldisabled.png");
			$("#option-d-button").prop("src", "resources/images/quizsmalldisabled.png");
		} else {
			if (!($("#option-a-button").prop("src").match("quizsmallselected.png"))) {
				$("#option-a-button").prop("src", "resources/images/quizsmalldisabled.png");
			}
			if (!($("#option-b-button").prop("src").match("quizsmallselected.png"))) {
				$("#option-b-button").prop("src", "resources/images/quizsmalldisabled.png");
			}
			if (!($("#option-c-button").prop("src").match("quizsmallselected.png"))) {
				$("#option-c-button").prop("src", "resources/images/quizsmalldisabled.png");
			}
			if (!($("#option-d-button").prop("src").match("quizsmallselected.png"))) {
				$("#option-d-button").prop("src", "resources/images/quizsmalldisabled.png");
			}
		}
	}
}

function processMessage(message) {
	if (message.messageName === 'gameFound') {
		screenMsg.text(message.result);

	} else if (message.messageName === 'gameReady') {
		positiveMsg.text("");
		addTextBeforeNode(message.result, updateMsg);
		setReadyButtonVisiblity(true);

	} else if (message.messageName === 'delayBeforeStart') {

		positiveMsg.text("");
		remainingTime.hide();
		totalNumberOfQuestions = message.totalNumberOfQuestions;

		// init data for mapping between user and the progress bar index
		userProgressMap.myMapAssigned = false;
		userProgressMap.count = 0;

		if ((message.playerList.player1 !== null)
				&& (message.playerList.player1 !== "")) {
			if (message.playerList.player1 !== myUserId) {
				addOpponentToSelectList(message.playerList.player1,
						opponentSelectList);
				addOpponentToList(message.playerList.player1);
			} else {
				addMyselfToList(myUserId);
			}
		}
		if ((message.playerList.player2 !== null)
				&& (message.playerList.player2 !== "")) {
			if (message.playerList.player2 !== myUserId) {
				addOpponentToSelectList(message.playerList.player2,
						opponentSelectList);
				addOpponentToList(message.playerList.player2);
			} else {
				addMyselfToList(myUserId);
			}
		}
		if ((message.playerList.player3 !== null)
				&& (message.playerList.player3 !== "")) {
			if (message.playerList.player3 !== myUserId) {
				addOpponentToSelectList(message.playerList.player3,
						opponentSelectList);
				addOpponentToList(message.playerList.player3);
			} else {
				addMyselfToList(myUserId);
			}
		}
		if ((message.playerList.player4 !== null)
				&& (message.playerList.player4 !== "")) {
			if (message.playerList.player4 !== myUserId) {
				addOpponentToSelectList(message.playerList.player4,
						opponentSelectList);
				addOpponentToList(message.playerList.player4);
			} else {
				addMyselfToList(myUserId);
			}
		}
		if ((message.playerList.player5 !== null)
				&& (message.playerList.player5 !== "")) {
			if (message.playerList.player5 !== myUserId) {
				addOpponentToSelectList(message.playerList.player5,
						opponentSelectList);
				addOpponentToList(message.playerList.player5);
			} else {
				addMyselfToList(myUserId);
			}
		}

		if (message.playerList.numberOfPlayers === 1) {
			// hide the chat window
			$("#content-lower-right").hide();

			// hide unused progress bar fields
			$("#my-progressbar2").hide();
			$("#my-progressbar-text2").hide();
			$("#my-progressbar3").hide();
			$("#my-progressbar-text3").hide();
			$("#my-progressbar4").hide();
			$("#my-progressbar-text4").hide();
			$("#my-progressbar5").hide();
			$("#my-progressbar-text5").hide();
		} else if (message.playerList.numberOfPlayers === 2) {
			$("#my-progressbar3").hide();
			$("#my-progressbar-text3").hide();
			$("#my-progressbar4").hide();
			$("#my-progressbar-text4").hide();
			$("#my-progressbar5").hide();
			$("#my-progressbar-text5").hide();
		} else if (message.playerList.numberOfPlayers === 3) {
			$("#my-progressbar4").hide();
			$("#my-progressbar-text4").hide();
			$("#my-progressbar5").hide();
			$("#my-progressbar-text5").hide();
		} else if (message.playerList.numberOfPlayers === 4) {
			$("#my-progressbar5").hide();
			$("#my-progressbar-text5").hide();
		}

		setReadyButtonVisiblity(false);
		screenMsg.text("");
		positiveMsg.text("");

		var counter = parseInt(message.delayTime, 10);

		var interval = setInterval(function() {
			if (counter === 0) {
				clearInterval(interval);
				screenMsg.text("");
			} else {
				screenMsg.text("Game will begin in " + counter);
				counter = counter - 1;
			}
		}, 1000);

	} else if (message.messageName === 'continueWaiting') {
		errorMsg.text("");
		positiveMsg.text("");
		setReadyButtonVisiblity(false);
		addTextBeforeNode(message.result, updateMsg);

	} else if (message.messageName === 'playerGuessed') {
		guessedMessage = "Question " + currentQuestionIdx + ": "
				+ message.result;
		setReadyButtonVisiblity(false);
		addTextAfterNode(guessedMessage, updateMsg);

		updateMsg.scrollTop(updateMsg[0].scrollHeight);
	} else if (message.messageName === 'question') {
		setReadyButtonVisiblity(false);

		// revert time box back to original color
		greenTimBox = true;
		$("#countdown").css("color", "green");
		$("#countdown").css("border", "1px solid green");

		positiveMsg.text("");
		screenMsg.text("");
		errorMsg.text("");
		$('input:radio[name="option"]').removeAttr('checked');
		$('#question-text').text(message.question.question);
		allOptions.hide();
		$('#option-a').text(message.question.option1);
		$('#option-b').text(message.question.option2);
		$('#option-c').text(message.question.option3);
		$('#option-d').text(message.question.option4);
		$('#questionTable').show();
		$('#questionId').text(message.question.questionId);
		$('#questionNumber').text(
				"Question # " + message.questionNumber + " of "
						+ totalNumberOfQuestions);
		currentQuestionIdx = message.questionNumber;
		currentTime = startingTime;
		currentQuestionDisplayTime = startingQuestionDisplayTime;

		//give 2 seconds to display the question only
		questionDisplayTimer.play(true);

		/*
		setDisableOfAnswerButtons(false, true);

		// $("#submitButton").prop("disabled", false);
		//currentTime = startingTime;
		answerTimer.play(true);
		remainingTime.show();
*/

	} else if ((message.messageName === 'questionResults')
			|| (message.messageName === 'gameResults')) {

		// var progBar;
		var progBarIndex;
		setReadyButtonVisiblity(false);
		setDisableOfAnswerButtons(true, false);

		// $("#submitButton").prop("disabled", true);

		if ((message.game.player1 !== null) && (message.game.player1 !== "")) {
			remainingTime.hide();
			progBarIndex = userProgressMap[message.game.player1];
			switch (progBarIndex) {
			case 1:
				progressBar1.setPercent(progressBar1.getPercentByValue(
						message.game.p1NumCorrect, "successData"),
						"successData");
				progressBar1.setPercent(progressBar1.getPercentByValue(
						message.game.p1NumWrong, "errorData"), "errorData");
				progressBar1.setPercent(progressBar1.getPercentByValue(
						message.game.p1NumNoAnswer, "noResponseData"),
						"noResponseData");

				break;

			case 2:
				progressBar2.setPercent(progressBar2.getPercentByValue(
						message.game.p1NumCorrect, "successData"),
						"successData");
				progressBar2.setPercent(progressBar2.getPercentByValue(
						message.game.p1NumWrong, "errorData"), "errorData");
				progressBar2.setPercent(progressBar2.getPercentByValue(
						message.game.p1NumNoAnswer, "noResponseData"),
						"noResponseData");

				break;

			default:
				break;
			}

		}
		if ((message.game.player2 !== null) && (message.game.player2 !== "")) {
			progBarIndex = userProgressMap[message.game.player2];
			switch (progBarIndex) {
			case 1:
				progressBar1.setPercent(progressBar1.getPercentByValue(
						message.game.p2NumCorrect, "successData"),
						"successData");
				progressBar1.setPercent(progressBar1.getPercentByValue(
						message.game.p2NumWrong, "errorData"), "errorData");
				progressBar1.setPercent(progressBar1.getPercentByValue(
						message.game.p2NumNoAnswer, "noResponseData"),
						"noResponseData");

				break;

			case 2:
				progressBar2.setPercent(progressBar2.getPercentByValue(
						message.game.p2NumCorrect, "successData"),
						"successData");
				progressBar2.setPercent(progressBar2.getPercentByValue(
						message.game.p2NumWrong, "errorData"), "errorData");
				progressBar2.setPercent(progressBar2.getPercentByValue(
						message.game.p2NumNoAnswer, "noResponseData"),
						"noResponseData");

				break;

			default:
				break;
			}

		}
		/*
		 * Add other players after 1/2 player verified
		 * 
		 * if ((message.game.player3 !== null) && (message.game.player3 !== "")) {
		 * progBar = userProgressMap[message.game.player3];
		 * progBar.setPercent(message.game.p3NumCorrect * 10, "successData");
		 * progBar.setPercent(message.game.p3NumWrong * 10, "errorData");
		 * progBar.setPercent(message.game.p3NumNoAnswer * 10,
		 * "noResponseData"); } if ((message.game.player4 !== null) &&
		 * (message.game.player4 !== "")) { progBar =
		 * userProgressMap[message.game.player4];
		 * progBar.setPercent(message.game.p4NumCorrect * 10, "successData");
		 * progBar.setPercent(message.game.p4NumWrong * 10, "errorData");
		 * progBar.setPercent(message.game.p4NumNoAnswer * 10,
		 * "noResponseData"); } if ((message.game.player5 !== null) &&
		 * (message.game.player5 !== "")) { progBar =
		 * userProgressMap[message.game.player5];
		 * progBar.setPercent(message.game.p5NumCorrect * 10, "successData");
		 * progBar.setPercent(message.game.p5NumWrong * 10, "errorData");
		 * progBar.setPercent(message.game.p5NumNoAnswer * 10,
		 * "noResponseData"); }
		 * 
		 */

		if (message.messageName === 'gameResults') {
			var iWon = false;

			addTextAfterNode("Game Over!", updateMsg);
			updateMsg.scrollTop(updateMsg[0].scrollHeight);


			var messageToPrint = "The ";
			var winners;

			if (message.winners.length > 1) {

				for (var winnersIndex = 0; winnersIndex < message.winners.length; winnersIndex++) {
					if (message.winners[winnersIndex] === myUserId) {
						iWon = true;
					}
					winners = winners + ", " + message.winners[winnersIndex];
				}

				winners = winners + "!";

				messageToPrint = messageToPrint + "winners are " + winners;
			} else {
				if (message.winners[0] === myUserId) {
					iWon = true;
				}

				winners = message.winners[0] + "!";

				messageToPrint = messageToPrint + "winner is " + winners;
			}

			if (iWon === true) {
				positiveMsg.text(messageToPrint);
			} else {
				errorMsg.text(messageToPrint);
			}

		}

	} else if (message.messageName === 'answerRight') {
		screenMsg.text(message.result);
		errorMsg.text("");

	} else if (message.messageName === 'answerWrong') {
		screenMsg.text("");
		errorMsg.text(message.result);

	} else if (message.messageName === 'answerLate') {
		screenMsg.text("");
		errorMsg.text(message.result);

	} else if (message.messageName === 'chat') {
		chatMessage = "";

		if (message.chatMsgType === 'private') {
			chatMessage = "Private Msg from ";
		}

		chatMessage = chatMessage + message.senderId + ": "
				+ message.chatMessage;

		addTextAfterNode(chatMessage, chatMsgField);

		chatMsgField.scrollTop(chatMsgField[0].scrollHeight);

	}
}

function connect(gameId, userId) {
	var socket = new SockJS('/Quiz/joinGame');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/queue/' + gameId + '/' + userId
				+ '/gameUpdates', function(message) {
			processMessage(JSON.parse(message.body));
		});
		stompClient.subscribe('/topic/' + gameId + '/gameUpdates', function(
				message) {
			processMessage(JSON.parse(message.body));
		});
	});

}

var init = function() {
	screenMsg = $('#confirmationMessage');
	errorMsg = $('#errorMessage');
	positiveMsg = $('#positivemessage');
	opponentSelectList = $("#opponentSelectList");
	updateMsg = $('#updateMessage');
	chatMsgField = $("#messages");

	remainingTime = $("#countdown");
	allOptions = $("#allOptions");
	
	incrementTime = 80;
	questionDisplayIncrementTime = 2000;
	startingTime = 10000;
	currentTime = 10000; // 10 seconds (in milliseconds)
	startingQuestionDisplayTime = 1500;
	currentQuestionDisplayTime = 1500;
	
	answerTimer = $.timer(updateTimer, incrementTime, false);
	questionDisplayTimer = $.timer(updateQuestionDisplayTimer, questionDisplayIncrementTime, false);

}
