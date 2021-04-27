let stompClient = null;

function connect() {

    let socket = new SockJS('/gs-guide-websocket');
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
    let intervalMillis = $("#interval").html() * 60000; // 1 мин = 60 000 мс
    let endAuctionDate = betDate + intervalMillis; // предполагаемая дата окончания аукциона (если не будет новых ставок)

    let timeLeft = Number((endAuctionDate - backendTime) / 1000).toFixed(0); // осталось до конца торгов, секунд

    if (timeLeft * 1000 > intervalMillis) { //если до конца аукциона осталось > чем интервал ставки (торги еще не начались)

        let secondsLeft = timeLeft % 60; // 1..59
        let minutesLeft = (timeLeft - secondsLeft - intervalMillis / 1000) / 60 % 60; // 1..59
        let hoursLeft = (timeLeft - minutesLeft * 60 - secondsLeft - intervalMillis / 1000) / 3600 % 60; // 1..23
        if(hoursLeft < 6){
            $("#timerText").text('До начала аукциона: ');
            $("#timer").text(hoursLeft + ' ч, ' + minutesLeft + ' м, ' + secondsLeft + ' с');
        }else{
            $("#timerText").text('Дата начала аукциона: ');
            $("#timer").text($("#betDate").html().toString())
        }

    } else { //если до конца аукциона осталось <= чем интервал ставки (торги в процессе)
        $("#timerText").text('До конца аукциона: ');
        $("#timer").text(timeLeft);

        if (timeLeft <= 1) {
            sendStatus('FINISHED');
            location.reload();
        }
    }

}

function getLotId() {
    let url = window.location.pathname;
    let urlParts = url.split('/');
    let idIndex = urlParts.indexOf('lot') + 1;

    return urlParts[idIndex];
}
