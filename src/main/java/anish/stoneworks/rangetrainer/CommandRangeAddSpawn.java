package anish.stoneworks.rangetrainer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandRangeAddSpawn implements CommandExecutor {


    public static HashMap<Location, Player> spawnAvailability = new HashMap<Location, Player>();
    public static ArrayList<Location> spawns = new ArrayList<Location>();

    public CommandRangeAddSpawn(SerializedData sd){

        if(sd != null){

            if(sd.getSpawns() != null){
                spawns = sd.getSpawns();

                for(Location spawn : spawns){

                    spawnAvailability.put(spawn, null);

                }

            }
        }

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){

            Player player = (Player)sender;

            Location loc = player.getLocation();

            boolean isAppropriate = true;

            for(Location l : spawns){

                if(loc.distance(l) < CommandRangeQueue.maxRangeRadius){

                    isAppropriate = false;
                    break;

                }

            }

            if(!isAppropriate){

                sender.sendMessage(ChatColor.RED + "Spawns are too close to each other, please move farther or set a lower range");
                return false;
            }

            spawns.add(loc);
            spawnAvailability.put(loc, null);

            player.sendMessage(ChatColor.LIGHT_PURPLE + "Successfully created range spawn at " + Math.round(loc.getX()) + ", " + Math.round(loc.getY()) + ", " + Math.round(loc.getZ()));


        }

        return true;
    }



}
