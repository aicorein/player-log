package top.miku.playerlog;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import top.miku.playerlog.modUtils.ServerSideTranslator;
import top.miku.playerlog.suggestions.LogHistorySuggestionProvider;
import top.miku.playerlog.config.ConfigManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.StringArgumentType;

public class PlayerLog implements ModInitializer {
	public static final String MOD_ID = "player-log";
	public static String MOD_VERSION = "<Unknown>";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID)
			.map(c -> c.getMetadata().getVersion().getFriendlyString())
			.orElse("<Unknown>");

		ConfigManager.load();
		ServerSideTranslator.initialize();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			if (!environment.includeDedicated && !environment.includeIntegrated) {
				return;
			}

			dispatcher.register(
				Commands.literal("playerlog")
				.executes(CommandExecutor::EchoVersion)
				.then(Commands.literal("send")
					.then(Commands.argument("msg", StringArgumentType.greedyString())
						.suggests(new LogHistorySuggestionProvider())
						.executes(CommandExecutor::LogMessage)
					)
				)
				.then(Commands.literal("reload")
					.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))
					.executes(CommandExecutor::reloadConfig)
				)
			);
			dispatcher.register(
				Commands.literal("s")
				.then(Commands.argument("msg", StringArgumentType.greedyString())
					.suggests(new LogHistorySuggestionProvider())
					.executes(CommandExecutor::LogMessage)
				)
			);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayer player = handler.getPlayer();
			LogHistorySuggestionProvider.clearLogMessageHistory(player);
		});

		LOGGER.info(String.format("%s %s initialized!", MOD_ID, MOD_VERSION));
	}
}
