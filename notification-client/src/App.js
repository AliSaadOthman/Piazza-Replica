import './App.css';
import { useState } from "react";
import { getToken } from "./firebase";
import {AppBar, Badge,FormControl,IconButton,Button, TextField, Typography,Paper,Drawer} from "@mui/material"
import NotificationsIcon from '@mui/icons-material/Notifications';
import DeleteIcon from '@mui/icons-material/Delete';
import firebase from "firebase/app";
import "firebase/messaging";
import "firebase/firestore";

function App() {
  const [show, setShow] = useState(false);
  const [notification, setNotification] = useState([]);
  const [isTokenFound, setTokenFound] = useState(false);
  const [username, setUsername] = useState("");

  const messaging = firebase.messaging();
  messaging.onMessage((payload) => {
    console.log(payload,notification);
    setNotification(notification => [...notification, {title: payload.notification.title, body: payload.notification.body}]);
  });
const handleSubmit = (evt) => {
  console.log(notification);
  evt.preventDefault();
  alert(`Submitting Name ${username} `);
  getToken(setTokenFound, username);
};
const onChange = (event) => {
  setUsername(event.target.value);
};
  return (
    <div className="App">
      <header className="App-header">
        <AppBar style={{height:"50px",display:"flex"}}>
          <IconButton onClick={()=>setShow(!show)}>
          <Badge badgeContent={notification.length} color="primary" style={{width:"50px",height:"100%",position:"absolute",top:"10px",right:"3%"}}>
            <NotificationsIcon color="action" />
          </Badge>
          </IconButton>

        </AppBar>
        <div>
          {!isTokenFound && (
            <Paper>
              <FormControl
                className="mb-3"
              >
                <TextField
                  value={username}
                  onChange={onChange}
                  placeholder="username"
                  label={<Typography>User Id</Typography>}
                />
              </FormControl>
              <Button onClick={handleSubmit}>Submit</Button>
            </Paper>
          )}
          <div>
            {isTokenFound && <h1> Notification permission enabled 👍🏻 </h1>}
            {!isTokenFound && <h1> Need notification permission ❗️ </h1>}
          </div>
        </div>
        
      </header>
      <Drawer open={show} onClose={()=>setShow(!show)} anchor={"right"} >
            <Paper className="notificationsContainer" style={{width:"20vw",padding:"8px"}}>
              {notification.map((item,index)=>(
                <Paper key={index} className="notification" 
                style={{
                height:"75px",
                display:"flex",
                flexDirection:"column",
                width:"100%",
                border:"1px solid #1b1e2b",
                marginTop:"5px",
                position:"relative"
                }}>
                  <Typography 
                  style={{
                    fontSize:"1.2rem",
                    fontWeight:"bold",
                    color:"#1b1e2b",
                  }}
                  >{item.title}</Typography>
                  <Typography>{item.body}</Typography>
                  <IconButton 
                  style={{
                    position:"absolute",
                    top:"-5px",
                    right:"0px",
                  }}
                  onClick={()=>setNotification((oldArray) => [
                            ...oldArray.slice(0, index),
                            ...oldArray.slice(index + 1, oldArray.length),
                          ])}>
                    <DeleteIcon />
                  </IconButton>
                  </Paper>
              ))}
            </Paper>
      </Drawer>
    </div>
  );
}

export default App;
