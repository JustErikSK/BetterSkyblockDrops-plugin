package me.minecraft.plugin.betterskyblockdrops;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Particle;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class HalloweenEvent implements Listener {

    private final SkyblockDropsMain plugin;
    private final NamespacedKey PRESENT_KEY;
    private final java.util.Map<java.util.UUID, org.bukkit.scheduler.BukkitTask> auras = new java.util.HashMap<>();

    public HalloweenEvent(SkyblockDropsMain plugin) {
        this.plugin = plugin;
        this.PRESENT_KEY = new NamespacedKey(plugin, "present");
        initializeRewards();
    }

    public boolean reloadSettings() {
        FileConfiguration cfg = plugin.getConfig();
        return cfg.getBoolean("halloween_event", false);
    }

    private void startPumpkinAura(LivingEntity mob) {
        java.util.UUID id = mob.getUniqueId();
        if (auras.containsKey(id)) return;

        org.bukkit.scheduler.BukkitTask task = new org.bukkit.scheduler.BukkitRunnable() {
            @Override public void run() {
                if (!mob.isValid() || mob.isDead() || mob.getEquipment() == null) {
                    stop();
                    return;
                }
                ItemStack h = mob.getEquipment().getHelmet();
                if (h == null) { stop(); return; }

                Material m = h.getType();
                if (m != Material.CARVED_PUMPKIN && m != Material.JACK_O_LANTERN) { stop(); return; }

                Location loc = mob.getLocation().add(0, 1.0, 0);
                if (m == Material.JACK_O_LANTERN) {
                    mob.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 4, 0.15, 0.25, 0.15, 0.0);
                    mob.getWorld().spawnParticle(Particle.SOUL,            loc, 2, 0.15, 0.15, 0.15, 0.0);
                } else { // carved
                    Particle.DustOptions orange = new Particle.DustOptions(org.bukkit.Color.fromRGB(255,140,0), 1.1f);
                    mob.getWorld().spawnParticle(Particle.DUST, loc, 6, 0.2, 0.3, 0.2, 0.0, orange);
                }
            }
            private void stop() {
                org.bukkit.scheduler.BukkitTask t = auras.remove(id);
                if (t != null) t.cancel();
            }
        }.runTaskTimer(plugin, 0L, 10L);

        auras.put(id, task);
    }

    @EventHandler
    public void halloweenEventSpawn(CreatureSpawnEvent e) {

        // GET THE DEFAULT VALUE FOR HALLOWEEN EVENT RULE
        if (!plugin.getConfig().getBoolean("halloween_event", false)) return;

        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) return;

        LivingEntity mob = e.getEntity();
        EntityType type = mob.getType();

        if (type != EntityType.ZOMBIE &&
            type != EntityType.HUSK &&
            type != EntityType.WITHER_SKELETON &&
            type != EntityType.ZOMBIFIED_PIGLIN &&
            type != EntityType.SKELETON) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!mob.isValid() || mob.isDead()) return;
            if (mob.getEquipment() == null) return;

            int roll = java.util.concurrent.ThreadLocalRandom.current().nextInt(100);
            boolean jack = (roll < 10);
            boolean carved = !jack && roll < 50;

            if (!jack && !carved) return;

            ItemStack helm = new ItemStack(jack ? Material.JACK_O_LANTERN : Material.CARVED_PUMPKIN);
            mob.getEquipment().setHelmet(helm);
            mob.getEquipment().setHelmetDropChance(0.0f);

            Location loc = mob.getLocation().add(0, 1.0, 0);
            if (jack) {
                mob.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 40, 0.4, 0.6, 0.4, 0.01);
                mob.getWorld().spawnParticle(Particle.SOUL, loc, 20, 0.4, 0.4, 0.4, 0.0);
            } else {
                Particle.DustOptions orange = new Particle.DustOptions(Color.fromRGB(255,140,0), 1.3f);
                mob.getWorld().spawnParticle(Particle.DUST, loc, 60, 0.5, 0.6, 0.5, 0.0, orange);
            }
            startPumpkinAura(mob);
        }, 5L);
    }

    public ItemStack createHalloweenPresent() {
        ItemStack present = new ItemStack(Material.PUMPKIN);
        ItemMeta meta = present.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Trick or Treat!");
            meta.setLore(Arrays.asList(ChatColor.AQUA + "Right-click to open!", ChatColor.GRAY + "Contains a surprise!"));

            meta.getPersistentDataContainer().set(PRESENT_KEY, PersistentDataType.BYTE, (byte)1);

            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "uniqueID"),
                    PersistentDataType.STRING, java.util.UUID.randomUUID().toString());
            meta.addEnchant(Enchantment.LOOTING, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS, org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);

            present.setItemMeta(meta);
        }
        return present;
    }

    private boolean isHalloweenPresent(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        Byte flag = meta.getPersistentDataContainer().get(PRESENT_KEY, PersistentDataType.BYTE);
        return flag != null && flag == (byte)1;
    }

    @EventHandler
    public void onHalloweenPresentPlace(BlockPlaceEvent e) {
        if (isHalloweenPresent(e.getItemInHand())) {
            e.setCancelled(true);
            plugin.getLogger().info("Present use intercepted; cancelling place");
        }
    }

    private final List<ItemStack> commonRewards = new ArrayList<>(); // Spooky Tier
    private final List<ItemStack> rareRewards = new ArrayList<>(); // Cursed Tier
    private final List<ItemStack> legendaryRewards = new ArrayList<>(); // Wicked Tier

    public void initializeRewards() {
        // ==== Spooky Tier reward list ====
        commonRewards.add(new ItemStack(Material.ROTTEN_FLESH, 2));
        commonRewards.add(new ItemStack(Material.SPIDER_EYE, 2));
        commonRewards.add(new ItemStack(Material.CARVED_PUMPKIN, 1));
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
        org.bukkit.event.block.Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = e.getItem();
        if (!isHalloweenPresent(item)) return;

        e.setUseItemInHand(Event.Result.DENY);
        e.setUseInteractedBlock(Event.Result.DENY);
        e.setCancelled(true);

        Player player = e.getPlayer();

        org.bukkit.inventory.EquipmentSlot hand = e.getHand();
        if (hand == org.bukkit.inventory.EquipmentSlot.HAND) {
            ItemStack main = player.getInventory().getItemInMainHand();
            if (main != null && isHalloweenPresent(main)) {
                if (main.getAmount() <= 1) player.getInventory().setItemInMainHand(null);
                else main.setAmount(main.getAmount() - 1);
            }
        } else {
            ItemStack off = player.getInventory().getItemInOffHand();
            if (off != null && isHalloweenPresent(off)) {
                if (off.getAmount() <= 1) player.getInventory().setItemInOffHand(null);
                else off.setAmount(off.getAmount() - 1);
            }
        }

        Random random = new Random();
        int number = random.nextInt(100);

        if (number < 51 && number > 25) {
            int rewardCount = ThreadLocalRandom.current().nextInt(1, 4);
            List<ItemStack> randomCommonRewards = getCommonRewards(rewardCount);

            for (ItemStack reward : randomCommonRewards) {
                player.getInventory().addItem(reward);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 2.0f, 1.5f);
            }
        }
        if (number < 21 && number > 10) {
            int rewardCount = ThreadLocalRandom.current().nextInt(1, 2);
            List<ItemStack> randomRareRewards = getRareRewards(rewardCount);

            for (ItemStack reward : randomRareRewards) {
                player.getInventory().addItem(reward);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 2.0f, 1.0f);
            }
        }
        if (number == 0) {
            int rewardCount = 1;
            List<ItemStack> randomLegendaryRewards = getLegendaryRewards(rewardCount);

            for (ItemStack reward : randomLegendaryRewards) {
                player.getInventory().addItem(reward);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 2.0f, 0.5f);
            }
        }
    }

    @EventHandler
    public void halloweenEventDeath(EntityDeathEvent e) {

        // GET THE DEFAULT VALUE FOR HALLOWEEN EVENT RULE
        boolean halloweenEnabled = plugin.getConfig().getBoolean("halloween_event", false);

        Random random = new Random();
        int number = random.nextInt(100);
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
