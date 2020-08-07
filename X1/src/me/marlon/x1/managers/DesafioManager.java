package me.marlon.x1.managers;

import com.jackproehl.plugins.CombatLog;
import me.marlon.x1.Main;
import me.marlon.x1.model.Desafio;
import me.marlon.x1.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DesafioManager {

    private final Main plugin = Main.getInstance();
    private final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
    private Desafio desafio = new Desafio();
    private int taskDesafio;
    private int taskPedido;
    private int taskFinalizar;

    public void iniciarDesafio(Player mDesafiante, Player mDesafiado) {
        if (!mDesafiante.equals(mDesafiado)) {
            if (plugin.getConfigurationManager().getPos1Arena() == null || plugin.getConfigurationManager().getPos2Arena() == null) {
                mDesafiante.sendMessage(FileManager.getMessage("sem_entradas"));
                return;
            }
            if (plugin.getConfigurationManager().getPosSaida() == null) {
                mDesafiante.sendMessage(FileManager.getMessage("sem_saida"));
                return;
            }

            if (!(this.desafio.isBoolDesafio() || this.desafio.isBoolPedido())) {
                EconomyManager eco = new EconomyManager(plugin);
                double valorDesafiar = plugin.getConfigurationManager().getValorDesafiar();
                int tempoAceitarX1 = plugin.getConfigurationManager().getTempoAceitarx1();

                if (!eco.hasMoney(mDesafiante, valorDesafiar)) {
                    mDesafiante.sendMessage(FileManager.getMessage("money_desafiar1").replace("{price}", valorDesafiar + ""));
                    return;
                }

                if (!eco.hasMoney(mDesafiado, valorDesafiar)) {
                    mDesafiante.sendMessage(FileManager.getMessage("money_desafiar2").replace("{player}", mDesafiado.getName()).replace("{price}", valorDesafiar + ""));
                    return;
                }

                this.desafio.setDesafiante(mDesafiante);
                this.desafio.setDesafiado(mDesafiado);
                this.desafio.setBoolPedido(true);

                final int[] cont = {0};
                taskPedido = bukkitScheduler.scheduleSyncRepeatingTask(plugin, () -> {
                    if (cont[0] == 0) {
                        plugin.getServer().broadcastMessage(FileManager.getMessage("desafio").replace("{player1}", mDesafiante.getName()).replace("{player2}", mDesafiado.getName()));
                        plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_aceitar").replace("{player1}", mDesafiante.getName()).replace("{player2}", mDesafiante.getName()));
                        cont[0]++;
                    } else if (cont[0] == 1) {
                        this.desafio = new Desafio();
                        plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_arregou").replace("{player1}", mDesafiado.getName()).replace("{player2}", mDesafiante.getName()));
                        bukkitScheduler.cancelTask(taskPedido);
                    }
                }, 5L, tempoAceitarX1 * 20);
            } else {
                mDesafiante.sendMessage(FileManager.getMessage("desafio_aguardar"));
            }
        } else {
            mDesafiante.sendMessage(FileManager.getMessage("autodesafiar"));
        }
    }

    public void aceitarDesafio(Player mDesafiante, Player mDesafiado) {
        EconomyManager eco = new EconomyManager(plugin);
        Location pos1Arena = StringUtils.stringToLocation(plugin.getConfigurationManager().getPos1Arena());
        Location pos2Arena = StringUtils.stringToLocation(plugin.getConfigurationManager().getPos2Arena());
        int valorDesafiar = plugin.getConfigurationManager().getValorDesafiar();
        int tempoX1 = plugin.getConfigurationManager().getTempox1();

        if (eco.removeMoney(mDesafiado, valorDesafiar) && eco.removeMoney(mDesafiante, valorDesafiar)) {
            bukkitScheduler.cancelTask(taskPedido);

            if (plugin.getConfigurationManager().isUseSimpleClan()) {
                setFriendlyFire(mDesafiado, true);
                setFriendlyFire(mDesafiante, true);
            }

            forceTeleportPlayer(mDesafiante, pos1Arena);
            forceTeleportPlayer(mDesafiado, pos2Arena);

            this.desafio.setBoolPedido(false);
            this.desafio.setBoolDesafio(true);

            plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_aceito1").replace("{player1}", this.desafio.getDesafiado().getName()).replace("{player2}", this.desafio.getDesafiante().getName()));
            plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_aceito2"));

            final int[] seconds = {tempoX1};
            taskDesafio = bukkitScheduler.scheduleSyncRepeatingTask(plugin, () -> {
                if (seconds[0] != 0) {
                    long minutos = TimeUnit.SECONDS.toMinutes(seconds[0]);
                    long segundos = seconds[0] % 60;
                    String formato = addZero(minutos) + ":" + addZero(segundos);
                    String mensagem = ChatColor.GREEN + "§aTempo: " + formato;

                    for (Player player : desafio.getPlayersCamarote())
                        sendActionBarMessage(player, mensagem);

                    sendActionBarMessage(mDesafiante, mensagem);
                    sendActionBarMessage(mDesafiado, mensagem);

                    seconds[0]--;
                } else {
                    if (this.empatarDesafio(mDesafiante, mDesafiado)) {
                        plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_empate").replace("{player1}", mDesafiante.getName()).replace("{player2}", mDesafiado.getName()));
                        bukkitScheduler.cancelTask(taskDesafio);
                    }
                }
            }, 0, 20L);
        }
    }

    public void rejeitarDesafio(Player mDesafiante, Player mDesafiado) {
        if (this.desafio.isBoolPedido() && this.desafio.getDesafiado().getName().equals(mDesafiado.getName())) {
            this.desafio = new Desafio();
            plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_arregou").replace("{player1}", mDesafiado.getName()).replace("{player2}", mDesafiante.getName()));
            bukkitScheduler.cancelTask(taskPedido);
        }
    }

    public void vencedorDesafio(Player vencedor, Player perdedor) {
        EconomyManager eco = new EconomyManager(plugin);
        Location arenaPosSaida = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosSaida());
        List<ItemStack> items = new ArrayList<>();
        float premio = plugin.getConfigurationManager().getValorDesafiar() * 2;
        long tempoFinalizar = plugin.getConfigurationManager().getTempoFinalizar();
        desafio.setBoolDesafio(false);

        if (plugin.getConfigurationManager().isUseSimpleClan()) {
            setFriendlyFire(vencedor, false);
            setFriendlyFire(perdedor, false);
        }

        for (int i = 0; i < perdedor.getInventory().getSize(); i++) {
            items.add(perdedor.getInventory().getItem(i));
        }

        for (ItemStack item : items) {
            if (item == null)
                continue;
            perdedor.getWorld().dropItem(perdedor.getLocation(), item).setPickupDelay(20);
        }

        perdedor.getInventory().clear();
        items.clear();
        forceTeleportPlayer(perdedor, arenaPosSaida);

        final int[] cont = {0};
        bukkitScheduler.cancelTask(taskDesafio);
        taskFinalizar = bukkitScheduler.scheduleSyncRepeatingTask(plugin, () -> {
            if (cont[0] == 0) {
                plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_vencedor").replace("{player1}", vencedor.getName()).replace("{player2}", perdedor.getName()).replace("{price}", String.valueOf(premio)));
            } else if (cont[0] == 1) {
                if (eco.depositMoney(vencedor, premio)) {
                    forceTeleportPlayer(vencedor, arenaPosSaida);

                    for (Player p : this.desafio.getPlayersCamarote()) {
                        forceTeleportPlayer(p, arenaPosSaida);
                        this.desafio.removePlayerCamarote(p);
                    }
                    this.desafio = new Desafio();
                    bukkitScheduler.cancelTask(taskFinalizar);
                }
            }
            cont[0]++;
        }, 5L, tempoFinalizar * 20);
        cont[0] = 0;
    }

    public void cancelarDesafio(Player player) {
        if (this.desafio.isBoolPedido()) {
            this.desafio = new Desafio();
            bukkitScheduler.cancelTask(taskPedido);
            plugin.getServer().broadcastMessage(FileManager.getMessage("pedido_cancelado"));
        } else if (this.desafio.isBoolDesafio()) {
            Location arenaPosSaida = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosSaida());
            Player mDesafiante = this.desafio.getDesafiante();
            Player mDesafiado = this.desafio.getDesafiado();
            desafio.setBoolDesafio(false);

            if (plugin.getConfigurationManager().isUseSimpleClan()) {
                setFriendlyFire(mDesafiado, false);
                setFriendlyFire(mDesafiante, false);
            }

            forceTeleportPlayer(mDesafiante, arenaPosSaida);
            forceTeleportPlayer(mDesafiado, arenaPosSaida);

            for (Player p : this.desafio.getPlayersCamarote()) {
                this.desafio.removePlayerCamarote(p);
                forceTeleportPlayer(p, arenaPosSaida);
            }

            this.desafio = new Desafio();
            plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_cancelado"));
            bukkitScheduler.cancelTask(taskDesafio);
        } else {
            player.sendMessage(FileManager.getMessage("nenhum_desafio"));
        }
    }

    private boolean empatarDesafio(Player player1, Player player2) {
        try {
            Location arenaPosSaida = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosSaida());
            desafio.setBoolDesafio(false);

            if (plugin.getConfigurationManager().isUseSimpleClan()) {
                setFriendlyFire(player1, false);
                setFriendlyFire(player2, false);
            }

            forceTeleportPlayer(player1, arenaPosSaida);
            forceTeleportPlayer(player2, arenaPosSaida);

            for (Player p : this.desafio.getPlayersCamarote()) {
                forceTeleportPlayer(p, arenaPosSaida);
            }
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private void sendActionBarMessage(Player player, String text) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    private void setFriendlyFire(Player player, boolean status) {
        if (SimpleClans.getInstance().getClanManager().getClanPlayer(player) != null)
            SimpleClans.getInstance().getClanManager().getClanPlayer(player).setFriendlyFire(status);
    }

    private void forceTeleportPlayer(Player player, Location location) {
        boolean b = false;
        if ((plugin.getServer().getPluginManager().getPlugin("CombatLog") != null)) {
            b = CombatLog.getPlugin(CombatLog.class).blockTeleportationEnabled;
            CombatLog.getPlugin(CombatLog.class).blockTeleportationEnabled = false;
        }

        player.teleport(location);

        if ((plugin.getServer().getPluginManager().getPlugin("CombatLog") != null)) {
            CombatLog.getPlugin(CombatLog.class).blockTeleportationEnabled = b;
        }
    }

    private String addZero(long num) {
        if (num < 10)
            return "0" + num;
        else
            return String.valueOf(num);
    }

    public Desafio getDesafio() {
        return desafio;
    }
}
