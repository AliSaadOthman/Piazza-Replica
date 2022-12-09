package org.piazza;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.piazza.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatServiceTest {
    @Autowired
    private ChatService chatService;

    ArrayList<String> addedGroupChats = new ArrayList<String>();
    ArrayList<String> addedMessages = new ArrayList<String>();

    @AfterAll
     void tearDown() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        for(String addedGroupChat:addedGroupChats){
            chatService.db.collection("groupChats").document(addedGroupChat).delete();
            boolean completed = latch.await(250, TimeUnit.MILLISECONDS);
            System.out.println(completed);
        }
        for(String addedMessage:addedMessages){
            chatService.db.collection("messages").document(addedMessage).delete();
            boolean completed = latch.await(250, TimeUnit.MILLISECONDS);
            System.out.println(completed);
        }
        addedGroupChats.clear();
        addedMessages.clear();
    }
    @Test
    @Order(1)
    void createGroupChat() throws IOException, ParseException, ExecutionException, InterruptedException {
        HashMap<String,Object> resp1 = chatService.createGroupChat("user1");
        HashMap<String,Object> resp2 = chatService.createGroupChat("user2");
        HashMap<String,Object> resp3 = chatService.createGroupChat("user3");
        HashMap<String,Object> resp4 = chatService.createGroupChat("user4");

        String[] gpchat1 = {(String) resp1.get("doc"),(String) resp2.get("doc"),(String) resp3.get("doc"),(String) resp4.get("doc")};
        addedGroupChats.addAll(List.of(gpchat1));
        assertThat(addedGroupChats.size()).isEqualTo(4);
    }

    @Test
    @Order(2)
    void addMemberToGroupChat() throws InterruptedException, ExecutionException, ParseException, IOException {
        CountDownLatch latch = new CountDownLatch(3);
        Boolean flag  = true;
        for(String groupChatId:addedGroupChats){
            chatService.addMemberToGroupChat("usx1@gmail.com"+groupChatId,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            chatService.addMemberToGroupChat("usx2@gmail.com"+groupChatId,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            chatService.addMemberToGroupChat("usx3@gmail.com"+groupChatId,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            chatService.addMemberToGroupChat("usx4@gmail.com"+groupChatId,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            DocumentReference docRef = chatService.db.collection("groupChats").document(groupChatId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            Map<String,Object> groupChat = document.getData();
            ArrayList<String> userIds = (ArrayList<String>) groupChat.get("userIds");
            flag = flag  && userIds.contains("usx1@gmail.com"+groupChatId) && userIds.contains("usx2@gmail.com"+groupChatId)&& userIds.contains("usx3@gmail.com"+groupChatId) && userIds.contains("usx4@gmail.com"+groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
        }
        assertEquals(flag,true);
    }

    @Test
    @Order(3)
    void deleteMemberFromGroupChat() throws ParseException, IOException, ExecutionException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Boolean flag  = true;
        for(String groupChatId:addedGroupChats){
            chatService.deleteMemberFromGroupChat("usx1@gmail.com"+groupChatId,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            chatService.deleteMemberFromGroupChat("usx2@gmail.com"+groupChatId,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            DocumentReference docRef = chatService.db.collection("groupChats").document(groupChatId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            Map<String,Object> groupChat = document.getData();
            ArrayList<String> userIds = (ArrayList<String>) groupChat.get("userIds");
            System.out.println(userIds);
            flag = flag  && !userIds.contains("usx1@gmail.com"+groupChatId) && !userIds.contains("usx2@gmail.com"+groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
        }
        assertEquals(flag,true);
    }

    @Test
    @Order(4)
    void sendMessage() throws ParseException, IOException, ExecutionException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Boolean flag  = true;
        for(String groupChatId:addedGroupChats){
            String message1 = "Messsage 1 content this is new message";
            String message2 = "Messsage 2asdasdd content this is new message";
            String message3 = "Messsage 3adq1233 content this is new message";
            //message 1 should not be created or sent because usx1@gmail.com is no longer a participant in the chat
            chatService.sendMessage("usx1@gmail.com"+groupChatId,message1,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            chatService.sendMessage("usx3@gmail.com"+groupChatId,message2,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            chatService.sendMessage("usx4@gmail.com"+groupChatId,message3,groupChatId);
            latch.await(250L, TimeUnit.MILLISECONDS);
            CollectionReference cities = chatService.db.collection("messages");
            // Create a query against the collection.
            Query query = cities.whereEqualTo("groupChatId", groupChatId);
            // retrieve  query results asynchronously using query.get()
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            latch.await(250L, TimeUnit.MILLISECONDS);
            int count = 0;
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                DocumentReference docRef = chatService.db.collection("messages").document(document.getId());
                ApiFuture<DocumentSnapshot> future = docRef.get();
                DocumentSnapshot doc = future.get();
                Map<String,Object> message = doc.getData();
                System.out.println(message);
                addedMessages.add(document.getId());
                flag = flag && (message!=null && message.get("content").equals(message2) || message.get("content").equals(message3));
                count++;
            }
            flag = flag  && count==2;
            latch.await(250L, TimeUnit.MILLISECONDS);
        }
        assertEquals(flag,true);
    }
}