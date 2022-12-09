package org.piazza.managers.controller;

import org.piazza.managers.CommandsController;
import org.piazza.utils.Command;
import org.piazza.utils.Constants;
import org.piazza.utils.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ManagerController {

    @Autowired
    private CommandsController commandsController;
    @Resource
    private ApplicationContext ctx;

    @RequestMapping(value = "/{commandName}", method = RequestMethod.POST)
    public Map<String, Object> backDoorEndPoint(@PathVariable String commandName, @RequestBody(required = false) HashMap<String, Object> request) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> commandClass = commandsController.getBackDoorCommand(commandName);
        Command command = (Command) commandClass.getConstructor().newInstance();
        Receiver receiver = (Receiver) ctx.getBean(Constants.MANAGER_SERVICE);
        commandClass.getMethod(Constants.COMMAND_INITIALIZE_METHOD, Receiver.class).invoke(command, receiver);
        return (Map<String, Object>) commandClass.getMethod(Constants.COMMAND_EXECUTE_METHOD, HashMap.class).invoke(command, request);
    }
}