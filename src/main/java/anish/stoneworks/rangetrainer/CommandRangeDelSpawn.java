package anish.stoneworks.rangetrainer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandRangeDelSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){

            int toDel = Integer.parseInt(args[0]);

            if(toDel < CommandRangeAddSpawn.spawns.size()){

                CommandRangeAddSpawn.spawns.remove(toDel);

            } else {

                sender.sendMessage(ChatColor.RED + "Specified spawn index could not be found!");

            }

        }

        return true;
    }
}
