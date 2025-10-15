package me.minecraft.plugin.skyblockdrops;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Particle;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class HalloweenEvent implements Listener {

    private final SkyblockDropsMain plugin;

    public HalloweenEvent(SkyblockDropsMain plugin) {
        this.plugin = plugin;
        initializeRewards();
    }

    public boolean reloadSettings() {
        FileConfiguration cfg = plugin.getConfig();
        return cfg.getBoolean("halloween_event", false);
    }

    @EventHandler
    public void halloweenEventSpawn(CreatureSpawnEvent e) {

        // GET THE DEFAULT VALUE FOR HALLOWEEN EVENT RULE
        boolean halloweenEnabled = plugin.getConfig().getBoolean("halloween_event", false);

        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) return;

        LivingEntity mob = e.getEntity();
        EntityType type = mob.getType();

        if (type != EntityType.ZOMBIE &&
            type != EntityType.HUSK &&
            type != EntityType.WITHER_SKELETON &&
            type != EntityType.ZOMBIFIED_PIGLIN &&
            type != EntityType.SKELETON) return;

        if (mob.getEquipment() == null || mob.getEquipment().getHelmet() != null) return;

        int roll = java.util.concurrent.ThreadLocalRandom.current().nextInt(100);

        if (roll < 10) { // 10% for jack o'lantern
            mob.getEquipment().setHelmet(new ItemStack(Material.JACK_O_LANTERN));

            Location loc = mob.getLocation().add(0, 1.0, 0);
            mob.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 40, 0.4, 0.6, 0.4, 0.01);
            mob.getWorld().spawnParticle(Particle.SOUL, loc, 20, 0.4, 0.4, 0.4, 0.0);
        } else if (roll < 50) { // 40% for carved pumpkin
            mob.getEquipment().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));

            Particle.DustOptions orange = new Particle.DustOptions(Color.fromRGB(255, 140, 0), 1.3f);
            Location loc = mob.getLocation().add(0, 1.0, 0);
            mob.getWorld().spawnParticle(Particle.DUST, loc, 60, 0.5, 0.6, 0.5, 0.0, orange);
        }
    }

    public ItemStack createHalloweenPresent() {
        ItemStack present = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = present.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Trick or Treat!");
            meta.setLore(Arrays.asList(ChatColor.AQUA + "Right-click to open!", ChatColor.GRAY + "Contains a surprise!"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey((Plugin) this, "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());

            present.setItemMeta(meta);
        }
        return present;
    }

    private boolean isHalloweenPresent(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(new NamespacedKey((Plugin) this, "uniqueID"), PersistentDataType.STRING);
    }

    @EventHandler
    public void onHalloweenPresentPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();

        if (isHalloweenPresent(item)) {
            e.setCancelled(true);
        }
    }

    private final List<ItemStack> commonRewards = new ArrayList<>(); // Spooky Tier
    private final List<ItemStack> rareRewards = new ArrayList<>(); // Cursed Tier
    private final List<ItemStack> legendaryRewards = new ArrayList<>(); // Wicked Tier

    public void initializeRewards() {
        // ==== Spooky Tier reward list ====
        commonRewards.add(new ItemStack(Material.ROTTEN_FLESH, 2));
        commonRewards.add(new ItemStack(Material.SPIDER_EYE, 2));
        commonRewards.add(new ItemStack(Material.PUMPKIN, 1));
        commonRewards.add(new ItemStack(Material.BONE, 2));
        commonRewards.add(new ItemStack(Material.GUNPOWDER, 2));
        commonRewards.add(new ItemStack(Material.COOKED_BEEF, 1));
        commonRewards.add(new ItemStack(Material.POISONOUS_POTATO, 1));
        commonRewards.add(new ItemStack(Material.BLACK_DYE, 1));
        commonRewards.add(new ItemStack(Material.COOKED_CHICKEN, 1));
        commonRewards.add(new ItemStack(Material.WHEAT, 2));
        // ==== Cursed Tier reward tier ====
        rareRewards.add(new ItemStack(Material.ENDER_PEARL, 1));
        rareRewards.add(new ItemStack(Material.EMERALD, 1));
        rareRewards.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 2));
        rareRewards.add(new ItemStack(Material.SLIME_BALL, 1));
        rareRewards.add(new ItemStack(Material.IRON_SWORD, 1));
        rareRewards.add(new ItemStack(Material.FERMENTED_SPIDER_EYE, 1));
        rareRewards.add(new ItemStack(Material.SKELETON_SKULL, 1));
        rareRewards.add(new ItemStack(Material.ZOMBIE_HEAD, 1));
        // ==== Wicked Tier reward tier ====
        legendaryRewards.add(new ItemStack(Material.DIAMOND, 1));
        legendaryRewards.add(new ItemStack(Material.NETHERITE_SCRAP, 1));
        legendaryRewards.add(new ItemStack(Material.WITHER_SKELETON_SKULL, 1));
        legendaryRewards.add(new ItemStack(Material.TRIDENT, 1));
        // ==== Enchanted books for Cursed Tier ====
        ItemStack book1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta metaBook1 = (EnchantmentStorageMeta) book1.getItemMeta();
        if (metaBook1 != null) {
            metaBook1.addStoredEnchant(Enchantment.UNBREAKING, 1, true);
            book1.setItemMeta(metaBook1);
        }
        rareRewards.add(book1); // Unbreaking 1 book

        ItemStack book2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta metaBook2 = (EnchantmentStorageMeta) book2.getItemMeta();
        if (metaBook2 != null) {
            metaBook2.addStoredEnchant(Enchantment.PROTECTION, 1, true);
            book2.setItemMeta(metaBook2);
        }
        rareRewards.add(book2); // Protection 1 book

        ItemStack book3 = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta metaBook3 = (EnchantmentStorageMeta) book3.getItemMeta();
        if (metaBook3 != null) {
            metaBook3.addStoredEnchant(Enchantment.SHARPNESS, 1, true);
            book3.setItemMeta(metaBook3);
        }
        rareRewards.add(book3); // Sharpness 1 book

        ItemStack book4 = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta metaBook4 = (EnchantmentStorageMeta) book4.getItemMeta();
        if (metaBook4 != null) {
            metaBook4.addStoredEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
            book4.setItemMeta(metaBook4);
        }
        rareRewards.add(book4); // Luck of the Sea 1 book
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

    private ItemStack getRandomRareRewards() {
        if (rareRewards.isEmpty()) return null;

        int randomIndexC = ThreadLocalRandom.current().nextInt(rareRewards.size());
        return rareRewards.get(randomIndexC).clone();
    }
    private List<ItemStack> getRareRewards(int amount) {
        List<ItemStack> randomRareRewards = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            randomRareRewards.add(getRandomRareRewards());
        }
        return randomRareRewards;
    }

    private ItemStack getRandomLegendaryRewards() {
        if (legendaryRewards.isEmpty()) return null;

        int randomIndexC = ThreadLocalRandom.current().nextInt(legendaryRewards.size());
        return legendaryRewards.get(randomIndexC).clone();
    }
    private List<ItemStack> getLegendaryRewards(int amount) {
        List<ItemStack> randomLegendaryRewards = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            randomLegendaryRewards.add(getRandomLegendaryRewards());
        }
        return randomLegendaryRewards;
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && isHalloweenPresent(item)) {
            e.setCancelled(true);
            player.getInventory().removeItem(item);

            Random random = new Random();
            int number = random.nextInt(100);

            if (number < 51 && number > 25) { // 25% for Common Reward
                int rewardCount = ThreadLocalRandom.current().nextInt(1, 4);
                List<ItemStack> randomCommonRewards = getCommonRewards(rewardCount);

                for (ItemStack reward : randomCommonRewards) {
                    player.getInventory().addItem(reward);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 2.0f, 1.5f);
                }
            }
            if (number < 21 && number > 10) { // 10% for Rare Reward
                int rewardCount = ThreadLocalRandom.current().nextInt(1, 2);
                List<ItemStack> randomRareRewards = getRareRewards(rewardCount);

                for (ItemStack reward : randomRareRewards) {
                    player.getInventory().addItem(reward);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 2.0f, 1.0f);
                }
            }
            if (number == 0) { // 1% for Legendary Reward
                int rewardCount = ThreadLocalRandom.current().nextInt(1, 1);
                List<ItemStack> randomLegendaryRewards = getLegendaryRewards(rewardCount);

                for (ItemStack reward : randomLegendaryRewards) {
                    player.getInventory().addItem(reward);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 2.0f, 0.5f);
                }
            }
        }
    }

    @EventHandler
    public void halloweenEventDeath(EntityDeathEvent e) {

        // GET THE DEFAULT VALUE FOR HALLOWEEN EVENT RULE
        boolean halloweenEnabled = plugin.getConfig().getBoolean("halloween_event", false);

        Random random = new Random();
        int number = random.nextInt(1000);
        LivingEntity ent = e.getEntity();

        if (halloweenEnabled) {
            if (ent.getType() == EntityType.ZOMBIE || ent.getType() == EntityType.HUSK || ent.getType() == EntityType.BLAZE || ent.getType() == EntityType.WITHER_SKELETON || ent.getType() == EntityType.WITHER || ent.getType() == EntityType.ZOMBIFIED_PIGLIN || ent.getType() == EntityType.SKELETON) {
                if (number < 30) { // 30% to get a present
                    e.getDrops().add(createHalloweenPresent());
                }
            }
        }
    }
}
