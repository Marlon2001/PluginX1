package me.marlon.x1.commands;

import me.marlon.x1.Main;
import me.marlon.x1.managers.FileManager;
import me.marlon.x1.model.Command;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetCamaroteCommand implements Command {

    private final Main plugin = Main.getInstance();

    @Override
    public String getPermission() {
        return "x1.admin";
    }

    @Override
    public void onCommand(Player player, org.bukkit.command.Command command, String label, String... arguments) {
        Location location = player.getLocation();

        if(plugin.getConfigurationManager().setArenaCamarote(location)) {
            player.sendMessage(FileManager.getMessage("camarote_setado"));
        }
    }

    @Override
    public String getDescription() {
        return FileManager.getMessage("cmd_setcamarote");
    }
}
