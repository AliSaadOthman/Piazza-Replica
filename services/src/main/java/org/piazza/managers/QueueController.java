package org.piazza.managers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.piazza.manager.KafkaManager;
import org.piazza.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@Builder
public class QueueController {

    @Autowired
    private KafkaManager kafkaManager;
    @Autowired
    private InvokeController invokeController;
    @Value("${kafka.consumer.id}")
    private String listenerId;

    @KafkaListener(
            id = "${kafka.consumer.id}",
            topics = "${kafka.topics.course.name}",
            groupId = "${kafka.consumer.group-id}",
            containerFactory = "requestReplyListenerContainerFactory"
    )
    @SendTo
    public Map<String, Object> listener(Map<String, Object> request) {
        try {
            return invokeController.invokeCommand((String) request.get(Constants.COMMAND_NAME_ATTRIBUTE),
                    (HashMap<String, Object>) request.get(Constants.REQUEST_BODY_ATTRIBUTE)).get();
        } catch (ExecutionException | InterruptedException e) {
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Issue Consuming the Request");
            errorResponse.put("status", HttpStatus.BAD_REQUEST);
            return errorResponse;
        }
    }

    public void resumeConsumer() {
        System.out.println("Hena3");
        kafkaManager.resumeConsumer(listenerId);
    }

    public void pauseConsumer() {
        kafkaManager.pauseConsumer(listenerId);
    }

}
