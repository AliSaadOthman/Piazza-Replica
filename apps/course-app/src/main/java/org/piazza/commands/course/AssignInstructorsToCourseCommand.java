package org.piazza.commands.course;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AssignInstructorsToCourseCommand extends CourseCommand {

    protected boolean needsCourseAuthorization(){
        return true;
    }
    protected boolean needsInstructorAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field instructorEmail=new Field("instructorEmail");
        Field courseId=new Field("courseId");

        requiredFields= new ArrayList<Field>();
        requiredFields.add(instructorEmail);
        requiredFields.add(courseId);

    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
       return courseService.assignInstructorToCourse((String) request.get("userEmail"), (String) request.get("instructorEmail"), (String) request.get("courseId"));
    }
}