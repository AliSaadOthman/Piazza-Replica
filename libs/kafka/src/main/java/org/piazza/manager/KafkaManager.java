package org.piazza.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class KafkaManager {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public void activateConsumer(String consumerId) {
        MessageListenerContainer messageListenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(messageListenerContainer)) {
            throw new RuntimeException(String.format("Consumer with id %s is not found", consumerId));
        } else if (messageListenerContainer.isRunning()) {
            throw new RuntimeException(String.format("Consumer with id %s is already running", consumerId));
        } else {
            log.info("Activating a consumer with id " + consumerId);
            messageListenerContainer.start();
        }
    }

    public void pauseConsumer(String consumerId) {
        MessageListenerContainer messageListenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(messageListenerContainer)) {
            throw new RuntimeException(String.format("Consumer with id %s is not found", consumerId));
        } else if (!messageListenerContainer.isRunning()) {
            throw new RuntimeException(String.format("Consumer with id %s is not running", consumerId));
        } else if (messageListenerContainer.isContainerPaused()) {
            throw new RuntimeException(String.format("Consumer with id %s is already paused", consumerId));
        } else if (messageListenerContainer.isPauseRequested()) {
            throw new RuntimeException(String.format("Consumer with id %s is already requested to be paused", consumerId));
        } else {
            log.info("Pausing a consumer with id " + consumerId);
            messageListenerContainer.pause();
        }
    }

    public void resumeConsumer(String consumerId) {
        MessageListenerContainer messageListenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(messageListenerContainer)) {
            throw new RuntimeException(String.format("Consumer with id %s is not found", consumerId));
        } else if (!messageListenerContainer.isRunning()) {
            throw new RuntimeException(String.format("Consumer with id %s is not running", consumerId));
        } else if (!messageListenerContainer.isContainerPaused()) {
            throw new RuntimeException(String.format("Consumer with id %s is not paused", consumerId));
        } else {
            log.info("Resuming a consumer with id " + consumerId);
            messageListenerContainer.resume();
        }
    }

    public void deactivateConsumer(String consumerId) {
        MessageListenerContainer messageListenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(messageListenerContainer)) {
            throw new RuntimeException(String.format("Consumer with id %s is not found", consumerId));
        } else if (!messageListenerContainer.isRunning()) {
            throw new RuntimeException(String.format("Consumer with id %s is already stop", consumerId));
        } else {
            log.info("Stopping a consumer with id " + consumerId);
            messageListenerContainer.stop();
        }
    }
}