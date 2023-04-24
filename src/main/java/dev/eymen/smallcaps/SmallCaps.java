package dev.eymen.smallcaps;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmallCaps extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("Hey there!");
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			getLogger().info("PlaceholderAPI found, registering placeholders.");
			new PlaceholderExp().register();
			getLogger().info("Registered placeholders.");
		}
		getCommand("smallcaps").setExecutor(new CapsCommand());
		getLogger().info("Registered commands.");
		int pluginId = 18278;
		Metrics metrics = new Metrics(this, pluginId);
	}

	@Override
	public void onDisable() {
		getLogger().info("Good bye!");
	}
}
