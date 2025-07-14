package com.bsruHubPVP;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class BsruHubPVP extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    // (ส่วนของตัวแปรทั้งหมดเหมือนเดิม)
    private int countdownSeconds;
    private String pvpSwordName, divineArmorName;
    private List<String> pvpSwordLore, divineArmorLore;
    private int sharpnessLevel, protectionLevel, unbreakingLevel;
    private String msgPrefix, msgNoPerms, msgGetSword, msgReload, msgEquipCountdown, msgEquipCancel, msgEquipSuccess;
    private String msgUnequipCountdown, msgUnequipCancel, msgUnequipSuccess, msgCantRemove, msgAttackerNotReady;
    private String msgVictimNotReady, msgHealthDisplay, msgTopKillFormat, msgTopKillNotAvailable;
    private Sound soundTick, soundEquipSuccess, soundUnequipSuccess;
    private final Map<UUID, BukkitTask> playerTasks = new HashMap<>();
    private Map<UUID, Integer> playerKills = new HashMap<>();
    private File killsFile;
    private FileConfiguration killsConfig;

    // (onEnable, onDisable และ Event Handlers ส่วนใหญ่เหมือนเดิม)
    @Override
    public void onEnable() {
        loadConfigValues();
        loadKills();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("bsruhubpvp").setExecutor(this);
        getCommand("bsruhubpvp").setTabCompleter(this);
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPIExpansion(this).register();
            getLogger().info("Successfully hooked into PlaceholderAPI!");
        } else {
            getLogger().warning("PlaceholderAPI not found, leaderboards will not work.");
        }
        getLogger().info("BsruHubPVP has been enabled!");
    }
    @Override
    public void onDisable() {
        playerTasks.values().forEach(BukkitTask::cancel);
        playerTasks.clear();
        saveKills();
        getLogger().info("BsruHubPVP has been disabled!");
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Optional.ofNullable(event.getPlayer().getKiller()).ifPresent(killer ->
                playerKills.put(killer.getUniqueId(), playerKills.getOrDefault(killer.getUniqueId(), 0) + 1));
        List<ItemStack> swordToDrop = event.getDrops().stream().filter(this::isPvpSword).toList();
        event.getDrops().clear();
        event.getDrops().addAll(swordToDrop);
    }

    // --- แก้ไขลอจิกในเมธอดนี้ทั้งหมด ---
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        ItemStack newItem = inventory.getItem(event.getNewSlot());
        ItemStack oldItem = inventory.getItem(event.getPreviousSlot());

        boolean isNowHoldingSword = isPvpSword(newItem);
        boolean wasHoldingSword = isPvpSword(oldItem);

        // ถ้าไม่มีการเปลี่ยนแปลงเกี่ยวกับการถือดาบ ก็ไม่ต้องทำอะไรเลย
        if (isNowHoldingSword == wasHoldingSword) {
            return;
        }

        // ยกเลิก Task เก่าที่อาจจะค้างอยู่เสมอ
        cancelPlayerTask(player.getUniqueId());

        if (isNowHoldingSword) {
            // กรณี: เริ่มถือดาบ (และยังไม่ได้ใส่เกราะ) -> เริ่มนับเวลา "สวม"
            if (!isWearingFullDivineArmor(player)) {
                startEquipCountdown(player);
            }
        } else {
            // กรณี: เลิกถือดาบ (และกำลังใส่เกราะอยู่) -> เริ่มนับเวลา "ถอด"
            if (isWearingFullDivineArmor(player)) {
                startUnequipCountdown(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim && event.getDamager() instanceof Player damager)) return;
        if (!isWearingFullDivineArmor(damager)) {
            sendActionBar(damager, msgAttackerNotReady);
            event.setCancelled(true);
            return;
        }
        if (!isWearingFullDivineArmor(victim)) {
            sendActionBar(damager, msgVictimNotReady.replace("%player%", victim.getName()));
            event.setCancelled(true);
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                String damagerView = msgHealthDisplay.replace("%player%", victim.getName());
                damagerView = String.format(Locale.US, damagerView, victim.getHealth());
                String victimView = msgHealthDisplay.replace("%player%", damager.getName());
                victimView = String.format(Locale.US, victimView, damager.getHealth());
                sendActionBar(damager, damagerView);
                sendActionBar(victim, victimView);
            }
        }.runTaskLater(this, 1L);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() != SlotType.ARMOR || !(event.getWhoClicked() instanceof Player player)) return;
        if (isDivineArmor(event.getCurrentItem())) {
            event.setCancelled(true);
            sendActionBar(player, msgCantRemove);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cancelPlayerTask(event.getPlayer().getUniqueId());
    }

    // (โค้ดส่วนอื่นๆ ที่เหลือทั้งหมดเหมือนเดิม ไม่มีการเปลี่ยนแปลง)
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendPluginInfo(sender);
            return true;
        }
        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "getsword":
                handleGetSwordCommand(sender);
                break;
            case "reload":
                handleReloadCommand(sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /bsruhubpvp for info.");
                break;
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subcommands = new ArrayList<>();
            if (sender.hasPermission("bsruhubpvp.command.getsword")) {
                subcommands.add("getsword");
            }
            if (sender.hasPermission("bsruhubpvp.command.reload")) {
                subcommands.add("reload");
            }
            return StringUtil.copyPartialMatches(args[0], subcommands, new ArrayList<>());
        }
        return Collections.emptyList();
    }
    private void handleGetSwordCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command must be run by a player.");
            return;
        }
        if (!player.hasPermission("bsruhubpvp.command.getsword")) {
            player.sendMessage(msgNoPerms.replace("%prefix%", msgPrefix));
            return;
        }
        ItemStack pvpSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = pvpSword.getItemMeta();
        meta.setDisplayName(pvpSwordName);
        meta.setLore(pvpSwordLore);
        meta.addEnchant(Enchantment.SHARPNESS, sharpnessLevel, true);
        meta.setUnbreakable(true);
        pvpSword.setItemMeta(meta);
        player.getInventory().addItem(pvpSword);
        player.sendMessage(msgGetSword.replace("%prefix%", msgPrefix));
    }
    private void handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("bsruhubpvp.command.reload")) {
            sender.sendMessage(msgNoPerms.replace("%prefix%", msgPrefix));
            return;
        }
        reloadConfig();
        loadConfigValues();
        sender.sendMessage(msgReload.replace("%prefix%", msgPrefix));
    }
    private void sendPluginInfo(CommandSender sender) {
        sender.sendMessage("§8§m---------------------------------------------");
        sender.sendMessage("  §b§lBsruHub§f§lPVP §fv" + getPluginMeta().getVersion());
        sender.sendMessage("  §7Advanced PVP system for Hub servers.");
        sender.sendMessage("");
        sender.sendMessage("  §eAuthor: §f" + getPluginMeta().getAuthors().get(0));
        sender.sendMessage("  §eGitHub: §fhttps://github.com/nattapat2871/BsruHubPVP");
        sender.sendMessage("");
        sender.sendMessage("  §6Commands:");
        sender.sendMessage("  §f/bsruhubpvp getsword §7- Get the PVP Sword.");
        sender.sendMessage("  §f/bsruhubpvp reload §7- Reload the configuration.");
        sender.sendMessage("§8§m---------------------------------------------");
    }
    private void startEquipCountdown(Player player) {
        UUID playerUUID = player.getUniqueId();
        BukkitTask task = new BukkitRunnable() {
            private int countdown = countdownSeconds;
            @Override
            public void run() {
                if (!isPvpSword(player.getInventory().getItemInMainHand())) {
                    cancelPlayerTask(playerUUID);
                    sendActionBar(player, msgEquipCancel);
                    return;
                }
                if (countdown > 0) {
                    sendActionBar(player, msgEquipCountdown.replace("%time%", String.valueOf(countdown)));
                    player.playSound(player.getLocation(), soundTick, 1.0f, 1.0f);
                    countdown--;
                } else {
                    equipPlayerWithArmor(player);
                    sendActionBar(player, msgEquipSuccess);
                    player.playSound(player.getLocation(), soundEquipSuccess, 1.0f, 1.0f);
                    cancelPlayerTask(playerUUID);
                }
            }
        }.runTaskTimer(this, 0L, 20L);
        playerTasks.put(playerUUID, task);
    }
    private void startUnequipCountdown(Player player) {
        UUID playerUUID = player.getUniqueId();
        BukkitTask task = new BukkitRunnable() {
            private int countdown = countdownSeconds;
            @Override
            public void run() {
                if (isPvpSword(player.getInventory().getItemInMainHand())) {
                    cancelPlayerTask(playerUUID);
                    sendActionBar(player, msgUnequipCancel);
                    return;
                }
                if (countdown > 0) {
                    sendActionBar(player, msgUnequipCountdown.replace("%time%", String.valueOf(countdown)));
                    player.playSound(player.getLocation(), soundTick, 1.0f, 0.8f);
                    countdown--;
                } else {
                    removeDivineArmor(player);
                    sendActionBar(player, msgUnequipSuccess);
                    player.playSound(player.getLocation(), soundUnequipSuccess, 1.0f, 1.0f);
                    cancelPlayerTask(playerUUID);
                }
            }
        }.runTaskTimer(this, 0L, 20L);
        playerTasks.put(playerUUID, task);
    }
    private void equipPlayerWithArmor(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(createEnchantedArmor(Material.DIAMOND_HELMET));
        inv.setChestplate(createEnchantedArmor(Material.DIAMOND_CHESTPLATE));
        inv.setLeggings(createEnchantedArmor(Material.DIAMOND_LEGGINGS));
        inv.setBoots(createEnchantedArmor(Material.DIAMOND_BOOTS));
    }
    private void removeDivineArmor(Player player) {
        PlayerInventory inv = player.getInventory();
        if (isDivineArmor(inv.getHelmet())) inv.setHelmet(null);
        if (isDivineArmor(inv.getChestplate())) inv.setChestplate(null);
        if (isDivineArmor(inv.getLeggings())) inv.setLeggings(null);
        if (isDivineArmor(inv.getBoots())) inv.setBoots(null);
    }
    private ItemStack createEnchantedArmor(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(divineArmorName);
        meta.setLore(divineArmorLore);
        meta.addEnchant(Enchantment.PROTECTION, protectionLevel, true);
        meta.addEnchant(Enchantment.UNBREAKING, unbreakingLevel, true);
        item.setItemMeta(meta);
        return item;
    }
    private boolean isPvpSword(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(pvpSwordName);
    }
    private boolean isDivineArmor(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(divineArmorName);
    }
    private boolean isWearingFullDivineArmor(Player player) {
        return Arrays.stream(player.getInventory().getArmorContents()).allMatch(this::isDivineArmor);
    }
    private void cancelPlayerTask(UUID playerUUID) {
        Optional.ofNullable(playerTasks.remove(playerUUID)).ifPresent(BukkitTask::cancel);
    }
    private void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
    public Map<UUID, Integer> getKillsMap() { return this.playerKills; }
    public String getMsgTopKillFormat() { return msgTopKillFormat; }
    public String getMsgTopKillNotAvailable() { return msgTopKillNotAvailable; }
    private void loadConfigValues() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        countdownSeconds = config.getInt("settings.countdown-seconds", 5);
        pvpSwordName = translateColors(config.getString("items.sword.name"));
        pvpSwordLore = translateColors(config.getStringList("items.sword.lore"));
        sharpnessLevel = config.getInt("items.sword.enchantments.sharpness-level");
        divineArmorName = translateColors(config.getString("items.armor.name"));
        divineArmorLore = translateColors(config.getStringList("items.armor.lore"));
        protectionLevel = config.getInt("items.armor.enchantments.protection-level");
        unbreakingLevel = config.getInt("items.armor.enchantments.unbreaking-level");
        msgPrefix = translateColors(config.getString("messages.prefix"));
        msgNoPerms = translateColors(config.getString("messages.no-permission"));
        msgGetSword = translateColors(config.getString("messages.get-sword-success"));
        msgReload = translateColors(config.getString("messages.reload-success", "%prefix%&aConfiguration reloaded successfully."));
        msgEquipCountdown = translateColors(config.getString("messages.countdown.equip"));
        msgEquipCancel = translateColors(config.getString("messages.countdown.equip-cancel"));
        msgEquipSuccess = translateColors(config.getString("messages.countdown.equip-success"));
        msgUnequipCountdown = translateColors(config.getString("messages.countdown.unequip"));
        msgUnequipCancel = translateColors(config.getString("messages.countdown.unequip-cancel"));
        msgUnequipSuccess = translateColors(config.getString("messages.countdown.unequip-success"));
        msgCantRemove = translateColors(config.getString("messages.pvp.cant-remove-armor"));
        msgAttackerNotReady = translateColors(config.getString("messages.pvp.attacker-not-ready"));
        msgVictimNotReady = translateColors(config.getString("messages.pvp.victim-not-ready"));
        msgHealthDisplay = translateColors(config.getString("messages.pvp.health-display"));
        msgTopKillFormat = translateColors(config.getString("placeholders.top-kill-format"));
        msgTopKillNotAvailable = translateColors(config.getString("placeholders.top-kill-not-available"));
        try {
            soundTick = Sound.valueOf(config.getString("sounds.countdown-tick", "UI_BUTTON_CLICK").toUpperCase());
            soundEquipSuccess = Sound.valueOf(config.getString("sounds.equip-success", "ENTITY_PLAYER_LEVELUP").toUpperCase());
            soundUnequipSuccess = Sound.valueOf(config.getString("sounds.unequip-success", "ITEM_ARMOR_EQUIP_GENERIC").toUpperCase());
        } catch (IllegalArgumentException e) {
            getLogger().severe("Invalid sound name in config.yml! Using default sounds.");
            soundTick = Sound.UI_BUTTON_CLICK;
            soundEquipSuccess = Sound.ENTITY_PLAYER_LEVELUP;
            soundUnequipSuccess = Sound.ITEM_ARMOR_EQUIP_GENERIC;
        }
    }
    private List<String> translateColors(List<String> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                .map(this::translateColors)
                .collect(Collectors.toList());
    }
    private String translateColors(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    private void loadKills() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        killsFile = new File(getDataFolder(), "kills.yml");
        if (!killsFile.exists()) {
            try {
                killsFile.createNewFile();
            } catch (IOException e) { e.printStackTrace(); }
        }
        killsConfig = YamlConfiguration.loadConfiguration(killsFile);
        Optional.ofNullable(killsConfig.getConfigurationSection("kills")).ifPresent(section ->
                section.getKeys(false).forEach(uuidStr ->
                        playerKills.put(UUID.fromString(uuidStr), section.getInt(uuidStr))));
    }
    private void saveKills() {
        if (killsFile == null || killsConfig == null) return;
        killsConfig.set("kills", null);
        playerKills.forEach((uuid, kills) -> killsConfig.set("kills." + uuid.toString(), kills));
        try {
            killsConfig.save(killsFile);
        } catch (IOException e) { e.printStackTrace(); }
    }
}