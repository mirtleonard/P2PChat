function test() {
            const url: string = "ws://127.0.0.1:8080/chat/leonard";
            const connection: WebSocket = new WebSocket(url);
            const data: string = '{"headers": {"type": "message"}, "body": "content"}';
            connection.addEventListener("open", (event) => {
                            console.log(event);
                            connection.send(data);
                        });
            
}

function connect() {
        //@ts-nocheck
        const ip : String = document.getElementById("ip")?.value;
        //@ts-nocheck
        const port : String = document.getElementById("port")?.value;
        //@ts-nocheck
        const message = document.getElementById("message")?.value;
        //@ts-nocheck
        const url: string = "ws://" + ip + ":" + port +  + '/chat/leonard';
        console.log(url);
        const xhr = new XMLHttpRequest();
        xhr.open('POST', url).
        $.post(url, message, (data, response) => console.log(response)); 
}