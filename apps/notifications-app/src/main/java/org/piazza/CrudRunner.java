package org.piazza;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan("org.piazza")
@RestController
public class CrudRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.setProperty("server.port","5000");

    }
}
