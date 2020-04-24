package dk.kudishin.statsbot.tasks;

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