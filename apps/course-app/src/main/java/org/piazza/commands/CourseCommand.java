package org.piazza.commands;

import org.json.JSONException;
import org.piazza.services.CourseService;
import org.piazza.utils.Command;
import org.piazza.utils.Receiver;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class CourseCommand implements Command {

    protected CourseService courseService;
    protected HashMap<String, Object> request;
    protected ArrayList<Field> requiredFields = new ArrayList<Field>();
    protected ArrayList<String> missingFields;

    public void init(Receiver receiver) {
        courseService = (CourseService) receiver;
    }

    protected abstract void getRequiredFields();

    protected abstract HashMap<String, Object> execute() throws IOException, IllegalAccessException, ParseException;

    protected boolean needsCourseAuthorization() {
        return false;
    }

    protected boolean needsInstructorAuthorization() {
        return false;
    }

    public HashMap<String, Object> execute(HashMap<String, Object> request) throws JSONException, IOException, ParseException, IllegalAccessException {
        this.request = request;
        HashMap<String, Object> response = new HashMap<>();
        if (!checkRequiredFields()) {
            response.put("message", "Missing Fields " + missingFields.toString());
            response.put("status", 400);
            return response;
        }
        if (needsCourseAuthorization()) {
            if (courseService.isUserBannedFromCourse(request)) {
                response.put("message", "User banned from course");
                response.put("status", 401);
                return response;
            }
            if (!courseService.checkUserInCourse(request)) {
                response.put("message", "User not enrolled in course");
                response.put("status", 401);
                return response;
            }
            if (needsInstructorAuthorization() && !request.get("userRole").equals("instructor")) {
                response.put("message", "User not instructor in course");
                response.put("status", 401);
                return response;
            }
        }
        return execute();
    }

    public boolean checkRequiredFields() {
        getRequiredFields();
        missingFields = new ArrayList<String>();

        for (Field field : requiredFields) {
            if (!request.containsKey(field.getName())) {
                missingFields.add(field.getName());
            }
        }
        return missingFields.isEmpty();
    }
}