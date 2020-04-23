package dk.kudishin.statsbot.data;

import lombok.Data;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@NoArgsConstructor
@Data
@Entity
@Table(name = "bot_users")
public class BotUser {

    @Id
    private Integer userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String subscribed;

    @Transient
    private boolean achievementToday;

    public BotUser(User user) {

        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
        this.subscribed = "N";
    }
}