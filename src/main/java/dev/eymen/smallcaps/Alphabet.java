package dev.eymen.smallcaps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Alphabet {
	private static final String ORIGINAL_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String REPLACEMENT_ALPHABET = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";

	private static final Map<Character, Character> alphabet = new HashMap<Character, Character>() {{
		for (int i = 0; i < ORIGINAL_ALPHABET.length(); i++) {
			char[] original_array = ORIGINAL_ALPHABET.toCharArray();
			char[] replace_array = REPLACEMENT_ALPHABET.toCharArray();
			put(original_array[i], replace_array[i]);
		}
	}};

	public static @NotNull String convert(@NotNull String input) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char currentChar = input.charAt(i);
			Set<Character> keyset = alphabet.keySet();
			if (keyset.contains(currentChar)) {
				output.append(alphabet.get(currentChar));
			} else
				output.append(currentChar);
		}
		return output.toString();
	}
}
