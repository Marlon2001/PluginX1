package me.marlon.x1.commands;

import me.marlon.x1.Main;
import me.marlon.x1.managers.FileManager;
import me.marlon.x1.model.Command;
import org.bukkit.entity.Player;

public class DesafiarCommand implements Command {

    private final Main plugin = Main.getInstance();

    @Override
    public String getPermission() {
        return "x1.player";
    }

    @Override
    public void onCommand(Player mDesafiante, org.bukkit.command.Command command, String label, String... arguments) {
        if (arguments.length <= 1) {
            mDesafiante.sendMessage(FileManager.getMessage("cmd_desafiar"));
        } else {
            final Player mDesafiado = plugin.getServer().getPlayer(arguments[1]);

            if (mDesafiado != null) {
                plugin.getDesafioManager().iniciarDesafio(mDesafiante, mDesafiado);
            } else {
                mDesafiante.sendMessage(FileManager.getMessage("jogador_inexistente").replace("{player}", arguments[1]));
            }
        }
    }

    @Override
    public String getDescription() {
        return FileManager.getMessage("cmd_desafiar");
    }
}
