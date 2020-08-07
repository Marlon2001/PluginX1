package me.marlon.x1.commands;

import me.marlon.x1.Main;
import me.marlon.x1.managers.FileManager;
import me.marlon.x1.model.Command;
import me.marlon.x1.model.Desafio;
import me.marlon.x1.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SairCamaroteCommand implements Command {

    private final Main plugin = Main.getInstance();
    @Override
    public String getPermission() {
        return "x1.player";
    }

    @Override
    public void onCommand(Player player, org.bukkit.command.Command command, String label, String... arguments) {
        Desafio desafio = plugin.getDesafioManager().getDesafio();
        Location posSaida = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosSaida());

        if(desafio.getPlayersCamarote().contains(player)) {
            desafio.removePlayerCamarote(player);
            player.teleport(posSaida);
            player.sendMessage(FileManager.getMessage("camarote_saiu"));
        } else {
            player.sendMessage(FileManager.getMessage("camarote_naoesta"));
        }
    }

    @Override
    public String getDescription() {
        return FileManager.getMessage("cmd_saircamarote");
    }
}
