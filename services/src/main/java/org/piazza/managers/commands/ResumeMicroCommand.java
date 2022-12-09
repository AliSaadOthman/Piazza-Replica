package org.piazza.managers.commands;

import java.util.ArrayList;
import java.util.HashMap;

public class ResumeMicroCommand extends BackDoorCommand {

    @Override
    protected void getRequiredFields() {
        requiredFields= new ArrayList<>();
    }

    @Override
    protected HashMap<String, Object> execute() {
        return managerService.resumeMS();
    }
}
