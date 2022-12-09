package org.piazza.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginCommand extends UserCommand{

    @Override
    protected void getRequiredFields() {
        Field email = new Field("email");
        Field password = new Field("password");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(email);
        requiredFields.add(password);
    }
    @Override
    public HashMap<String,Object> execute() throws IOException {
        return userService.logIn((String) request.get("password"), (String) request.get("email"), (String) request.get("url"));
    }
}