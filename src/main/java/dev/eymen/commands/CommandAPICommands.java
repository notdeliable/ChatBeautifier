package dev.eymen.commands;

import dev.eymen.ChatBeautifier;
import dev.eymen.customfont.CustomFontManager;
import dev.eymen.smallcaps.Alphabet;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;

import static dev.eymen.Utils.mm;
public class CommandAPICommands {
    ChatBeautifier instance;

    /*
     *
     * MAIN COMMAND START
     *
     */

    public CommandAPICommands(ChatBeautifier instance) {
        this.instance = instance;
    }

    public CommandTree mainCommand = new CommandTree("chatbeautifier")
            .withPermission("chatbeautifier.command.main")
            .executes((sender, args) -> {
                sender.sendMessage(mm.deserialize("<gradient:#FBF91A:#FBA83B><bold>" + Alphabet.convert("CHATBEAUTIFIER - HELP")));
                sender.sendMessage("\n\n");
                sender.sendMessage(mm.deserialize("<gradient:#FBF91A:#FBA83B>/chatbeautifier reload: Reload the plugin."));
                sender.sendMessage(mm.deserialize("<gradient:#FBF91A:#FBA83B>/chatbeautifier send <player> <type> [font] <text>: Send a formatted message to player."));
                sender.sendMessage(mm.deserialize("<gradient:#FBF91A:#FBA83B>/chatbeautifier toggle <option>: Toggle an option from the plugin."));
            })
            .then(new LiteralArgument("reload")
                    .withPermission("chatbeautifier.command.reload")
                    .executes((sender, args) -> {
                        try {
                            instance.config.reload();
                            sender.sendMessage(mm.deserialize("<green>Reloaded config!"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (instance.config.getBoolean("features.custom_fonts")) {
                            Boolean isIaEnabledPl = Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");
                            Boolean isOraxenEnabledPl = Bukkit.getPluginManager().isPluginEnabled("Oraxen");
                            Boolean isIaEnabledConfig = instance.config.getBoolean("custom_fonts.auto_resource_pack.itemsadder");
                            Boolean isOraxenEnabledConfig = instance.config.getBoolean("custom_fonts.auto_resource_pack.oraxen");

                            if (isIaEnabledPl && !isIaEnabledConfig) {
                                instance.getLogger().info("Configuring auto resource pack for ItemsAdder...");
                                try {
                                    instance.config.set("custom_fonts.auto_resource_pack.itemsadder", true);
                                    instance.config.save();
                                    instance.getLogger().info("Configured auto resource pack for ItemsAdder.");
                                    sender.sendMessage(mm.deserialize("<green>Configured auto resource pack for ItemsAdder!"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    sender.sendMessage(mm.deserialize("<red>Couldn't configure auto resource pack for ItemsAdder, please check the logs for more information."));
                                }
                            }
                            if (isOraxenEnabledPl && !isOraxenEnabledConfig) {
                                instance.getLogger().info("Configuring auto resource pack for Oraxen...");
                                try {
                                    instance.config.set("custom_fonts.auto_resource_pack.oraxen", true);
                                    instance.config.save();
                                    instance.getLogger().info("Configured auto resource pack for Oraxen.");
                                    sender.sendMessage(mm.deserialize("<green>Configured auto resource pack for Oraxen!"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    sender.sendMessage(mm.deserialize("<red>Couldn't configure auto resource pack for Oraxen, please check the logs for more information."));
                                }
                            }
                            instance.customFontManager.init();
                            sender.sendMessage(mm.deserialize("<green>Reloaded resource pack!"));
                            instance.miniPlaceholders.init();
                            sender.sendMessage(mm.deserialize("<green>Reloaded MiniPlaceholders placeholders!"));
                        }
                        instance.smallCapsPlaceholderExp.init();
                        sender.sendMessage(mm.deserialize("<green>Reloaded PlaceholderAPI placeholders!"));
                    })
            )
            .then(new LiteralArgument("toggle")
                    .withPermission("chatbeautifier.command.toggle")
                    .then(new LiteralArgument("small_caps_chat")
                            .executes((sender, args) -> {
                                try {
                                    instance.config.set("features.small_caps_chat", !instance.config.getBoolean("features.small_caps_chat"));
                                    instance.config.save();
                                    sender.sendMessage(instance.config.getBoolean("features.small_caps_chat") ? mm.deserialize("<green>Enabled <yellow>small_caps_chat<green>!") : mm.deserialize("<red>Disabled <yellow>small_caps_chat<red>!"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            })
                    )
                    .then(new LiteralArgument("small_caps_keywords")
                            .executes((sender, args) -> {
                                try {
                                    instance.config.set("features.small_caps_keywords", !instance.config.getBoolean("features.small_caps_keywords"));
                                    instance.config.save();
                                    sender.sendMessage(instance.config.getBoolean("features.small_caps_keywords") ? mm.deserialize("<green>Enabled <yellow>small_caps_keywords<green>!") : mm.deserialize("<red>Disabled <yellow>small_caps_keywords<red>!"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            })
                    )
                    .then(new LiteralArgument("custom_fonts")
                            .executes((sender, args) -> {
                                try {
                                    instance.config.set("features.custom_fonts", !instance.config.getBoolean("features.custom_fonts"));
                                    instance.config.save();
                                    sender.sendMessage(instance.config.getBoolean("features.custom_fonts") ? mm.deserialize("<green>Enabled <yellow>custom_fonts<green>!") : mm.deserialize("<red>Disabled <yellow>custom_fonts<red>!"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            })
                    )
            )
            .then(new LiteralArgument("send")
                    .withPermission("chatbeautifier.command.send")
                    .then(new PlayerArgument("player")
                            .then(new LiteralArgument("small_caps")
                                    .then(new GreedyStringArgument("text")
                                            .executes((sender, args) -> {
                                                Player target = (Player) args.get("player");
                                                String formattedText = Alphabet.convert((String) args.get("text"));
                                                target.sendMessage(mm.deserialize("<click:copy_to_clipboard:" + formattedText + "><hover:show_text:Click to copy!><green>" + sender.getName() + " sent you a small caps formatted text. Click to copy it to clipboard!</green></hover></click>\n\n<green>Also, maybe you want to see it here: <white>" + formattedText));
                                                sender.sendMessage(mm.deserialize("<green>Sent succesfully!"));
                                            })
                                    )
                            )
                            .then(new LiteralArgument("custom_fonts")
                                    .then(new StringArgument("font")
                                            .then(new GreedyStringArgument("text")
                                                    .executesPlayer((player, args) -> {
                                                        if (!instance.config.getBoolean("features.custom_fonts")) {
                                                            throw CommandAPI.failWithString("Custom fonts feature isn't enabled");
                                                        }
                                                        if (!instance.customFontManager.customFonts.contains(args.get("font"))) {
                                                            throw CommandAPI.failWithString("Font doesn't exist");
                                                        }
                                                        Player target = (Player) args.get("player");
                                                        Component formattedText = CustomFontManager.convert((String) args.get("font"), (String) args.get("text"));
                                                        String messageString = "<yellow><player_name> <green>sent you a custom font formatted text with <yellow><font_name> <green>font! Here it is: <reset><formatted_text>";
                                                        Component messageComponent = mm.deserialize(
                                                                messageString,
                                                                Placeholder.unparsed("player_name", player.getName()),
                                                                Placeholder.unparsed("font_name", (String) args.get("font")),
                                                                Placeholder.component("formatted_text", formattedText));
                                                        target.sendMessage(messageComponent);
                                                        player.sendMessage(mm.deserialize("<green>Sent succesfully!"));
                                                    })
                                            )
                                    )
                            )
                    )
            );

    /*
     *
     * MAIN COMMAND END
     *
     */
}