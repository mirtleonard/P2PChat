import {Chat} from './Chat';
import { ConnectionForm } from './ConnectionForm';
import './App.css';
import { Provider } from 'react-redux';
import { store } from './redux/store';

function App() {

  return (
    <Provider store = {store}>
      <div className="hello">
        <h1> Welcome to the chat! </h1>
        <ConnectionForm></ConnectionForm>
        <Chat></Chat>
      </div>
    </Provider>
  )
}

export default App

