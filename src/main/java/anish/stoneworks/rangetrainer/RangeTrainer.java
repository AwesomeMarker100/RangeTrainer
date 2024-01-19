package anish.stoneworks.rangetrainer;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static anish.stoneworks.rangetrainer.CommandRangeQueue.maxRangeRadius;
import static org.bukkit.Bukkit.getServer;

public final class RangeTrainer extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private static SerializedData sd;

    public static CommandRangeQueue crq;

    @Override
    public void onEnable() {
        // Plugin startup logic

        getLogger().info(ChatColor.RED +  "Range Trainer plugin starting up!");

        saveDefaultConfig();

        sd = SerializedData.loadData("spawns.json");

        SetupConfig();
        InitializeCommands();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {

            if(Bukkit.getOnlinePlayers().size() > 0) {
                crq.checkQueue();

                for (Location l : CommandRangeAddSpawn.spawns) {

                    Player player = CommandRangeAddSpawn.spawnAvailability.get(l);

                    if (player != null) {

                        if (player.getLocation().distance(l) > maxRangeRadius) {

                            CommandRangeAddSpawn.spawnAvailability.replace(l, null);

                            if (CommandRangeSpawnMob.getMap().containsKey(player)) {

                                CommandRangeSpawnMob.getMap().get(player).setHealth(0);

                            }

                            player.sendMessage(ChatColor.RED + "You were kicked from Range Training because you strayed too far!");
                            player.removePotionEffect(PotionEffectType.SATURATION);

                            player.getInventory().clear();
                        }

                    }

                }

            }

        }, 200, 200);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {

            if(Bukkit.getOnlinePlayers().size() > 0) {
                for (Player p : CommandRangeSpawnMob.getMap().keySet()) {

                    RangeTrainMob rtm = CommandRangeSpawnMob.getMap().get(p);

                    if (p != null && rtm != null) {

                        if (rtm.getLocation().distance(p.getLocation()) > 3.3) {

                            rtm.changeName(ChatColor.RED + "FAR");

                        } else if (rtm.getLocation().distance(p.getLocation()) >= rtm.getMinRange()) {

                            rtm.changeName(ChatColor.GREEN + "GREAT");

                        } else {

                            rtm.changeName(ChatColor.RED + "CLOSE");


                        }

                        rtm.heal();
                        p.setHealth(20);

                    }
                }

            }

        }, 0, 1);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(Bukkit.getOnlinePlayers().size() > 0) {
                for (Player p : CommandRangeSpawnMob.getMap().keySet()) {

                    RangeTrainMob rtm = CommandRangeSpawnMob.getMap().get(p);
                    rtm.followPlayer();

                    if(rtm.getDisplayName().equals(ChatColor.RED + "CLOSE")){

                        rtm.randomAttack(1.25);

                    }

                }

            }


        }, 20, 15);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(Bukkit.getOnlinePlayers().size() > 0) {
                for (Player p : CommandRangeSpawnMob.getMap().keySet()) {

                    RangeTrainMob rtm = CommandRangeSpawnMob.getMap().get(p);

                    if(rtm.getDisplayName().equals(ChatColor.GREEN + "GREAT")){

                        rtm.randomAttack(1.3);

                    }

                }

            }


        }, 20, 1);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Location[] locationArr = new Location[CommandRangeAddSpawn.spawns.size()];
        CommandRangeAddSpawn.spawns.toArray(locationArr);

        for(RangeTrainMob rtm : CommandRangeSpawnMob.getMap().values()){

            rtm.setHealth(0);

        }

        sd = new SerializedData(locationArr, CommandRangeSpawnMob.getPlayerScores(), "spawns.json");
        sd.saveData();

        getLogger().info(ChatColor.RED + "Range Trainer plugin shutting down!");
    }


    private void InitializeCommands(){

        crq = new CommandRangeQueue(this, config);

        getCommand("rangeaddspawn").setExecutor(new CommandRangeAddSpawn(sd));
        getCommand("rangelistspawns").setExecutor(new CommandRangeListSpawns());
        getCommand("rangequeue").setExecutor(crq);
        getCommand("rangespawnmob").setExecutor(new CommandRangeSpawnMob());
        getCommand("rangedelspawn").setExecutor(new CommandRangeDelSpawn());

    }

    private void SetupConfig(){

        config = this.getConfig();
        config.addDefault("range-maximum-radius", 30);

    }

    public static SerializedData getSd(){

        return sd;

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe){

        Player player = pqe.getPlayer();

        RangeTrainer.crq.playerQueue.remove(player);

        for(Location l : CommandRangeAddSpawn.spawnAvailability.keySet()){

            if(CommandRangeAddSpawn.spawnAvailability.get(l).getName().equals(player.getName())){

                CommandRangeAddSpawn.spawnAvailability.replace(l, null);
                break;
            }

        }

        if(CommandRangeSpawnMob.getMap().containsKey(player)){

            CommandRangeSpawnMob.getMap().get(player).setHealth(0);
            CommandRangeSpawnMob.getMap().remove(player);

        }

    }


}
