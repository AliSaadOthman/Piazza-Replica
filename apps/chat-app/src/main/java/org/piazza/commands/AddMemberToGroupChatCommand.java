package org.piazza.commands;

import org.json.simple.parser.ParseException;
import org.piazza.services.ChatService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class AddMemberToGroupChatCommand extends ChatCommand{
    @Override
    protected void getRequiredFields() {
        Field userId=new Field("userId");
        Field groupChatId=new Field("groupChatId");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(userId);
        requiredFields.add(groupChatId);
    }
    @Override
    public HashMap<String,Object> execute() throws ParseException, IOException, ExecutionException, InterruptedException {
        String userId= (String)request.get("userId");
        String groupChatId = (String) request.get("groupChatId");
        return chatService.addMemberToGroupChat(userId,groupChatId);
    }
}
