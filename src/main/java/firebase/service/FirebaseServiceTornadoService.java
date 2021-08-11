package firebase.service;

import bean.TornadoMonster;
import firebase.error.FirebaseException;
import firebase.error.JacksonUtilityException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseServiceTornadoService {

    private static FirebaseService service = initService();


    public static Map<String, Object> getStair(final int stair) throws UnsupportedEncodingException, FirebaseException {
        if (Objects.nonNull(service)) {
            final FirebaseResponse response = service.get(String.valueOf(stair));
            return response.getBody();
        } else {
            return Collections.emptyMap();
        }
    }

    public static Map<String, Object> saveNewMonster(final TornadoMonster monster) {

        if (Objects.nonNull(service)) {
            try {
                final Map<String, Object> attributMap = new HashMap<>();
                attributMap.put("chance", monster.getLuck());
                attributMap.put("intelligence", monster.getIntelligence());
                attributMap.put("skill", monster.getSkill());
                attributMap.put("stamina", monster.getStamina());
                attributMap.put("strength", monster.getStrength());

                final Map<String, Object> map = new HashMap<>();
                map.put("attributs", attributMap);
                map.put("level", monster.getLevel());
                map.put("name", monster.getName());
                map.put("image", "");

                final JSONObject jsonObject = new JSONObject();
                jsonObject
                        .put("attributs", new JSONObject()
                                .put("chance", monster.getLuck())
                                .put("intelligence", monster.getIntelligence())
                                .put("skill", monster.getSkill())
                                .put("stamina", monster.getStamina())
                                .put("strength", monster.getStrength()))
                        .put("level", monster.getLevel())
                        .put("name", monster.getName())
                        .put("image", "");
                final FirebaseResponse response = service.patch(String.valueOf(monster.getStair()), map);
                return response.getBody();
            } catch (JacksonUtilityException | FirebaseException | UnsupportedEncodingException ignored) {
            }
        }

        return Collections.emptyMap();
    }

    private static FirebaseService initService() {
        try {
            return new FirebaseService("https://songwritter-377b0.firebaseio.com/shakefidget/tornado");
        } catch (FirebaseException exception) {
            return null;
        }
    }

    private FirebaseServiceTornadoService() {
    }
}
