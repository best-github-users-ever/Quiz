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
		// just testing out STOMP interface for the game
		// not very useful invocation of sendGameInfo since all it does
		// is send the gameId that we got from the server, send it back
		// to the server
		// and get it back a second time to output in a screen message
		sendGameInfo(gameId, userId);
	});
}

function disconnect() {
	stompClient.disconnect();
	setConnected(false);
	console.log("Disconnected");
}
function sendGameInfo(gameId, userId) {
	console.log('in sendGameInfo' + gameId + userId)
	stompClient.send("/gameApp/joinGame", {}, JSON.stringify({
		'gameId' : gameId,
		'userId' : userId
	}));
}
function processMessage(message) {
	var screenMsg = $('#confirmationMessage');
	var screenMsg2 = $('#confirmationMessage2');

	if (message.messageName === 'gameFound') {
		screenMsg.text(message.result);
//		readyButton = $('#readyButton').hide();
	} else if (message.messageName === 'gameReady') {
		// create button also to indicate ready to start
		screenMsg.text("");
		$('#positivemessage').text("");
		screenMsg2.text(message.result);
		var readyButton = $('#readyButton').show();

	}
}

function setButtonVisible(visible) {
	if (visible) {
		$('#readyButton').show();
	} else {
		$('#readyButton').hide();

	}
}
