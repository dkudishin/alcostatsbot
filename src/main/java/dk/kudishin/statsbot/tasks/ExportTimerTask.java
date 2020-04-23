package dk.kudishin.statsbot.tasks;

import dk.kudishin.statsbot.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.TimerTask;

@Component
public class ExportTimerTask extends TimerTask {

    private final Storage storage;

    @Autowired
    public ExportTimerTask(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        String now = new Date().toString();
        storage.getBotUsers().forEach(botUser -> System.out.println(now + ";" + botUser.toString()));
    }
}