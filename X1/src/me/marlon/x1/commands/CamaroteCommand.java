package me.marlon.x1.commands;

import me.marlon.x1.Main;
import me.marlon.x1.managers.FileManager;
import me.marlon.x1.model.Command;
import me.marlon.x1.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CamaroteCommand implements Command {

    private final Main plugin = Main.getInstance();

    @Override
    public String getPermission() {
        return "x1.player";
    }

    @Override
    public void onCommand(Player player, org.bukkit.command.Command command, String label, String... arguments) {
        Location location = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosCamarote());

        if (location != null) {
            player.sendMessage(FileManager.getMessage("camarote_teleporte"));
            if(!plugin.getDesafioManager().getDesafio().getPlayersCamarote().contains(player))
                plugin.getDesafioManager().getDesafio().addPlayerCamarote(player);
            player.teleport(location);
        } else {
            player.sendMessage(FileManager.getMessage("sem_camarote"));
        }
    }

    @Override
    public String getDescription() {
        return FileManager.getMessage("cmd_camarote");
    }
}
