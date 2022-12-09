package org.piazza.commands.course;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GetCourseReports extends CourseCommand {
    @Override
    protected boolean needsCourseAuthorization(){
        return true;
    }
    @Override
    protected boolean needsInstructorAuthorization(){
        return true;
    }
    @Override
    protected void getRequiredFields() {
        Field courseId=new Field("courseId");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(courseId);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
        return courseService.getReports((String) request.get("courseId"));
    }
}

