package me.marlon.x1.managers;

import me.marlon.x1.Main;
import org.bukkit.configuration.ConfigurationSection;

public class FileManager {

    public static String getMessage(String key) {
        Main plugin = Main.getInstance();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("Mensagens");

        if (plugin.getConfigurationManager().isPrefixMessage())
            return section.getString("prefix").replaceAll("&", "§") + section.getString(key).replaceAll("&", "§");
        else
            return section.getString(key).replaceAll("&", "§");
    }

    public static String getMessageNoPrefix(String key) {
        Main plugin = Main.getInstance();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("Mensagens");

        return section.getString(key).replaceAll("&", "§");
    }

    public static void savePositions(String key, String loc) {
        Main plugin = Main.getInstance();

        plugin.getConfig().set(key, loc);
        plugin.saveConfig();
    }
}
