package me.minecraft.plugin.skyblockdrops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
        //this.getServer().getPluginManager().registerEvents(new MobDrops(), this);
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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("skyblockdrops")) {
            if (!sender.hasPermission("skyblockdrops.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have permissions to use this command!");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /skyblockdrops reload");
                return true;
            }
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("reload_message")));
                }
                    this.reloadConfig();
            }
        }return false;
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {

        int gravel_perc = this.getConfig().getInt("gravel_perc", 20);
        int gravel_amount = this.getConfig().getInt("gravel_amount", 1);

        LivingEntity entity = e.getEntity();
        Random random = new Random();

        ItemStack drop1 = new ItemStack(Material.GRAVEL, gravel_amount);

        if (entity.getType() == EntityType.ZOMBIE) {
            int number1 = random.nextInt(100);
            if (number1 < gravel_perc) {
                e.getDrops().add(drop1);
            }
        }
    }
}
