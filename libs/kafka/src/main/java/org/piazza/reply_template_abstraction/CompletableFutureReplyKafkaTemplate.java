package org.piazza.reply_template_abstraction;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.GenericMessageListenerContainer;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureReplyKafkaTemplate<K, V, R> extends PartitionedReplyKafkaTemplate<K, V, R>{

    public CompletableFutureReplyKafkaTemplate(ProducerFactory<K, V> producerFactory, GenericMessageListenerContainer<K, R> replyContainer) {
        super(producerFactory, replyContainer);
    }

    public CompletableFuture<R> requestReply(String topic, V value) {
        return adapt(sendAndReceive(topic, value));
    }

    public CompletableFuture<R> requestReply(String topic, K key, V value) {
        return adapt(sendAndReceive(topic, key, value));
    }

    private CompletableFuture<R> adapt(RequestReplyFuture<K, V, R> requestReplyFuture) {
        CompletableFuture<R> completableResult = new CompletableFuture<R>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean result = requestReplyFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };
        // Add callback to the request sending result
        requestReplyFuture.getSendFuture().addCallback(new ListenableFutureCallback<SendResult<K, V>>() {
            @Override
            public void onSuccess(SendResult<K, V> sendResult) {
                // Message Sent successfully, Nothing to Do.
            }
            @Override
            public void onFailure(Throwable t) {
                completableResult.completeExceptionally(t);
            }
        });
        // Add callback to the reply
        requestReplyFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(ConsumerRecord<K, R> result) {
                completableResult.complete(result.value());
            }

            @Override
            public void onFailure(Throwable t) {
                completableResult.completeExceptionally(t);
            }
        });
        return completableResult;
    }
}
