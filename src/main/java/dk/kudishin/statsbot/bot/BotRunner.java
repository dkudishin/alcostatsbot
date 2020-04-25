package dk.kudishin.statsbot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import dk.kudishin.statsbot.tasks.DeleteUnprocessedMessageTimerTask;
import dk.kudishin.statsbot.tasks.EverydayPollTimerTask;
import dk.kudishin.statsbot.tasks.ExportTimerTask;

import javax.annotation.PostConstruct;
import java.time.*;
import java.util.Date;
import java.util.Timer;

@Component
public class BotRunner {

    private final StatsBot bot;
    private final EverydayPollTimerTask everydayPollTask;
    private final DeleteUnprocessedMessageTimerTask deleteMessageTask;
    private final ExportTimerTask exportTask;

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

    public BotRunner(StatsBot bot, EverydayPollTimerTask everydayPollTask, DeleteUnprocessedMessageTimerTask deleteMessageTask, ExportTimerTask exportTask) {
        this.bot = bot;
        this.everydayPollTask = everydayPollTask;
        this.deleteMessageTask = deleteMessageTask;
        this.exportTask = exportTask;
    }

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
        Timer timer = new Timer();

//        timer.scheduleAtFixedRate(everydayPollTask, getStartDateFor(pollHour, pollMinute), getPeriod());
//        timer.scheduleAtFixedRate(deleteMessageTask, getStartDateFor(deleteHour, deleteMinute), getPeriod());
//        timer.scheduleAtFixedRate(exportTask, getStartDateFor(exportHour, exportMinute), getPeriod());

//        timer.scheduleAtFixedRate(everydayPollTask, getTestStartDateForNow(5), 1000);
        timer.scheduleAtFixedRate(everydayPollTask, getTestStartDateForNow(5), getPeriod());
        timer.scheduleAtFixedRate(deleteMessageTask, getTestStartDateForNow(10), 10000);
        timer.scheduleAtFixedRate(exportTask, getTestStartDateForNow(15), 5000);

    }

    private Long getPeriod() {
        return 1000L * 60L * 60L * 24L;
    }

    private Date getStartDateFor(int hour, int minute) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(hour, minute);
        LocalDateTime startTime = LocalDateTime.of(tomorrow, time);
        return Date.from(startTime.toInstant((ZoneOffset) ZoneId.of("+2")));
    }

    private Date getTestStartDateForNow(int delay) {
        LocalDateTime now = LocalDateTime.now().plusSeconds(delay);
        return Date.from(now.toInstant((ZoneOffset) ZoneId.of("+2")));
    }
}