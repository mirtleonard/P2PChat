import React, { useState, useEffect, useCallback } from 'react';
import useWebSocket, { ReadyState } from 'react-use-websocket';

import './App.css';

export const Chat = () => {

  const WS_URL = 'ws://127.0.0.1:8080/chat/webApp';
  const [messageHistory, setMessageHistory] = useState([]);


  const { sendMessage, lastMessage, readyState } = useWebSocket(WS_URL, {
    onOpen: () => {
      console.log('WebSocket connection established.');
    },
    onMessage: (event: WebSocketEventMap['message']) => {
      console.log(event.data);
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
    if (lastMessage !== null) {
      setMessageHistory((prev) => prev.concat(lastMessage));
    }
  }, [lastMessage, setMessageHistory]);

  const handleClickSendMessage = useCallback(() => sendMessage('{"header": {}, "body": "salut"}'), []);


  return (
    <div>
      <h1> Hello WebSockets! </h1>
      <span>The WebSocket is currently {connectionStatus}</span>
      {/* {lastMessage ? <span>Last message: {lastMessage.data}</span> : null} */}
      <ul>
        {messageHistory.map((message, idx) => (
          <li> <span key={idx}>{message ? message.data : null}</span></li>
        ))}
      </ul>
      <div className='userInteraction'>
        <textarea>
        </textarea>
        <button
          onClick={handleClickSendMessage}
          disabled={readyState !== ReadyState.OPEN}
        > Send Message</button>
      </div>

    </div>
  );
}
