package statsbot.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.telegram.telegrambots.meta.api.objects.User;

@ToString
public class Alcoholic  {

    @Getter @Setter private Integer id;
    @Getter @Setter private String firstName;
    @Getter @Setter private String lastName;
    @Getter @Setter private String userName;
    @Getter @Setter private boolean drunkToday;

    public Alcoholic(User user) {

        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
    }
}