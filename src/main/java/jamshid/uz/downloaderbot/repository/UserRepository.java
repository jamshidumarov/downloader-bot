package jamshid.uz.downloaderbot.repository;

import jamshid.uz.downloaderbot.entity.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<BotUser, Integer> {
    Optional<BotUser> findByChatId(Long chatId);
}
