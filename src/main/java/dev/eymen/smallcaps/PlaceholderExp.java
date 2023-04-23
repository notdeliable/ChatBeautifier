package dev.eymen.smallcaps;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholderExp extends PlaceholderExpansion {
    private final SmallCaps pl;

    public PlaceholderExp(SmallCaps pl) {
        this.pl = pl;
    }

    @Override
    public String getAuthor() {
        return "Deliable";
    }

    @Override
    public String getIdentifier() {
        return "smallcaps";
    }

    @Override
    public String getVersion() {
        return "1.0.0-STABLE";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        String[] givenInput = params.split("_");
        if (params.)
        if (givenInput[0].equalsIgnoreCase("convert")) {
            if (givenInput.length > 1) {
                if (givenInput[1] == null || givenInput[1].trim().isEmpty()) {
                    return "No argument provided.";
                } else {
                    return pl.convertAlphabet(givenInput[1]);
                }
            } else {
                return "No argument provided.";
            }
        }
        return "Invalid or no identifier provided.";
    }
}
