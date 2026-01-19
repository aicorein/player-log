package top.miku.playerlog.suggestions;

import com.google.common.collect.EvictingQueue;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import top.miku.playerlog.config.ConfigManager;

public class LogHistorySuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    private static final Map<ServerPlayer, EvictingQueue<String>> playerHistorys = new HashMap<>();

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder) throws CommandSyntaxException {
        EvictingQueue<String> history = playerHistorys.getOrDefault(context.getSource().getPlayerOrException(), null);
        if (history != null) {
            for (String msg : history) {
                builder.suggest(msg);
            }
        }
        return builder.buildFuture();
    }

    public static void addLogMessageHistory(ServerPlayer player, String msg) {
        EvictingQueue<String> historys = playerHistorys.computeIfAbsent(player,
                k -> EvictingQueue.create(ConfigManager.getConfig().maxLogMessageHistory));
        historys.add(msg);
    }

    public static void clearLogMessageHistory(ServerPlayer player) {
        if (playerHistorys.containsKey(player)) {
            playerHistorys.remove(player);
        }
    }
}