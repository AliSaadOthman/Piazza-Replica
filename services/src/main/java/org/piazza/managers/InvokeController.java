package org.piazza.managers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.piazza.utils.Command;
import org.piazza.utils.Constants;
import org.piazza.utils.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Builder
@AllArgsConstructor
public class InvokeController {

    @Autowired
    private CommandsController commandsController;
    @Resource
    private ApplicationContext ctx;

    @Async
    public CompletableFuture<Map<String, Object>> invokeCommand(String commandName, HashMap<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Class<?> commandClass = commandsController.getCommand(commandName);
            Command command = (Command) commandClass.getConstructor().newInstance();
            Receiver receiver = (Receiver) ctx.getBean(commandsController.getService());
            commandClass.getMethod(Constants.COMMAND_INITIALIZE_METHOD, Receiver.class).invoke(command, receiver);
            return CompletableFuture.completedFuture(
                    (Map<String, Object>) commandClass.getMethod(Constants.COMMAND_EXECUTE_METHOD, HashMap.class).invoke(command, request)
            );
        } catch (Exception e) {
            response.put("message", "Failed to Invoke Command.");
            response.put("status", HttpStatus.BAD_REQUEST);
            e.printStackTrace();
            return CompletableFuture.completedFuture(response);
        }
    }
}
