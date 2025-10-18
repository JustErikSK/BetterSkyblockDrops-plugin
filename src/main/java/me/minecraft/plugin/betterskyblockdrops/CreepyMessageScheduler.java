package me.minecraft.plugin.betterskyblockdrops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class CreepyMessageScheduler {

    private final JavaPlugin plugin;
    private BukkitTask task;

    private final List<String> messages = List.of(
            "I can hear footsteps behind you...",
            "Don’t look up. It sees you.",
            "Something moved in the darkness.",
            "The cave is whispering your name.",
            "You weren’t alone in that tunnel.",
            "It’s getting closer.",
            "I saw eyes in the fog.",
            "Why did the torch go out?",
            "The shadows are counting your steps…",
            "There was another torch here. Who took it?",
            "Something is breathing just out of sight.",
            "Your name echoed back… but you didn’t say it.",
            "The wind is blowing against you… underground.",
            "A door closed. There are no doors here.",
            "You left five torches. There are four.",
            "I saw a figure at the edge of your render distance.",
            "Cold fingers brushed your shoulder.",
            "It stopped moving when you looked at it."
    );

    public CreepyMessageScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        long periodTicks = 24L * 60L * 60L * 20L;
        long initialDelay = ThreadLocalRandom.current().nextLong(30L * 60L * 20L, 180L * 60L * 20L);
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::broadcastOnce, initialDelay, periodTicks);
    }

    public void shutdown() {
        if (task != null) { task.cancel(); task = null; }
    }

    private void broadcastOnce() {
        if (Bukkit.getOnlinePlayers().isEmpty() || messages.isEmpty()) return;

        String msg = messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
        String styled = ChatColor.DARK_RED + "" + ChatColor.ITALIC + msg;

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(styled);
            p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 0.6f, 0.8f);
        }
    }
}
