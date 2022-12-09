package org.piazza.commands.course;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddStudentsCommand extends CourseCommand {

    protected boolean needsCourseAuthorization(){
        return true;
    }
    protected boolean needsInstructorAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field studentEmail=new Field("studentEmail");
        Field courseId=new Field("courseId");

        requiredFields= new ArrayList<Field>();
        requiredFields.add(studentEmail);
        requiredFields.add(courseId);

    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
        return courseService.addStudentsManually((String) request.get("userEmail"), (String) request.get("studentEmail"), (String) request.get("courseId"));
    }
}