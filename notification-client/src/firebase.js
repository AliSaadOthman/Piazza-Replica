import firebase from "firebase/app";
import "firebase/messaging";
import "firebase/firestore";
const firebaseConfig = {
  apiKey: "AIzaSyCxpHTQucyRTIlGbb_chEE0DdOH_k1LdyE",
  authDomain: "piazza-c2c52.firebaseapp.com",
  projectId: "piazza-c2c52",
  storageBucket: "piazza-c2c52.appspot.com",
  messagingSenderId: "820949303419",
  appId: "1:820949303419:web:6bacf734bbb3a5c67a62de"
};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();
export const getToken = (setTokenFound, username) => {
  return messaging
    .getToken()
    .then(async (currentToken) => {
      if (currentToken) {
        fetch(`http://localhost:8080/registerToken`, {
          method: "POST",
          mode:"cors",
          headers: {
            "Content-Type": "application/json",
            "Function-Name": "REGISTER_DEVICE_TOKEN"
          },
          body: JSON.stringify({
            userId: username,
            token: currentToken,
          }),
        })
          .then((res) => {
            if (res.status === 200) {
              setTokenFound(true);
            }
          })
          .catch((err) => console.log(err));
        console.log("current token for client: ", currentToken);
      } else {
        console.log(
          "No registration token available. Request permission to generate one."
        );
        setTokenFound(false);
      }
    })
    .catch((err) => {
      console.log("An error occurred while retrieving token. ", err);
    });
};

export const onMessageListener = () =>
  new Promise((resolve) => {
    messaging.onMessage((payload) => {
      resolve(payload);
    });
  });
