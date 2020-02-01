package statsbot;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.logging.BotLogger;
import org.telegram.telegrambots.meta.logging.BotsFileHandler;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {

        ApiContextInitializer.init();

        var applicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);

        setupLog();

        Initializer i = applicationContext.getBean(Initializer.class);
        i.init();
    }

    private static void setupLog() {
        BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe("statsbot.Main", e);
        }
    }
}