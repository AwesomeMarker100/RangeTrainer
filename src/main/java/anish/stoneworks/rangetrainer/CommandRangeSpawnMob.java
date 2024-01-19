package anish.stoneworks.rangetrainer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

public class CommandRangeSpawnMob implements CommandExecutor {

    private static HashMap<Player, RangeTrainMob> map = new HashMap<Player, RangeTrainMob>();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player) {

            Player player = (Player)sender;
            Collection<Player> players = CommandRangeAddSpawn.spawnAvailability.values();

            for (Player p : players) {

                if(p == player && !map.containsKey(p)){

                    SpawnRangeMob(player, player.getLocation(), args);
                    player.sendMessage(ChatColor.RED + "Incorrect parameters! Make sure to input two parameters, first is the minimum range and second is maximum range!");

                    break;

                }

            }

        }

        return true;
    }

    private void SpawnRangeMob(Player player, Location location, String[] args){

        double minRange = Double.parseDouble(args[0]);

        if(minRange < 1.5 || minRange > 3.0){

            player.sendMessage(ChatColor.RED + "Min value must be higher than or equal to 1.5 and less than or equal to 3.0!");
            return;

        }

        RangeTrainMob rtm = new RangeTrainMob(player, location, minRange);
        map.put(player, rtm);

       /* if(RangeTrainer.getSd() != null){

            if(RangeTrainer.getSd().getPlayerScores() != null){

                for(Player p : RangeTrainer.getSd().getPlayerScores().keySet()){

                    if(p == player){

                        rtm.setLongestCombo(RangeTrainer.getSd().getPlayerScores().get(p));

                    }

                }

            }

        }*/
    }

    public static HashMap<Player, RangeTrainMob> getMap(){

        return map;
    }

    public static HashMap<Player, Integer> getPlayerScores(){

        HashMap<Player, Integer> playerScores = new HashMap<>();

        for(Player p : map.keySet()){

            if(p != null){

                playerScores.put(p, map.get(p).getLongestCombo());

            }

        }

        return playerScores;

    }


}
