var stompClient = null;


function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (response) {
            showResponse(JSON.parse(response.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendResponse() {
    stompClient.send("/app/hello", {}, JSON.stringify({'bet': $("#inputBet").val()}));
}

function showResponse(response) {
    $("#bet").text(response);
}

$(function () {
    $('#form').on('submit', function (e) {e.preventDefault();});
    $( "#connect" ).click(function() {connect();});
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendResponse(); });
});
