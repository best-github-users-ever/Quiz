Quiz
====

quiz with web interface

This project was intended as a way to get more familiar with Websockets and JavaScript. It is modeled after QuizUp except that it allows for more simultaneous players and uses a web interface. The Android interface is planned for later. The majority of the game uses STOMP over Websockets so it should be adaptable to Android or other clients.  The login, account creation, topic selection and admin (addition/deletion/edit of topics and questions) use Spring MVC.  There are also a REST APIs for these functions.  That interface is described below.

The following is provided:
- account creation
- login (access to topic/question addition for user 'admin'
- topic selection (very small set of topics & questions. Needs many more to make it remotely interesting).
- handshake among players. Game created against a topic and number of players and players wait until all players have joined.  All players notified of their opponents and need to indicate their readiness to start.
- 5 second countdown before first question
- chat allowed at any point after players notified of their opponents. Can be a general message to all players or a private message to selected players.
- question is shown for a few seconds followed by the Answer options.
- 10 second digital clock counts down.
- elapsed time is sent in with the selected answer
- message displayed immediately whether it is right or wrong
- progress bar displays green starting from left for correct answers and yellow (no answer) and red (wrong) from right.
- game updates (other players guessing, game over message) are displayed in Game Updates section of screen, most recent messages at bottom.
- any reloading of page or leaving of the quiz page will cause websocket to be reset/previous game lost to that player.

Needed changes:
- Android interface
- currently elapsed time to answer is only used as a tie breaker at the end of the game.  It would be better to indicate during the game which player has the time edge...possibly giving more weight to quick answer than to a slow right answer.

WebSocket interface: documentation TBD

REST Interface is below:
All requests are of content type JSON and all accept JSON

-------------------------------------------------------------------------------
**Add User**
/Quiz/users is a POST and it has the following JSON structure:
{
"user" : {
               "userId": "test",
                "password" : "test2",
                "hint" : "myhint",
                "emailAddress" : "test@test.com"
        },
                "confirmPassword" : "test2"

  }
}

The response is either:
1)

Status Code: 201 Created
Msg Body:

User test was successfully created!

or 2)

Status Code: 409 Conflict
Msg Body:

Username test already exists.

-------------------------------------------------------------------------------
**Login**
/Quiz/users/login is a POST and looks like...
{   "userId": "test",
    "password" : "test2"
  }

The response is either:
1)

Status Code: 200 OK
Msg body:

{"JSESSIONID":"18A753F0A419A41D17623F3ED014E69B","maxPlayers":"5","topic1":"Sports","topic2":"Monster Movies","topic3":"Arthur","topic4":"Horror Movies","topic5":"Magic School Bus","topic6":"Grays Anatomy","topic7":"Scrubs","topic12":"my new topic 24"}

or 2)

Status Code: 401 Unauthorized
Msg body:

{"message":"User/Password combination is not valid."}



-------------------------------------------------------------------------------
**choose topic and number of players (after successful login)**
/Quiz/game is a POST and looks like...

{
    "userId": "test",
    "topicId" : "3",
    "password" : "test2",
    "jsessionId" : "18A753F0A419A41D17623F3ED014E69B",
    "numberPlayers" : "1"
}

The response is either:
1)

Status Code: 200 OK
Msg body:

{"gameId":"9","allPlayersFound":"true"}

or 2)

Status Code: 200 OK

Msg body:
{"gameId":"11","message":"Game Found--Waiting for other players to join."}

or 3)

Status Code: 401 Unauthorized
Msg body:

{"message":"User/Password combination is not valid."}

or 4)
Status Code: 400 Bad Request
Msg body:

{"message":"Invalid topicId."}

or 5)
Status Code: 400 Bad Request
Msg body:
{"message":"Invalid numberPlayers."}



-------------------------------------------------------------------------------
**Get Hint for user**
/Quiz/users/hint is a POST and looks like...
{    "userId": "test"
  }

The response is either:
1)

Status Code: 200 OK
Msg body:

myhint

or 2)

Status Code: 400 Bad Request
Msg body:

User 'badtest' not found


-------------------------------------------------------------------------------
**Add Question**
/Quiz/questions is a POST and it has the following JSON structure:

{     "topicId"  : "7",
      "question": "This is my question ?",
       "option1"  : "yes it is",
      "option2"  : "no it is not",
      "option3"  : "none of these",
      "option4"  : "all of these",
      "answerIdx"  : "0"
  }

The response is either:
1)

Status Code: 201 Created
Msg Body:

/questions/12

or 2)

Status Code: 409 Conflict
Msg Body:

Question 'This is my question ?' already exists.

or 3)

Status Code: 400 Bad Request
Msg body:
Option 1 must be included.

(similarly for text fields)

-------------------------------------------------------------------------------
**Delete Question**
/Quiz/questions/# is a DELETE (# is the questionid of the question to be deleted)

The response is always:
1)

Status Code: 204 NO CONTENT

-------------------------------------------------------------------------------
**Add Topic**
/Quiz/topics is a POST and it has the following JSON structure:
{               "name": "test topic"
  }

The response is either:
1)

Status Code: 201 Created
Msg Body:

/topics/7

or 2)

Status Code: 409 Conflict
Msg Body:

Topic 'test topic' already exists.

or 3)

Status Code: 400 Bad Request
Msg Body:

Topic Name must be included.


-------------------------------------------------------------------------------
**Delete Topic**
/Quiz/topics/# is a DELETE (# is the topicid of the topic to be deleted)

The response is always:
1)

Status Code: 204 NO CONTENT

**Show Topics**
/Quiz/topics is a GET
example output:
[
  {
    "topicId": 1,
    "name": "Sports"
  },
  {
    "topicId": 2,
    "name": "Monster Movies"
  },
...
]

-------------------------------------------------------------------------------
**Show Questions**
/Quiz/questions is a GET

example output:
[
  {
    "questionId": 1,
    "topicId": 1,
    "question": "What sport was played by Arthur Ashe ?",
    "option1": "Football",
    "option2": "Baseball",
    "option3": "Tennis",
    "option4": "Auto Racing",
    "answerIdx": 2
  },
...
]

