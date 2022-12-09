package org.piazza;
import lombok.AllArgsConstructor;
import org.piazza.commands.*;
import org.piazza.services.NotificationService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Hashtable;

@RestController
@AllArgsConstructor
public class NotificationController {
    @Autowired
    BeanFactory beanFactory;
    Hashtable<String,Class<?>> commands=new Hashtable<String,Class<?>>();
    public void init(){
        commands.put("SendNotificationSingleTokenCommand",SendNotificationSingleTokenCommand.class);
        commands.put("SendMulticastNotificationCommand",SendMulticastNotificationCommand.class);
        commands.put("RegisterTokenCommand", RegisterTokenCommand.class);
    }
    @CrossOrigin
    @PostMapping(value = "/registerToken")
    public void registerToken(@RequestBody HashMap<String,Object> request, HttpServletResponse response) throws Exception {
        init();
        String commandName = (String) request.get("command");
        Class<?> commandClass = commands.get(commandName);
        Constructor<?> cons1 = commandClass.getConstructor(NotificationService.class);
        NotificationService notificationService = beanFactory.getBean(NotificationService.class);
        NotificationsCommand command = (NotificationsCommand) cons1.newInstance(notificationService);
        command.execute(request);
    }

    @PostMapping(value = "/sendSingleNotification")
    public void sendSingleNotification(@RequestBody HashMap<String,Object> request, HttpServletResponse response) throws Exception {
        init();
        String commandName = (String) request.get("command");
        Class<?> commandClass = commands.get(commandName);
        Constructor<?> cons1 = commandClass.getConstructor(NotificationService.class);
        NotificationService notificationService = beanFactory.getBean(NotificationService.class);
        NotificationsCommand command = (NotificationsCommand) cons1.newInstance(notificationService);
        command.execute(request);
    }
    @PostMapping(value = "/sendMulticastNotification")
    public void sendMulticastNotification(@RequestBody HashMap<String,Object> request, HttpServletResponse response) throws Exception {
        init();
        String commandName = (String) request.get("command");
        Class<?> commandClass = commands.get(commandName);
        Constructor<?> cons1 = commandClass.getConstructor(NotificationService.class);
        NotificationService notificationService = beanFactory.getBean(NotificationService.class);
        NotificationsCommand command = (NotificationsCommand) cons1.newInstance(notificationService);
        command.execute(request);
    }
}
