package org.piazza.commands.course;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddCourseCommand extends CourseCommand {

    @Override
    protected void getRequiredFields() {
        System.out.println(request);
        Field courseName=new Field("name");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(courseName);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
       return courseService.addCourse((String) request.get("userEmail"), (String) request.get("name"));
    }
}
