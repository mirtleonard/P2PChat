import {Chat} from './Chat';
import { ConnectionForm } from './ConnectionForm';
import './App.css';
import { Provider } from 'react-redux';
import { store } from './redux/store';
import $ from 'jquery';

function App() {

  login();

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

function login() {
  $.ajax({
    url: "https://auth-dev-1.app.toradev.net/auth/realms/INTERNSHIP/protocol/openid-connect/token",
    type: "POST",
    data: {
      grant_type: 'password',
      client_id: 'chat-client',
      username: 'leonardmirt26@gmail.com',
      password: '',
    },
    contentType: "application/json",
    success: (response: any) => {
      console.log(JSON.stringify(response));
    }
  });
  $.ajaxSetup({
    headers: { 'Authorization': 
    'Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1TjdNcWFHTGJrejJuRFRQeV9ZVFhleWI2QWFTQy1oVi1aSW9zTDZYR1JNIn0.eyJleHAiOjE2OTI3MDIwOTYsImlhdCI6MTY5MjcwMDI5NiwianRpIjoiNWRlMjcyNGEtNjI2NC00Yzc1LTg0OTktMGU0MTJjZTkzMWU1IiwiaXNzIjoiaHR0cHM6Ly9hdXRoLWRldi0xLmFwcC50b3JhZGV2Lm5ldC9hdXRoL3JlYWxtcy9JTlRFUk5TSElQIiwiYXVkIjpbImNoYXQtc2VydmVyIiwiYWNjb3VudCJdLCJzdWIiOiIwYzFlMDlmMC00MmQyLTQwMDktOWU5MS1lNmM2NWZjNTUwNjgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjaGF0LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiIzYzRkZWY1NC1jNTk3LTRkMGItOTNjYS1kMjc4YjE1NzYwMjIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly8xMC40LjEuMjQ1OjgwODAiLCJodHRwOi8vMTAuNC4xLjIyNDo4MDgwIiwiaHR0cDovLzEwLjQuMC4xMzI6ODA4MCIsImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImh0dHA6Ly8xMC40LjAuMTcxOjgwODAiLCJodHRwOi8vMTI3LjAuMC4xOjgwODAiLCJodHRwOi8vMTAuNC4wLjI1MTo4MDgwIiwiaHR0cDovLzEwLjQuMC4xNTY6ODA4MCIsImh0dHA6Ly8xMC40LjEuMjIwOjgwODAiLCJodHRwOi8vMTAuNC4wLjE0Mzo4MDgwIiwiaHR0cDovLzEwLjQuMC4xNTI6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtaW50ZXJuc2hpcCJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6IjNjNGRlZjU0LWM1OTctNGQwYi05M2NhLWQyNzhiMTU3NjAyMiIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiTGVvbmFyZCBNaXJ0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoibGVvbmFyZG1pcnQyNkBnbWFpbC5jb20iLCJnaXZlbl9uYW1lIjoiTGVvbmFyZCIsImZhbWlseV9uYW1lIjoiTWlydCIsImVtYWlsIjoibGVvbmFyZG1pcnQyNkBnbWFpbC5jb20ifQ.HEUezWo6DJVukD3LNNFJnfpKyxZHgFV1L4St28dEvhoG0T4Jp-8hDx_BLHjvT1omSczofHWkviBQUVEpZxPOPvwcAn6ZamM4Ct0whUTqBAh3XLa9RIyDZunKl4UgtHTPsWQRt_WuwLZ61ZuaF3iisJjBhM8N6Q45lwQDVahAh7WRc8nVTaJdHmJ0TRL9hCtEYdqrZ8NAysvDcI5puikTwqdzFRFAfSfQfKDtHUIyS0Q0FEZe1aBNWWBCgW6qTWW57_A5f6j2sbPHOWf5R1q4Sy5Eof22vc_8byY4DndkG2tNiY1TQDLhKlQoAtRbCs34q1bPIZ1ePArQA2YMxNaigQ',
    }
  });
  /*
 */
}