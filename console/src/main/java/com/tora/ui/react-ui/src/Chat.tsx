import { useState, useEffect } from 'react';
import useWebSocket, { ReadyState } from 'react-use-websocket';
import { useDispatch, useSelector } from 'react-redux'
import { setContent, setReceiver, addMessage } from './redux/messageSlice';
import { RootState } from './redux/store';

import './App.css';
import $ from 'jquery';

//const url: String = "http://3.83.65.26:8081/api/chat";
const url: String = "http://127.0.0.1:8080/api/chat";

export const Chat = () => {
  //const WS_URL = 'ws://3.83.65.26:8081/chat/webApp';
  const WS_URL = 'ws://127.0.0.1:8080/chat/webApp';
  const [connectedUsers, setConnectedUsers] = useState(["None"]);
  const [username, setUsername] = useState("leonardmirt26@gmail.com");
  const [password, setPassword] = useState("");
  const message = useSelector((state : RootState) => state.messages);
  const dispatch = useDispatch();

  const { lastMessage, readyState } = useWebSocket(WS_URL, {
    onOpen: () => {
      console.log('WebSocket connection established.');
    },
  });

  const connectionStatus = {
    [ReadyState.CONNECTING]: 'Connecting',
    [ReadyState.OPEN]: 'Open',
    [ReadyState.CLOSING]: 'Closing',
    [ReadyState.CLOSED]: 'Closed',
    [ReadyState.UNINSTANTIATED]: 'Uninstantiated',
  }[readyState];


  useEffect(() => {
    if (lastMessage === null) {
      return;
    }
    const header = JSON.parse(lastMessage.data).header;
    if (header.type == 'message') {
      dispatch(addMessage(lastMessage.data));
    } else if (header.type == 'notification') {
      alert(lastMessage.data);
    } else if (header.type == 'connection') {
      setConnectedUsers([...connectedUsers, header.alias]);
    }
  }, [lastMessage, addMessage]);


  const handleClickSendMessage = () => {
    $.ajax({
      url: url + "/" + message.receiver + "/send-message",
      type: "POST",
      crossDomain: true,
      data: message.content,
      contentType: "application/json",
      success: (response: any) => {
        console.log(JSON.stringify(response));
      }
    });
    dispatch(setContent(''));
  };
  
  const renderMessage = (message: any) => {
    const timeString = new Date(message.header.timestamp).toLocaleTimeString();
    return (
      <div className={message.header.from !== "me" ? "messageReceived" : "messageSent"}>
        <div className="content">
          {message.body}
        </div>
        <div className="details">
          {timeString} - {message.header.from !== "me"? message.header.from : ""}
        </div>
      </div>
    );
  }

function login() {
  $.ajax({
    url: "https://auth-dev-1.app.toradev.net/auth/realms/INTERNSHIP/protocol/openid-connect/token",
    type: "POST",
    data: {
      grant_type: 'password',
      client_id: 'chat-client',
      username,
      password,
    },
    success: (response: any) => {
      console.log(JSON.stringify(response));
       $.ajaxSetup({
          headers: {
            'Authorization': "Bearer " + response.access_token,
          }
        });
    }
  });
}


  return (
    <div>
      <div>
        <input 
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        ></input>
        <input 
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        ></input>
        <button onClick={login}>Send</button>
      </div>
      <span>The WebSocket is currently {connectionStatus}</span>
      <div className='chat-window'>
        <div className="chat-content">
          {message.allMessages.filter((mess: any) => (mess.header.from === message.receiver
            || mess.header.to === message.receiver)).map(mess => renderMessage(mess))}
        </div>
      </div>
      <div className='user-interaction'>
        <select
          value={message.receiver}
          onChange={e => dispatch(setReceiver(e.target.value))}
        >
          {connectedUsers.map((user, idx) => (
            <option key={idx}>{user}</option>
          ))}
        </select>
        <input
          type="text"
          value={message.content}
          onChange={e => dispatch(setContent(e.target.value))}
        />
        <button
          onClick={handleClickSendMessage}
          disabled={readyState !== ReadyState.OPEN}
        > Send Message</button>
      </div>

    </div>
  );
}
