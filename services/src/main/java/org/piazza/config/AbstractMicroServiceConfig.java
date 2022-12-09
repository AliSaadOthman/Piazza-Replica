package org.piazza.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.piazza.managers.CommandsController;
import org.piazza.managers.InvokeController;
import org.piazza.managers.QueueController;
import org.piazza.managers.ThreadPoolingController;
import org.piazza.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableKafka
@EnableAsync
@Import(value = {KafkaConfig.class})
public class AbstractMicroServiceConfig {

    @Autowired
    private ConsumerService consumerService;

    @Value("${kafka.topics.course.name}")
    private String requestTopic;
    @Value("${kafka.consumer.group-id}")
    private String groupId;
    @Value("${kafka.consumer.concurrency}")
    private String concurrency;
    @Value("${kafka.topics.course.partitions}")
    private String partitions;
    @Value("${kafka.topics.course.replicas}")
    private String replicas;

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Map<String, Object>>> requestReplyListenerContainerFactory() {
        return consumerService.requestReplyListenerContainerFactory(groupId, Integer.parseInt(concurrency));
    }

    @Bean
    public NewTopic appTopic() {
        return new NewTopic(requestTopic, Integer.parseInt(partitions), (short) Integer.parseInt(replicas));
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public QueueController queueManager() {
        return QueueController.builder().build();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public InvokeController invokeController() {
        return InvokeController.builder().build();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public CommandsController commandsController() {
        return CommandsController.builder().build();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ThreadPoolingController threadPoolingManager() {
        return ThreadPoolingController.builder().build();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ThreadPoolTaskExecutor threadPool() {
        int cores = Runtime.getRuntime().availableProcessors();
        int acceptableTime = 30;
        double timeToCompleteTask = 0.1;
        int queueSize = (int) (cores * (acceptableTime / timeToCompleteTask));
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(cores / 2);
        threadPoolTaskExecutor.setQueueCapacity(queueSize);
        threadPoolTaskExecutor.setMaxPoolSize(cores);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}