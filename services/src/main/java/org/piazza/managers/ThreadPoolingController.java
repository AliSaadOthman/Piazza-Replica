package org.piazza.managers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Builder
public class ThreadPoolingController {

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Integer setMaxPoolSize(Integer maxPoolSize) {
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        return threadPoolTaskExecutor.getMaxPoolSize();
    }

    public Integer setCorePoolSize(Integer corePoolSize) {
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        return threadPoolTaskExecutor.getCorePoolSize();
    }

    public void resetQueueCapacity() {
        int acceptableTime = 30;
        double timeToCompleteTask = 0.1;
        int queueCapacity = (int) (threadPoolTaskExecutor.getMaxPoolSize() * (acceptableTime / timeToCompleteTask));
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
    }

    public void freeze() {
        if (threadPoolTaskExecutor == null || isShutdown())
            return;
        try {
            threadPoolTaskExecutor.shutdown();
            if (!threadPoolTaskExecutor.getThreadPoolExecutor().awaitTermination(4, TimeUnit.MINUTES)) {
                threadPoolTaskExecutor.getThreadPoolExecutor().shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPoolTaskExecutor.getThreadPoolExecutor().shutdownNow();
        }
    }

    public void resume() {
        if (!isShutdown())
            return;
        threadPoolTaskExecutor.initialize();
    }

    public boolean isShutdown() {
        return threadPoolTaskExecutor.getThreadPoolExecutor().isShutdown();
    }
}
