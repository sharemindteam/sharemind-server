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
document.getElementById('connect').addEventListener('click', function() {
    var socket = new SockJS('/customerChat'); // WebSocket 연결 URL로 변경
    var stompClient = Stomp.over(socket);

    stompClient.connect({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiYmJAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOiJST0xFX0NVU1RPTUVSIiwiZXhwIjoxNzA1ODgyMjMwfQ.LraOlUyXzZUUIGONBLZbmqZzXWs3piRE2uL1DbqP4uQ' // 로그인 시 나오는 토큰값으로 변경
    }, function(frame) {
        console.log('Connected: ' + frame);

        // '/chattings' 엔드포인트로 빈 메시지를 보내는 예시
        stompClient.send('/app/chattings', {}, '');
    });
});