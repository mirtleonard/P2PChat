import { useState, useEffect } from 'react';
import useWebSocket, { ReadyState } from 'react-use-websocket';
import { useDispatch, useSelector } from 'react-redux'
import { setContent, setReceiver, addMessage } from './redux/messageSlice';
import { RootState } from './redux/store';

import './App.css';
import $ from 'jquery';

const url: String = "http://127.0.0.1:8080/api/chat";

export const Chat = () => {
  const WS_URL = 'ws://127.0.0.1:8080/chat/webApp';
  const [messageHistory, setMessageHistory] = useState([]);
  const [connectedUsers, setConnectedUsers] = useState(["None"]);
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
      setMessageHistory([...messageHistory, lastMessage.data]);
    } else if (header.type == 'notification') {
      alert(lastMessage.data);
    } else if (header.type == 'connection') {
      setConnectedUsers([...connectedUsers, header.alias]);
    }
  }, [lastMessage, setMessageHistory]);


  const handleClickSendMessage = () => {
    $.ajax({
      url: url + "/" + message.receiver + "/send-message",
      type: "POST",
      data: message.content,
      contentType: "application/json",
      success: (response: any) => {
        console.log(JSON.stringify(response));
      }
    });
    dispatch(setContent(''));
  };
  
  const renderMessage = (message) => {
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


  return (
    <div>
      <span>The WebSocket is currently {connectionStatus}</span>
      <div className='chat-window'>
        <div className="chat-content">
          {message.allMessages.filter(mess => (mess.header.from === message.receiver
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
