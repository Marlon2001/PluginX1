package me.marlon.x1.commands;

import me.marlon.x1.Main;
import me.marlon.x1.managers.FileManager;
import me.marlon.x1.model.Command;
import org.bukkit.entity.Player;

public class CancelarCommand implements Command {

    private final Main plugin = Main.getInstance();

    @Override
    public String getPermission() {
        return "x1.admin";
    }

    @Override
    public void onCommand(Player player, org.bukkit.command.Command command, String label, String... arguments) {
        plugin.getDesafioManager().cancelarDesafio(player);
    }

    @Override
    public String getDescription() {
        return FileManager.getMessage("cmd_cancelar");
    }
}


