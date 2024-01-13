document.addEventListener('DOMContentLoaded', function() {
    // Bind event listener for the "Send Message" button
    document.getElementById('sendMessage').addEventListener('click', sendMessage);

    var stompClient = null;
    var token = '';
    var isCustomer = false;

    document.getElementById('connect').addEventListener('click', function() {
        token = document.getElementById('token').value;
        isCustomer = document.getElementById('isCustomer').checked;

        var socket = new SockJS('/customerChat');
        stompClient = Stomp.over(socket);

        stompClient.connect({
            'Authorization': 'Bearer ' + token,
            'isCustomer': isCustomer
        }, function(frame) {
            console.log('Connected: ' + frame);

            var subscriptionPath = isCustomer ? '/queue/chattings/customers/1' : '/queue/chattings/counselors/1'; //만약 다른 채팅방에 들어가고 싶다면 chatId를 다른 걸로 바꿔야함
            stompClient.subscribe(subscriptionPath, function(message) {
                showMessage(JSON.parse(message.body));
            });

            console.log('Subscribed to ' + subscriptionPath);
        });
    });

    function showMessage(message) {
        var chatWindow = document.getElementById('chatWindow');
        var messageElement = document.createElement('div');
        messageElement.innerHTML = "isCustomer : " + message.isCustomer + " " + message.sendTime + " From " + message.senderName + ": " + message.content;
        chatWindow.appendChild(messageElement);
    }

    function sendMessage() {
        console.log("sendMessage function triggered"); // Debugging line
        var chatId = document.getElementById('chatId').value;
        var messageContent = document.getElementById('messageContent').value;

        if (stompClient && stompClient.connected && chatId) {
            var chatMessage = {content: messageContent};
            stompClient.send('/app/api/v1/chatMessages/' + (isCustomer ? 'customers' : 'counselors') + '/' + chatId, {}, JSON.stringify(chatMessage));
            document.getElementById('messageContent').value = '';
        }
    }
});
