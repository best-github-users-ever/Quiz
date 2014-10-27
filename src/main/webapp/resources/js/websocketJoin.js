var stompClient = null;
var myUserId = null;

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
	myUserId = userId;
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
		'questionId' : $('#questionId').text(),
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

function addTextBeforeNode(message, node) {
	var p = $("<p></p>");
	p.text(message);

	node.children().first().css("color", "gray")
	node.prepend(p);
}

function addOpponentToSelectList(message, node) {
	var option = $("<option value='" + message + "'>" + message + "</option>");
	node.append(option);
}

function addOpponentToList(message, node) {
	var p = $("<p id='player_" + message + "'></p>");
	node.append(p);
}

function addMyselfToList(message, node) {
	var p = $("<p id='player_" + message + "' class='my_user_statistics'></p>");
	node.prepend(p);
}

function processMessage(message) {
	var screenMsg = $('#confirmationMessage');
	var errorMsg = $('#errorMessage');
	var updateMsg = $('#updateMessage');
	var opponentSelectList = $("#opponentSelectList");
	var playerList = $("#playerList");

	if (message.messageName === 'gameFound') {
		screenMsg.text(message.result);

	} else if (message.messageName === 'gameReady') {
		$('#positivemessage').text("");
		addTextBeforeNode(message.result, updateMsg);
		setReadyButtonVisiblity(true);

	} else if (message.messageName === 'delayBeforeStart') {

		$('#positivemessage').text("");

		if ((message.playerList.player1 != null)
				& (message.playerList.player1 != "")) {
			addOpponentToSelectList(message.playerList.player1,
					opponentSelectList);
			addOpponentToList(message.playerList.player1, playerList);
		}
		if ((message.playerList.player2 != null)
				& (message.playerList.player2 != "")) {
			addOpponentToSelectList(message.playerList.player2,
					opponentSelectList);
			addOpponentToList(message.playerList.player2, playerList);
		}
		if ((message.playerList.player3 != null)
				& (message.playerList.player3 != "")) {
			addOpponentToSelectList(message.playerList.player3,
					opponentSelectList);
			addOpponentToList(message.playerList.player3, playerList);
		}
		if ((message.playerList.player4 != null)
				& (message.playerList.player4 != "")) {
			addOpponentToSelectList(message.playerList.player4,
					opponentSelectList);
			addOpponentToList(message.playerList.player4, playerList);
		}

		if ((message.playerList.player1 === null)
				| (message.playerList.player1 === "")) {
			$("#content-upper-right").hide();
			$("#content-lower-right").hide();
		}

		setReadyButtonVisiblity(false);
		screenMsg.text("");
		$('#positivemessage').text("");

		var counter = parseInt(message.delayTime);

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
		$('#positivemessage').text("");
		setReadyButtonVisiblity(false);
		addTextBeforeNode(message.result, updateMsg);

	} else if (message.messageName === 'playerGuessed') {
		setReadyButtonVisiblity(false);
		addTextBeforeNode(message.result, updateMsg);

	} else if (message.messageName === 'question') {
		setReadyButtonVisiblity(false);
		$('#positivemessage').text("");
		errorMsg.text("");
		$('#question').text(message.question.question);
		$('#ansOpt1').text(message.question.option1);
		$('#ansOpt2').text(message.question.option2);
		$('#ansOpt3').text(message.question.option3);
		$('#ansOpt4').text(message.question.option4);
		$('#questionTable').show();
		$('#questionId').text(message.question.questionId);

	} else if (message.messageName === 'questionResults') {
		setReadyButtonVisiblity(false);
		// $('#positivemessage').text("");
		errorMsg.text("");
		if ((message.game.player1 != null) & (message.game.player1 != "")) {
			var player_id = "#player_" + message.game.player1;
			if (message.game.player1 === myUserId) {

				addMyselfToList(myUserId, playerList);
			}
			$(player_id).text(
					"'" + message.game.player1 + "' : "
							+ message.game.p1NumCorrect + "/"
							+ message.game.currQIndex);
		}
		if ((message.game.player2 != null) & (message.game.player2 != "")) {
			var player_id = "#player_" + message.game.player2;
			if (message.game.player2 === myUserId) {

				addMyselfToList(myUserId, playerList);
			}
			$(player_id).text(
					"'" + message.game.player2 + "' : "
							+ message.game.p2NumCorrect + "/"
							+ message.game.currQIndex);
		}
		if ((message.game.player3 != null) & (message.game.player3 != "")) {
			var player_id = "#player_" + message.game.player3;
			if (message.game.player3 === myUserId) {

				addMyselfToList(myUserId, playerList);
			}
			$(player_id).text(
					"'" + message.game.player3 + "' : "
							+ message.game.p3NumCorrect + "/"
							+ message.game.currQIndex);

		}
		if ((message.game.player4 != null) & (message.game.player4 != "")) {
			var player_id = "#player_" + message.game.player4;
			if (message.game.player4 === myUserId) {

				addMyselfToList(myUserId, playerList);
			}
			$(player_id).text(
					"'" + message.game.player4 + "' : "
							+ message.game.p4NumCorrect + "/"
							+ message.game.currQIndex);

		}
		if ((message.game.player5 != null) & (message.game.player5 != "")) {
			var player_id = "#player_" + message.game.player5;
			if (message.game.player5 === myUserId) {

				addMyselfToList(myUserId, playerList);
			}
			$(player_id).text(
					"'" + message.game.player5 + "' : "
							+ message.game.p5NumCorrect + "/"
							+ message.game.currQIndex);

		}

		if ((message.game.player2 != null) & (message.game.player2 != "")) {
			$("#playerList").show();
		}
		
	} else if (message.messageName === 'answerRight') {
		screenMsg.text(message.result);
		errorMsg.text("");

	} else if (message.messageName === 'answerWrong') {
		screenMsg.text("");
		errorMsg.text(message.result);

	}
}
