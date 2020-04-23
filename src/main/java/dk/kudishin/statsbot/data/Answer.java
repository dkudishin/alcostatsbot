package dk.kudishin.statsbot.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;
    private LocalDateTime answerTime;
    private String answerFlag;

    @ManyToOne(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private BotUser botUserId;

    public Answer(BotUser botUser) {
        this.botUserId = botUser;
        this.answerTime = LocalDateTime.now();
    }
}