package org.piazza.services;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.json.simple.parser.ParseException;
import org.piazza.document.GroupChat;
import org.piazza.document.Message;
import org.piazza.utils.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service("chatService")
public class ChatService implements Receiver {
    @Autowired
    public Firestore db ;

    public HashMap<String, Object> createGroupChat(String userId) throws IOException, ParseException, ExecutionException, InterruptedException {
        HashMap<String,Object> response=new HashMap<>();
        try{
            ArrayList<String> userIds = new ArrayList<>();
            userIds.add(userId);
            GroupChat groupChat = new GroupChat(userIds);
            ApiFuture<DocumentReference> addedDocRef = db.collection("groupChats").add(groupChat);
            System.out.println("Added document with ID: " + addedDocRef.get().getId());
            response.put("status",200);
            response.put("doc",addedDocRef.get().getId());
            return response;
        }
        catch (Exception error) {
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }

    }
    public HashMap<String, Object> addMemberToGroupChat(String userId, String groupChatId) throws ParseException, IOException, ExecutionException, InterruptedException {
        HashMap<String,Object> response=new HashMap<>();
        try{
            DocumentReference docRef = db.collection("groupChats").document(groupChatId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if(document.exists()){
                Map<String,Object> groupChat = document.getData();
                ArrayList<String> userIds = (ArrayList<String>) groupChat.get("userIds");
                if(!userIds.contains(userId))
                    userIds.add(userId);
                ApiFuture<WriteResult> futureWrite =  docRef.update("userIds",userIds);
                WriteResult result = futureWrite.get();
                System.out.println("Write Result : "+result);
                response.put("status",200);
            }
            else{
                response.put("status",422);
                response.put("message","Groupchat doesn't exist");
            }
            return response;

        }
        catch (Exception error) {
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }



    }
    public HashMap<String, Object> deleteMemberFromGroupChat(String userId, String groupChatId) throws ParseException, IOException, ExecutionException, InterruptedException {
        HashMap<String,Object> response=new HashMap<>();
        try{
            DocumentReference docRef = db.collection("groupChats").document(groupChatId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if(document.exists()){
                Map<String,Object> groupChat = document.getData();
                ArrayList<String> userIds = (ArrayList<String>) groupChat.get("userIds");
                userIds.remove(userId);
                ApiFuture<WriteResult> futureWrite =  docRef.update("userIds",userIds);
                WriteResult result = futureWrite.get();
                System.out.println("Write Result : "+result);
                response.put("status",200);
            }
            else{
                response.put("status",422);
                response.put("message","Groupchat doesn't exist");
            }
            return response;
        }
        catch (Exception error) {
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }



    }
    public HashMap<String,Object> sendMessage(String userId,String content,String groupChatId) throws IOException, ParseException, ExecutionException, InterruptedException {
        HashMap<String,Object> response=new HashMap<>();
        try{
            FieldValue timestamp = FieldValue.serverTimestamp();
            DocumentReference docRef = db.collection("groupChats").document(groupChatId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot groupChat = future.get();
            if(groupChat.exists()){
                ArrayList<String> chatMembers = (ArrayList<String>) groupChat.get("userIds");
                if(chatMembers.contains(userId)){
                    Message message = new Message(userId,content,groupChatId,timestamp);
                    db.collection("messages").add(message);
                    response.put("status",200);
                }
                else{
                    response.put("status",422);
                    response.put("message","User is not member of group chat");
                }

            }
            else{
                response.put("status",422);
                response.put("message","Invalid Group Chat Id");
            }

            return response;
        }
        catch (Exception error) {
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }


    }
    /*void FirebaseApp() throws Exception {
        //ClassLoader classLoader = Application.class.getClassLoader();
        File file = new File("C:\\Users\\filoi\\Documents\\Github Desktop\\Piazza\\apps\\chat-app\\src\\main\\java\\org\\piazza\\serviceAccountKey.json");
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://piazza-5a192-default-rtdb.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }*/

}