package anish.stoneworks.rangetrainer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandRangeListSpawns implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if(sender instanceof Player){

            ArrayList<Location> keyArray = CommandRangeAddSpawn.spawns;
            sender.sendMessage(ChatColor.DARK_PURPLE + "---SPAWNS---");

            for(int i = 0; i < keyArray.size(); i++){

                Location loc = keyArray.get(i);

                sender.sendMessage(ChatColor.LIGHT_PURPLE + Integer.toString(i) + ": " + Math.round(loc.getX()) + ", " +
                        Math.round(loc.getY()) + ", " + Math.round(loc.getZ()));

            }
        }

        return true;
    }
}
