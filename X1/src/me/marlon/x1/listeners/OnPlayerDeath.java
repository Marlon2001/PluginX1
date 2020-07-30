package me.marlon.x1.listeners;

import me.marlon.x1.Main;
import me.marlon.x1.model.Desafio;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Main plugin = Main.getInstance();

        Desafio desafio = plugin.getDesafioManager().getDesafio();
        Player desafiado = desafio.getDesafiado();
        Player desafiante = desafio.getDesafiante();
        Player playerDeath = event.getEntity();

        if (desafio.isBoolDesafio()) {
            if (playerDeath.getName().equals(desafiante.getName())) {
                plugin.getDesafioManager().vencedorDesafio(desafiado, desafiante);
            } else if (playerDeath.getName().equals(desafiado.getName())) {
                plugin.getDesafioManager().vencedorDesafio(desafiante, desafiado);
            }
        }
    }
}
