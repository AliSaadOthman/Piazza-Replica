package org.piazza;
import org.piazza.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class NotificationApp implements CommandLineRunner {

    //NotificationService notificationService;
    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public void run(String... strings) {
        //Object object = applicationContext.getBean("notificationService");
        //this.notificationService = (NotificationService) object;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NotificationApp.class, args);
        System.out.println("App is running");

    }
}
