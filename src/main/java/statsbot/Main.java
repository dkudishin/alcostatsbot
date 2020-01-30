package statsbot;

import statsbot.common.AlcoStatsBot;
import statsbot.common.Config;
import statsbot.export.Export;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import statsbot.storage.Storage;
import statsbot.tasks.DeleteUnprocessedMessageTimerTask;
import statsbot.tasks.EverydayPollTimerTask;
import statsbot.tasks.ExportTimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import org.telegram.telegrambots.meta.logging.BotsFileHandler;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;


public class Main {

    @Autowired
    private static Storage storage;
    @Autowired
    private static Export export;

    public static void main(String[] args) {

        ApiContextInitializer.init();

        var applicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);

        setupLog();

        TelegramBotsApi telegramBotsApi = applicationContext.getBean("telegramBotsApi", TelegramBotsApi.class);
        AlcoStatsBot alcobot = applicationContext.getBean(AlcoStatsBot.class);

        try {
            telegramBotsApi.registerBot(alcobot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        setTimers(alcobot);
    }

    private static void setTimers(AlcoStatsBot alcobot) {
        TimerTask everydayPollTask = new EverydayPollTimerTask(alcobot, storage);
        TimerTask deleteMessageTask = new DeleteUnprocessedMessageTimerTask(alcobot, storage);
        TimerTask exportTask = new ExportTimerTask(export);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(everydayPollTask, Config.getStartDateFor(Config.POLL_HOUR, Config.POLL_MINUTE), Config.getPeriod());
        timer.scheduleAtFixedRate(deleteMessageTask, Config.getStartDateFor(Config.DELETE_HOUR, Config.DELETE_MINUTE), Config.getPeriod());
        timer.scheduleAtFixedRate(exportTask, Config.getStartDateFor(Config.EXPORT_HOUR, Config.EXPORT_MINUTE), Config.getPeriod());
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