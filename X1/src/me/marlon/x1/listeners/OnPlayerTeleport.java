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
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class OnPlayerTeleport implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Main plugin = Main.getInstance();
        Player player = event.getPlayer();
        Desafio desafio = plugin.getDesafioManager().getDesafio();
        Location teleportTo = event.getTo();
        Location pos1Arena = StringUtils.stringToLocation(plugin.getConfigurationManager().getPos1Arena());
        Location pos2Arena = StringUtils.stringToLocation(plugin.getConfigurationManager().getPos1Arena());
        Location posCamarote = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosCamarote());
        List<Player> playersCamarote = plugin.getDesafioManager().getDesafio().getPlayersCamarote();

        if (desafio.isBoolDesafio()) {
            if (desafio.getDesafiante().getName().equals(player.getName()) || desafio.getDesafiado().getName().equals(player.getName())) {
                assert pos1Arena != null;
                if (!pos1Arena.equals(teleportTo)) {
                    assert pos2Arena != null;
                    if (!pos2Arena.equals(teleportTo)) {
                        if (!plugin.getConfigurationManager().isTeleporteArena()) {
                            player.sendMessage(FileManager.getMessage("teleporte_bloqueado"));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

        if(playersCamarote.contains(player)) {
            assert posCamarote != null;
            if (!posCamarote.equals(teleportTo)) {
                plugin.getDesafioManager().getDesafio().removePlayerCamarote(player);
            }
        }
    }
}
