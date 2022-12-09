package org.piazza.commands.course;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.HashMap;

public class BanUserCommand extends CourseCommand {
    protected boolean needsCourseAuthorization(){
        return true;
    }
    protected boolean needsInstructorAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field duration=new Field("duration");
        Field isPermenant=new Field("isPermanent");
        Field courseId=new Field("courseId");
        Field userEmail=new Field("email");

        requiredFields.add(duration);
        requiredFields.add(isPermenant);
        requiredFields.add(courseId);
        requiredFields.add(userEmail);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IllegalAccessException, IOException {
        String email = (String) request.get("userEmail");
        String courseId = (String) request.get("courseId");
        int duration = (int) request.get("duration");
        boolean isPermanent = (boolean) request.get("isPermanent");
        String userId = (String) request.get("email");
        return courseService.banUser(email, courseId, duration, isPermanent, userId);
    }
}
