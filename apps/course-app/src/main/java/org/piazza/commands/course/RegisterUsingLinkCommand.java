package org.piazza.commands.course;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class RegisterUsingLinkCommand extends CourseCommand {

    @Override
    protected void getRequiredFields() {
        Field inviteLink=new Field("inviteLink");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(inviteLink);
    }

    @Override
    public HashMap<String, Object> execute() throws JSONException, IOException {
        return courseService.enrollUsingCourseLink((String) request.get("userEmail"), (String) request.get("link"));
    }
}

