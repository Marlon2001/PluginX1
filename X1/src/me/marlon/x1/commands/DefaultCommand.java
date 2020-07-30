package me.marlon.x1.commands;

import me.marlon.x1.Main;
import me.marlon.x1.model.Command;
import org.bukkit.entity.Player;

public class DefaultCommand implements Command {

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public void onCommand(Player player, org.bukkit.command.Command command, String label, String... arguments) {
        for (Command comando : Main.getInstance().getCommandManager().getCommands().values()) {
            if (comando.getPermission() == null || player.hasPermission(comando.getPermission())) {
                player.sendMessage(comando.getDescription());
            }
        }
    }

    @Override
    public String getDescription() {
        return null;
    }
}
