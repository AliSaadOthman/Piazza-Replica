package org.piazza.commands.answers;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EndorseAnswerCommand extends CourseCommand {

    protected boolean needsCourseAuthorization(){
        return true;
    }
    protected boolean needsInstructorAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field answerId=new Field("answerId");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(answerId);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException {
        return courseService.endorseAnswer((String) request.get("answerId"));
    }
}

