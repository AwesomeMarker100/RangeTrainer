package anish.stoneworks.rangetrainer;

import jdk.internal.joptsimple.util.KeyValuePair;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.bukkit.Bukkit.getServer;

public class CommandRangeQueue implements CommandExecutor {

    public Queue<Player> playerQueue = new LinkedList<>();
    public static double maxRangeRadius = 30;

    private Plugin plugin;

    public CommandRangeQueue(Plugin plugin, FileConfiguration config){

        this.plugin = plugin;
        maxRangeRadius = config.getInt("range-maximum-radius");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){

            if(!playerQueue.contains((Player)sender) && !CommandRangeAddSpawn.spawnAvailability.containsKey((Player)sender)){

                playerQueue.add((Player)sender);
                checkQueue();

            }

        }

        return true;
    }


    public void checkQueue(){



        if(playerQueue.size() > 0 && playerQueue.peek() != null && !CommandRangeAddSpawn.spawnAvailability.containsKey(playerQueue.peek())){

            for(Location l : CommandRangeAddSpawn.spawns){

                if(CommandRangeAddSpawn.spawnAvailability.get(l) == null){

                   Player player = playerQueue.poll();

                   if(player != null) {
                       player.teleport(l);
                       player.setGameMode(GameMode.SURVIVAL);

                       player.getInventory().clear();
                       ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);

                       ItemMeta swordMeta = sword.getItemMeta();
                       swordMeta.setUnbreakable(true);

                       sword.setItemMeta(swordMeta);

                       player.getInventory().setItemInMainHand(sword);
                       player.setInvulnerable(false);

                       player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1000000000, 20));

                       CommandRangeAddSpawn.spawnAvailability.replace(l, player);

                   }

                   break;
                }
            }
        }

        return;
    }



}
