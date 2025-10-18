package me.minecraft.plugin.betterskyblockdrops;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BloodMoonListener implements Listener {

    private final JavaPlugin plugin;
    private final BloodMoonManager manager;
    private final Map<UUID, BukkitTask> auras = new HashMap<>();
    private static boolean isHostile(LivingEntity e) {
        return (e instanceof Monster);
    }

    public BloodMoonListener(JavaPlugin plugin, BloodMoonManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(CreatureSpawnEvent e) {
        if (!plugin.getConfig().getBoolean("halloween_event", false)) return;

        if (manager == null) return;

        World w = e.getLocation().getWorld();
        if (w == null || !manager.isActive(w)) return;

        CreatureSpawnEvent.SpawnReason r = e.getSpawnReason();
        if (r == CreatureSpawnEvent.SpawnReason.SPAWNER) return;

        LivingEntity mob = e.getEntity();
        if (!isHostile(mob)) return;

        mob.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*60*5, 0, true, false, true)); // 5m Str I
        mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,          20*60*5, 0, true, false, true)); // 5m Speed I
        mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,   20*30,   0, true, false, true)); // 30s Regen I

        Location loc = mob.getLocation().add(0, 1.0, 0);
        mob.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 40, 0.4, 0.6, 0.4, 0.01);
        mob.getWorld().spawnParticle(Particle.SOUL,            loc, 20, 0.4, 0.4, 0.4, 0.0);

        startAura(mob);
    }

    private void startAura(LivingEntity mob) {
        UUID id = mob.getUniqueId();
        if (auras.containsKey(id)) return;

        BukkitTask task = new BukkitRunnable() {
            @Override public void run() {
                if (!mob.isValid() || mob.isDead()) { stop(); return; }
                Location loc = mob.getLocation().add(0, 1.0, 0);

                Particle.DustOptions red = new Particle.DustOptions(Color.fromRGB(200,20,20), 1.1f);
                mob.getWorld().spawnParticle(Particle.DUST, loc, 6, 0.2, 0.3, 0.2, 0, red);

                mob.getWorld().spawnParticle(Particle.ASH, loc, 2, 0.2, 0.3, 0.2, 0);
            }
            private void stop() {
                BukkitTask t = auras.remove(id);
                if (t != null) t.cancel();
            }
        }.runTaskTimer(plugin, 0L, 10L);

        auras.put(id, task);
    }

    public void shutdown() {
        for (BukkitTask t : auras.values()) {
            if (t != null) t.cancel();
        }
        auras.clear();
    }
}
