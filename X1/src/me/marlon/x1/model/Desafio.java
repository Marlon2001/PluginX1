package me.marlon.x1.model;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Desafio {

    private final List<Player> playersCamarote = new ArrayList<>();
    private Player desafiante;
    private Player desafiado;
    private boolean boolDesafio;
    private boolean boolPedido;

    public Player getDesafiante() {
        return desafiante;
    }

    public void setDesafiante(Player desafiante) {
        this.desafiante = desafiante;
    }

    public Player getDesafiado() {
        return desafiado;
    }

    public void setDesafiado(Player desafiado) {
        this.desafiado = desafiado;
    }

    public boolean isBoolDesafio() {
        return boolDesafio;
    }

    public void setBoolDesafio(boolean boolDesafio) {
        this.boolDesafio = boolDesafio;
    }

    public boolean isBoolPedido() {
        return boolPedido;
    }

    public void setBoolPedido(boolean boolPedido) {
        this.boolPedido = boolPedido;
    }

    public List<Player> getPlayersCamarote() {
        return playersCamarote;
    }

    public void addPlayerCamarote(Player player) {
        this.playersCamarote.add(player);
    }

    public void removePlayerCamarote(Player player) {
        this.playersCamarote.remove(player);
    }

    @Override
    public String toString() {
        return "Desafio{" +
                "playersCamarote=" + playersCamarote +
                ", desafiante=" + desafiante +
                ", desafiado=" + desafiado +
                ", boolDesafio=" + boolDesafio +
                ", boolPedido=" + boolPedido +
                '}';
    }
}
