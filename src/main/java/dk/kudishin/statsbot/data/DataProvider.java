package dk.kudishin.statsbot.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataProvider {

    private final BotUserRepository botUserRepository;
    private final AnswerRepository answerRepository;
    private final PollMessageRepository pollMessageRepository;

    @Autowired
    public DataProvider(BotUserRepository botUserRepository, AnswerRepository answerRepository, PollMessageRepository pollMessageRepository) {
        this.botUserRepository = botUserRepository;
        this.answerRepository = answerRepository;
        this.pollMessageRepository = pollMessageRepository;
    }

    public BotUser getBotUserById(Integer userId) {
        Optional<BotUser> some = botUserRepository.findById(userId);
        return some.orElse(null);
    }

    public BotUser saveBotUser(BotUser botUser) {
        return botUserRepository.save(botUser);
    }

    public Answer saveAnswer(Answer userAnswer) {
        return answerRepository.save(userAnswer);
    }

    public PollMessage getPollMessageById(Integer messageId) {
        Optional<PollMessage> some = pollMessageRepository.findById(messageId);
        return some.orElse(null);
    }

    public PollMessage savePollMessage(PollMessage message) {
        return pollMessageRepository.save(message);
    }

    public List<PollMessage> getPollMessageByProcessedFlag(String flagValue) {
        return pollMessageRepository.findByProcessedFlag(flagValue);
    }

    public List<PollMessage> savePollMessages(List<PollMessage> messages) {
        return pollMessageRepository.saveAll(messages);
    }


    public List<BotUser> getBotUsersBySubscribed(String flagValue) {
        return botUserRepository.getBotUsersBySubscribed(flagValue);
    }
}
