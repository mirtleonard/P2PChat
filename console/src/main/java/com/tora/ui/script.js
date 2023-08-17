function test() {
    var url = "ws://127.0.0.1:8080/chat/leonard";
    var connection = new WebSocket(url);
    var data = '{"headers": {"type": "message"}, "body": "content"}';
    connection.addEventListener("open", function (event) {
        console.log(event);
        connection.send(data);
    });
}
function connect() {
    var _a, _b, _c;
    //@ts-nocheck
    var ip = (_a = document.getElementById("ip")) === null || _a === void 0 ? void 0 : _a.value;
    //@ts-nocheck
    var port = (_b = document.getElementById("port")) === null || _b === void 0 ? void 0 : _b.value;
    //@ts-nocheck
    var message = (_c = document.getElementById("message")) === null || _c === void 0 ? void 0 : _c.value;
    //@ts-nocheck
    var url = "http://" + ip + ":" + port +'api/chat/connect';
    console.log(url);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url).
        $.post(url, message, function (data, response) { return console.log(response); });
}
