package me.marlon.x1.managers;

import me.marlon.x1.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final Map<String, me.marlon.x1.model.Command> commands = new LinkedHashMap<>();

    private final Main plugin = Main.getInstance();

    public Map<String, me.marlon.x1.model.Command> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length <= 0) {
                this.commands.get("default").onCommand(player, command, s, strings);
            } else {
                String commandName = strings[0];
                if (this.commands.containsKey(commandName)) {
                    me.marlon.x1.model.Command commandX1 = this.commands.get(commandName);
                    if (commandX1.getPermission() == null || player.hasPermission(commandX1.getPermission())) {
                        commandX1.onCommand(player, command, s, strings);
                    } else {
                        player.sendMessage(FileManager.getMessage("sem_permissao"));
                    }
                } else {
                    player.sendMessage(FileManager.getMessage("comando_desconhecido"));
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> stringList = new ArrayList<>();

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (strings.length == 1) {
                for (String string : this.commands.keySet()) {
                    if(string.equals("default"))
                        continue;
                    me.marlon.x1.model.Command commandX1 = this.commands.get(string);
                    if (commandX1.getPermission() == null || player.hasPermission(commandX1.getPermission())) {
                        if (string.startsWith(strings[0])) {
                            stringList.add(string);
                        }
                    }
                }
                return stringList;
            }

            if (strings.length == 2) {
                switch (strings[0].toLowerCase()) {
                    case "desafiar":
                        List<Player> players = (List<Player>) plugin.getServer().getOnlinePlayers();

                        for (Player p : players) {
                            if(player.getName().equals(p.getName()))
                                continue;
                            stringList.add(p.getName());
                        }
                        return stringList;
                    case "aceitar":
                    case "rejeitar":
                        if (plugin.getDesafioManager().getDesafio().isBoolPedido() && player.getName().equals(plugin.getDesafioManager().getDesafio().getDesafiado().getName()))
                            stringList.add(plugin.getDesafioManager().getDesafio().getDesafiante().getName());
                        return stringList;
                    default:
                        return null;
                }
            }
        }
        return null;
    }

    public void registerCommand(String name, me.marlon.x1.model.Command command) {
        this.commands.put(name, command);
    }

    public void unregisterCommand(String name) {
        this.commands.remove(name);
    }
}
