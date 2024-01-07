var stompClient = null;
var urlParams = new URLSearchParams(window.location.search);
var userId = urlParams.get('userId');
var roomId = urlParams.get('room');
var isCustomer = urlParams.get('customer') === 'true';

function connect() {
    var socket = isCustomer ? new SockJS('/customerChat') : new SockJS('/counselorChat');
    stompClient = Stomp.over(socket);
    stompClient.connect({userId: userId, isCustomer: isCustomer}, function (frame) {
        console.log('Connected: ' + frame);
        fetch('/channels?userId=' + userId + '&isCustomer=' + isCustomer)
            .then(response => response.json())
            .then(channelIds => {
                channelIds.forEach(function (channelId) {
                   stompClient.subscribe('/queue/chattings/customers/' + channelId, function (chatMessage) {
                        var messageData = JSON.parse(chatMessage.body);
                        if (roomId == null || roomId === channelId.toString()) {
                            showMessage(messageData.senderName, messageData.content, messageData.sendTime);
                        }
                    });
                    stompClient.subscribe('/queue/chattings/counselors/' + channelId, function (chatMessage) {
                        var messageData = JSON.parse(chatMessage.body);
                        if (roomId == null || roomId === channelId.toString()) {
                            showMessage(messageData.senderName, messageData.content, messageData.sendTime);
                        }
                    });
                });
            })
            .catch(error => console.error('Error fetching channel IDs:', error));
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function sendMessage() {
    var messageContent = document.getElementById('message').value;
    if (messageContent) {
        var chatMessage = {
            content: messageContent
        };
        var destination = isCustomer ? "/app/chattings/customers/" + roomId : "/app/chattings/counselors/" + roomId;
        stompClient.send(destination, {}, JSON.stringify(chatMessage));
    }
}

function showMessage(senderName, message, sendTime) {
    var chat = document.getElementById('chat');
    var text = document.createTextNode(senderName + ": " + message + " (" + sendTime + ")");
    var p = document.createElement('p');
    p.appendChild(text);
    chat.appendChild(p);
}

window.onload = connect;
window.onbeforeunload = disconnect;
