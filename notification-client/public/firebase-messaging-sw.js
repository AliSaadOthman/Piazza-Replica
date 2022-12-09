// Scripts for firebase and firebase messaging
importScripts('https://www.gstatic.com/firebasejs/8.2.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.2.0/firebase-messaging.js');




// Initialize the Firebase app in the service worker by passing the generated config
const firebaseConfig = {
    apiKey: "AIzaSyCxpHTQucyRTIlGbb_chEE0DdOH_k1LdyE",
    authDomain: "piazza-c2c52.firebaseapp.com",
    projectId: "piazza-c2c52",
    storageBucket: "piazza-c2c52.appspot.com",
    messagingSenderId: "820949303419",
    appId: "1:820949303419:web:6bacf734bbb3a5c67a62de"
  };
  
  

// eslint-disable-next-line no-undef
firebase.initializeApp(firebaseConfig);

// Retrieve firebase messaging
// eslint-disable-next-line no-undef
const messaging = firebase.messaging();
messaging.onBackgroundMessage(function(payload) {
  console.log('Received background message ', payload);

  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
  };

  self.registration.showNotification(notificationTitle,notificationOptions);
});
