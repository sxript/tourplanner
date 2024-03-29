package at.fhtw.swen2.tutorial;

import at.fhtw.swen2.tutorial.presentation.event.ApplicationStartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Swen2TemplateApplication extends Application {

    private final Logger logger = LoggerFactory.getLogger(Swen2TemplateApplication.class);
    private ConfigurableApplicationContext applicationContext;
    
    @Override
    public void start(Stage stage) {
        logger.debug("Starting TutorialApplication");
        applicationContext.publishEvent(new ApplicationStartupEvent(this, stage));
    }

    @Override
    public void init() {
        logger.debug("Initializing Spring ApplicationContext");

        applicationContext = new SpringApplicationBuilder(Swen2TemplateApplicationBoot.class)
            .sources(Swen2TemplateApplicationBoot.class)
            .initializers(initializers())
            .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void stop() {
        logger.debug("Stopping TutorialApplication");
        
        applicationContext.close();
        Platform.exit();
    }     

    ApplicationContextInitializer<GenericApplicationContext> initializers() { 
        return ac -> {
            ac.registerBean(Application.class, () -> Swen2TemplateApplication.this);
            ac.registerBean(Parameters.class, this::getParameters);
            ac.registerBean(HostServices.class, this::getHostServices);
        };
    }
}
