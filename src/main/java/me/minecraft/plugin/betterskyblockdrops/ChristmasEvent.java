package me.minecraft.plugin.betterskyblockdrops;

import org.bukkit.*;
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

    public ItemStack createPresent() {
        ItemStack present = new ItemStack(Material.CHEST);
        ItemMeta meta = present.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Present");
            meta.setLore(Arrays.asList(ChatColor.AQUA + "Right-click to open!", ChatColor.GRAY + "What could be inside?"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());

            present.setItemMeta(meta);
        }
        return present;
    }

    public ItemStack createLegendaryPresent() {
        ItemStack present = new ItemStack(Material.CHEST);
        ItemMeta meta = present.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "A Special Present");
            meta.setLore(Arrays.asList(ChatColor.AQUA + "Right-click to open!", ChatColor.GRAY + "There's something special inside!"));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());

            present.setItemMeta(meta);
        }
        return present;
    }

    private boolean isPresent(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(new NamespacedKey(plugin, "uniqueID"), PersistentDataType.STRING);
    }

    private boolean isLegendaryPresent(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(new NamespacedKey(plugin, "uniqueID"), PersistentDataType.STRING);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();

        if (isPresent(item)) {
            e.setCancelled(true);
        }

        if (isLegendaryPresent(item)) {
            e.setCancelled(true);
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
            randomLegendaryRewards.add(getRandomCommonRewards());
        }
        return randomLegendaryRewards;
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && isPresent(item)) {
            e.setCancelled(true);
            player.getInventory().removeItem(item);

            Location loc = player.getLocation().add(0, 1.0, 0);
            World w = player.getWorld();

            Random random = new Random();
            int number = random.nextInt(100);

            if (number < 85) {
                int rewardCount = ThreadLocalRandom.current().nextInt(1, 3);
                List<ItemStack> randomCommonRewards = getCommonRewards(rewardCount);

                for (ItemStack reward : randomCommonRewards) {
                    player.getInventory().addItem(reward);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    Particle.DustOptions red   = new Particle.DustOptions(org.bukkit.Color.fromRGB(200, 30, 30), 1.2f);
                    Particle.DustOptions green = new Particle.DustOptions(org.bukkit.Color.fromRGB(30, 200, 60), 1.2f);
                    w.spawnParticle(Particle.DUST, loc, 25, 0.5, 0.6, 0.5, 0.0, red);
                    w.spawnParticle(Particle.DUST, loc, 25, 0.5, 0.6, 0.5, 0.0, green);
                    w.spawnParticle(Particle.FIREWORK, loc, 20, 0.4, 0.5, 0.4, 0.01);
                }
            } else {
                int rewardCount = ThreadLocalRandom.current().nextInt(1, 2);
                List<ItemStack> randomRareReward = getRareRewards(rewardCount);

                for (ItemStack reward : randomRareReward) {
                    player.getInventory().addItem(reward);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.5f);
                    w.spawnParticle(Particle.FIREWORK, loc, 40, 0.5, 0.6, 0.5, 0.01);
                    w.spawnParticle(Particle.HAPPY_VILLAGER,  loc, 15, 0.4, 0.5, 0.4, 0.0);
                    w.spawnParticle(Particle.END_ROD,         loc, 12, 0.25, 0.35, 0.25, 0.0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractionLegendary(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();

        Location loc = player.getLocation().add(0, 1.0, 0);
        World w = player.getWorld();

        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && isLegendaryPresent(item)) {
            e.setCancelled(true);
            player.getInventory().removeItem(item);

            int rewardCount = ThreadLocalRandom.current().nextInt(1, 1);
            List<ItemStack> randomLegendaryRewards = getLegendaryRewards(rewardCount);

            for (ItemStack reward : randomLegendaryRewards) {
                player.getInventory().addItem(reward);
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, 1.0f, 2.0f);
                w.spawnParticle(Particle.TOTEM_OF_UNDYING, loc, 40, 0.6, 0.7, 0.6, 0.0);
                w.spawnParticle(Particle.FIREWORK,  loc, 60, 0.7, 0.8, 0.7, 0.02);
                w.spawnParticle(Particle.END_ROD,          loc, 20, 0.3, 0.4, 0.3, 0.0);
                w.spawnParticle(Particle.EXPLOSION, loc, 1, 0, 0, 0, 0.0);
            }
        }
    }

    @EventHandler
    public void christmasEventDeath(EntityDeathEvent e) {
        boolean christmasEnabled = plugin.getConfig().getBoolean("christmas_event", true);
        if (!christmasEnabled) return;

        LivingEntity ent = e.getEntity();

        if (ent.getScoreboardTags().contains("santa_claus")) {
            e.getDrops().add(createLegendaryPresent());
            return;
        }

        if (Math.random() < 0.08) {
            e.getDrops().add(createPresent());
        }
    }
}
