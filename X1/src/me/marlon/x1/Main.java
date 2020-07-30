package me.marlon.x1;

import me.marlon.x1.commands.*;
import me.marlon.x1.listeners.*;
import me.marlon.x1.managers.CommandManager;
import me.marlon.x1.managers.ConfigurationManager;
import me.marlon.x1.managers.DesafioManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {

    private static Main instance;
    private ConfigurationManager configurationManager;
    private CommandManager commandManager;
    private DesafioManager desafioManager;
    private BukkitScheduler bukkitScheduler;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.configurationManager = new ConfigurationManager();
        this.commandManager = new CommandManager();
        this.desafioManager = new DesafioManager();
        this.bukkitScheduler = Bukkit.getScheduler();
        final PluginManager pluginManager = getServer().getPluginManager();

        this.commandManager.registerCommand("default", new DefaultCommand());
        this.commandManager.registerCommand("desafiar", new DesafiarCommand());
        this.commandManager.registerCommand("aceitar", new AceitarDesafioCommand());
        this.commandManager.registerCommand("rejeitar", new RejeitarDesafioCommand());
        this.commandManager.registerCommand("camarote", new CamaroteCommand());

        this.commandManager.registerCommand("setpos1", new SetPos1Command());
        this.commandManager.registerCommand("setpos2", new SetPos2Command());
        this.commandManager.registerCommand("setcamarote", new SetCamaroteCommand());
        this.commandManager.registerCommand("setsaida", new SetSaidaCommand());
        this.commandManager.registerCommand("cancelar", new CancelarCommand());
        this.commandManager.registerCommand("reload", new ReloadCommand());

        this.getCommand("x1").setExecutor(this.commandManager);
        this.getCommand("x1").setTabCompleter(this.commandManager);

        pluginManager.registerEvents(new OnPlayerCommandPreprocess(), instance);
        pluginManager.registerEvents(new OnPlayerDeath(), instance);
        pluginManager.registerEvents(new OnEntityDamageByEntity(), instance);
        pluginManager.registerEvents(new OnPlayerQuit(), instance);
        pluginManager.registerEvents(new OnPlayerTeleport(), instance);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public DesafioManager getDesafioManager() {
        return desafioManager;
    }

    public BukkitScheduler getBukkitScheduler() {
        return bukkitScheduler;
    }
}
