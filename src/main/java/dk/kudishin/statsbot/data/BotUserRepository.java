package dk.kudishin.statsbot.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BotUserRepository extends JpaRepository<BotUser, Integer> {
}
