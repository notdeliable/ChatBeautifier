package dev.eymen;

import dev.eymen.customfont.CustomFontManager;
import io.github.miniplaceholders.api.Expansion;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class MiniPlaceholders {
    private final ChatBeautifier plugin;

    public MiniPlaceholders(ChatBeautifier plugin) {
        this.plugin = plugin;
    }

    public void init() {
        if (!Bukkit.getPluginManager().isPluginEnabled("MiniPlaceholders")) {
            plugin.getLogger().warning("Couldn't register custom font placeholders, because MiniPlaceholders was not found.");
            return;
        }

        try {
            plugin.getLogger().info("MiniPlaceholders found, registering placeholders.");
            final Expansion expansion = Expansion.builder("customfonts")
                    .globalPlaceholder("convertglobal", (queue, ctx) -> {
                        final String font;
                        final String input;

                        if (!queue.hasNext() || (font = queue.pop().value()).trim().isBlank()) {
                            return Tag.selfClosingInserting(Component.text("No font provided.", NamedTextColor.RED));
                        }
                        if (!queue.hasNext() || (input = queue.pop().value()).trim().isBlank()) {
                            return Tag.selfClosingInserting(Component.text("No string provided.", NamedTextColor.RED));
                        }

                        if (!plugin.customFontManager.customFonts.contains(font)) {
                            return Tag.selfClosingInserting(Component.text("Font isn't valid.", NamedTextColor.RED));
                        }

                        return Tag.selfClosingInserting(CustomFontManager.convert(font, PlaceholderAPI.setBracketPlaceholders(null, input)));
                    })
                    .audiencePlaceholder("convertplayer", (audience, queue, ctx) -> {
                        final String font;
                        final String input;

                        if (!queue.hasNext() || (font = queue.pop().value()).trim().isBlank()) {
                            return Tag.selfClosingInserting(Component.text("No font provided.", NamedTextColor.RED));
                        }
                        if (!queue.hasNext() || (input = queue.pop().value()).trim().isBlank()) {
                            return Tag.selfClosingInserting(Component.text("No string provided.", NamedTextColor.RED));
                        }

                        if (!plugin.customFontManager.customFonts.contains(font)) {
                            return Tag.selfClosingInserting(Component.text("Font isn't valid.", NamedTextColor.RED));
                        }
                        return Tag.selfClosingInserting(CustomFontManager.convert(font, PlaceholderAPI.setBracketPlaceholders((Player) audience, input)));
                    })
                    .build();
            if (!expansion.registered()) {
                expansion.register();
                plugin.getLogger().info("Registered MiniPlaceholders placeholders.");
                return;
            }
            plugin.getLogger().warning("Couldn't register MiniPlaceholders placeholders, because they're already registered.");
        } catch (NoClassDefFoundError ignored) {
            plugin.getLogger().warning("Couldn't register custom font placeholders, because MiniPlaceholders was not found.");
        }
    }
}
