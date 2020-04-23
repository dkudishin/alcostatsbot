package dk.kudishin.statsbot.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollMessageRepository extends JpaRepository<PollMessage, Integer> {

    List<PollMessage> findByProcessedFlag(String processedFlagValue);
}
