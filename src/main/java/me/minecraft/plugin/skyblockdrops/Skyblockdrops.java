package me.minecraft.plugin.skyblockdrops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Skyblockdrops extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.broadcastMessage(ChatColor.GREEN + this.getName() + " >> Plugin has been enabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "CONSOLE ONLY : " + this.getName() + " >> Plugin has been enabled!");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "List of drops: Zombies drop gravel, Husks drop sand, Blazes drop quartz," +
                " Wither Skeletons drop soul sand, Withers drop ancient debris and Zombified Piglins drop magma cream!");
        this.getServer().getPluginManager().registerEvents(new MobDrops(), this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage(ChatColor.RED + this.getName() + " >> Plugin has been disabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "CONSOLE ONLY : " + this.getName() + " >> Plugin has been disabled!");
        this.getServer().getPluginManager().disablePlugin(this);
    }

}