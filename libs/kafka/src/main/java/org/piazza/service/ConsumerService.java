package org.piazza.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.Map;

@Service
@ComponentScan(basePackages = {"org.piazza"})
public class ConsumerService {

    @Autowired
    private ConsumerFactory<String, Map<String, Object>> consumerFactory;
    @Autowired
    private KafkaTemplate<String, Map<String, Object>> replyKafkaTemplate;

    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,  Map<String, Object>>> requestReplyListenerContainerFactory(String groupId, Integer concurrency) {
        ConcurrentKafkaListenerContainerFactory<String,  Map<String, Object>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> configs = new Hashtable<>();
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerFactory.updateConfigs(configs);
        factory.setConsumerFactory(consumerFactory);
        factory.setReplyTemplate(replyKafkaTemplate);
        factory.setConcurrency(concurrency);
        return factory;
    }
}
