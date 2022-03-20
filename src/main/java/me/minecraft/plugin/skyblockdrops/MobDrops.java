package me.minecraft.plugin.skyblockdrops;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MobDrops implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();
        Random random = new Random();
        ItemStack drop1 = new ItemStack(Material.GRAVEL,1);
        ItemStack drop2 = new ItemStack(Material.QUARTZ,1);
        ItemStack drop3 = new ItemStack(Material.SAND,1);
        ItemStack drop4 = new ItemStack(Material.SOUL_SAND,1);
        ItemStack drop5 = new ItemStack(Material.ANCIENT_DEBRIS,2);
        ItemStack drop6 = new ItemStack(Material.MAGMA_CREAM, 2);

        if (entity.getType() == EntityType.ZOMBIE) {
            int number1 = random.nextInt(100);
            if (number1 < 20) {
                event.getDrops().add(drop1);
            }
        } else if (entity.getType() == EntityType.BLAZE) {
            int number2 = random.nextInt(100);
            if (number2 < 20) {
                event.getDrops().add(drop2);
            }
        } else if (entity.getType() == EntityType.HUSK) {
            int number3 = random.nextInt(100);
            if (number3 < 20) {
                event.getDrops().add(drop3);
            }
        } else if (entity.getType() == EntityType.WITHER_SKELETON) {
            int number4 = random.nextInt(100);
            if (number4 < 20) {
                event.getDrops().add(drop4);
            }
        } else if (entity.getType() == EntityType.WITHER) {
            event.getDrops().add(drop5);
        } else if (entity.getType() == EntityType.ZOMBIFIED_PIGLIN) {
            int number5 = random.nextInt(100);
            if (number5 < 25) {
                event.getDrops().add(drop6);
            }
        }
    }
}
