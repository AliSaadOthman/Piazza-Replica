package org.piazza.commands;

import java.util.ArrayList;
import java.util.HashMap;

public class DeleteAccountCommand extends UserCommand{

    @Override
    protected void getRequiredFields() {
        Field email = new Field("email");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(email);

    }

    @Override
    public HashMap<String,Object> execute(){
        return userService.deleteUser((String) request.get("email"), (String) request.get("userEmail"), (String) request.get("userRole"));
    }
}