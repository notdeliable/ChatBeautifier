package dev.eymen.smallcaps;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import static dev.eymen.ChatBeautifier.instance;

public class SmallCapsPlaceholderExp extends PlaceholderExpansion {
	@Override
	public String getAuthor() {
		return "Deliable";
	}

	@Override
	public String getIdentifier() {
		return "smallcaps";
	}

	@Override
	public String getVersion() {
		return "2.0.0-STABLE";
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, String params) {
		String parsedParams = PlaceholderAPI.setBracketPlaceholders(player, params);
		String[] givenInput = parsedParams.split("_", 2);
		if (givenInput[0].equalsIgnoreCase("convert")) {
			if (givenInput.length > 1) {
				if (givenInput[1] == null || givenInput[1].trim().isEmpty()) {
					return "No string provided.";
				} else {
					return Alphabet.convert(givenInput[1]);
				}
			} else {
				return "No string provided.";
			}
		}
		return "Invalid or no option provided.";
	}

	public void init() {
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			instance.getLogger().info("PlaceholderAPI found, registering placeholders.");
			if (isRegistered()) {
				instance.getLogger().warning("Couldn't register PlaceholderAPI placeholders, because they're already registered.");
				return;
			}
			register();
			instance.getLogger().info("Registered PlaceholderAPI placeholders.");
		}
	}
}
