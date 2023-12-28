package jamshid.uz.downloaderbot.bot;

import jamshid.uz.downloaderbot.service.Bundle;
import jamshid.uz.downloaderbot.service.Store;
import jamshid.uz.downloaderbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DownloaderBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final UserService userService;
    private final Store store;
    private final Bundle bundle;

    private static final Logger _logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        User user = null;
        if (update.hasMessage()) {
            user = update.getMessage().getFrom();
            userService.save(update.getMessage().getFrom());
            if (List.of("/start", "/settings").contains(update.getMessage().getText()))
                sendWelcomeMessage(user.getId());
        } else if (update.hasCallbackQuery()) {
            user = update.getCallbackQuery().getFrom();
            String data = update.getCallbackQuery().getData();
            if (List.of("en", "ru", "uz").contains(data)) {
                store.put(user.getId(), data);
                sendIntroMessage(user.getId(), data);
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    public void sendWelcomeMessage(Long chatId) {
        try {
            String text = "\uD83C\uDDF7\uD83C\uDDFA Выберите язык:\n\n" +
                    "\uD83C\uDDEC\uD83C\uDDE7 Choose language:\n\n" +
                    "\uD83C\uDDFA\uD83C\uDDFF Tilni tanlang:";
            SendMessage sendMessage = new SendMessage(chatId.toString(), text);
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


            InlineKeyboardButton russian = new InlineKeyboardButton();
            russian.setText("\uD83C\uDDF7\uD83C\uDDFA Русский");
            russian.setCallbackData("ru");

            InlineKeyboardButton english = new InlineKeyboardButton();
            english.setText("\uD83C\uDDEC\uD83C\uDDE7 English");
            english.setCallbackData("en");

            InlineKeyboardButton uzbek = new InlineKeyboardButton();
            uzbek.setText("\uD83C\uDDFA\uD83C\uDDFF O'zbekcha");
            uzbek.setCallbackData("uz");

            List<List<InlineKeyboardButton>> rowList = List.of(
                    List.of(russian),
                    List.of(english),
                    List.of(uzbek)
            );
            inlineKeyboardMarkup.setKeyboard(rowList);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("Error: " + e);
        }
    }

    public void sendIntroMessage(Long chatId, String lang) {
        try {
            SendMessage sendMessage = new SendMessage(chatId.toString(), bundle.getTranslation("send.link", lang));
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("Error: " + e);
        }
    }
}
