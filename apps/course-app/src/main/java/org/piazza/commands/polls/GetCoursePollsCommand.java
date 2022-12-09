package org.piazza.commands.polls;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class GetCoursePollsCommand extends CourseCommand {

    @Override
    protected boolean needsCourseAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field courseId = new Field("courseId");
        requiredFields = new ArrayList<Field>();
        requiredFields.add(courseId);
    }

    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException, ParseException {
        return  courseService.getPolls((String) request.get("courseId"));
    }
}
