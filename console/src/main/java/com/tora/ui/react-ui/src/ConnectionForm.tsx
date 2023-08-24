import { useState } from 'react'
import $ from 'jquery';

//const url: String = "http://3.83.65.26:8081/api/chat";
const url: String = "http://127.0.0.1:8080/api/chat";

export const ConnectionForm = () => {
    const [ip, setIp] = useState('localhost');
    const [port, setPort] = useState('8082');
    const [alias, setAlias] = useState('test');

    const handleConnect = () => {
        console.log(ip + port + alias);
        $.ajax({
            url: url + "/connect",
            type: "POST",
            crossDomain: true,
            data: JSON.stringify({ ip, port, alias }),
            contentType: "application/json",
            success: (response) => {
                console.log(JSON.stringify(response));
            }
        });
    }

    return (
        <div>
            <label>Alias:</label>
            <input
                type="text"
                value={alias}
                onChange={e => setAlias(e.target.value)}
            />
            <label>Ip Adress:</label>
            <input
                type="text"
                value={ip}
                onChange={e => setIp(e.target.value)}
            />

            <label>Port</label>
            <input
                type="text"
                value={port}
                onChange={e => setPort(e.target.value)}
            />
            <button onClick={handleConnect}>Connect</button>
        </div>
    );
}