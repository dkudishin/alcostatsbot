package dk.kudishin.statsbot.tasks;

import dk.kudishin.statsbot.storage.Storage;

import java.util.Date;
import java.util.TimerTask;

public class ExportTimerTask extends TimerTask {

    private final Storage storage;

    public ExportTimerTask(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        String now = new Date().toString();
        storage.getBotUsers().forEach(botUser -> System.out.println(now + ";" + botUser.toString()));
    }
}