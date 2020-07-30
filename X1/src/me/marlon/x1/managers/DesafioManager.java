package me.marlon.x1.managers;

import me.iiSnipez.CombatLog.CombatLog;
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
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.TimeUnit;

public class DesafioManager {

    private final Main plugin = Main.getInstance();
    private Desafio desafio = new Desafio();
    private final BukkitScheduler bukkitScheduler = plugin.getServer().getScheduler();

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

                if (eco.hasMoney(mDesafiante, valorDesafiar)) {
                    mDesafiante.sendMessage(FileManager.getMessage("money_desafiar1").replace("{price}", valorDesafiar + ""));
                    return;
                }

                if (eco.hasMoney(mDesafiado, valorDesafiar)) {
                    mDesafiante.sendMessage(FileManager.getMessage("money_desafiar2").replace("{player}", mDesafiado.getName()).replace("{price}", valorDesafiar + ""));
                    return;
                }

                this.desafio.setDesafiante(mDesafiante);
                this.desafio.setDesafiado(mDesafiado);
                this.desafio.setBoolPedido(true);
                this.desafio.setBoolDesafio(false);

                final int[] cont = {0};
                bukkitScheduler.scheduleSyncRepeatingTask(plugin, () -> {
                    if (cont[0] == 0) {
                        plugin.getServer().broadcastMessage(FileManager.getMessage("desafio").replace("{player1}", mDesafiante.getName()).replace("{player2}", mDesafiado.getName()));
                        plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_aceitar").replace("{player}", mDesafiante.getName()));
                        plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_rejeitar").replace("{player}", mDesafiante.getName()));
                        cont[0]++;
                    } else if (cont[0] == 1) {
                        this.desafio = new Desafio();
                        plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_arregou").replace("{player1}", mDesafiado.getName()).replace("{player2}", mDesafiante.getName()));
                        bukkitScheduler.cancelAllTasks();
                    }
                }, 5L, tempoAceitarX1 * 20);
                cont[0] = 0;

            } else {
                mDesafiante.sendMessage(FileManager.getMessage("desafio_aguardar"));
            }
        } else {
            mDesafiante.sendMessage(FileManager.getMessage("desafiar_a_si_mesmo"));
        }
    }

    public void aceitarDesafio(Player mDesafiante, Player mDesafiado) {
        EconomyManager eco = new EconomyManager(plugin);
        Location pos1Arena = StringUtils.stringToLocation(plugin.getConfigurationManager().getPos1Arena());
        Location pos2Arena = StringUtils.stringToLocation(plugin.getConfigurationManager().getPos2Arena());
        int valorDesafiar = plugin.getConfigurationManager().getValorDesafiar();
        int tempoX1 = plugin.getConfigurationManager().getTempox1();

        if (eco.removeMoney(mDesafiado, valorDesafiar) && eco.removeMoney(mDesafiante, valorDesafiar)) {
            if (plugin.getConfigurationManager().isUseSimpleClan()) {
                setFriendlyFire(mDesafiado, true);
                setFriendlyFire(mDesafiante, true);
            }

            forceTeleportPlayer(mDesafiante, pos1Arena);
            forceTeleportPlayer(mDesafiado, pos2Arena);

            this.desafio.setBoolPedido(false);
            this.desafio.setBoolDesafio(true);

            bukkitScheduler.cancelAllTasks();

            plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_aceito1").replace("{player1}", this.desafio.getDesafiado().getName()).replace("{player2}", this.desafio.getDesafiante().getName()));
            plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_aceito2"));

            final int[] seconds = {tempoX1};
            bukkitScheduler.scheduleSyncRepeatingTask(plugin, () -> {
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
                        bukkitScheduler.cancelAllTasks();
                    }
                }
            }, 0, 20L);
        }
    }

    public void rejeitarDesafio(Player mDesafiante, Player mDesafiado) {
        if (this.desafio.isBoolPedido() && this.desafio.getDesafiado().getName().equals(mDesafiado.getName())) {
            this.desafio = new Desafio();
            plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_arregou").replace("{player1}", mDesafiado.getName()).replace("{player2}", mDesafiante.getName()));
            bukkitScheduler.cancelAllTasks();
        }
    }

    public void vencedorDesafio(Player vencedor, Player perdedor) {
        EconomyManager eco = new EconomyManager(plugin);
        Location arenaPosSaida = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosSaida());
        float premio = plugin.getConfigurationManager().getValorDesafiar() * 2;
        long tempoFinalizar = plugin.getConfigurationManager().getTempoFinalizar();

        boolean combat = false;

        if ((plugin.getServer().getPluginManager().getPlugin("CombatLog") != null))
            combat = CombatLog.getPlugin(CombatLog.class).taggedPlayers.containsKey(perdedor.getName());

        if (!combat) {
            forceTeleportPlayer(perdedor, arenaPosSaida);
        }

        if (plugin.getConfigurationManager().isUseSimpleClan()) {
            setFriendlyFire(vencedor, false);
            setFriendlyFire(perdedor, false);
        }

        bukkitScheduler.cancelAllTasks();
        final int[] cont = {0};

        bukkitScheduler.scheduleSyncRepeatingTask(plugin, () -> {
            if (cont[0] == 0) {
                this.desafio = new Desafio();
                plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_vencedor").replace("{player1}", vencedor.getName()).replace("{player2}", perdedor.getName()).replace("{price}", String.valueOf(premio)));
            } else if (cont[0] == 1) {
                if (eco.depositMoney(vencedor, premio)) {
                    forceTeleportPlayer(vencedor, arenaPosSaida);
                    bukkitScheduler.cancelAllTasks();
                }
            }
            cont[0]++;
        }, 5L, tempoFinalizar * 20);
        cont[0] = 0;
    }

    public boolean cancelarDesafio(Player player) {
        if (this.desafio.isBoolPedido()) {
            this.desafio = new Desafio();
            bukkitScheduler.cancelAllTasks();
            return true;
        } else if (this.desafio.isBoolDesafio()) {
            Location arenaPosSaida = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosSaida());
            Player mDesafiante = this.desafio.getDesafiante();
            Player mDesafiado = this.desafio.getDesafiado();

            if (plugin.getConfigurationManager().isUseSimpleClan()) {
                setFriendlyFire(mDesafiado, false);
                setFriendlyFire(mDesafiante, false);
            }

            this.desafio = new Desafio();
            forceTeleportPlayer(mDesafiante, arenaPosSaida);
            forceTeleportPlayer(mDesafiado, arenaPosSaida);

            plugin.getServer().broadcastMessage(FileManager.getMessage("desafio_cancelado"));
            bukkitScheduler.cancelAllTasks();

            return true;
        } else {
            player.sendMessage(FileManager.getMessage("nenhum_desafio_ocorrendo"));
            return false;
        }
    }

    private boolean empatarDesafio(Player player1, Player player2) {
        try {
            Location arenaPosSaida = StringUtils.stringToLocation(plugin.getConfigurationManager().getPosSaida());

            if (plugin.getConfigurationManager().isUseSimpleClan()) {
                setFriendlyFire(player1, false);
                setFriendlyFire(player2, false);
            }

            forceTeleportPlayer(player1, arenaPosSaida);
            forceTeleportPlayer(player2, arenaPosSaida);

            this.desafio = new Desafio();
            bukkitScheduler.cancelAllTasks();

            return true;
        } catch (Exception e) {
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

    private void cancelTask(int id) {
        Bukkit.getScheduler().cancelTask(id);
    }

    public Desafio getDesafio() {
        return desafio;
    }
}
