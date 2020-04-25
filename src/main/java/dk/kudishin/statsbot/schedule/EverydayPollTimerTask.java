package dk.kudishin.statsbot.schedule;

import dk.kudishin.statsbot.actions.SendPollMessage;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class EverydayPollTimerTask extends TimerTask {

    private final SendPollMessage sendPollMessageAction;

    public EverydayPollTimerTask(SendPollMessage sendPollMessageAction) {
        this.sendPollMessageAction = sendPollMessageAction;
    }

    @Override
    public void run() {
        sendPollMessageAction.execute();
    }
}