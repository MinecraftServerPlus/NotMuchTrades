package io.github.gum4.notmuchtrades.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ConfigUtil {
    /**
     * Create a data folder of this plugin.
     * @param plugin main class of this plugin
     * @return {@code true} if the data folder exists and successfully created; {@code false} otherwise.
     */
    public static File getPluginDataFolder(JavaPlugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (dataFolder.exists()) {
            return dataFolder;
        }

        if (dataFolder.mkdirs()) {
            Bukkit.getLogger().info("Successfully create a data folder of this plugin.");
            return dataFolder;
        }
        else {
            Bukkit.getLogger().severe("Failed to create a data folder of this plugin.");
            return null;
        }
    }

    public static File getConfigFile(JavaPlugin plugin) {
        File dataFolder = getPluginDataFolder(plugin);

        if (Objects.isNull(dataFolder)) return null;

        File file = new File(dataFolder, "config.yml");

        if (!file.exists()) {
            InputStream fileStream = plugin.getResource("config.yml");
            try {
                if (Objects.isNull(fileStream)) {
                    if (file.createNewFile()) {
                        Bukkit.getLogger().info("Successfully created config.yml file.");
                        return file;
                    } else {
                        Bukkit.getLogger().severe("Failed to create data.yml file.");
                        Bukkit.getPluginManager().disablePlugin(plugin);
                        return null;
                    }
                }
                else {
                    FileUtils.copyInputStreamToFile(fileStream, file);
                    Bukkit.getLogger().info("Successfully created config.yml file.");
                    return file;
                }
            }
            catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }
        return file;
    }
}
