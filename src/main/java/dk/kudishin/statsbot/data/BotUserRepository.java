package dk.kudishin.statsbot.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BotUserRepository extends JpaRepository<BotUser, Integer> {

    public List<BotUser> getBotUsersBySubscribed(String subscribedFlagValue);
}
