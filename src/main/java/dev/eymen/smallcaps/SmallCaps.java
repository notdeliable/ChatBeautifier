package dev.eymen.smallcaps;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmallCaps extends JavaPlugin implements CommandExecutor {
    private static final String ORIGINAL_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String REPLACEMENT_ALPHABET = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";

    public String convertAlphabet(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            int index = ORIGINAL_ALPHABET.indexOf(currentChar);
            if (index >= 0) {
                output.append(REPLACEMENT_ALPHABET.charAt(index));
            } else {
                output.append(currentChar);
            }
        }
        return output.toString();
    }

    @Override
    public void onEnable() {
        getLogger().info("Hey there!");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("PlaceholderAPI found, registering placeholders.");
            new PlaceholderExp(this).register();
            getLogger().info("Registered placeholders.");
        }
        getCommand("smallcaps").setExecutor(this);
        getLogger().info("Registered commands.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Good bye!");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("smallcaps.command")) {
            sender.sendMessage(ChatColor.RED + "You don't have enough permission.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You must provide a string when using this command.");
            return false;
        }
        String givenInput = String.join(" ", args).trim();
        String convertedSentence = convertAlphabet(givenInput);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            TextComponent message = new TextComponent(ChatColor.GREEN + "Click to get converted message to your message box!");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, convertedSentence));
            player.spigot().sendMessage(message);
            player.sendMessage("\n\n" + ChatColor.GREEN + "Here is your converted message: " + ChatColor.RESET + convertedSentence);
        } else {
            sender.sendMessage(ChatColor.GREEN + "Here is your converted message: " + ChatColor.RESET + convertedSentence);
        }
        return true;
    }
}
