package org.piazza.managers.services;

import org.piazza.managers.CommandsController;
import org.piazza.managers.QueueController;
import org.piazza.managers.ThreadPoolingController;
import org.piazza.utils.BackDoorResponse;
import org.piazza.utils.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("managerService")
public class ManagerService implements Receiver {

    @Autowired
    ThreadPoolingController threadPoolingController;
    @Autowired
    QueueController queueController;
    @Autowired
    CommandsController commandsController;

    private boolean isPaused = false;

    public HashMap<String, Object> pauseMS() {
        if(isPaused)
            return new BackDoorResponse("Micro-Service already paused", HttpStatus.OK).constructResponse();
        try {
            queueController.pauseConsumer();
            threadPoolingController.freeze();
            isPaused = true;
            return new BackDoorResponse("Micro-Service paused successfully", HttpStatus.OK).constructResponse();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new BackDoorResponse(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST).constructResponse();
        }
    }

    public HashMap<String, Object> resumeMS() {
        if (!isPaused) {
            return new BackDoorResponse("Micro-Service is already Running", HttpStatus.OK).constructResponse();
        }try {
            threadPoolingController.resume();
            queueController.resumeConsumer();
            isPaused = false;
            return new BackDoorResponse("Micro-Service resumed successfully", HttpStatus.OK).constructResponse();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new BackDoorResponse(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST).constructResponse();
        }
    }

    public HashMap<String, Object> setThreadPoolConnections(int coreConnections, int maxConnections) {
        try {
            threadPoolingController.setCorePoolSize(coreConnections);
            threadPoolingController.setMaxPoolSize(maxConnections);
            threadPoolingController.resetQueueCapacity();
            return new BackDoorResponse("Successfully Setting ThreadPool Connections", HttpStatus.OK).constructResponse();
        }
        catch (Exception e) {
            return new BackDoorResponse(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST).constructResponse();
        }
    }

    public void setDbMaxPoolConnection() {

    }

    public void addCommand() {

    }

    public void updateCommand() {

    }

    public HashMap<String, Object> removeCommand(String commandName) {
        try {
            commandsController.deleteCommand(commandName);
            return new BackDoorResponse("Successfully Removed Command : " + commandName, HttpStatus.OK).constructResponse();
        }catch (Exception e) {
            return new BackDoorResponse(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST).constructResponse();
        }
    }
}