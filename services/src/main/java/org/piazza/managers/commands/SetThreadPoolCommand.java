package org.piazza.managers.commands;

import org.piazza.utils.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class SetThreadPoolCommand extends BackDoorCommand {

    @Override
    protected void getRequiredFields() {
        requiredFields = new ArrayList<>();
        requiredFields.add(new Field("coreConnections"));
        requiredFields.add(new Field("maxConnections"));
    }

    @Override
    protected HashMap<String, Object> execute() {
        return managerService.setThreadPoolConnections(Integer.parseInt((String) request.get("coreConnections")),
                Integer.parseInt((String) request.get("maxConnections")));
    }
}
