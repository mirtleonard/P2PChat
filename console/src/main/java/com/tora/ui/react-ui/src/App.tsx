import {Chat} from './Chat'
import { ConnectionForm } from './ConnectionForm'
import './App.css'

function App() {

  return (
    <>
      <div className="hello">
        <h1> Welcome to the chat! </h1>
        <ConnectionForm></ConnectionForm>
        <Chat></Chat>
      </div>
    </>
  )
}

export default App
