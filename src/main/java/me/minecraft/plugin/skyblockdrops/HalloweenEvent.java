package me.minecraft.plugin.skyblockdrops;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class HalloweenEvent implements Listener {

    private final SkyblockDropsMain plugin;

    public HalloweenEvent(SkyblockDropsMain plugin) {
        this.plugin = plugin;
    }

    public void reloadSettings() {
        FileConfiguration cfg = plugin.getConfig();
    }
}
