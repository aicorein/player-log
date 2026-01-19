package top.miku.playerlog;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import static top.miku.playerlog.PlayerLog.MOD_VERSION;
import static top.miku.playerlog.PlayerLog.LOGGER;

import top.miku.playerlog.suggestions.LogHistorySuggestionProvider;
import top.miku.playerlog.modUtils.RateLimitManager;
import top.miku.playerlog.modUtils.CommonUtil;
import top.miku.playerlog.config.ConfigManager;

public class CommandExecutor {
    private static RateLimitManager LogMessageLimiter = new RateLimitManager(
            ConfigManager.getConfig().logMessageMaxPermits, ConfigManager.getConfig().logMessageMaxInterval);

    public static int EchoVersion(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        MutableComponent versionInfo = CommonUtil
                .getTranslatedComponent(player, "command.player-log.version_str", MOD_VERSION)
                .withStyle(ChatFormatting.GREEN);
        context.getSource().sendSuccess(() -> versionInfo, false);
        return 1;
    }

    public static int LogMessage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var src = context.getSource();
        ServerPlayer player = src.getPlayerOrException();

        if (!LogMessageLimiter.tryAcquire()) {
            src.sendFailure(CommonUtil.getTranslatedComponent(player, "command.player-log.message.rate_limited"));
            return 0;
        }

        String msg = StringArgumentType.getString(context, "msg");
        Integer maxMessageLength = ConfigManager.getConfig().maxLogMessageLength;
        if (msg.length() > maxMessageLength) {
            String errMsg = CommonUtil.getTranslatedComponent(player, "command.player-log.message.too_long",
                    maxMessageLength).getString();
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create(errMsg);
        }
        LogHistorySuggestionProvider.addLogMessageHistory(player, msg);
        LOGGER.info(CommonUtil.getLogPrefix(String.format("<%s> %s",
                player.getDisplayName().getString(),
                msg)));
        return 1;
    }

    public static int reloadConfig(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConfigManager.load();
        LogMessageLimiter = new RateLimitManager(
                ConfigManager.getConfig().logMessageMaxPermits, ConfigManager.getConfig().logMessageMaxInterval);

        var src = context.getSource();
        ServerPlayer player = src.getPlayerOrException();
        context.getSource().sendSuccess(() -> CommonUtil
                .getTranslatedComponent(player, "command.player-log.config.reloaded").withStyle(ChatFormatting.GREEN),
                false);
        return 1;
    }
}
