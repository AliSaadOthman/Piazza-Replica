package org.piazza.commands.questions;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GetQuestionCommand extends CourseCommand {

    @Override
    protected boolean needsCourseAuthorization(){
        return true;
    }
    @Override
    protected void getRequiredFields() {
        Field courseId=new Field("courseId");
        Field questionId=new Field("questionId");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(courseId);
        requiredFields.add(questionId);

    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
       return courseService.getQuestion((String) request.get("questionId"), (String) request.get("userEmail"), (String) request.get("userRole"));
    }
}

