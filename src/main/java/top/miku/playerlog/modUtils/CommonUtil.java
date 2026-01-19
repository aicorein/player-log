package top.miku.playerlog.modUtils;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import static top.miku.playerlog.PlayerLog.MOD_ID;

public class CommonUtil {
    public static String getLogPrefix(String content) {
        return String.format("[%s] %s", MOD_ID, content);
    }

    public static MutableComponent getTranslatedComponent(ServerPlayer player, String key, Object... args) {
        String langCode = player.clientInformation().language();
        return ServerSideTranslator.getTranslatedComponent(langCode, key, args);
    }
}
