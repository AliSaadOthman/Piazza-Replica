package org.piazza.commands;

import java.util.ArrayList;
import java.util.HashMap;

public class RefreshTokenCommand extends UserCommand{

    @Override
    protected void getRequiredFields() {
        Field authHeader = new Field("authorizationHeader");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(authHeader);
    }
    @Override
    public HashMap<String,Object> execute(){
        return userService.refreshToken((String) request.get("authorizationHeader"), (String) request.get("url"));
    }
}
