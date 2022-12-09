package org.piazza.commands.course;

import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DeleteUserCommand extends CourseCommand {
    @Override
    protected void getRequiredFields() {
        Field userEmail = new Field("email");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(userEmail);

    }
    @Override
    public HashMap<String,Object> execute() throws IOException {
        return courseService.deleteUser((String) request.get("email"));
    }
}
