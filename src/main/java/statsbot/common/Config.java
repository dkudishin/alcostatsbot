package statsbot.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

public class Config {

    private Config() {
        throw new IllegalStateException("Utility class should not be instantiated");
    }

    public static final String BOT_TOKEN = System.getenv("ALCOBOT_TOKEN");
    public static final String BOT_NAME = "AlcoStatsBot";

    public static final int POLL_HOUR = 10;
    public static final int POLL_MINUTE = 0;

    public static final int DELETE_HOUR = 19;
    public static final int DELETE_MINUTE = 0;

    public static final int EXPORT_HOUR = 19;
    public static final int EXPORT_MINUTE = 10;

    public static final String POLL_MESSAGE = "Have you been drinking?";

    public static Long getPeriod() {
        return 1000L * 60L * 60L * 24L;
    }

    public static Date getStartDateFor(int hour, int minute) {
        LocalDate tmrw = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(hour, minute);
        LocalDateTime startTime = LocalDateTime.of(tmrw, time);
        return Date.from(startTime.toInstant(ZoneOffset.UTC));
    }
}
