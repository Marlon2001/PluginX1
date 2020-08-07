package me.marlon.x1.managers;

import me.marlon.x1.Main;
import me.marlon.x1.utils.StringUtils;
import org.bukkit.Location;

import java.util.List;

public class ConfigurationManager {

    private final Main plugin = Main.getInstance();
    private final boolean useSimpleClan;
    private final boolean teleporteArena;
    private final boolean isPrefixMessage;
    private final int valorDesafiar;
    private final int tempoFinalizar;
    private final int tempoAceitarx1;
    private final int tempox1;
    private final boolean comandosX1;
    private final List<String> comandosPermX1;
    private final boolean comandosCamarote;
    private final List<String> comandosPermCamarote;

    public ConfigurationManager() {
        this.useSimpleClan = plugin.getServer().getPluginManager().getPlugin("SimpleClans") != null;
        this.teleporteArena = plugin.getConfig().getBoolean("TeleporteArena");
        this.isPrefixMessage = plugin.getConfig().getBoolean("PrefixMessage");
        this.tempoFinalizar = plugin.getConfig().getInt("TempoFinalizar");
        this.valorDesafiar = plugin.getConfig().getInt("ValorDesafiar");
        this.tempoAceitarx1 = plugin.getConfig().getInt("TempoAceitarX1");
        this.tempox1 = plugin.getConfig().getInt("TempoX1");
        this.comandosX1 = plugin.getConfig().getBoolean("ComandosX1");
        this.comandosPermX1 = plugin.getConfig().getStringList("ComandosPermX1");
        this.comandosCamarote = plugin.getConfig().getBoolean("ComandosCamarote");
        this.comandosPermCamarote = plugin.getConfig().getStringList("ComandosPermCamarote");
    }

    public boolean setPos1(Location location) {
        FileManager.savePositions("ArenaPos1", StringUtils.locationToString(location));
        return true;
    }

    public boolean setPos2(Location location) {
        FileManager.savePositions("ArenaPos2", StringUtils.locationToString(location));
        return true;
    }

    public boolean setArenaSaida(Location location) {
        FileManager.savePositions("ArenaSaida", StringUtils.locationToString(location));
        return true;
    }

    public boolean setArenaCamarote(Location location) {
        FileManager.savePositions("ArenaCamarote", StringUtils.locationToString(location));
        return true;
    }

    public boolean isUseSimpleClan() {
        return useSimpleClan;
    }

    public boolean isTeleporteArena() {
        return teleporteArena;
    }

    public boolean isPrefixMessage() {
        return isPrefixMessage;
    }

    public int getTempoFinalizar() {
        return tempoFinalizar;
    }

    public int getValorDesafiar() {
        return valorDesafiar;
    }

    public int getTempoAceitarx1() {
        return tempoAceitarx1;
    }

    public int getTempox1() {
        return tempox1;
    }

    public String getPos1Arena() {
        return plugin.getConfig().getString("ArenaPos1");
    }

    public String getPos2Arena() {
        return plugin.getConfig().getString("ArenaPos2");
    }

    public String getPosCamarote() {
        return plugin.getConfig().getString("ArenaCamarote");
    }

    public String getPosSaida() {
        return plugin.getConfig().getString("ArenaSaida");
    }

    public boolean isComandosX1() {
        return comandosX1;
    }

    public List<String> getComandosPermX1() {
        return comandosPermX1;
    }

    public boolean isComandosCamarote() {
        return comandosCamarote;
    }

    public List<String> getComandosPermCamarote() {
        return comandosPermCamarote;
    }
}
