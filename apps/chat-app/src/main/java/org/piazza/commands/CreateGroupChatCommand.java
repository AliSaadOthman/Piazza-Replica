package org.piazza.commands;

import org.json.simple.parser.ParseException;
import org.piazza.services.ChatService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class CreateGroupChatCommand extends ChatCommand{
    @Override
    protected void getRequiredFields() {
        Field userId=new Field("userId");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(userId);
    }
    @Override
    public HashMap<String, Object> execute() throws IOException, ParseException, ExecutionException, InterruptedException {
        String userId = (String) request.get("userId");
        return chatService.createGroupChat(userId);
    }
}
