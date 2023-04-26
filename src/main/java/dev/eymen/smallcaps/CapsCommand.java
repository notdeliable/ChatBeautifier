package dev.eymen.smallcaps;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static dev.eymen.smallcaps.Utils.color;
import static dev.eymen.smallcaps.SmallCaps.instance;

public class CapsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("smallcaps.command")) {
			sender.sendMessage(color("&cYou don't have enough permission."));
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(color("&cYou didn't provide enough arguments to use this command. Available arguments for the next argument position: &econvert&c, &ereload&c, &etoggle"));
			return true;
		}
		if (args[0].equalsIgnoreCase("toggle")) {
			if (args.length == 1) {
				sender.sendMessage(color("&cYou didn't provide enough arguments to use this command. Available arguments for the next argument position: &ereplace_chat"));
				return true;
			}
			switch (args[1]) {
				case "replace_chat":
					try {
						instance.config.set("features.replace_chat", !instance.config.getBoolean("features.replace_chat"));
						instance.config.save();
						sender.sendMessage(instance.config.getBoolean("features.replace_chat") ? color("&aEnabled replace_chat!") : color("&cDisabled replace_chat!"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				default:
					sender.sendMessage(color("&cThe specified argument isn't valid."));
					break;
			}
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			try {
				instance.config.reload();
				sender.sendMessage(color("&aReloaded &econfig.yml&a!"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		if (args[0].equalsIgnoreCase("convert")) {
			String givenInput = String.join(" ", args[1]).trim();
			String convertedSentence;
			if (sender instanceof Player) {
				Player player = (Player) sender;
				convertedSentence = Alphabet.convert(PlaceholderAPI.setPlaceholders(player, givenInput));
				TextComponent message = new TextComponent(color("&aClick to get converted message to your message box!"));
				message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, convertedSentence));
				player.spigot().sendMessage(message);
				player.sendMessage(color("\n\n&aHere is your converted message: &r" + convertedSentence));
				return true;
			}
			convertedSentence = Alphabet.convert(PlaceholderAPI.setPlaceholders(null, givenInput));
			sender.sendMessage(color("\n\n&aHere is your converted message: &r" + convertedSentence));
			return true;
		}
		sender.sendMessage(color("&cThe specified argument isn't valid."));
		return true;
	}
}
