package org.piazza.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {DatasourceConfig.class, AbstractMicroServiceConfig.class})
@ComponentScan("org.piazza")
public class ConfigApp {

}
