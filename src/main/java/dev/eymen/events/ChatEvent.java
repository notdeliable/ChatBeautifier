package dev.eymen.events;

import dev.eymen.ChatBeautifier;
import dev.eymen.smallcaps.Alphabet;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.StringJoiner;
import java.util.regex.Pattern;

import static dev.eymen.Utils.mm;

public class ChatEvent implements Listener {
    ChatBeautifier instance;

    public ChatEvent(ChatBeautifier instance) {
        this.instance = instance;
    }

    @EventHandler
    public void smallCapsChat(AsyncChatEvent event) {
        if (instance.config.getBoolean("features.small_caps_chat")) {
            event.message(mm.deserialize(Alphabet.convert(mm.serialize(event.message()))));
        }
    }

    @EventHandler
    public void smallCapsKeywords(AsyncChatEvent event) {
        if (instance.config.getBoolean("features.small_caps_keywords")) {
            Component originalMessage = event.message();
            StringJoiner regexList = new StringJoiner("|");
            for (String string : instance.config.getStringList("small_caps_keywords.keywords")) {
                regexList.add(string);
            }
            TextReplacementConfig textReplacementConfig = TextReplacementConfig.builder()
                    .match(Pattern.compile("\\b(" + regexList + ")\\b", Pattern.CASE_INSENSITIVE))
                    .replacement((matchResult, builder) -> builder.content(Alphabet.convert(matchResult.group(0))))
                    .build();
            originalMessage = originalMessage.replaceText(textReplacementConfig);
            event.message(originalMessage);
        }
    }
}
