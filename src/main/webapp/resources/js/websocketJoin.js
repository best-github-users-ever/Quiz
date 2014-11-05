var totalNumberOfQuestions = 10;
var stompClient = null;
var myUserId = null;
var currentQuestionIdx = 0;
var guessedMessage; // used for temp storage of message when other user
// guesses

var timer = null;
var remainingTime;
var incrementTime = 80;
var startingTime = 10000;
var currentTime = 10000; // 10 seconds (in milliseconds

var progressBar1 = null;
var progressBar2 = null;
var progressBar3 = null;
var progressBar4 = null;
var progressBar5 = null;

var userProgressMap = {};

function formatSeconds(milliseconds) {
	var seconds = Math.floor(milliseconds / 1000);
	var milli = Math.floor(milliseconds % 1000);
	return ( seconds + "." + milli);
}

function stopGetRemaining() {
	var elapsed = startingTime - currentTime;
	timer.stop();

	// return time took to make guess
	return elapsed;

}

function updateTimer() {

	// remainingTime.text(formatSeconds(currentTime));
	remainingTime.html(formatSeconds(currentTime));

	// If timer is complete, trigger alert
	if (currentTime <= 0) {
		// Example2.Timer.stop();
		// Setup the timer
		stopGetRemaining();
		$("#submitButton").prop("disabled", true);

	} else {

		// Increment timer position
		currentTime -= incrementTime;
		if (currentTime < 0) {
			currentTime = 0;
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
			'width' : '300px',
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
			'width' : '300px',
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
			'width' : '300px',
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
			'width' : '300px',
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
			'width' : '300px',
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
	console.log('in readyPressed' + gameId + userId);
	remainingTime = $("#countdown");
	myUserId = userId;
	stompClient.send("/gameApp/joinGame/ready", {}, JSON.stringify({
		'gameId' : gameId,
		'userId' : userId,
		'jsessionId' : $("#JSESSIONID").text()
	}));

}

function answerSubmitted(gameId, userId) {
	console.log('in answerSubmitted' + gameId + userId)
	var answerTime = stopGetRemaining();
	stompClient.send("/gameApp/joinGame/answer", {}, JSON.stringify({
		'gameId' : gameId,
		'userId' : userId,
		'questionId' : $('#questionId').text(),
		'guess' : $("input[name=option]:checked").val(),
		'answerTime' : answerTime,
		'jsessionId' : $("#JSESSIONID").text()
	}));
	$("#submitButton").prop("disabled", true);

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

function processMessage(message) {
	var screenMsg = $('#confirmationMessage');
	var errorMsg = $('#errorMessage');
	var updateMsg = $('#updateMessage');
	var opponentSelectList = $("#opponentSelectList");

	if (message.messageName === 'gameFound') {
		screenMsg.text(message.result);

	} else if (message.messageName === 'gameReady') {
		$('#positivemessage').text("");
		addTextBeforeNode(message.result, updateMsg);
		setReadyButtonVisiblity(true);

	} else if (message.messageName === 'delayBeforeStart') {

		timer = $.timer(updateTimer, incrementTime, false);

		$('#positivemessage').text("");
		totalNumberOfQuestions = message.totalNumberOfQuestions;

		// create_progressbars();
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
		$('#positivemessage').text("");

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
		$('#positivemessage').text("");
		setReadyButtonVisiblity(false);
		addTextBeforeNode(message.result, updateMsg);

	} else if (message.messageName === 'playerGuessed') {
		guessedMessage = "Question " + currentQuestionIdx + ": "
				+ message.result;
		setReadyButtonVisiblity(false);
		addTextBeforeNode(guessedMessage, updateMsg);

	} else if (message.messageName === 'question') {
		setReadyButtonVisiblity(false);
		$('#positivemessage').text("");
		screenMsg.text("");
		errorMsg.text("");
		$('input:radio[name="option"]').removeAttr('checked');
		$('#question').text(message.question.question);
		$('#ansOpt1').text(message.question.option1);
		$('#ansOpt2').text(message.question.option2);
		$('#ansOpt3').text(message.question.option3);
		$('#ansOpt4').text(message.question.option4);
		$('#questionTable').show();
		$('#questionId').text(message.question.questionId);
		$('#questionNumber')
				.text(
						"# " + message.questionNumber + " of "
								+ totalNumberOfQuestions);
		$("#submitButton").prop("disabled", false);
		currentTime = startingTime;
		timer.play(true);
		remainingTime.show();
		currentQuestionIdx = message.questionNumber;

	} else if ((message.messageName === 'questionResults')
			|| (message.messageName === 'gameResults')) {

		// var progBar;
		var progBarIndex;
		setReadyButtonVisiblity(false);
		$("#submitButton").prop("disabled", true);

		if ((message.game.player1 !== null) && (message.game.player1 !== "")) {
			remainingTime.hide();
			progBarIndex = userProgressMap[message.game.player1];
			switch (progBarIndex) {
			case 1:
				console.log("num correct p1-" + message.game.p1NumCorrect * 10
						+ " old value: "
						+ progressBar1.getPercent("successData"));
				;
				console
						.log("num wrong p1-" + message.game.p1NumWrong * 10
								+ " old value: "
								+ progressBar1.getPercent("errorData"));
				console.log("num no resp p1-" + message.game.p1NumNoAnswer * 10
						+ " old value: "
						+ progressBar1.getPercent("noResponseData"));
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
				console.log("num correct p1-" + message.game.p1NumCorrect * 10
						+ " old value: "
						+ progressBar2.getPercent("successData"));
				;
				console
						.log("num wrong p1-" + message.game.p1NumWrong * 10
								+ " old value: "
								+ progressBar2.getPercent("errorData"));
				console.log("num no resp p1-" + message.game.p1NumNoAnswer * 10
						+ " old value: "
						+ progressBar2.getPercent("noResponseData"));
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
				console.log("num correct p2-" + message.game.p2NumCorrect * 10
						+ " old value: "
						+ progressBar1.getPercent("successData"));
				;
				console
						.log("num wrong p2-" + message.game.p2NumWrong * 10
								+ " old value: "
								+ progressBar1.getPercent("errorData"));
				console.log("num no resp p2-" + message.game.p2NumNoAnswer * 10
						+ " old value: "
						+ progressBar1.getPercent("noResponseData"));
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
				console.log("num correct p2-" + message.game.p2NumCorrect * 10
						+ " old value: "
						+ progressBar2.getPercent("successData"));
				;
				console
						.log("num wrong p2-" + message.game.p2NumWrong * 10
								+ " old value: "
								+ progressBar2.getPercent("errorData"));
				console.log("num no resp p2-" + message.game.p2NumNoAnswer * 10
						+ " old value: "
						+ progressBar2.getPercent("noResponseData"));
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

			addTextBeforeNode("Game Over!", updateMsg);

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
				$('#positivemessage').text(messageToPrint);
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

	}
}

function connect(gameId, userId) {
	var socket = new SockJS('/Quiz/joinGame');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log('Joined game: ' + frame);
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
