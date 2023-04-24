package dev.eymen.smallcaps;

import org.jetbrains.annotations.NotNull;

public class Alphabet {

	private static final String ORIGINAL_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String REPLACEMENT_ALPHABET = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";

	public static @NotNull String convert(@NotNull String input) {
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
		return Utils.color(output.toString());
	}
}
