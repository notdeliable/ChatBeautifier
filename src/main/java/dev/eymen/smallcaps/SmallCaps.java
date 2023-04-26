package dev.eymen.smallcaps;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SmallCaps extends JavaPlugin {
	public YamlDocument config;
	public static SmallCaps instance;

	@Override
	public void onEnable() {
		getLogger().info("Hey there!");
		instance = this;
		try {
			config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
					GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.DEFAULT);
			getLogger().info("Registered configuration files.");
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().severe("Couldn't register configuration files. More information should be located above.");
		}
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			getLogger().info("PlaceholderAPI found, registering placeholders.");
			new PlaceholderExp().register();
			getLogger().info("Registered placeholders.");
		}
		getCommand("smallcaps").setExecutor(new CapsCommand());
		getLogger().info("Registered commands.");
		Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
		getLogger().info("Registered events.");
		int pluginId = 18278;
		Metrics metrics = new Metrics(this, pluginId);
		getLogger().info("Registered bStats.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Good bye!");
	}
}
