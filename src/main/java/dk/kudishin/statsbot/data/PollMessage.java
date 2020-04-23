package dk.kudishin.statsbot.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Data
@Entity
@Table(name = "messages")
public class PollMessage {

    @Id
    private Integer messageId;
    private String processedFlag;
    private Integer userId;

    public PollMessage(Integer messageId, Integer chatId) {
        this.messageId = messageId;
        this.processedFlag = "N";
        this.userId = chatId;
    }
}
