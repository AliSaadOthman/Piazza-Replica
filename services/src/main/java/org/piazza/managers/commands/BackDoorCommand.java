package org.piazza.managers.commands;

import org.piazza.managers.services.ManagerService;
import org.piazza.utils.BackDoorResponse;
import org.piazza.utils.Command;
import org.piazza.utils.Field;
import org.piazza.utils.Receiver;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BackDoorCommand implements Command {

    protected ManagerService managerService;
    protected HashMap<String, Object> request;
    protected ArrayList<Field> requiredFields = new ArrayList<>();
    protected ArrayList<String> missingFields;

    @Override
    public void init(Receiver receiver) throws Exception {
        managerService = (ManagerService) receiver;
    }

    protected abstract void getRequiredFields();

    protected abstract HashMap<String, Object> execute();

    public boolean checkRequiredFields() {
        getRequiredFields();
        missingFields = new ArrayList<>();
        for (Field field : requiredFields) {
            if (!request.containsKey(field.getName())) {
                missingFields.add(field.getName());
            }
        }
        return missingFields.isEmpty();
    }

    @Override
    public HashMap<String, Object> execute(HashMap<String, Object> request) throws Exception {
        this.request = request;
        if (!checkRequiredFields()) {
            return new BackDoorResponse("Missing Fields : " + missingFields.toString(), HttpStatus.BAD_REQUEST).constructResponse();
        }
        return execute();
    }
}
