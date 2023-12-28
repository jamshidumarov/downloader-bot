package jamshid.uz.downloaderbot.service;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class Bundle {

    public String getTranslation(String key, String lang) {
        try {
            return ResourceBundle.getBundle("language", new Locale(lang)).getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
