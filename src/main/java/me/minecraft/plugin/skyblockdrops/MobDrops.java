package me.minecraft.plugin.skyblockdrops;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class MobDrops implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {

        JavaPlugin plugin = null;
        FileConfiguration config = plugin.getConfig();

        int gravel_perc = plugin.getConfig().getInt("gravel_perc", 20);
        int gravel_amount = plugin.getConfig().getInt("gravel_amount", 1);
        int sand_perc = plugin.getConfig().getInt("sand_perc", 20);
        int sand_amount = plugin.getConfig().getInt("sand_amount", 1);
        int quartz_perc = plugin.getConfig().getInt("quartz_perc", 20);
        int quartz_amount = plugin.getConfig().getInt("quartz_amount", 1);
        int soul_sand_perc = plugin.getConfig().getInt("soul_sand_perc", 20);
        int soul_sand_amount = plugin.getConfig().getInt("soul_sand_amount", 1);
        int ancient_debris_perc = plugin.getConfig().getInt("ancient_debris_perc", 100);
        int ancient_debris_amount = plugin.getConfig().getInt("ancient_debris_amount", 2);
        int magma_cream_perc = plugin.getConfig().getInt("magma_cream_perc", 15);
        int magma_cream_amount = plugin.getConfig().getInt("magma_cream_amount", 1);

        LivingEntity entity = event.getEntity();
        Random random = new Random();
        ItemStack drop1 = new ItemStack(Material.GRAVEL, gravel_amount);
        ItemStack drop3 = new ItemStack(Material.SAND, sand_amount);
        ItemStack drop2 = new ItemStack(Material.QUARTZ, quartz_amount);
        ItemStack drop4 = new ItemStack(Material.SOUL_SAND, soul_sand_amount);
        ItemStack drop5 = new ItemStack(Material.ANCIENT_DEBRIS, ancient_debris_amount);
        ItemStack drop6 = new ItemStack(Material.MAGMA_CREAM, magma_cream_amount);

        if (entity.getType() == EntityType.ZOMBIE) {
            int number1 = random.nextInt(100);
            if (number1 < gravel_perc) {
                event.getDrops().add(drop1);
            }
        } else if (entity.getType() == EntityType.HUSK) {
            int number2 = random.nextInt(100);
            if (number2 < sand_perc) {
                event.getDrops().add(drop2);
            }
        } else if (entity.getType() == EntityType.BLAZE) {
            int number3 = random.nextInt(100);
            if (number3 < quartz_perc) {
                event.getDrops().add(drop3);
            }
        } else if (entity.getType() == EntityType.WITHER_SKELETON) {
            int number4 = random.nextInt(100);
            if (number4 < soul_sand_perc) {
                event.getDrops().add(drop4);
            }
        } else if (entity.getType() == EntityType.WITHER) {
            int number5 = random.nextInt(100);
            if (number5 < ancient_debris_perc) {
                event.getDrops().add(drop5);
            }
        } else if (entity.getType() == EntityType.ZOMBIFIED_PIGLIN) {
            int number6 = random.nextInt(100);
            if (number6 < magma_cream_perc) {
                event.getDrops().add(drop6);
            }
        }
    }
}
