package org.piazza.commands.course;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.HashMap;

public class ReportStudentCommand extends CourseCommand {

    @Override
    protected boolean needsCourseAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field courseId = new Field("courseId");
        Field reportedEmail=new Field("reportedEmail");
        Field reason=new Field("reason");
        requiredFields.add(reason);
        requiredFields.add(courseId);
        requiredFields.add(reportedEmail);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
        String email = (String) request.get("userEmail");
        String reportedEmail = (String) request.get("reportedEmail");
        String reason = (String) request.get("reason");
        String courseId = (String) request.get("courseId");
        return  courseService.reportStudent(email, reportedEmail, reason, courseId);
    }
}