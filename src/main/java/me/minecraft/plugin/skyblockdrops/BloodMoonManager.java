package me.minecraft.plugin.skyblockdrops;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class BloodMoonManager {

    private final JavaPlugin plugin;
    private final Set<World> active = new HashSet<>();
    private final Map<UUID, BossBar> bars = new HashMap<>();
    private final Map<UUID, Boolean> wasNight = new HashMap<>();

    public BloodMoonManager(JavaPlugin plugin) {
        this.plugin = plugin;
        runWorldTickWatcher();
    }

    public void shutdown() {
        bars.values().forEach(bar -> { bar.removeAll(); bar.setVisible(false); });
        bars.clear();
    }

    public boolean isActive(World w) {
        return active.contains(w);
    }

    private void runWorldTickWatcher() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (World w : Bukkit.getWorlds()) {
                long time = w.getTime();
                boolean isNight = (time >= 13000 || time < 0);
                boolean prev = wasNight.getOrDefault(w.getUID(), false);

                if (isNight && !prev) {
                    long day = w.getFullTime() / 24000L;
                    int phase = (int)(day % 8L);
                    if (phase == 0) startBloodMoon(w); else endBloodMoon(w);
                }
                if (!isNight && prev) endBloodMoon(w);

                wasNight.put(w.getUID(), isNight);
            }
        }, 20L, 40L);
    }

    private void startBloodMoon(World w) {
        if (active.add(w)) {
            BossBar bar = Bukkit.createBossBar("§cBLOOD MOON", BarColor.RED, BarStyle.SOLID);
            bar.setProgress(1.0);
            bars.put(w.getUID(), bar);
            for (Player p : w.getPlayers()) bar.addPlayer(p);

            w.playSound(w.getSpawnLocation(), Sound.AMBIENT_NETHER_WASTES_MOOD, 1f, 0.6f);
            Bukkit.broadcastMessage("§cThe Blood Moon rises... the cursed walk freely!");
        }
    }

    private void endBloodMoon(World w) {
        if (active.remove(w)) {
            BossBar bar = bars.remove(w.getUID());
            if (bar != null) {
                for (Player p : w.getPlayers()) bar.removePlayer(p);
                bar.setVisible(false);
            }
            Bukkit.broadcastMessage("§7Dawn breaks. The Blood Moon fades...");
        }
    }

    public void handleJoinOrWorldChange(Player p) {
        UUID worldId = p.getWorld().getUID();
        BossBar current = bars.get(worldId);
        if (current != null) current.addPlayer(p);

        for (Map.Entry<UUID, BossBar> entry : bars.entrySet()) {
            if (!entry.getKey().equals(worldId)) {
                entry.getValue().removePlayer(p);
            }
        }
    }
}
