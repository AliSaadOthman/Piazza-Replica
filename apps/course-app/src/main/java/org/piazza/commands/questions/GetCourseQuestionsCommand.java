package org.piazza.commands.questions;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GetCourseQuestionsCommand extends CourseCommand {

    @Override
    protected void getRequiredFields() {
        Field courseId=new Field("courseId");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(courseId);
    }

    @Override
    protected boolean needsCourseAuthorization(){
        return true;
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
       return courseService.getCourseQuestions((String) request.get("courseId"), (String) request.get("userEmail"), (String) request.get("userRole"));
    }
}

