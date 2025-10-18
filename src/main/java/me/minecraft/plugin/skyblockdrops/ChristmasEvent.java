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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChristmasEvent implements Listener {

    private final SkyblockDropsMain plugin;
    private final NamespacedKey PRESENT_KEY;

    public ChristmasEvent(SkyblockDropsMain plugin) {
        this.plugin = plugin;
        this.PRESENT_KEY = new NamespacedKey(plugin, "present");
        initializeRewards();
    }

    public boolean reloadSettings() {
        FileConfiguration cfg = plugin.getConfig();
        return cfg.getBoolean("christmas_event", false);
    }

    @EventHandler
    public void christmasEventSpawn(EntitySpawnEvent e) {

        // GET THE DEFAULT VALUE FOR CHRISTMAS EVENT RULE
        boolean christmasEnabled = plugin.getConfig().getBoolean("christmas_event", false);

        Random random = new Random();

        if (christmasEnabled) {
            if (e.getEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getEntity();
                if (ent.getType() == EntityType.ZOMBIE || ent.getType() == EntityType.HUSK || ent.getType() == EntityType.WITHER_SKELETON || ent.getType() == EntityType.ZOMBIFIED_PIGLIN || ent.getType() == EntityType.SKELETON) {
                    int number1 = random.nextInt(100);
                    if (number1 > 85) { // SANTA'S HAT - RED LEATHER HELMET (15%)
                        ItemStack redHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
                        LeatherArmorMeta redHelmetMeta = (LeatherArmorMeta) redHelmet.getItemMeta();
                        redHelmetMeta.setColor(Color.fromRGB(255, 0, 0));
                        redHelmetMeta.setDisplayName(ChatColor.GOLD + "Santa's Hat");
                        redHelmet.setItemMeta(redHelmetMeta);
                        Objects.requireNonNull(ent.getEquipment()).setHelmet(redHelmet);
                    }
                    if (number1 < 25) { // SANTA'S HELPER'S HAT - GREEN LEATHER HELMET (25%)
                        ItemStack greenHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
                        LeatherArmorMeta greenHelmetMeta = (LeatherArmorMeta) greenHelmet.getItemMeta();
                        greenHelmetMeta.setColor(Color.fromRGB(69, 230, 0));
                        greenHelmetMeta.setDisplayName(ChatColor.DARK_PURPLE + "Santa's Helper's Hat");
                        greenHelmet.setItemMeta(greenHelmetMeta);
                        Objects.requireNonNull(ent.getEquipment()).setHelmet(greenHelmet);
                    }
                }
            }
            if (e.getEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getEntity();
                if (ent.getType() == EntityType.ZOMBIE || ent.getType() == EntityType.HUSK || ent.getType() == EntityType.WITHER_SKELETON || ent.getType() == EntityType.ZOMBIFIED_PIGLIN || ent.getType() == EntityType.SKELETON) {
                    int number2 = random.nextInt(100);
                    if (number2 < 30) { // SWEET CANDIES - SWEET BERRIES (15%)
                        ItemStack candies = new ItemStack(Material.SWEET_BERRIES, 1);
                        ItemMeta itemStackMeta = candies.getItemMeta();
                        itemStackMeta.setDisplayName(ChatColor.DARK_PURPLE + "Sweet Candies");
                        candies.setItemMeta(itemStackMeta);
                        Objects.requireNonNull(ent.getEquipment()).setItemInMainHand(candies);
                    }
                    if (number2 < 15) { // GINGERBREAD COOKIES - COOKIES (15%)
                        ItemStack cookies = new ItemStack(Material.COOKIE, 1);
                        ItemMeta itemStackMeta = cookies.getItemMeta();
                        itemStackMeta.setDisplayName(ChatColor.DARK_PURPLE + "Gingerbread Cookies");
                        cookies.setItemMeta(itemStackMeta);
                        Objects.requireNonNull(ent.getEquipment()).setItemInMainHand(cookies);
                    }
                }
            }
        }
    }

    public ItemStack createPresent() { // Present creation
        ItemStack present = new ItemStack(Material.CHEST);
        ItemMeta meta = present.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Present");
            meta.setLore(Arrays.asList(ChatColor.AQUA + "Right-click to open!", ChatColor.GRAY + "Contains a surprise!"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());

            present.setItemMeta(meta);
        }
        return present;
    }

    private boolean isPresent(ItemStack item) { // isPresent method
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(new NamespacedKey(plugin, "uniqueID"), PersistentDataType.STRING);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) { // Present placement prevention
        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();

        if (isPresent(item)) {
            e.setCancelled(true);
        }
    }

    private final List<ItemStack> commonRewards = new ArrayList<>();
    private final List<ItemStack> rareRewards = new ArrayList<>();

    public void initializeRewards() {
        commonRewards.add(new ItemStack(Material.GLOW_BERRIES, 2));
        commonRewards.add(new ItemStack(Material.SNOWBALL, 3));
        commonRewards.add(new ItemStack(Material.EGG));
        commonRewards.add(new ItemStack(Material.WHEAT_SEEDS, 2));
        commonRewards.add(new ItemStack(Material.KELP));
        commonRewards.add(new ItemStack(Material.INK_SAC, 2));
        commonRewards.add(new ItemStack(Material.GLASS_BOTTLE));
        commonRewards.add(new ItemStack(Material.BOWL));
        commonRewards.add(new ItemStack(Material.SUGAR, 3));
        commonRewards.add(new ItemStack(Material.YELLOW_DYE));

        rareRewards.add(new ItemStack(Material.DIAMOND));
        rareRewards.add(new ItemStack(Material.ENDER_PEARL, 2));
        rareRewards.add(new ItemStack(Material.EMERALD, 5));
        rareRewards.add(new ItemStack(Material.ANCIENT_DEBRIS));
        rareRewards.add(new ItemStack(Material.COPPER_INGOT, 10));
        rareRewards.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
    }

    private ItemStack getRandomCommonRewards() {
        if (commonRewards.isEmpty()) return null;

        int randomIndexC = ThreadLocalRandom.current().nextInt(commonRewards.size());
        return commonRewards.get(randomIndexC).clone();
    }
    private List<ItemStack> getCommonRewards(int amount) {
        List<ItemStack> randomCommonRewards = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            randomCommonRewards.add(getRandomCommonRewards());
        }
        return randomCommonRewards;
    }

    private ItemStack getRandomRareReward() {
        if (rareRewards.isEmpty()) return null;

        int randomIndexR = ThreadLocalRandom.current().nextInt(rareRewards.size());
        return rareRewards.get(randomIndexR).clone();
    }
    private List<ItemStack> getRareRewards(int amount) {
        List<ItemStack> randomRareRewards = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            randomRareRewards.add(getRandomRareReward());
        }
        return randomRareRewards;
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && isPresent(item)) {
            e.setCancelled(true);
            player.getInventory().removeItem(item);

            Random random = new Random();
            int number = random.nextInt(100);

            if (number < 85) { // 85%
                int rewardCount = ThreadLocalRandom.current().nextInt(1, 3);
                List<ItemStack> randomCommonRewards = getCommonRewards(rewardCount);

                for (ItemStack reward : randomCommonRewards) {
                    player.getInventory().addItem(reward);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                }
            } else if (number > 85) { // 15%
                int rewardCount = 1;
                List<ItemStack> randomRareReward = getRareRewards(rewardCount);

                for (ItemStack reward : randomRareReward) {
                    player.getInventory().addItem(reward);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.5f);
                }
            }
        }
    }

    @EventHandler
    public void christmasEventDeath(EntityDeathEvent e) {
        boolean christmasEnabled = plugin.getConfig().getBoolean("christmas_event", false);
        if (christmasEnabled) {
            if (Math.random() < 0.1) { // 10%
                e.getDrops().add(createPresent());
            }
        }
    }
}
