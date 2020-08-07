package me.marlon.x1.listeners;

import me.marlon.x1.Main;
import me.marlon.x1.managers.FileManager;
import me.marlon.x1.model.Desafio;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreprocess implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Main plugin = Main.getInstance();
        Player player = event.getPlayer();
        Desafio desafio = plugin.getDesafioManager().getDesafio();
        Player mDesafiante = plugin.getDesafioManager().getDesafio().getDesafiante();
        Player mDesafiado = plugin.getDesafioManager().getDesafio().getDesafiado();

        if (desafio.isBoolDesafio()) {
            String command = event.getMessage().split("\\s+")[0].substring(1);
            if (mDesafiante.getName().equals(player.getName()) || mDesafiado.getName().equalsIgnoreCase(player.getName())) {
                if(plugin.getConfigurationManager().isComandosX1()) {
                    if(command.equalsIgnoreCase("camarote"))
                        event.setCancelled(true);
                    if (!plugin.getConfigurationManager().getComandosPermX1().contains(command) && !player.hasPermission("x1.admin")) {
                        event.setCancelled(true);
                        player.sendMessage(FileManager.getMessage("comando_bloqueado"));
                    }
                } else {
                    player.sendMessage(FileManager.getMessage("comando_bloqueado"));
                    event.setCancelled(true);
                }
            } else if(desafio.getPlayersCamarote().contains(player)) {
                if (plugin.getConfigurationManager().isComandosCamarote()) {
                    if(command.equalsIgnoreCase("aceitar"))
                        event.setCancelled(true);
                    if (!plugin.getConfigurationManager().getComandosPermCamarote().contains(command) && !player.hasPermission("x1.admin")) {
                        player.sendMessage(FileManager.getMessage("comando_bloqueado_camarote"));
                        event.setCancelled(true);
                    }
                } else {
                    player.sendMessage(FileManager.getMessage("comando_bloqueado_camarote"));
                    event.setCancelled(true);
                }
            }
        }
    }
}
