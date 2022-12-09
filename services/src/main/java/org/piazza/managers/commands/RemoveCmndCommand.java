package org.piazza.managers.commands;

import org.piazza.utils.Field;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoveCmndCommand extends BackDoorCommand{

    @Override
    protected void getRequiredFields() {
        requiredFields = new ArrayList<>();
        requiredFields.add(new Field("commandName"));
    }

    @Override
    protected HashMap<String, Object> execute() {
        return managerService.removeCommand((String) request.get("commandName"));
    }
}
