package dev.eymen;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.eymen.commands.CommandAPICommands;
import dev.eymen.customfont.CustomFontManager;
import dev.eymen.events.ChatEvent;
import dev.eymen.smallcaps.SmallCapsPlaceholderExp;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ChatBeautifier extends JavaPlugin {
    public static ChatBeautifier instance;
    public YamlDocument config;
    public File custom_fonts = new File(getDataFolder(), "custom_fonts");
    public File resource_pack = new File(getDataFolder(), "resource_pack");
    public File resource_pack_assets = new File(resource_pack, "assets");
    public CustomFontManager customFontManager;
    public MiniPlaceholders miniPlaceholders;
    public SmallCapsPlaceholderExp smallCapsPlaceholderExp;
    public CommandAPICommands commandAPICommands;


    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        getLogger().info("Hey there!");
        instance = this;
        try {
            config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.DEFAULT);
            if (!custom_fonts.exists()) {
                custom_fonts.mkdirs();
            }
            if (!resource_pack.exists()) {
                resource_pack.mkdirs();
            }
            if (!resource_pack_assets.exists()) {
                resource_pack_assets.mkdirs();
            }
            getLogger().info("Registered configuration files.");
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().severe("Couldn't register configuration files. More information should be located above.");
        }
        customFontManager = new CustomFontManager();
        miniPlaceholders = new MiniPlaceholders(this);
        smallCapsPlaceholderExp = new SmallCapsPlaceholderExp();
        commandAPICommands = new CommandAPICommands(this);
        if (config.getBoolean("features.custom_fonts")) {
            miniPlaceholders.init();
        }
        smallCapsPlaceholderExp.init();
        getLogger().info("Registered placeholders.");
        CommandAPI.onEnable();
        commandAPICommands.mainCommand.register();
        getLogger().info("Registered commands.");
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        getLogger().info("Registered events.");
        int pluginId = 18409;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("is_custom_font_feature_enabled", () -> String.valueOf(config.getBoolean("features.custom_font"))));
        getLogger().info("Registered bStats.");
        if (config.getBoolean("features.custom_fonts")) {
            boolean isIaEnabledPl = Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");
            boolean isOraxenEnabledPl = Bukkit.getPluginManager().isPluginEnabled("Oraxen");
            Boolean isIaEnabledConfig = config.getBoolean("custom_fonts.auto_resource_pack.itemsadder");
            Boolean isOraxenEnabledConfig = config.getBoolean("custom_fonts.auto_resource_pack.oraxen");

            if (isIaEnabledPl && !isIaEnabledConfig) {
                getLogger().info("Configuring auto resource pack for ItemsAdder...");
                try {
                    config.set("custom_fonts.auto_resource_pack.itemsadder", true);
                    config.save();
                    getLogger().info("Configured auto resource pack for ItemsAdder.");
                } catch (IOException e) {
                    e.printStackTrace();
                    getLogger().severe("Couldn't configure auto resource pack for ItemsAdder, please check the logs above for more information.");
                }
            }
            if (isOraxenEnabledPl && !isOraxenEnabledConfig) {
                getLogger().info("Configuring auto resource pack for Oraxen...");
                try {
                    config.set("custom_fonts.auto_resource_pack.oraxen", true);
                    config.save();
                    getLogger().info("Configured auto resource pack for Oraxen.");
                } catch (IOException e) {
                    e.printStackTrace();
                    getLogger().severe("Couldn't configure auto resource pack for Oraxen, please check the logs above for more information.");
                }
            }

            customFontManager.init();
        }
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        getLogger().info("Good bye!");
    }
}