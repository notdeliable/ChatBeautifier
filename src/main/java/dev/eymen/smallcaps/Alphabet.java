package dev.eymen.smallcaps;

import org.jetbrains.annotations.NotNull;

public class Alphabet {

    private static final char[] REPLACEMENT_ALPHABET = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".toCharArray();

    public static String convert(@NotNull String input) {

        StringBuilder sb = new StringBuilder();

        char[] chars = input.toLowerCase().toCharArray();

        for (char c : chars) {
            int index = c - 'a';

            if (index >= 0 && index < REPLACEMENT_ALPHABET.length) {
                sb.append(REPLACEMENT_ALPHABET[index]);
            } else {
                sb.append(c);
            }

        }

        return sb.toString();
    }

}
