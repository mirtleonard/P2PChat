import { useState, useEffect, useCallback } from 'react';
import useWebSocket, { ReadyState } from 'react-use-websocket';

import './App.css';
import $ from 'jquery';

const url: String = "http://127.0.0.1:8080/api/chat";

export const Chat = () => {
  const WS_URL = 'ws://127.0.0.1:8080/chat/webApp';
  const [messageHistory, setMessageHistory] = useState([]);
  const [currentUser, setCurrentUser] = useState('');
  const [connectedUsers, setConnectedUsers] = useState(["None"]);
  const [notificationHistory, setNotificationHistory] = useState([]);
  const [message, setMessage] = useState('');

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

  const parseMessage = (jsonString: string): string => {
    const json = JSON.parse(jsonString);
    if (json.header.from !== 'me') {
      return json.header.from + ": " + json.body;
    } else {
      return "sent to " + json.header.to + ": " + json.body;
    }
    return "error";
  }

  useEffect(() => {
    if (lastMessage === null) {
      return;
    }
    const header = JSON.parse(lastMessage.data).header;
    if (header.type == 'message') {
      setMessageHistory([...messageHistory, parseMessage(lastMessage.data)]);
    } else if (header.type == 'notification') {
      alert(lastMessage.data);
    } else if (header.type == 'connection') {
      setConnectedUsers([...connectedUsers, header.alias]);
    }
  }, [lastMessage, setMessageHistory]);


  const handleClickSendMessage = () => {

    const formattedMessage = "sent to " + currentUser + ": " + message;
    $.ajax({
      url: url + "/" + currentUser + "/send-message",
      type: "POST",
      data: JSON.stringify(message),
      contentType: "application/json",
      success: (response) => {
        console.log(JSON.stringify(response));
      }
    });
    setMessage('');
  };


  return (
    <div>
      <span>The WebSocket is currently {connectionStatus}</span>
      {/* {lastMessage ? <span>Last message: {lastMessage.data}</span> : null} */}
      <div className='chat-window'>
        <ul>
          {messageHistory.map((currentMessage, idx) => (
            <li> <span key={idx}>{currentMessage}</span></li>
          ))}
        </ul>
      </div>
      <strong>{currentUser}</strong>
      <div className='user-interaction'>
        <select
          value={currentUser}
          onChange={e => setCurrentUser(e.target.value)}
        >
          {connectedUsers.map((user, idx) => (
            <option key={idx}>{user}</option>
          ))}
        </select>
        <input
          type="text"
          value={message}
          onChange={e => setMessage(e.target.value)}
        />
        <button
          onClick={handleClickSendMessage}
          disabled={readyState !== ReadyState.OPEN}
        > Send Message</button>
      </div>

    </div>
  );
}
