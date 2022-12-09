package org.piazza.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.piazza.reply_template_abstraction.CompletableFutureReplyKafkaTemplate;
import org.piazza.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;

@Configuration
@EnableKafka
@EnableAsync
@Import(KafkaConfig.class)
public class ServerConfig {

    @Autowired
    private ProducerService producerService;

    @Value("${kafka.topics.server.name}")
    private String topic;
    @Value("${kafka.consumer.group-id}")
    private String groupId;
    @Value("${kafka.topics.server.partitions}")
    private String partitions;
    @Value("${kafka.topics.server.replicas}")
    private String replicas;
    @Value("${kafka.consumer.concurrency}")
    private String concurrency;

    @Bean
    public CompletableFutureReplyKafkaTemplate<String, Map<String, Object>, Map<String, Object>> replyKafkaTemplate() {
        return producerService.replyKafkaTemplate(topic, groupId, Integer.parseInt(concurrency));
    }

    @Bean
    public NewTopic ServerControllerTopic() {
        return new NewTopic(topic, Integer.parseInt(partitions), (short) Integer.parseInt(replicas));
    }

}
