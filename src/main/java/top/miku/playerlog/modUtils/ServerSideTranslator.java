package top.miku.playerlog.modUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import top.miku.playerlog.PlayerLog;

import static top.miku.playerlog.PlayerLog.LOGGER;

public class ServerSideTranslator {
    private static final Map<String, Map<String, String>> LOCALIZATIONS = new HashMap<>();

    public static void initialize() {
        tryLoadTranslations("en_us");
        tryLoadTranslations("zh_cn");
    }

    public static MutableComponent getTranslatedComponent(String langCode, String key, Object... args) {
        tryLoadTranslations(langCode.toLowerCase());
        String translatedText = LOCALIZATIONS.get(langCode.toLowerCase()).getOrDefault(key, key);
        if (args.length > 0) {
            translatedText = String.format(translatedText, args);
        }
        return Component.literal(translatedText);
    }

    private static void tryLoadTranslations(String langCode) {
        String path = "/assets/%s/lang/%s.json".formatted(PlayerLog.MOD_ID, langCode);
        try {
            InputStream stream = ServerSideTranslator.class.getResourceAsStream(path);
            if (stream == null) {
                LOCALIZATIONS.put(langCode, LOCALIZATIONS.get("en_us"));
                return;
            }

            Gson gson = new Gson();
            JsonObject json = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
            Map<String, String> map = new HashMap<>();
            json.entrySet().forEach(entry -> map.put(entry.getKey(), entry.getValue().getAsString()));
            LOCALIZATIONS.put(langCode, map);
        } catch (Exception e) {
            LOGGER.error("Failed to load language file: " + path, e);
        }
    }
}
