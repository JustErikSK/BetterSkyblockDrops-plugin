package me.minecraft.plugin.skyblockdrops.versions;

import org.bukkit.event.entity.EntityDeathEvent;

public interface Skyblockdrops {

    public void onEnable();
    public void onMobKill(EntityDeathEvent e);
}
