package dk.kudishin.statsbot.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.telegram.telegrambots.meta.api.objects.User;

@Getter
@Setter
@ToString
public class BotUser {

    private Integer id;
    private String firstName;
    private String lastName;
    private String userName;
    private boolean drunkToday;

    public BotUser(User user) {

        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
    }
}