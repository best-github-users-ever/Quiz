var stompClient = null;

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
		// sendGameInfo(gameId, userId);
	});
}

function readyPressed(gameId, userId) {
	console.log('in readyPressed' + gameId + userId)
	stompClient.send("/gameApp/joinGame/ready", {}, JSON.stringify({
		'gameId' : gameId,
		'userId' : userId
	}));

}

function answerSubmitted(gameId, userId) {
	console.log('in answerSubmitted' + gameId + userId)
	stompClient.send("/gameApp/joinGame/answer", {}, JSON.stringify({
		'gameId' : gameId,
		'userId' : userId,
		'questionId': $('#questionId').text(),
		'guess' : $("input[name=option]:checked").val(),
		'jsessionId' : $("#JSESSIONID").text()
	}));

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

function processMessage(message) {
	var screenMsg = $('#confirmationMessage');
	var errorMsg = $('#errorMessage');

	if (message.messageName === 'gameFound') {
		screenMsg.text(message.result);

	} else if (message.messageName === 'gameReady') {
		// create button also to indicate ready to start
		screenMsg.text("");
		$('#positivemessage').text("");
		screenMsg.text(message.result);
		setReadyButtonVisiblity(true);

	} else if (message.messageName === 'delayBeforeStart') {
		setReadyButtonVisiblity(false);
		screenMsg.text("");
		$('#positivemessage').text("");

		var counter = parseInt(message.result);

		var interval = setInterval(function() {
			if (counter == 0) {
				clearInterval(interval);
				screenMsg.text("");
			} else {
				screenMsg.text("Game will begin in " + counter);
				counter--;
			}
		}, 1000);

	} else if (message.messageName === 'continueWaiting') {
		errorMsg.text("");
		setReadyButtonVisiblity(false);
		screenMsg.text(message.result);

	} else if (message.messageName === 'playerGuessed') {
		errorMsg.text("");
		setReadyButtonVisiblity(false);
		screenMsg.text(screenMsg.text() + " (" + message.result + ")");

	} else if (message.messageName === 'question') {
		setReadyButtonVisiblity(false);
		errorMsg.text("");
		$('#question').text(message.question.question);
		$('#ansOpt1').text(message.question.option1);
		$('#ansOpt2').text(message.question.option2);
		$('#ansOpt3').text(message.question.option3);
		$('#ansOpt4').text(message.question.option4);
		$('#questionTable').show();
		$('#questionId').text(message.question.questionId);

	} else if (message.messageName === 'answerRight') {
		screenMsg.text(message.result);
		errorMsg.text("");

	}
	else if (message.messageName === 'answerWrong') {
		screenMsg.text("");
		errorMsg.text(message.result);

	}
}
