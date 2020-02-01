package statsbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import statsbot.common.AlcoStatsBot;
import statsbot.common.Config;
import statsbot.export.Export;
import statsbot.storage.Storage;
import statsbot.tasks.DeleteUnprocessedMessageTimerTask;
import statsbot.tasks.EverydayPollTimerTask;
import statsbot.tasks.ExportTimerTask;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class Initializer {

    @Autowired
    private TelegramBotsApi api;
    @Autowired
    private AlcoStatsBot bot;
    @Autowired
    private Storage storage;
    @Autowired
    private Export export;

    public void init() {
        registerBot();
        setTimers();
    }

    private void registerBot() {
        try {
            api.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    private void setTimers() {
        TimerTask everydayPollTask = new EverydayPollTimerTask(bot, storage);
        TimerTask deleteMessageTask = new DeleteUnprocessedMessageTimerTask(bot, storage);
        TimerTask exportTask = new ExportTimerTask(export);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(everydayPollTask, Config.getStartDateFor(Config.POLL_HOUR, Config.POLL_MINUTE), Config.getPeriod());
        timer.scheduleAtFixedRate(deleteMessageTask, Config.getStartDateFor(Config.DELETE_HOUR, Config.DELETE_MINUTE), Config.getPeriod());
        timer.scheduleAtFixedRate(exportTask, Config.getStartDateFor(Config.EXPORT_HOUR, Config.EXPORT_MINUTE), Config.getPeriod());
    }
}