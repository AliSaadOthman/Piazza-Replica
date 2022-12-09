package org.piazza.commands.questions;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchQuestionCommand extends CourseCommand {

    @Override
    protected boolean needsCourseAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field searchQuery=new Field("searchQuery");
        Field courseId=new Field("courseId");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(searchQuery);
        requiredFields.add(courseId);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
        return courseService.searchForQuestion((String) request.get("courseId"), (String) request.get("searchQuery"), (String) request.get("userRole"), (String) request.get("userEmail"));
    }
}

