package org.piazza.config;

import org.piazza.commands.course.AddCourseCommand;
import org.piazza.utils.Command;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Import(value = {ArangoDbConfiguration.class, AbstractMicroServiceConfig.class})
public class ConfigApp {

    @Bean
    public Map<String, Command> commandMap() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        HashMap<String, Command> commandHashMap = new HashMap<>();
        String commandPath = "org.piazza.commands.course.AddCourseCommand";
        Class<?> commandClass = Class.forName(commandPath);
        AddCourseCommand command = (AddCourseCommand) commandClass.getConstructor().newInstance();
        commandHashMap.put("AddCourseCommand", command);
        return commandHashMap;
    }
}
