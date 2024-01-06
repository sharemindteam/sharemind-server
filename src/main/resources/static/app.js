var stompClient = null;
var urlParams = new URLSearchParams(window.location.search);
var roomId = urlParams.get('room');

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/chattings/' + roomId, function (chatMessage) {
            var messageData = JSON.parse(chatMessage.body);
            showMessage(messageData.senderName, messageData.content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function sendMessage() {
    var name = document.getElementById('name').value;
    var messageContent = document.getElementById('message').value;
    if (name && messageContent) {
        var chatMessage = {
            senderName: name,
            content: messageContent
        };
        stompClient.send("/app/chattings/" + roomId + "/messages", {}, JSON.stringify(chatMessage));
    }
}

function showMessage(senderName, message) {
    var chat = document.getElementById('chat');
    var text = document.createTextNode(senderName + ": " + message);
    var p = document.createElement('p');
    p.appendChild(text);
    chat.appendChild(p);
}

window.onload = connect;
window.onbeforeunload = disconnect;
