package me.minecraft.plugin.skyblockdrops;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class SkyblockDropsMain extends JavaPlugin implements Listener {

    private BloodMoonManager bloodMoon;
    private BloodMoonListener bloodMoonListener;
    private CreepyMessageScheduler creepy;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "BetterSkyblockDrops >> Plugin has been enabled!");

        this.getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new HalloweenEvent(this), this);
        getServer().getPluginManager().registerEvents(new ChristmasEvent(this), this);

        bloodMoon = new BloodMoonManager(this);
        bloodMoonListener = new BloodMoonListener(this, bloodMoon);
        getServer().getPluginManager().registerEvents(bloodMoonListener, this);
        getServer().getPluginManager().registerEvents(new BloodMoonListener(this, bloodMoon), this);
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler public void onJoin(PlayerJoinEvent e) {
                bloodMoon.handleJoinOrWorldChange(e.getPlayer());
            }
            @EventHandler public void onChange(PlayerChangedWorldEvent e) {
                bloodMoon.handleJoinOrWorldChange(e.getPlayer());
            }
        }, this);

        creepy = new CreepyMessageScheduler(this);
        creepy.start();

        saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        config.addDefault("gravel_perc", 10);
        config.addDefault("gravel_amount", 1);
        config.addDefault("gravel_drop", true);
        config.addDefault("sand_perc", 10);
        config.addDefault("sand_amount", 1);
        config.addDefault("sand_drop", true);
        config.addDefault("quartz_perc", 10);
        config.addDefault("quartz_amount", 1);
        config.addDefault("quartz_drop", true);
        config.addDefault("soul_sand_perc", 10);
        config.addDefault("soul_sand_amount", 1);
        config.addDefault("soul_sand_drop", true);
        config.addDefault("ancient_debris_perc", 100);
        config.addDefault("ancient_debris_amount", 4);
        config.addDefault("ancient_debris_drop", true);
        config.addDefault("magma_cream_perc", 10);
        config.addDefault("magma_cream_amount", 1);
        config.addDefault("magma_cream_drop", true);
        config.addDefault("glowstone_perc", 10);
        config.addDefault("glowstone_amount", 1);
        config.addDefault("glowstone_drop", true);
        config.addDefault("halloween_event", false);
        config.addDefault("christmas_event", false);

    }

    public void reloadAll() {
        reloadConfig();
    }

    @Override
    public void onDisable() {
        if (bloodMoon != null) bloodMoon.shutdown();
        if (bloodMoonListener != null) bloodMoonListener.shutdown();
        if (creepy != null) creepy.shutdown();
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {

        //  GET DEFAULT VALUES FOR ALL DROPS
        int gravel_perc = this.getConfig().getInt("gravel_perc", 10);
        int gravel_amount = this.getConfig().getInt("gravel_amount", 1);
        String gravel_drop = this.getConfig().getString("gravel_drop", "true");
        int sand_perc = this.getConfig().getInt("sand_perc", 10);
        int sand_amount = this.getConfig().getInt("sand_amount", 1);
        String sand_drop = this.getConfig().getString("sand_drop", "true");
        int quartz_perc = this.getConfig().getInt("quartz_perc", 10);
        int quartz_amount = this.getConfig().getInt("quartz_amount", 1);
        String quartz_drop = this.getConfig().getString("quartz_drop", "true");
        int soul_sand_perc = this.getConfig().getInt("soul_sand_perc", 10);
        int soul_sand_amount = this.getConfig().getInt("soul_sand_amount", 1);
        String soul_sand_drop = this.getConfig().getString("soul_sand_drop", "true");
        int ancient_debris_perc = this.getConfig().getInt("ancient_debris_perc", 100);
        int ancient_debris_amount = this.getConfig().getInt("ancient_debris_amount", 4);
        String ancient_debris_drop = this.getConfig().getString("ancient_debris_drop", "true");
        int magma_cream_perc = this.getConfig().getInt("magma_cream_perc", 10);
        int magma_cream_amount = this.getConfig().getInt("magma_cream_amount", 1);
        String magma_cream_drop = this.getConfig().getString("magma_cream_drop", "true");
        int glowstone_perc = this.getConfig().getInt("glowstone_perc", 10);
        int glowstone_amount = this.getConfig().getInt("glowstone_amount", 1);
        String glowstone_drop = this.getConfig().getString("glowstone_drop", "true");

        LivingEntity ent = e.getEntity();
        Random ran = new Random();

        int num = ran.nextInt(100);

        // DROPS WILL WORK ONLY IF THE DROP OPTION IS SET TO TRUE IN CONFIG.YML
        // IF PERCENTAGE IS MORE THAN 100 OR LESS THAN 1, DEFAULT VALUE WILL BE ACTIVE
        // IF AMOUNT IS MORE THAN 10 OR LESS THAN 1, ONLY VANILLA DROPS WILL BE APPLIED

        if (ent.getType() == EntityType.ZOMBIE && gravel_drop.equals("true")) { // GRAVEL
            if (gravel_perc > 100 || gravel_perc < 1) {
                gravel_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (gravel_amount > 10 || gravel_amount < 1) {
                gravel_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= gravel_perc) {
                e.getDrops().add(new ItemStack(Material.GRAVEL, gravel_amount)); // GRAVEL DROP
            }
        }

        if (ent.getType() == EntityType.HUSK && sand_drop.equals("true")) { // SAND
            if (sand_perc > 100 || sand_perc < 1) {
                sand_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (sand_amount > 10 || sand_amount < 1) {
                sand_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= sand_perc) {
                e.getDrops().add(new ItemStack(Material.SAND, sand_amount)); // SAND DROP
            }
        }

        if (ent.getType() == EntityType.BLAZE && quartz_drop.equals("true")) { // QUARTZ
            if (quartz_perc > 100 || quartz_perc < 1) {
                quartz_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (quartz_amount > 10 || quartz_amount < 1) {
                quartz_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= quartz_perc) {
                e.getDrops().add(new ItemStack(Material.QUARTZ, quartz_amount)); // QUARTZ DROP
            }
        }

        if (ent.getType() == EntityType.WITHER_SKELETON && soul_sand_drop.equals("true")) { // SOUL SAND
            if (soul_sand_perc > 100 || soul_sand_perc < 1) {
                soul_sand_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (soul_sand_amount > 10 || soul_sand_amount < 1) {
                soul_sand_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= soul_sand_perc) {
                e.getDrops().add(new ItemStack(Material.SOUL_SAND, soul_sand_amount)); // SOUL SAND DROP
            }
        }

        if (ent.getType() == EntityType.WITHER && ancient_debris_drop.equals("true")) { // ANCIENT DEBRIS
            if (ancient_debris_perc > 100 || ancient_debris_perc < 1) {
                ancient_debris_perc = 100; // USE DEFAULT PERCENTAGE
            }
            if (ancient_debris_amount > 10 || ancient_debris_amount < 1) {
                ancient_debris_amount = 4; // USE DEFAULT AMOUNT
            }
            if (num <= ancient_debris_perc) {
                e.getDrops().add(new ItemStack(Material.ANCIENT_DEBRIS, ancient_debris_amount)); // ANCIENT DEBRIS DROP
            }
        }

        if (ent.getType() == EntityType.ZOMBIFIED_PIGLIN && magma_cream_drop.equals("true")) { // MAGMA CREAM
            if (magma_cream_perc > 100 || magma_cream_perc < 1) {
                magma_cream_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (magma_cream_amount > 10 || magma_cream_amount < 1) {
                magma_cream_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= magma_cream_perc) {
                e.getDrops().add(new ItemStack(Material.MAGMA_CREAM, magma_cream_amount)); // MAGMA CREAM DROP
            }
        }

        if (ent.getType() == EntityType.SKELETON && glowstone_drop.equals("true")) { // GLOWSTONE
            if (glowstone_perc > 100 || glowstone_perc < 1) {
                glowstone_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (glowstone_amount > 10 || glowstone_amount < 1) {
                glowstone_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= glowstone_perc) {
                e.getDrops().add(new ItemStack(Material.GLOWSTONE_DUST, glowstone_amount)); // GLOWSTONE DROP
            }
        }
    }
}
