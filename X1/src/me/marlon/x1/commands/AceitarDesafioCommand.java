package me.marlon.x1.commands;

import me.marlon.x1.Main;
import me.marlon.x1.managers.FileManager;
import me.marlon.x1.model.Command;
import me.marlon.x1.model.Desafio;
import org.bukkit.entity.Player;

public class AceitarDesafioCommand implements Command {

    private final Main plugin = Main.getInstance();

    @Override
    public String getPermission() {
        return "x1.player";
    }

    @Override
    public void onCommand(Player mDesafiado, org.bukkit.command.Command command, String label, String... arguments) {
        if (arguments.length <= 1) {
            mDesafiado.sendMessage(FileManager.getMessage("cmd_aceitar"));
        } else {
            Player mDesafiante = plugin.getDesafioManager().getDesafio().getDesafiante();
            Desafio desafio = plugin.getDesafioManager().getDesafio();

            if (desafio.isBoolPedido() && desafio.getDesafiado().getName().equals(mDesafiado.getName())) {
                plugin.getDesafioManager().aceitarDesafio(mDesafiante, mDesafiado);
            } else {
                mDesafiado.sendMessage(FileManager.getMessage("nenhum_pedido"));
            }
        }
    }

    @Override
    public String getDescription() {
        return FileManager.getMessage("cmd_aceitar");
    }
}
