package org.piazza.commands.questions;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddQuestionCommand extends CourseCommand {

    protected boolean needsCourseAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field courseId=new Field("courseId");
        Field description=new Field("description");
        Field title=new Field("title");
        Field isPublic=new Field("isPublic");
        Field anonymous=new Field("anonymous");

        requiredFields= new ArrayList<Field>();
        requiredFields.add(courseId);
        requiredFields.add(description);
        requiredFields.add(title);
        requiredFields.add(isPublic);
        requiredFields.add(anonymous);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
        return courseService.addQuestion((String) request.get("title"), (String) request.get("mediaLink"), (String) request.get("description"), (Boolean) request.get("anonymous"), (String[]) request.get("taggedUsers"), (String) request.get("courseId"), (Boolean) request.get("isPublic"), (String) request.get("userEmail"));
    }
}

