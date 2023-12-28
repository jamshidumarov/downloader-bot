package jamshid.uz.downloaderbot.service;

import jamshid.uz.downloaderbot.entity.BotUser;
import jamshid.uz.downloaderbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    private static final Logger _logger = LoggerFactory.getLogger(UserService.class);
    public void save(User user){
        try {
            BotUser botUser = userRepository.findByChatId(user.getId()).orElse(null);
            if (botUser == null){
                botUser = BotUser
                        .builder()
                        .username(user.getUserName())
                        .firstname(user.getFirstName())
                        .lastname(user.getLastName())
                        .chatId(user.getId())
                        .build();
            }else {
                botUser.setLastname(botUser.getLastname());
                botUser.setFirstname(botUser.getFirstname());
                botUser.setUsername(botUser.getUsername());
            }
            userRepository.save(botUser);
        }catch (Exception e){
            e.printStackTrace();
            _logger.error("Error: " + e);
        }
    }
}
