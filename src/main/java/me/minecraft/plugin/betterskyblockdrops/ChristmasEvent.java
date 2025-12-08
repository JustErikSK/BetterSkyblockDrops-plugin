package me.minecraft.plugin.betterskyblockdrops;

import org.bukkit.*;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChristmasEvent implements Listener {

    private final SkyblockDropsMain plugin;
    private final NamespacedKey KEY_PRESENT;
    private final NamespacedKey KEY_TIER;
    private final NamespacedKey KEY_UUID;

    private final Map<UUID, Long> clickCooldown = new HashMap<>();

    public ChristmasEvent(SkyblockDropsMain plugin) {
        this.plugin = plugin;
        this.KEY_PRESENT = new NamespacedKey(plugin, "present_mark");
        this.KEY_TIER = new NamespacedKey(plugin, "present_tier");
        this.KEY_UUID = new NamespacedKey(plugin, "present_uuid");
        initializeRewards();
    }

    @EventHandler
    public void christmasEventSpawn(EntitySpawnEvent e) {
        boolean christmasEnabled = plugin.getConfig().getBoolean("christmas_event", true);

        Random random = new Random();

        if (christmasEnabled) {
            if (e.getEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getEntity();
                if (ent.getType() == EntityType.ZOMBIE || ent.getType() == EntityType.HUSK || ent.getType() == EntityType.WITHER_SKELETON || ent.getType() == EntityType.ZOMBIFIED_PIGLIN || ent.getType() == EntityType.SKELETON) {
                    int number1 = random.nextInt(100);
                    if (number1 > 85) {
                        ItemStack orangeHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
                        LeatherArmorMeta orangeHelmetMeta = (LeatherArmorMeta) orangeHelmet.getItemMeta();
                        orangeHelmetMeta.setColor(Color.fromRGB(245, 116, 2));
                        orangeHelmet.setItemMeta(orangeHelmetMeta);
                        Objects.requireNonNull(ent.getEquipment()).setHelmet(orangeHelmet);
                    }
                    if (number1 < 25) {
                        ItemStack greenHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
                        LeatherArmorMeta greenHelmetMeta = (LeatherArmorMeta) greenHelmet.getItemMeta();
                        greenHelmetMeta.setColor(Color.fromRGB(102, 227, 48));
                        greenHelmet.setItemMeta(greenHelmetMeta);
                        Objects.requireNonNull(ent.getEquipment()).setHelmet(greenHelmet);
                    }
                    if (number1 == 0 && ent.getType() == EntityType.ZOMBIE) {
                        ItemStack redHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
                        LeatherArmorMeta redHelmetMeta = (LeatherArmorMeta) redHelmet.getItemMeta();
                        redHelmetMeta.setColor(Color.fromRGB(227, 34, 34));
                        redHelmet.setItemMeta(redHelmetMeta);
                        Objects.requireNonNull(ent.getEquipment()).setHelmet(redHelmet);
                        ent.setCustomName("§cSanta Claus");
                        ent.setCustomNameVisible(true);
                        ent.setRemoveWhenFarAway(false);
                        ent.addScoreboardTag("santa_claus");
                    }
                }
            }
            if (e.getEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getEntity();
                if (ent.getType() == EntityType.ZOMBIE || ent.getType() == EntityType.HUSK || ent.getType() == EntityType.WITHER_SKELETON || ent.getType() == EntityType.ZOMBIFIED_PIGLIN || ent.getType() == EntityType.SKELETON) {
                    int number2 = random.nextInt(100);
                    if (number2 < 30) {
                        ItemStack candies = new ItemStack(Material.SWEET_BERRIES, 1);
                        ItemMeta itemStackMeta = candies.getItemMeta();
                        itemStackMeta.setDisplayName(ChatColor.DARK_PURPLE + "Sweet Candies");
                        candies.setItemMeta(itemStackMeta);
                        Objects.requireNonNull(ent.getEquipment()).setItemInMainHand(candies);
                    }
                    if (number2 < 15) {
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

    public ItemStack createPresent(int tier) {
        ItemStack present = new ItemStack(Material.CHEST);
        ItemMeta meta = present.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Present");
            meta.setLore(Arrays.asList(ChatColor.AQUA + "Right-click to open!", ChatColor.GRAY + "What could be inside?"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(KEY_PRESENT, PersistentDataType.BYTE, (byte)1);
            data.set(KEY_TIER, PersistentDataType.BYTE, (byte)Math.max(0, Math.min(2, tier)));
            data.set(KEY_UUID, PersistentDataType.STRING, UUID.randomUUID().toString());

            meta.setEnchantmentGlintOverride(true);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

            present.setItemMeta(meta);
        }
        return present;
    }

    public ItemStack createCommonPresent() {return createPresent(0);}
    public ItemStack createRarePresent() {return createPresent(1);}
    public ItemStack createLegendaryPresent() {return createPresent(2);}

    private boolean isPresent(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        Byte mark = meta.getPersistentDataContainer().get(KEY_PRESENT, PersistentDataType.BYTE);
        return mark != null && mark == (byte)1;
    }

    private int getPresentTier(ItemStack item) {
        if (!isPresent(item)) return -1;
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        Byte t = meta.getPersistentDataContainer().get(KEY_TIER, PersistentDataType.BYTE);
        return (t == null) ? 0 : Math.max(0, Math.min(2, t));
    }

    private boolean onCooldown(Player p) {
        long now = System.currentTimeMillis();
        Long last = clickCooldown.get(p.getUniqueId());
        if (last != null && (now - last) < 150L) return true;
        clickCooldown.put(p.getUniqueId(), now);
        return false;
    }

    private void playPresentParticles(Player p, int tier) {
        Location loc = p.getLocation().add(0, 1.0, 0);
        World w = p.getWorld();
        switch (tier) {
            case 0 -> {
                Particle.DustOptions red = new Particle.DustOptions(org.bukkit.Color.fromRGB(200, 30, 30), 1.2f);
                Particle.DustOptions green = new Particle.DustOptions(org.bukkit.Color.fromRGB(30, 200, 60), 1.2f);
                w.spawnParticle(Particle.DUST, loc, 25, 0.5, 0.6, 0.5, 0.0, red);
                w.spawnParticle(Particle.DUST, loc, 25, 0.5, 0.6, 0.5, 0.0, green);
                w.spawnParticle(Particle.FIREWORK, loc, 20, 0.4, 0.5, 0.4, 0.01);
            }
            case 1 -> {
                w.spawnParticle(Particle.FIREWORK, loc, 40, 0.5, 0.6, 0.5, 0.01);
                w.spawnParticle(Particle.HAPPY_VILLAGER, loc, 15, 0.4, 0.5, 0.4, 0.0);
                w.spawnParticle(Particle.END_ROD, loc, 12, 0.25, 0.35, 0.25, 0.0);
            }
            case 2 -> {
                w.spawnParticle(Particle.TOTEM_OF_UNDYING, loc, 40, 0.6, 0.7, 0.6, 0.0);
                w.spawnParticle(Particle.FIREWORK,  loc, 60, 0.7, 0.8, 0.7, 0.02);
                w.spawnParticle(Particle.END_ROD,          loc, 20, 0.3, 0.4, 0.3, 0.0);
                w.spawnParticle(Particle.EXPLOSION, loc, 1, 0, 0, 0, 0.0);
            }
        }
    }

    private void giveTo(Player p, ItemStack item) {
        Map<Integer, ItemStack> overflow = p.getInventory().addItem(item);
        overflow.values().forEach(it -> p.getWorld().dropItemNaturally(p.getLocation(), it));
    }

    @EventHandler
    public void onPresentPlace(BlockPlaceEvent e) {
        if (isPresent(e.getItemInHand())) e.setCancelled(true);
    }

    @EventHandler
    public void onPresentUse(PlayerInteractEvent e) {
        Action a = e.getAction();
        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;

        Player p = e.getPlayer();
        EquipmentSlot hand = e.getHand();
        ItemStack held = (hand == EquipmentSlot.HAND)
                ? p.getInventory().getItemInMainHand()
                : p.getInventory().getItemInOffHand();

        if (!isPresent(held)) return;
        if (onCooldown(p)) return;

        e.setUseItemInHand(Event.Result.DENY);
        e.setUseInteractedBlock(Event.Result.DENY);
        e.setCancelled(true);

        int tier = getPresentTier(held);

        if (held.getAmount() <= 1) {
            if (hand == EquipmentSlot.HAND) p.getInventory().setItemInMainHand(null);
            else                             p.getInventory().setItemInOffHand(null);
        } else {
            held.setAmount(held.getAmount() - 1);
            if (hand == EquipmentSlot.HAND) p.getInventory().setItemInMainHand(held);
            else                             p.getInventory().setItemInOffHand(held);
        }

        // rewards by tier
        switch (tier) {
            case 2 -> openLegendary(p);
            case 1 -> openRare(p);
            default -> openCommon(p);
        }
    }

    private final List<ItemStack> commonRewards = new ArrayList<>();
    private final List<ItemStack> rareRewards = new ArrayList<>();
    private final List<ItemStack> legendaryRewards = new ArrayList<>();

    public void initializeRewards() {
        commonRewards.add(new ItemStack(Material.SNOWBALL, 8));
        commonRewards.add(new ItemStack(Material.COOKIE, 3));
        commonRewards.add(new ItemStack(Material.SWEET_BERRIES, 2));
        commonRewards.add(new ItemStack(Material.HONEY_BOTTLE, 1));
        commonRewards.add(new ItemStack(Material.GLOW_BERRIES, 2));
        commonRewards.add(new ItemStack(Material.COAL, 4));
        commonRewards.add(new ItemStack(Material.EMERALD, 1));
        commonRewards.add(new ItemStack(Material.PACKED_ICE, 3));
        commonRewards.add(new ItemStack(Material.SPRUCE_SAPLING, 1));
        commonRewards.add(new ItemStack(Material.GOLD_NUGGET, 2));
        commonRewards.add(new ItemStack(Material.IRON_NUGGET, 2));
        commonRewards.add(new ItemStack(Material.PUMPKIN_PIE, 1));
        commonRewards.add(new ItemStack(Material.AMETHYST_SHARD, 2));

        rareRewards.add(new ItemStack(Material.GOLDEN_CARROT, 4));
        rareRewards.add(new ItemStack(Material.LEAD, 3));
        rareRewards.add(new ItemStack(Material.POWDER_SNOW_BUCKET, 1));
        rareRewards.add(new ItemStack(Material.ICE, 12));
        rareRewards.add(new ItemStack(Material.TURTLE_SCUTE, 2));
        rareRewards.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 8));
        rareRewards.add(new ItemStack(Material.RED_DYE, 6));
        rareRewards.add(new ItemStack(Material.GREEN_DYE, 6));
        rareRewards.add(new ItemStack(Material.DARK_OAK_SAPLING, 2));

        legendaryRewards.add(new ItemStack(Material.NAME_TAG, 3));
        legendaryRewards.add(new ItemStack(Material.NETHER_STAR, 1));
        legendaryRewards.add(new ItemStack(Material.TRIDENT, 1));
        legendaryRewards.add(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
        legendaryRewards.add(new ItemStack(Material.GOLDEN_APPLE, 2));
        legendaryRewards.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
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

    private static final ThreadLocalRandom RNG = ThreadLocalRandom.current();

    private ItemStack pickOne(List<ItemStack> pool) {
        if (pool == null || pool.isEmpty()) return null;
        int i = RNG.nextInt(pool.size());
        return pool.get(i).clone();
    }

    private List<ItemStack> pickMany(List<ItemStack> pool, int count) {
        List<ItemStack> out = new ArrayList<>(Math.max(0, count));
        if (pool == null || pool.isEmpty() || count <= 0) return out;
        for (int k = 0; k < count; k++) out.add(pickOne(pool));
        return out;
    }

    private void openCommon(Player p) {
        int count = RNG.nextInt(1, 3);
        List<ItemStack> items = pickMany(commonRewards, count);
        if (items.isEmpty()) items = List.of(new ItemStack(Material.COOKIE, 2));
        items.forEach(it -> giveTo(p, it));
        playPresentParticles(p, 0);
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
    }

    private void openRare(Player p) {
        List<ItemStack> items = pickMany(rareRewards, 1);
        if (items.isEmpty()) items = List.of(new ItemStack(Material.GOLDEN_CARROT, 2));
        items.forEach(it -> giveTo(p, it));
        playPresentParticles(p, 1);
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1.5f);
    }

    private void openLegendary(Player p) {
        ItemStack it = pickOne(legendaryRewards);
        if (it == null) it = new ItemStack(Material.DIAMOND);
        giveTo(p, it);
        playPresentParticles(p, 2);
        p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1f);
    }

    @EventHandler
    public void christmasEventDeath(EntityDeathEvent e) {
        boolean christmasEnabled = plugin.getConfig().getBoolean("christmas_event", true);
        if (!christmasEnabled) return;

        LivingEntity ent = e.getEntity();

        if (ent.getScoreboardTags().contains("santa_claus")) {
            e.getDrops().add(createLegendaryPresent());
            plugin.getLogger().info("Legendary Present");
            return;
        }

        if (Math.random() < 0.08) {
            e.getDrops().add(createCommonPresent());
            plugin.getLogger().info("Common Present");
        }

        if (Math.random() > 0.08 && Math.random() < 0.15) {
            e.getDrops().add(createRarePresent());
            plugin.getLogger().info("Rare Present");
        }
    }
}
