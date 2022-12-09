package org.piazza.commands;

import org.json.JSONException;
import org.piazza.user.UserService;
import org.piazza.utils.Command;
import org.piazza.utils.Receiver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class UserCommand implements Command {
    protected HttpServletResponse response;

    protected UserService userService;

    protected HashMap<String,Object> request;
    protected ArrayList<Field> requiredFields;
    protected ArrayList<String> missingFields;

    protected abstract void getRequiredFields();

    protected abstract HashMap<String, Object> execute() throws IOException, IllegalAccessException, ParseException;

    public void init(Receiver receiver) {
        userService = (UserService) receiver;
    }

    public HashMap<String,Object> execute(HashMap<String,Object> request) throws JSONException, IOException, ParseException, IllegalAccessException, JSONException {
        this.request=request;
        HashMap<String,Object> response=new HashMap<>();
        if(!checkRequiredFields()){
            response.put("message","Missing Fields "+missingFields.toString());
            response.put("status",400);
            return response;
        }
//        if(needsUserAuthorization()){
//            if(userService.isUserBannedFromCourse(request)){
//                response.put("message","User banned from course");
//                response.put("status",401);
//                return response;
//
//            }
//            if(!userService.checkUserInCourse(request)){
//                response.put("message","User not enrolled in course");
//                response.put("status",401);
//                return response;
//            }
//            if(needsInstructorAuthorization()&& !request.get("userRole").equals("instructor")){
//                response.put("message","User not instructor in course");
//                response.put("status",401);
//                return response;
//            }

//        }
        return execute();

    }





    public boolean checkRequiredFields(){
        getRequiredFields();
        missingFields=new ArrayList<String>();

        for(Field field:requiredFields){
            if(!request.containsKey(field.getName())){
                missingFields.add(field.getName());
            }
        }
        return missingFields.isEmpty();
    }
}
