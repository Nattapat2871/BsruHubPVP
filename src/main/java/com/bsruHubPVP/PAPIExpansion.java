package com.bsruHubPVP;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PAPIExpansion extends PlaceholderExpansion {

    private final BsruHubPVP plugin;

    public PAPIExpansion(BsruHubPVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bsruhubpvp";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getPluginMeta().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (identifier.startsWith("topkill_")) {
            try {
                int rank = Integer.parseInt(identifier.substring("topkill_".length()));
                if (rank <= 0) return "Invalid Rank";

                Map<UUID, Integer> sortedKills = plugin.getKillsMap().entrySet().stream()
                        .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                if (rank > sortedKills.size()) {
                    return plugin.getMsgTopKillNotAvailable();
                }

                Map.Entry<UUID, Integer> entry = (Map.Entry<UUID, Integer>) sortedKills.entrySet().toArray()[rank - 1];
                OfflinePlayer topPlayer = plugin.getServer().getOfflinePlayer(entry.getKey());
                int kills = entry.getValue();

                String playerName = topPlayer.getName() != null ? topPlayer.getName() : "Unknown";

                return plugin.getMsgTopKillFormat()
                        .replace("%player%", playerName)
                        .replace("%kills%", String.valueOf(kills));

            } catch (NumberFormatException e) {
                return "Invalid Number";
            }
        }
        return null;
    }
}