package org.piazza.commands.course;

import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddUserCommand extends CourseCommand {
    @Override
    protected void getRequiredFields() {
        Field userEmail = new Field("email");
        Field userRole=new Field("role");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(userEmail);
        requiredFields.add(userRole);

    }
    @Override
    public HashMap<String,Object> execute() throws IOException {
        return courseService.addUser((String) request.get("email"), (String) request.get("role"));
    }
}
