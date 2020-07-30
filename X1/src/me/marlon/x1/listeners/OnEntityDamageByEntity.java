package me.marlon.x1.listeners;

import me.marlon.x1.Main;
import me.marlon.x1.model.Desafio;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnEntityDamageByEntity implements Listener {

    private final Main plugin = Main.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void nnEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Player player;
        Player p;
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        Desafio desafio = plugin.getDesafioManager().getDesafio();

        if (entity instanceof Player) {
            player = (Player) entity;

            if (desafio.isBoolDesafio()) {
                if (desafio.getDesafiado().getName().equals(player.getName()) || desafio.getDesafiante().getName().equals(player.getName())) {
                    if (damager instanceof Player) {
                        p = (Player) damager;
                        if (!(desafio.getDesafiado().getName().equals(p.getName()) || desafio.getDesafiante().getName().equals(p.getName()))) {
                            event.setCancelled(true);
                        }
                    }
                } else {
                    if (damager instanceof Player) {
                        p = (Player) damager;
                        if (desafio.getDesafiado().getName().equals(p.getName()) || desafio.getDesafiante().getName().equals(p.getName())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
