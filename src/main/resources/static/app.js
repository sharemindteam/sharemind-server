var stompClient = null;
var urlParams = new URLSearchParams(window.location.search);
var userId = urlParams.get('userId');
var roomId = urlParams.get('room');
var isCustomer = urlParams.get('customer') === 'true';

function connect() {
    // if (!isCustomer) {
    //     console.log('Not a customer, skipping WebSocket connection.');
    //     return;
    // }
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({userId: userId}, function (frame) {
        console.log('Connected: ' + frame);
        fetch('/channels?customerId=' + userId + '&isCustomer=' + isCustomer)
            .then(response => response.json())
            .then(channelIds => {
                // 가져온 채널 ID 목록으로 구독
                channelIds.forEach(function (channelId) {
                    stompClient.subscribe('/queue/chattings/' + channelId, function (chatMessage) {
                        var messageData = JSON.parse(chatMessage.body);
                        if (roomId == null || roomId === channelId.toString()) {
                            showMessage(messageData.senderName, messageData.content);
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
