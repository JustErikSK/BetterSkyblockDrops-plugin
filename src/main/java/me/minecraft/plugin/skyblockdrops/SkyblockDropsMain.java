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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class SkyblockDropsMain extends JavaPlugin implements Listener {

    private HalloweenEvent halloweenEvent;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "BetterSkyblockDrops >> Plugin has been enabled!");

        this.getServer().getPluginManager().registerEvents(this, this);
        halloweenEvent = new HalloweenEvent(this);
        this.getServer().getPluginManager().registerEvents(halloweenEvent, this);
        saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        config.addDefault("gravel_perc", 10);
        config.addDefault("gravel_amount", 1);
        config.addDefault("gravel_drop", true);
        config.addDefault("sand_perc", 10);
        config.addDefault("sand_amount", 1);
        config.addDefault("sand_drop", true);
        config.addDefault("quartz_perc", 10);
        config.addDefault("quartz_amount", 1);
        config.addDefault("quartz_drop", true);
        config.addDefault("soul_sand_perc", 10);
        config.addDefault("soul_sand_amount", 1);
        config.addDefault("soul_sand_drop", true);
        config.addDefault("ancient_debris_perc", 100);
        config.addDefault("ancient_debris_amount", 4);
        config.addDefault("ancient_debris_drop", true);
        config.addDefault("magma_cream_perc", 10);
        config.addDefault("magma_cream_amount", 1);
        config.addDefault("magma_cream_drop", true);
        config.addDefault("glowstone_perc", 10);
        config.addDefault("glowstone_amount", 1);
        config.addDefault("glowstone_drop", true);
        config.addDefault("halloween_event", false);
        config.addDefault("christmas_event", false);

        initializeRewards();
    }

    public void reloadAll() {
        reloadConfig();
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {

        //  GET DEFAULT VALUES FOR ALL DROPS
        int gravel_perc = this.getConfig().getInt("gravel_perc", 10);
        int gravel_amount = this.getConfig().getInt("gravel_amount", 1);
        String gravel_drop = this.getConfig().getString("gravel_drop", "true");
        int sand_perc = this.getConfig().getInt("sand_perc", 10);
        int sand_amount = this.getConfig().getInt("sand_amount", 1);
        String sand_drop = this.getConfig().getString("sand_drop", "true");
        int quartz_perc = this.getConfig().getInt("quartz_perc", 10);
        int quartz_amount = this.getConfig().getInt("quartz_amount", 1);
        String quartz_drop = this.getConfig().getString("quartz_drop", "true");
        int soul_sand_perc = this.getConfig().getInt("soul_sand_perc", 10);
        int soul_sand_amount = this.getConfig().getInt("soul_sand_amount", 1);
        String soul_sand_drop = this.getConfig().getString("soul_sand_drop", "true");
        int ancient_debris_perc = this.getConfig().getInt("ancient_debris_perc", 100);
        int ancient_debris_amount = this.getConfig().getInt("ancient_debris_amount", 4);
        String ancient_debris_drop = this.getConfig().getString("ancient_debris_drop", "true");
        int magma_cream_perc = this.getConfig().getInt("magma_cream_perc", 10);
        int magma_cream_amount = this.getConfig().getInt("magma_cream_amount", 1);
        String magma_cream_drop = this.getConfig().getString("magma_cream_drop", "true");
        int glowstone_perc = this.getConfig().getInt("glowstone_perc", 10);
        int glowstone_amount = this.getConfig().getInt("glowstone_amount", 1);
        String glowstone_drop = this.getConfig().getString("glowstone_drop", "true");

        LivingEntity ent = e.getEntity();
        Random ran = new Random();

        int num = ran.nextInt(100);

        // DROPS WILL WORK ONLY IF THE DROP OPTION IS SET TO TRUE IN CONFIG.YML
        // IF PERCENTAGE IS MORE THAN 100 OR LESS THAN 1, DEFAULT VALUE WILL BE ACTIVE
        // IF AMOUNT IS MORE THAN 10 OR LESS THAN 1, ONLY VANILLA DROPS WILL BE APPLIED

        if (ent.getType() == EntityType.ZOMBIE && gravel_drop.equals("true")) { // GRAVEL
            if (gravel_perc > 100 || gravel_perc < 1) {
                gravel_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (gravel_amount > 10 || gravel_amount < 1) {
                gravel_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= gravel_perc) {
                e.getDrops().add(new ItemStack(Material.GRAVEL, gravel_amount)); // GRAVEL DROP
            }
        }

        if (ent.getType() == EntityType.HUSK && sand_drop.equals("true")) { // SAND
            if (sand_perc > 100 || sand_perc < 1) {
                sand_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (sand_amount > 10 || sand_amount < 1) {
                sand_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= sand_perc) {
                e.getDrops().add(new ItemStack(Material.SAND, sand_amount)); // SAND DROP
            }
        }

        if (ent.getType() == EntityType.BLAZE && quartz_drop.equals("true")) { // QUARTZ
            if (quartz_perc > 100 || quartz_perc < 1) {
                quartz_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (quartz_amount > 10 || quartz_amount < 1) {
                quartz_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= quartz_perc) {
                e.getDrops().add(new ItemStack(Material.QUARTZ, quartz_amount)); // QUARTZ DROP
            }
        }

        if (ent.getType() == EntityType.WITHER_SKELETON && soul_sand_drop.equals("true")) { // SOUL SAND
            if (soul_sand_perc > 100 || soul_sand_perc < 1) {
                soul_sand_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (soul_sand_amount > 10 || soul_sand_amount < 1) {
                soul_sand_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= soul_sand_perc) {
                e.getDrops().add(new ItemStack(Material.SOUL_SAND, soul_sand_amount)); // SOUL SAND DROP
            }
        }

        if (ent.getType() == EntityType.WITHER && ancient_debris_drop.equals("true")) { // ANCIENT DEBRIS
            if (ancient_debris_perc > 100 || ancient_debris_perc < 1) {
                ancient_debris_perc = 100; // USE DEFAULT PERCENTAGE
            }
            if (ancient_debris_amount > 10 || ancient_debris_amount < 1) {
                ancient_debris_amount = 4; // USE DEFAULT AMOUNT
            }
            if (num <= ancient_debris_perc) {
                e.getDrops().add(new ItemStack(Material.ANCIENT_DEBRIS, ancient_debris_amount)); // ANCIENT DEBRIS DROP
            }
        }

        if (ent.getType() == EntityType.ZOMBIFIED_PIGLIN && magma_cream_drop.equals("true")) { // MAGMA CREAM
            if (magma_cream_perc > 100 || magma_cream_perc < 1) {
                magma_cream_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (magma_cream_amount > 10 || magma_cream_amount < 1) {
                magma_cream_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= magma_cream_perc) {
                e.getDrops().add(new ItemStack(Material.MAGMA_CREAM, magma_cream_amount)); // MAGMA CREAM DROP
            }
        }

        if (ent.getType() == EntityType.SKELETON && glowstone_drop.equals("true")) { // GLOWSTONE
            if (glowstone_perc > 100 || glowstone_perc < 1) {
                glowstone_perc = 10; // USE DEFAULT PERCENTAGE
            }
            if (glowstone_amount > 10 || glowstone_amount < 1) {
                glowstone_amount = 1; // USE DEFAULT AMOUNT
            }
            if (num <= glowstone_perc) {
                e.getDrops().add(new ItemStack(Material.GLOWSTONE_DUST, glowstone_amount)); // GLOWSTONE DROP
            }
        }
    }

    @EventHandler
    public void halloweenEventSpawn(EntitySpawnEvent e) {

        // GET THE DEFAULT VALUE FOR HALLOWEEN EVENT RULE
        String halloween_event = this.getConfig().getString("halloween_event", "false");

        ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
        ItemStack fancy_pumpkin = new ItemStack(Material.JACK_O_LANTERN);

        Random random = new Random();
        int number = random.nextInt(100);

        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity ent = (LivingEntity) e.getEntity();
            if (halloween_event.equals("true")) {
                if (ent.getType() == EntityType.ZOMBIE || ent.getType() == EntityType.HUSK || ent.getType() == EntityType.WITHER_SKELETON || ent.getType() == EntityType.ZOMBIFIED_PIGLIN || ent.getType() == EntityType.SKELETON) {
                    if (number > 60) {
                        Objects.requireNonNull(ent.getEquipment()).setHelmet(pumpkin);
                    }
                    if (number < 10) {
                        Objects.requireNonNull(ent.getEquipment()).setHelmet(fancy_pumpkin);
                    }
                }
            }
        }
    }

    @EventHandler
    public void halloweenEventDeath(EntityDeathEvent e) {

        // GET THE DEFAULT VALUE FOR HALLOWEEN EVENT RULE
        String halloween_event = this.getConfig().getString("halloween_event", "false");

        Random random = new Random();
        int number = random.nextInt(1000);
        LivingEntity ent = e.getEntity();

        if (halloween_event.equals("true")) {
            if (ent.getType() == EntityType.ZOMBIE || ent.getType() == EntityType.HUSK || ent.getType() == EntityType.BLAZE || ent.getType() == EntityType.WITHER_SKELETON || ent.getType() == EntityType.WITHER || ent.getType() == EntityType.ZOMBIFIED_PIGLIN || ent.getType() == EntityType.SKELETON) {
                if (number == 1) {
                    e.getDrops().add(new ItemStack(Material.DIAMOND));
                }
            }
        }
    }

    @EventHandler
    public void christmasEventSpawn(EntitySpawnEvent e) {

        // GET THE DEFAULT VALUE FOR CHRISTMAS EVENT RULE
        String christmas_event = this.getConfig().getString("christmas_event", "false");

        Random random = new Random();

        if (christmas_event.equals("true")) {
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
            data.set(new NamespacedKey(this, "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());

            present.setItemMeta(meta);
        }
        return present;
    }

    private boolean isPresent(ItemStack item) { // isPresent method
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(new NamespacedKey(this, "uniqueID"), PersistentDataType.STRING);
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
        String christmas_event = this.getConfig().getString("christmas_event", "false");
        if (christmas_event.equals("true")) {
            if (Math.random() < 0.1) { // 10%
                e.getDrops().add(createPresent());
            }
        }
    }
}
