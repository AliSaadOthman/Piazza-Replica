package org.piazza.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.piazza.reply_template_abstraction.CompletableFutureReplyKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Hashtable;
import java.util.Map;

@Service
@ComponentScan(basePackages = {"org.piazza"})
public class ProducerService {

    @Autowired
    ProducerFactory<String, Map<String, Object>> producerFactory;
    @Autowired
    ConsumerFactory<String, Map<String, Object>> consumerFactory;

    ConcurrentMessageListenerContainer<String, Map<String, Object>> replyListenerContainer(String replyTopic, String groupId) {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        Map<String, Object> configs = new Hashtable<>();
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerFactory.updateConfigs(configs);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    public CompletableFutureReplyKafkaTemplate<String, Map<String, Object>, Map<String, Object>> replyKafkaTemplate(String replyTopic, String groupId, Integer concurrency) {
        ConcurrentMessageListenerContainer<String, Map<String, Object>> replyListenerContainer = replyListenerContainer(replyTopic, groupId);
        replyListenerContainer.setConcurrency(concurrency);
        CompletableFutureReplyKafkaTemplate<String, Map<String, Object>, Map<String, Object>> requestReplyKafkaTemplate =
                new CompletableFutureReplyKafkaTemplate<>(producerFactory, replyListenerContainer);
        requestReplyKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(60));
        requestReplyKafkaTemplate.setSharedReplyTopic(true);
        return requestReplyKafkaTemplate;
    }
}
