package me.minecraft.plugin.skyblockdrops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public final class Skyblockdrops extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "OriginalSkyblockDrops >> Plugin has been enabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "CONSOLE ONLY : OriginalSkyblockDrops >> Plugin has been enabled!");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "List of drops: Zombies drop gravel, Husks drop sand, Blazes drop quartz," +
                " Wither Skeletons drop soul sand, Withers drop ancient debris and Zombified Piglins drop magma cream!");
        this.getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        config.addDefault("gravel_perc", 20);
        config.addDefault("gravel_amount", 1);
        config.addDefault("sand_perc", 20);
        config.addDefault("sand_amount", 1);
        config.addDefault("quartz_perc", 20);
        config.addDefault("quartz_amount", 1);
        config.addDefault("soul_sand_perc", 20);
        config.addDefault("soul_sand_amount", 1);
        config.addDefault("ancient_debris_perc", 100);
        config.addDefault("ancient_debris_amount", 2);
        config.addDefault("magma_cream_perc", 15);
        config.addDefault("magma_cream_amount", 1);
        config.addDefault("reload_message","&9Reloaded OriginalSkyblockDrops Config");
    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage(ChatColor.RED + "OriginalSkyblockDrops >> Plugin has been disabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "CONSOLE ONLY : OriginalSkyblockDrops >> Plugin has been disabled!");
        this.getServer().getPluginManager().disablePlugin(this);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {

        int gravel_perc = this.getConfig().getInt("gravel_perc", 20);
        int gravel_amount = this.getConfig().getInt("gravel_amount", 1);
        int sand_perc = this.getConfig().getInt("sand_perc", 20);
        int sand_amount = this.getConfig().getInt("sand_amount", 1);
        int quartz_perc = this.getConfig().getInt("quartz_perc", 20);
        int quartz_amount = this.getConfig().getInt("quartz_amount", 20);
        int soul_sand_perc = this.getConfig().getInt("soul_sand_perc", 20);
        int soul_sand_amount = this.getConfig().getInt("soul_sand_amount", 20);
        int ancient_debris_perc = this.getConfig().getInt("ancient_debris_perc", 20);
        int ancient_debris_amount = this.getConfig().getInt("ancient_debris_amount", 20);
        int magma_cream_perc = this.getConfig().getInt("magma_cream_perc", 20);
        int magma_cream_amount = this.getConfig().getInt("magma_cream_amount", 20);

        LivingEntity entity = e.getEntity();
        Random random = new Random();

        ItemStack drop1 = new ItemStack(Material.GRAVEL, gravel_amount);
        ItemStack drop2 = new ItemStack(Material.SAND, sand_amount);
        ItemStack drop3 = new ItemStack(Material.QUARTZ, quartz_amount);
        ItemStack drop4 = new ItemStack(Material.SOUL_SAND, soul_sand_amount);
        ItemStack drop5 = new ItemStack(Material.ANCIENT_DEBRIS, ancient_debris_amount);
        ItemStack drop6 = new ItemStack(Material.MAGMA_CREAM, magma_cream_amount);

        // IF PERCENTAGE IS MORE THAN 100 OR LESS THAN 1, DEFAULT VALUE WILL BE ACTIVE
        // IF AMOUNT IS MORE THAN 10 OR LESS THAN 1, ONLY VANILLA DROPS WILL BE APPLIED

        if (entity.getType() == EntityType.ZOMBIE) { // GRAVEL
            int number = random.nextInt(100);
            if (gravel_perc > 100 || gravel_perc < 1) {
                gravel_perc = 20; // USE DEFAULT PERCENTAGE
            } if (number <= gravel_perc) {
                if (gravel_amount <= 10) {
                    e.getDrops().add(drop1); // GRAVEL DROP
                }
            }
        if (entity.getType() == EntityType.HUSK) { // SAND
            if (sand_perc > 100 || sand_perc < 1) {
                sand_perc = 20; // USE DEFAULT PERCENTAGE
            } if (number <= sand_perc) {
                if (sand_amount <= 10) {
                    e.getDrops().add(drop2); // SAND DROP
                }
            }
        if (entity.getType() == EntityType.BLAZE) { // QUARTZ
            if (quartz_perc > 100 || quartz_perc < 1) {
                quartz_perc = 20; // USE DEFAULT PERCENTAGE
            } if (number <= quartz_perc) {
                if (quartz_amount <= 10) {
                    e.getDrops().add(drop3); // QUARTZ DROP
                }
            }
        if (entity.getType() == EntityType.WITHER_SKELETON) { // SOUL SAND
            if (soul_sand_perc > 100 || soul_sand_perc < 1) {
                soul_sand_perc = 20; // USE DEFAULT PERCENTAGE
            } if (number <= soul_sand_perc) {
                if (soul_sand_amount <= 10) {
                    e.getDrops().add(drop4); // SOUL SAND DROP
                }
            }
        if (entity.getType() == EntityType.WITHER) { // ANCIENT DEBRIS
            if (ancient_debris_perc > 100 || ancient_debris_perc < 1) {
                ancient_debris_perc = 100; // USE DEFAULT PERCENTAGE
            } if (number <= ancient_debris_perc)  {
                if (ancient_debris_amount <= 10) {
                    e.getDrops().add(drop5); // ANCIENT DEBRIS DROP
                }
            }
        if (entity.getType() == EntityType.ZOMBIFIED_PIGLIN) { // MAGMA CREAM
            if (magma_cream_perc > 100 || magma_cream_perc < 1) {
                magma_cream_perc = 15; // USE DEFAULT PERCENTAGE
            } if (number <= magma_cream_perc) {
                if (magma_cream_amount <= 10) {
                    e.getDrops().add(drop6); // MAGMA CREAM DROP
                }
            }
        }}}}}}
    }
}
