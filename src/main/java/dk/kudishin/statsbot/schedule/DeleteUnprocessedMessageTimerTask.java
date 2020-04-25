package dk.kudishin.statsbot.schedule;

import dk.kudishin.statsbot.actions.DeleteUnprocessedMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class DeleteUnprocessedMessageTimerTask extends TimerTask {

    private final DeleteUnprocessedMessages deleteUnprocessedMessagesAction;

    @Autowired
    public DeleteUnprocessedMessageTimerTask(DeleteUnprocessedMessages deleteUnprocessedMessagesAction) {
        this.deleteUnprocessedMessagesAction = deleteUnprocessedMessagesAction;
    }

    @Override
    public void run() {
        deleteUnprocessedMessagesAction.execute();
    }
}