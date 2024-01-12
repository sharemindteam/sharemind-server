// //static 안 파일은 전체 삭제 예정
// var stompClient = null;
// var urlParams = new URLSearchParams(window.location.search);
// var userId = urlParams.get('userId');
// var roomId = urlParams.get('room');
// var isCustomer = urlParams.get('customer') === 'true';
//
// function connect() {
//     var socket = isCustomer ? new SockJS('/customerChat') : new SockJS('/counselorChat');
//     stompClient = Stomp.over(socket);
//     stompClient.connect({userId: userId, isCustomer: isCustomer}, function (frame) {
//         console.log('Connected: ' + frame);
//         fetch('/channels?userId=' + userId + '&isCustomer=' + isCustomer)
//             .then(response => response.json())
//             .then(channelIds => {
//                 channelIds.forEach(function (channelId) {
//                     stompClient.subscribe('/queue/demo/chattings/customers/' + channelId, function (chatMessage) {
//                         var messageData = JSON.parse(chatMessage.body);
//                         if (roomId == null || roomId === channelId.toString()) {
//                             showMessage(messageData.senderName, messageData.content, messageData.sendTime);
//                         }
//                     });
//                     stompClient.subscribe('/queue/demo/chattings/counselors/' + channelId, function (chatMessage) {
//                         var messageData = JSON.parse(chatMessage.body);
//                         if (roomId == null || roomId === channelId.toString()) {
//                             showMessage(messageData.senderName, messageData.content, messageData.sendTime);
//                         }
//                     });
//                 });
//             })
//             .catch(error => console.error('Error fetching channel IDs:', error));
//     });
// }
//
// function disconnect() {
//     if (stompClient !== null) {
//         stompClient.disconnect();
//     }
// }
//
// function sendMessage() {
//     var messageContent = document.getElementById('message').value;
//     if (messageContent) {
//         var chatMessage = {
//             content: messageContent
//         };
//         var destination = isCustomer ? "/app/demo/chattings/customers/" + roomId : "/app/demo/chattings/counselors/" + roomId;
//         stompClient.send(destination, {}, JSON.stringify(chatMessage));
//     }
// }
//
// function showMessage(senderName, message, sendTime) {
//     var chat = document.getElementById('chat');
//     var text = document.createTextNode(senderName + ": " + message + " (" + sendTime + ")");
//     var p = document.createElement('p');
//     p.appendChild(text);
//     chat.appendChild(p);
// }
//
// window.onload = connect;
// window.onbeforeunload = disconnect;
var stompClient = null;

window.onload = function () {
    var socket = new SockJS('/customerChat'); // WebSocket 연결 URL로 변경
    stompClient = Stomp.over(socket);

    stompClient.connect({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYWFAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOiJST0xFX0NVU1RPTUVSIiwiZXhwIjoxNzA1OTgyMTM4fQ.0uSuq0_LJOPTlmDDy4fp90P9R8XZ-EBVEpo9tOAILjE', // 로그인 시 나오는 토큰값으로 변경
        'isCustomer': false
    }, function (frame) {
        console.log('Connected: ' + frame);
        subscribeToChat();
    });

    document.getElementById('sendMessage').addEventListener('click', sendMessage);
};

function subscribeToChat() {
    stompClient.subscribe('/queue/chattings/customers/1', function (message) {
        showMessage(JSON.parse(message.body));
    });
}

function sendMessage() {
    var chatId = document.getElementById('chatId').value;
    var messageContent = document.getElementById('messageContent').value;

    if (stompClient && stompClient.connected && chatId) {
        var chatMessage = {content: messageContent};
        stompClient.send('/app/chatMessage/customers/' + chatId, {}, JSON.stringify(chatMessage));
        document.getElementById('messageContent').value = ''; // 메시지 전송 후 입력 필드 초기화
    }
}

function showMessage(message) {
    var chatWindow = document.getElementById('chatWindow');
    var messageElement = document.createElement('div');
    messageElement.innerHTML = "isCustomer : " + message.isCustomer + " " + message.sendTime + "From " + message.senderName + ": " + message.content;
    chatWindow.appendChild(messageElement);
}
