package org.piazza.commands.course;

import org.piazza.commands.CourseCommand;
import org.piazza.commands.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseRecommendationCommand extends CourseCommand {

    @Override
    protected void getRequiredFields() {
        Field userEmail = new Field("userEmail");
        Field userRole=new Field("userRole");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(userEmail);
        requiredFields.add(userRole);

    }
    @Override
    public HashMap<String,Object> execute() throws IOException {
        return courseService.courseRecommendation((String) request.get("userEmail"), (String) request.get("userRole"));
    }
}