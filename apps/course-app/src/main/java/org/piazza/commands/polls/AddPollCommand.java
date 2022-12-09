package org.piazza.commands.polls;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddPollCommand extends CourseCommand {

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
        Field courseId = new Field("courseId");
        Field options = new Field("options");
        Field title = new Field("title");
        Field date = new Field("date");
        requiredFields = new ArrayList<Field>();
        requiredFields.add(courseId);
        requiredFields.add(date);
        requiredFields.add(title);
        requiredFields.add(options);
    }


    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException, ParseException {
        System.out.println(request.get("options").getClass());
       return  courseService.createPoll((String) request.get("title"), (ArrayList<String>) request.get("options"), (String) request.get("date"), (String) request.get("userEmail"), (String) request.get("courseId"));
    }
}