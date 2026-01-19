package top.miku.playerlog.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static top.miku.playerlog.PlayerLog.LOGGER;
import static top.miku.playerlog.PlayerLog.MOD_ID;

public class ConfigManager {
    private static ModConfig config;
    // 1. 获取全局 config 目录 (.minecraft/config)
    private static final Path GLOBAL_CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    // 2. 定义你模组的专属目录 (.minecraft/config/mymod)
    // 注意：这里建议使用你的 modid，避免和其他模组冲突
    private static final Path MOD_CONFIG_DIR = GLOBAL_CONFIG_DIR.resolve(MOD_ID);
    // 3. 定义具体的文件 (.minecraft/config/mymod/config.json)
    // 因为已经在专属目录里了，文件名可以直接叫 config.json 或者 main.json
    private static final File CONFIG_FILE = MOD_CONFIG_DIR.resolve("config.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        // 【关键步骤】在读写之前，必须确保父目录存在！
        // getParentFile() 会获取 'mymod' 文件夹
        // mkdirs() 会创建文件夹（如果已存在则什么都不做）
        File parentDir = CONFIG_FILE.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                LOGGER.warn("player-log cannot read config file, fallback to defaults.");
                config = new ModConfig();
            }
        } else {
            config = new ModConfig(); // 使用默认值
            save(); // 保存到硬盘
        }
    }

    public static void save() {
        // 保存时也建议检查一下目录，防止目录被用户意外删除
        File parentDir = CONFIG_FILE.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            LOGGER.error("player-log cannot write config file!", e);
        }
    }

    public static ModConfig getConfig() {
        if (config == null) {
            load();
        }
        return config;
    }
}
