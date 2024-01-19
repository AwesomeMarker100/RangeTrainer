package anish.stoneworks.rangetrainer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ComboTracker implements Listener {




    private boolean meetsDistance(Player player, Entity attacked, RangeTrainMob rtm){

        double distance = player.getLocation().distance(attacked.getLocation());
        return distance >= rtm.getMinRange() && distance <= 3.0;

    }

    private boolean meetsTime(RangeTrainMob rtm){

        if(System.currentTimeMillis() - rtm.getLastTimeHit() < 1200){ //1.2 seconds

            rtm.setLastTimeHit(System.currentTimeMillis());
            return true;

        }

        return false;
    }
}
