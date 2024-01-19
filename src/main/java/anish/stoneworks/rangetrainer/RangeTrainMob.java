package anish.stoneworks.rangetrainer;

import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.bukkit.Bukkit.*;


public class RangeTrainMob  {

    private double minRange;

    private Player partner;
    private Zombie rtm;

    private long lastTimeHit = 0l;
    private int longestCombo = 0;

    private World world;

    public RangeTrainMob(Player player, Location location, double minRange){

        this.minRange = minRange;
        this.partner = player;
        this.world = location.getWorld();

        rtm = (Zombie)world.spawnEntity(location, EntityType.ZOMBIE);
        SetupBaseAttributes();

    }

    public void followPlayer(){

        rtm.getPathfinder().moveTo(partner.getLocation());

    }

    private void SetupBaseAttributes(){

        SetEquipment();

        rtm.setAdult();
        rtm.setCanBreakDoors(false);

        rtm.setAI(true);

        rtm.setShouldBurnInDay(false);
        rtm.setInvulnerable(false);

        rtm.setCanPickupItems(false);


        rtm.setMaxHealth(200000000);

        rtm.setCustomNameVisible(true);


        rtm.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000000, 1));



    }

    public void attack(){

        rtm.attack(partner);

    }

    public Location getLocation(){

        return rtm.getLocation();

    }

    public void changeName(String text){

        rtm.customName(Component.text(text));

    }

    public void heal(){

        rtm.setHealth(20);

    }
    //must be greater than or equal to 1!
    public void randomAttack(double total){

        int randNum = (int)(Math.random() * total + 1);

        if(randNum == 1) {
            partner.damage(1);
            partner.setVelocity(partner.getLocation().getDirection().multiply(rtm.getVelocity().multiply(2)));
        }

    }

    public String getDisplayName(){

        return rtm.customName().toString();

    }

    public void setHealth(float health){

        rtm.setHealth(health);

    }


    private void SetEquipment(){

        ItemStack[] armor = GetArmor();
        ItemStack sword = GetSword();

        rtm.getEquipment().setHelmet(armor[0]);
        rtm.getEquipment().setChestplate(armor[1]);
        rtm.getEquipment().setLeggings(armor[2]);
        rtm.getEquipment().setBoots(armor[3]);

        rtm.getEquipment().setItemInMainHand(sword);
    }

    private ItemStack[] GetArmor(){

        HashMap<Enchantment, Integer> armorEnchants = new HashMap<Enchantment, Integer>();
        armorEnchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        armorEnchants.put(Enchantment.DURABILITY, 3);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta helmetMeta = helmet.getItemMeta();

        helmetMeta.setUnbreakable(true);
        helmet.setItemMeta(helmetMeta);
        helmet.addEnchantments(armorEnchants);


        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta chestplateMeta = chestplate.getItemMeta();

        chestplateMeta.setUnbreakable(true);
        helmet.setItemMeta(chestplateMeta);

        chestplate.addEnchantments(armorEnchants);

        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta leggingsMeta = leggings.getItemMeta();

        leggingsMeta.setUnbreakable(true);
        leggings.setItemMeta(leggingsMeta);

        leggings.addEnchantments(armorEnchants);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta bootsMeta = boots.getItemMeta();

        bootsMeta.setUnbreakable(true);
        boots.setItemMeta(bootsMeta);

        boots.addEnchantments(armorEnchants);

        return new ItemStack[]{

                helmet, chestplate, leggings, boots

        };
    }

    private ItemStack GetSword(){


        HashMap<Enchantment, Integer> swordEnchants = new HashMap<>();

        swordEnchants.put(Enchantment.DAMAGE_ALL, 5);
        swordEnchants.put(Enchantment.DURABILITY, 3);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();

        swordMeta.setUnbreakable(true);
        sword.setItemMeta(swordMeta);

        sword.addEnchantments(swordEnchants);

        return sword;

    }

    public Player getPartner(){

        return partner;

    }

    public void setLastTimeHit(long lastTimeHit){

        this.lastTimeHit = lastTimeHit;

    }

    public long getLastTimeHit(){
        return lastTimeHit;
    }


    public double getMinRange(){

        return minRange;

    }

    public int getLongestCombo(){

        return longestCombo;

    }

    public void setLongestCombo(int combo){

        longestCombo = combo;

    }



    public World getWorld() {
        return world;
    }
}
