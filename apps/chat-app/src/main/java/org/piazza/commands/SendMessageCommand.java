package org.piazza.commands;

import org.json.simple.parser.ParseException;
import org.piazza.services.ChatService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SendMessageCommand extends ChatCommand{
    protected void getRequiredFields() {
        Field userId=new Field("userId");
        Field groupChatId=new Field("groupChatId");
        Field content = new Field("content");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(userId);
        requiredFields.add(groupChatId);
        requiredFields.add(content);
    }
    @Override
    public HashMap<String,Object> execute() throws IOException, ParseException, ExecutionException, InterruptedException {
        String userId = (String) request.get("userId");
        String content = (String) request.get("content");
        String groupChatId = (String) request.get("groupChatId");
       return chatService.sendMessage(userId,content,groupChatId);

    }
}
