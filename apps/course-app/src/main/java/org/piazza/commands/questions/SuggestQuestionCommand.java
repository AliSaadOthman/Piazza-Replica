package org.piazza.commands.questions;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SuggestQuestionCommand extends CourseCommand {

    protected boolean needsCourseAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field courseId=new Field("courseId");
        Field description=new Field("description");
        Field title=new Field("title");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(courseId);
        requiredFields.add(description);
        requiredFields.add(title);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
        return courseService.suggestQuestion((String) request.get("title"), (String) request.get("description"), (String) request.get("courseId"), (String) request.get("userEmail"), (String) request.get("userRole"));
    }
}

