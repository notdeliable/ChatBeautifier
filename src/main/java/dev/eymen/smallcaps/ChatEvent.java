package dev.eymen.smallcaps;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static dev.eymen.smallcaps.SmallCaps.instance;

public class ChatEvent implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String replacedMessage = message.replace(message, Alphabet.convert(message));
        if (instance.config.getBoolean("features.replace_chat")) {
            event.setMessage(replacedMessage);
        }
    }
}
