package anish.stoneworks.rangetrainer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SerializedData implements Serializable {

    private Location[] spawns;
    private HashMap<Player, Integer> playerScores;
    private String filePath;

    public SerializedData(Location[] spawns, HashMap<Player, Integer> playerScores, String filePath){

        this.spawns = spawns;
        this.playerScores = playerScores;
        this.filePath = filePath;

    }

    public ArrayList<Location> getSpawns(){

        ArrayList<Location> locations = new ArrayList<Location>();

        for(Location l : spawns){

            locations.add(l);

        }

        return locations;

    }

    public HashMap<Player, Integer> getPlayerScores(){

        return playerScores;

    }

    public boolean saveData(){

        try{

            FileOutputStream fos = new FileOutputStream(filePath);
            GZIPOutputStream gos = new GZIPOutputStream(fos); //compresses data
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(gos); //the actual serializer we use

            boos.writeObject(this);
            boos.close();

            return true;

        } catch(Exception exception){

            return false;

        }

    }

    public static SerializedData loadData(String filePath){

        try{

            BukkitObjectInputStream bois = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(filePath)));
            SerializedData sd = (SerializedData) bois.readObject();

            bois.close();
            return sd;

        } catch(Exception exception){

            return null;

        }

    }
}
