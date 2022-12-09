package org.piazza;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.piazza.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;
    @Test
    void sendMulticastNotification() throws IOException, ParseException, FirebaseMessagingException {
        String[] userIds ={"user1","user2","user3"};
        HashMap<String,Object> response = notificationService.sendMulticastNotification(userIds,"not1 title","not1 body");
        System.out.println(response);
        assertEquals(response.get("status")!=null,true);
    }

    @Test
    void sendNotificationToken() throws IOException, ParseException, FirebaseMessagingException {
        HashMap<String,Object> response = notificationService.SendNotificationToken("user1","not1 title","not1 body");
        assertEquals(response.get("status")!=null ,true);

    }
}