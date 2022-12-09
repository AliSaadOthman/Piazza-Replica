package org.piazza.commands.polls;

import org.json.JSONException;
import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class VotePollCommand extends CourseCommand {

    protected boolean needsCourseAuthorization(){
        return true;
    }

    @Override
    protected void getRequiredFields() {
        Field courseId = new Field("courseId");
        Field option = new Field("option");
        Field pollId = new Field("pollId");
        requiredFields = new ArrayList<Field>();
        requiredFields.add(courseId);
        requiredFields.add(option);
        requiredFields.add(pollId);
    }


    @Override
    public HashMap<String,Object> execute() throws JSONException, IOException, ParseException {
        return courseService.votePoll((String) request.get("pollId"), (String) request.get("option"), (String) request.get("courseId"), (String) request.get("userEmail"));
    }
}