package jamshid.uz.downloaderbot.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Store {
    public static Map<Long, String> store = new HashMap<>();

    public int size(){
        return store.size();
    }

    public String get(Long user_id){
        return store.getOrDefault(user_id, "uz");
    }

    public void put(Long user_id, String lang){
        store.put(user_id, lang);
    }
}
