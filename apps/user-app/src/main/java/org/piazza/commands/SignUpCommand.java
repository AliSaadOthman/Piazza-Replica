package org.piazza.commands;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpCommand extends UserCommand{

    @Override
    protected void getRequiredFields() {
        Field email = new Field("email");
        Field password = new Field("password");
        Field firstName = new Field("firstName");
        Field lastName = new Field("lastName");
        Field role = new Field("role");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(email);
        requiredFields.add(password);
        requiredFields.add(firstName);
        requiredFields.add(lastName);
        requiredFields.add(role);

    }
    @Override
    public HashMap<String,Object> execute() {
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        String firstName = (String) request.get("firstName");
        String lastName = (String) request.get("lastName");
        String role = (String) request.get("role");

        return userService.addNewUser(email, password, firstName, lastName, role);
    }
}