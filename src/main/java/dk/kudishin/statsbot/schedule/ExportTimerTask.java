package dk.kudishin.statsbot.schedule;

import dk.kudishin.statsbot.actions.ExportStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class ExportTimerTask extends TimerTask {

    private final ExportStats exportStats;

    @Autowired
    public ExportTimerTask(ExportStats exportStats) {
        this.exportStats = exportStats;
    }

    @Override
    public void run() {
        exportStats.execute();
    }
}