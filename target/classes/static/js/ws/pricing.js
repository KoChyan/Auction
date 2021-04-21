var stompClient = null;

function connect() {

    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);


        stompClient.subscribe('/topic/bets/' + getLotId(), function (pricingResponse) {
            showPricingResponse(pricingResponse);
        });

        stompClient.subscribe('/topic/timer', function (timerResponse) {
            updateTimer(timerResponse);
        });

    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendStatus(status) {
    stompClient.send("/app/pricing", {}, JSON.stringify({
        'id': getLotId(),
        'status': status
    }));
}

function showPricingResponse(pricingResponse) {
    let pricing = JSON.parse(pricingResponse.body);

    $("#bet").text(pricing.bet);
    $("#username").text(pricing.username);

    let date = moment(pricing.date);
    $("#betDate").text(date.format('YYYY.MM.DD H:mm:ss'));
}

function updateTimer(timerResponse) {

    let backendTime = JSON.parse(timerResponse.body).content;

    let betDateString = $("#betDate").html().toString();
    betDateString = betDateString
        .replaceAll('.', '-')
        .replace(' ', 'T');

    let betDate = moment(betDateString);
    let intervalMillis = $("#interval").html() * 60000;
    let endAuctionDate = betDate + intervalMillis;

    let timeLeft = Number((endAuctionDate - backendTime) / 1000).toFixed(0);

    $("#timer").text(timeLeft);

    if (timeLeft <= 270) {
        sendStatus('finished');
        window.location.href = location.href;
    }

}

function getLotId() {
    let url = window.location.pathname;
    let urlParts = url.split('/');
    let idIndex = urlParts.indexOf('lot') + 1;

    return urlParts[idIndex];
}
