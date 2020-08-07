package me.marlon.x1.managers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    private Economy econ;
    private final Plugin plugin;

    public EconomyManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasMoney(Player player, double ammountBalance) {
        if (setupEconomy()) {
            player.sendMessage(FileManager.getMessage("Plugin de Vault não carregado."));
            return true;
        } else {
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(player.getUniqueId());
             return econ.has(offlinePlayer, ammountBalance);
        }
    }

    public boolean removeMoney(Player player, double ammountBalance) {
        if (setupEconomy()) {
            player.sendMessage(FileManager.getMessage("Plugin de Vault não carregado."));
            return false;
        } else {
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(player.getUniqueId());
            EconomyResponse e = econ.withdrawPlayer(offlinePlayer, ammountBalance);

            return e.transactionSuccess();
        }
    }

    public boolean depositMoney(Player player, double ammount) {
        if (setupEconomy()) {
            player.sendMessage(FileManager.getMessage("Plugin de Vault não carregado."));
            return false;
        } else {
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(player.getUniqueId());
            EconomyResponse e = econ.depositPlayer(offlinePlayer, ammount);

            return e.transactionSuccess();
        }
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return true;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return true;
        }
        econ = rsp.getProvider();
        return econ == null;
    }

}
