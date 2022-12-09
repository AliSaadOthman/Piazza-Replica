package org.piazza.commands;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangePasswordCommand extends UserCommand{

    @Override
    protected void getRequiredFields() {
        Field oldPassword = new Field("oldPassword");
        Field newPassword = new Field("newPassword");
        Field username = new Field("userEmail");

        requiredFields= new ArrayList<Field>();
        requiredFields.add(username);
        requiredFields.add(oldPassword);
        requiredFields.add(newPassword);
    }

    @Override
    public HashMap<String,Object> execute(){
        return userService.changePassword((String) request.get("oldPassword"), (String) request.get("newPassword"), (String) request.get("userEmail"));
    }
}