package dev.eymen;

import dev.eymen.customfont.CustomFontManager;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.Bukkit;

import static dev.eymen.ChatBeautifier.instance;

public class MiniPlaceholders {
    public void init() {
        if (Bukkit.getPluginManager().getPlugin("MiniPlaceholders") != null) {
            try {
                instance.getLogger().info("MiniPlaceholders found, registering placeholders.");
                MiniMessage mm = MiniMessage.miniMessage();
                Expansion.Builder builder = Expansion.builder("customfonts");
                builder.globalPlaceholder("convert", (queue, ctx) -> {
                    Tag.Argument font = null;
                    Tag.Argument givenInput = null;
                    while (queue.hasNext()) {
                        font = queue.popOr("No font provided.");
                        givenInput = queue.popOr("No string provided.");
                    }

                    if (font == null || font.value().trim().isEmpty()) {
                        return Tag.selfClosingInserting(mm.deserialize("<red>No font provided."));
                    }

                    if (givenInput == null || givenInput.value().trim().isEmpty()) {
                        return Tag.selfClosingInserting(mm.deserialize("<red>No string provided."));
                    }

                    if (!instance.customFontManager.customFonts.contains(font.value())) {
                        return Tag.selfClosingInserting(mm.deserialize("<red>Font isn't valid."));
                    }

                    return Tag.selfClosingInserting(CustomFontManager.convert(font.value(), givenInput.value()));
                });
                Expansion expansion = builder.build();
                if (!expansion.registered()) {
                    expansion.register();
                    instance.getLogger().info("Registered MiniPlaceholders placeholders.");
                    return;
                }
                instance.getLogger().warning("Couldn't register MiniPlaceholders placeholders, because they're already registered.");
                return;
            } catch (NoClassDefFoundError ignored) {
                instance.getLogger().warning("Couldn't register custom font placeholders, because MiniPlaceholders was not found.");
                return;
            }
        }
        instance.getLogger().warning("Couldn't register custom font placeholders, because MiniPlaceholders was not found.");
    }
}
