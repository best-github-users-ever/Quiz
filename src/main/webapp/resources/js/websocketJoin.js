
        var stompClient = null; 

        function connect(gameId) {
            var socket = new SockJS('/Quiz/joinGame');
			stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                console.log('Joined game: ' + frame);
                stompClient.subscribe('/topic/gameUpdates', function(message){
                	showMessage(JSON.parse(message.body).result);
                });
                //just testing out STOMP interface for the game
                //not very useful invocation of sendGameInfo since all it does
                //is send the gameId that we got from the server, send it back to the server
                //and get it back a second time to output in a screen message
                sendGameInfo(gameId);
            });
        }
        function disconnect() {
            stompClient.disconnect();
            setConnected(false);
            console.log("Disconnected");
        }
        function sendGameInfo(gameId) {
            stompClient.send("/gameApp/joinGame", {}, JSON.stringify({ 'gameId': gameId}));
        }
        function showMessage(message) {
            var screenMsg = $('#confirmationMessage');
            screenMsg.text(message);
            //create button also to indicate ready to start
        }
