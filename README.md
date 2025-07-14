# BsruHubPVP

BsruHubPVP is an advanced Player vs. Player management plugin designed for hub or event servers. It creates a controlled PVP environment where players must actively opt-in to combat by holding a specific item, receiving a full set of enchanted armor to fight. The system is fully configurable, from items and messages to sounds and leaderboards.

---

## ✨ Features

- **Opt-in PVP:** Players enter PVP mode by holding a special sword for a configurable amount of time.
- **Dynamic Armor:** Automatically equips players with a full set of enchanted diamond armor upon entering PVP mode and removes it upon exiting.
- **Controlled Environment:** PVP damage is only possible between players who are both in PVP mode (wearing the special armor).
- **Fully Configurable:** Almost every aspect of the plugin can be customized via `config.yml`, including all messages, sounds, item names, lores, and enchantments.
- **Unbreakable & Bound Items:** The PVP Sword is unbreakable, and the Divine Armor cannot be manually removed during combat.
- **Death Mechanics:** On death, players do not drop their inventory, except for the PVP Sword. Death messages are also suppressed to keep the chat clean.
- **Live Health Display:** Players in combat can see their opponent's health on their action bar.
- **Kill Leaderboard:** Tracks player kills and provides placeholders for leaderboards via PlaceholderAPI.
- **Admin Commands:** Includes commands for getting items and reloading the configuration, complete with tab-completion.

---

## 📋 Requirements

- **Server:** PaperMC 1.21+ (Recommended)
- **Java:** Version 21+
- **Dependencies:**
    - [**PlaceholderAPI**](https://www.spigotmc.org/resources/placeholderapi.6245/): Required for leaderboard placeholders to work.

---

## 🚀 Installation

1.  Download the `BsruHubPVP.jar` file from the [releases page](https://github.com/nattapat2871/BsruHubPVP/releases).
2.  Install **PlaceholderAPI** on your server.
3.  Place the `BsruHubPVP.jar` file into your server's `/plugins` directory.
4.  Start or restart your server to generate the default configuration files.
5.  Customize `config.yml` and `kills.yml` to your liking.
6.  Use `/bsruhubpvp reload` to apply config changes without restarting.

---

## ⚙️ Commands & Permissions

| Command                     | Permission                    | Description                                  |
| --------------------------- | ----------------------------- | -------------------------------------------- |
| `/bsruhubpvp`               | `bsruhubpvp.command.use`      | Displays plugin information. (Default: all)  |
| `/bsruhubpvp getsword`      | `bsruhubpvp.command.getsword` | Gives the user the special PVP Sword. (Default: op) |
| `/bsruhubpvp reload`        | `bsruhubpvp.command.reload`   | Reloads the `config.yml` file. (Default: op) |

---

## 📊 Placeholders

To use these placeholders, you need PlaceholderAPI installed.

-   `%bsruhubpvp_topkill_1%` - Displays the player with the most kills.
-   `%bsruhubpvp_topkill_2%` - Displays the player with the second-most kills.
-   `%bsruhubpvp_topkill_N%` - Displays the Nth player in the kill leaderboard.

The format of the output can be changed in `config.yml`.

---

## 📝 Configuration (`config.yml`)

Below is the default configuration file with detailed explanations for each option.

```yaml
# ------------------------------------
# Configuration for BsruHubPVP
# ------------------------------------
# Placeholders available for messages: %player%, %time%, %kills%
# All messages support '&' color codes.
# Sound names can be found here: [https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html)

# General settings
settings:
  # The duration in seconds for the countdown.
  countdown-seconds: 5

# Item configuration
items:
  sword:
    # The display name of the PVP sword.
    name: "&c&lPVP Sword"
    # Lore (description lines) for the sword.
    lore:
      - "&7A sword for those ready to fight."
    # Enchantments for the sword.
    enchantments:
      sharpness-level: 5
  armor:
    # The display name for all pieces of the special armor.
    name: "&a&lDivine Armor"
    # Lore for the armor pieces.
    lore:
      - "&7Armor forged for the champion"
      - "&7of the arena."
    # Enchantments for the armor.
    enchantments:
      protection-level: 2
      unbreaking-level: 10

# All user-facing messages
messages:
  # A prefix for all chat messages from this plugin.
  prefix: "&8[&bBsruHub&fPVP&8] &r"
  # Message sent when a player lacks permission.
  no-permission: "%prefix%&cYou do not have permission to use this command."
  # Success message for the /getsword command.
  get-sword-success: "%prefix%&aYou have received the &ePVP Sword&a!"
  # Success message for the /reload command.
  reload-success: "%prefix%&aConfiguration has been reloaded."
  
  # Countdown messages shown on the action bar
  countdown:
    equip: "&aEquipping armor in &e%time% &aseconds..."
    equip-cancel: "&cEquip cancelled."
    equip-success: "&bYou have entered PVP mode!"
    unequip: "&eUnequipping armor in &e%time% &eseconds..."
    unequip-cancel: "&aUnequip cancelled."
    unequip-success: "&7You have left PVP mode."
    
  # Messages related to combat
  pvp:
    cant-remove-armor: "&cYou cannot remove this armor in PVP mode."
    attacker-not-ready: "&cYou must be in PVP mode to fight."
    victim-not-ready: "&e%player% is not ready for PVP."
    # Format for the health display. %.1f is for the health number (e.g., 18.5).
    health-display: "&c❤ %.1f &8| &e%player%"

# PlaceholderAPI format
placeholders:
  # The format for the top kill placeholder.
  top-kill-format: "%player% &7- &e%kills% Kills"
  # Text to display when a requested rank is not available.
  top-kill-not-available: "N/A"

# All plugin sounds
sounds:
  countdown-tick: "UI_BUTTON_CLICK"
  equip-success: "ENTITY_PLAYER_LEVELUP"
  unequip-success: "ITEM_ARMOR_EQUIP_GENERIC"

```

---

## 🧑‍💻 Author

Created by **Nattapat2871**.
- **GitHub:** [https://github.com/nattapat2871/BsruHubPVP](https://github.com/nattapat2871/BsruHubPVP)