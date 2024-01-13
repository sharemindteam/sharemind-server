var stompClient = null;

window.onload = function () {
    var socket = new SockJS('/customerChat'); // WebSocket 연결 URL로 변경
    stompClient = Stomp.over(socket);

    stompClient.connect({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiYmJAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOiJST0xFX0NVU1RPTUVSIiwiZXhwIjoxNzA2MTI4MDczfQ.qHmvZ6A6nTKdxmJXFlq6-nMG-ELkrUFEacf4ILuXR-M', // 로그인 시 나오는 토큰값으로 변경
        'isCustomer': true
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
        stompClient.send('/app/api/v1/chatMessages/customers/' + chatId, {}, JSON.stringify(chatMessage));
        document.getElementById('messageContent').value = ''; // 메시지 전송 후 입력 필드 초기화
    }
}

function showMessage(message) {
    var chatWindow = document.getElementById('chatWindow');
    var messageElement = document.createElement('div');
    messageElement.innerHTML = "isCustomer : " + message.isCustomer + " " + message.sendTime + "From " + message.senderName + ": " + message.content;
    chatWindow.appendChild(messageElement);
}
