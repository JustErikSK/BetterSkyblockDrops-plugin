package me.minecraft.plugin.skyblockdrops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Skyblockdrops extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.broadcastMessage(ChatColor.GREEN + this.getName() + " >> Plugin has been enabled!");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Zombies, Blazes, Husks, Wither Skeletons and Withers now have a chance of " +
                "dropping loot from original 1.12 skyblock!");
        this.getServer().getPluginManager().registerEvents(new MobDrops(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage(ChatColor.RED + this.getName() + " >> Plugin has been disabled!");
        this.getServer().getPluginManager().disablePlugin(this);
    }
}