package me.marlon.x1.listeners;

import me.marlon.x1.Main;
import me.marlon.x1.managers.FileManager;
import me.marlon.x1.model.Desafio;
import me.marlon.x1.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Main plugin = Main.getInstance();

        Desafio desafio = plugin.getDesafioManager().getDesafio();
        Player desafiado = desafio.getDesafiado();
        Player desafiante = desafio.getDesafiante();
        Player playerQuit = event.getPlayer();
        Location arenaPosSaida = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosSaida());

        if (desafio.isBoolDesafio()) {
            if (playerQuit.getName().equalsIgnoreCase(desafiante.getName())) {
                plugin.getDesafioManager().vencedorDesafio(desafiado, desafiante);
            } else if (playerQuit.getName().equalsIgnoreCase(desafiado.getName())) {
                plugin.getDesafioManager().vencedorDesafio(desafiante, desafiado);
            } else if (desafio.getPlayersCamarote().contains(playerQuit)) {
                playerQuit.teleport(arenaPosSaida);
            }
        }

        if (desafio.isBoolPedido()) {
            if (playerQuit.getName().equalsIgnoreCase(desafiante.getName())) {
                plugin.getServer().broadcastMessage(FileManager.getMessage("deslogou_desafio").replace("{player1}", desafiante.getName()).replace("{player2}", desafiado.getName()));
                plugin.getDesafioManager().cancelarDesafio(null);
            } else if (playerQuit.getName().equalsIgnoreCase(desafiado.getName())) {
                plugin.getServer().broadcastMessage(FileManager.getMessage("deslogou_desafio").replace("{player1}", desafiado.getName()).replace("{player2}", desafiante.getName()));
                plugin.getDesafioManager().cancelarDesafio(null);
            }
        }
    }
}
