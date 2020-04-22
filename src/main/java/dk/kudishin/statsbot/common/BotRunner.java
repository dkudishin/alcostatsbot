package dk.kudishin.statsbot.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import dk.kudishin.statsbot.common.StatsBot;
import dk.kudishin.statsbot.export.Export;
import dk.kudishin.statsbot.storage.Storage;
import dk.kudishin.statsbot.tasks.DeleteUnprocessedMessageTimerTask;
import dk.kudishin.statsbot.tasks.EverydayPollTimerTask;
import dk.kudishin.statsbot.tasks.ExportTimerTask;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class BotRunner {

    @Autowired
    private StatsBot bot;
    @Autowired
    private Storage storage;
    @Autowired
    private Export export;

    @Value("${POLL_MESSAGE}")
    private String pollMessage;

    @Value("${POLL_HOUR}")
    private int pollHour;

    @Value("${POLL_MINUTE}")
    private int pollMinute;

    @Value("${DELETE_HOUR}")
    private int deleteHour;

    @Value("${DELETE_MINUTE}")
    private int deleteMinute;

    @Value("${EXPORT_HOUR}")
    private int exportHour;

    @Value("${EXPORT_MINUTE}")
    private int exportMinute;


    @PostConstruct
    public void init() {
        registerBot();
        setTimers();
    }

    private void registerBot() {
        try {
            new TelegramBotsApi().registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    private void setTimers() {
        TimerTask everydayPollTask = new EverydayPollTimerTask(bot, storage, pollMessage);
        TimerTask deleteMessageTask = new DeleteUnprocessedMessageTimerTask(bot, storage);
        TimerTask exportTask = new ExportTimerTask(export);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(everydayPollTask, getStartDateFor(pollHour, pollMinute), getPeriod());
        timer.scheduleAtFixedRate(deleteMessageTask, getStartDateFor(deleteHour, deleteMinute), getPeriod());
        timer.scheduleAtFixedRate(exportTask, getStartDateFor(exportHour, exportMinute), getPeriod());
    }

    private Long getPeriod() {
        return 1000L * 60L * 60L * 24L;
    }

    private Date getStartDateFor(int hour, int minute) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(hour, minute);
        LocalDateTime startTime = LocalDateTime.of(tomorrow, time);
        return Date.from(startTime.toInstant(ZoneOffset.UTC));
    }
}