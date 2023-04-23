package dev.eymen.smallcaps;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CapsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("smallcaps.command")) {
			sender.sendMessage(ChatColor.RED + "You don't have enough permission.");
			return false;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "You must provide a string when using this command.");
			return false;
		}
		String givenInput = String.join(" ", args).trim();
		String convertedSentence = Alphabet.convert(givenInput);
		if (sender instanceof Player) {
			Player player = (Player) sender;
			TextComponent message = new TextComponent(ChatColor.GREEN + "Click to get converted message to your message box!");
			message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, convertedSentence));
			player.spigot().sendMessage(message);
			player.sendMessage("\n\n" + ChatColor.GREEN + "Here is your converted message: " + ChatColor.RESET + convertedSentence);
			return true;
		}
		sender.sendMessage(ChatColor.GREEN + "Here is your converted message: " + ChatColor.RESET + convertedSentence);
		return true;
	}
}
