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
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "Better Skyblock Drops >> Plugin has been enabled!");

        this.getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        config.addDefault("gravel_perc", 20);
        config.addDefault("gravel_amount", 1);
        config.addDefault("gravel_disable", false);
        config.addDefault("sand_perc", 20);
        config.addDefault("sand_amount", 1);
        config.addDefault("sand_disable", false);
        config.addDefault("quartz_perc", 20);
        config.addDefault("quartz_amount", 1);
        config.addDefault("quartz_disable", false);
        config.addDefault("soul_sand_perc", 20);
        config.addDefault("soul_sand_amount", 1);
        config.addDefault("soul_sand_disable", false);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Better Skyblock Drops >> Plugin has been disabled!");
        this.getServer().getPluginManager().disablePlugin(this);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {

        //  GET DEFAULT VALUES FOR ALL DROPS
        int gravel_perc = this.getConfig().getInt("gravel_perc", 20);
        int gravel_amount = this.getConfig().getInt("gravel_amount", 1);
        String gravel_disable = this.getConfig().getString("gravel_disable", "false");
        int sand_perc = this.getConfig().getInt("sand_perc", 20);
        int sand_amount = this.getConfig().getInt("sand_amount", 1);
        String sand_disable = this.getConfig().getString("sand_disable", "false");
        int quartz_perc = this.getConfig().getInt("quartz_perc", 20);
        int quartz_amount = this.getConfig().getInt("quartz_amount", 1);
        String quartz_disable = this.getConfig().getString("quartz_disable", "false");

        LivingEntity entity = e.getEntity();
        Random random = new Random();

        int number = random.nextInt(100);

        // DROPS WILL WORK ONLY IF THE DISABLE OPTION IS SET TO FALSE IN CONFIG.YML
        // IF PERCENTAGE IS MORE THAN 100 OR LESS THAN 1, DEFAULT VALUE WILL BE ACTIVE
        // IF AMOUNT IS MORE THAN 10 OR LESS THAN 1, ONLY VANILLA DROPS WILL BE APPLIED

        if (entity.getType() == EntityType.ZOMBIE && gravel_disable.equals("false")) { // GRAVEL
            if (gravel_perc > 100 || gravel_perc < 1) {
                gravel_perc = 20; // USE DEFAULT PERCENTAGE
            }
            if (gravel_amount > 10 || gravel_amount < 1) {
                gravel_amount = 1; // USE DEFAULT AMOUNT
            }
            if (number <= gravel_perc) {
                e.getDrops().add(new ItemStack(Material.GRAVEL, gravel_amount)); // GRAVEL DROP
            }
        }

        if (entity.getType() == EntityType.SKELETON && sand_disable.equals("false")) { // SAND
            if (sand_perc > 100 || sand_perc < 1) {
                sand_perc = 20; // USE DEFAULT PERCENTAGE
            }
            if (sand_amount > 10 || sand_amount < 1) {
                sand_amount = 1; // USE DEFAULT AMOUNT
            }
            if (number <= sand_perc) {
                e.getDrops().add(new ItemStack(Material.SAND, sand_amount)); // SAND DROP
            }
        }

        if (entity.getType() == EntityType.BLAZE && quartz_disable.equals("false")) { // QUARTZ
            if (quartz_perc > 100 || quartz_perc < 1) {
                quartz_perc = 20; // USE DEFAULT PERCENTAGE
            }
            if (quartz_amount > 10 || quartz_amount < 1) {
                quartz_amount = 1; // USE DEFAULT AMOUNT
            }
            if (number <= quartz_perc) {
                e.getDrops().add(new ItemStack(Material.QUARTZ, quartz_amount)); // QUARTZ DROP
            }
        }
    }
}
