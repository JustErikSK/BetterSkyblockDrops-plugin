package me.minecraft.plugin.skyblockdrops;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class ChristmasEvent implements Listener {

    private final SkyblockDropsMain plugin;

    public ChristmasEvent(SkyblockDropsMain plugin) {
        this.plugin = plugin;
    }

    public void reloadSettings() {
        FileConfiguration cfg = plugin.getConfig();
    }
}
