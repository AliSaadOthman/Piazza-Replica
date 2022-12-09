package org.piazza.managers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.piazza.utils.Constants;
import org.piazza.utils.CustomClassLoader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Builder
@AllArgsConstructor
public class CommandsController {

    @Autowired
    private CustomClassLoader customClassLoader;

    private final Map<String, Class<?>> commandsMap = new ConcurrentHashMap<>();
    private final Map<String, Class<?>> backDoorCommandsMap = new ConcurrentHashMap<>();
    private String service;

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        Properties properties = readPropertiesFile(Constants.COMMAND_MAP_FILE);
        for (final String commandName : properties.stringPropertyNames()) {
            if (commandName.equals(Constants.COMMAND_SERVICE_ATTRIBUTE)) {
                this.service = properties.getProperty(commandName);
            } else {
                commandsMap.put(commandName,  Class.forName(properties.getProperty(commandName)));
            }
        }
        System.out.println(commandsMap);
        Properties backDoorProperties = readPropertiesFile(Constants.BACK_DOOR_COMMAND_MAP_FILE);
        for (final String commandName : backDoorProperties.stringPropertyNames()) {
            if (commandName.equals(Constants.COMMAND_SERVICE_ATTRIBUTE)) {
                this.service = backDoorProperties.getProperty(commandName);
            } else {
                backDoorCommandsMap.put(commandName,  Class.forName(backDoorProperties.getProperty(commandName)));
            }
        }
        System.out.println(backDoorCommandsMap);
    }

    private Properties readPropertiesFile(String path) throws IOException {
        final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        final Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    public Class<?> getCommand(String commandName) throws ClassNotFoundException {
        final Class<?> clazz = commandsMap.get(commandName);
        if (clazz == null) {
            throw new ClassNotFoundException("Command Not Found");
        }
        return clazz;
    }

    public Class<?> getBackDoorCommand(String commandName) throws ClassNotFoundException {
        final Class<?> clazz = backDoorCommandsMap.get(commandName);
        if (clazz == null) {
            throw new ClassNotFoundException("Command Not Found");
        }
        return clazz;
    }

    // EX: {AddQuestion : org.piazza.commands.addQuestion} --> commandName = AddQuestion, className = org.piazza.commands.addQuestion.\
    // To be used when initializing the CommandMap.
    public void addUpdateCommand(String commandName, String className) throws ClassNotFoundException {
        final Class<?> clazz = customClassLoader.loadClassByName(className);
        commandsMap.put(commandName, clazz);
    }

    // To be used when Adding/Updating Commands during Runtime.
    public void addUpdateCommand(String commandName, String className, byte[] classBytes) {
        final Class<?> clazz = customClassLoader.loadClassFromBytes(className, classBytes);
        commandsMap.put(commandName, clazz);
    }

    public void deleteCommand(String commandName) {
        commandsMap.remove(commandName);
    }

    public String getService() {
        return service;
    }
}
