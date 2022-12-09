package org.piazza.commands.course;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.HashMap;

public class RegisterUserCommand extends CourseCommand {


    @Override
    protected void getRequiredFields() {
        Field courseId = new Field("courseId");
        Field studentEmail=new Field("userEmail");
        requiredFields.add(courseId);
        requiredFields.add(studentEmail);

    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
        return courseService.registerUser((String) request.get("courseId"), (String) request.get("userEmail"));
    }
}

