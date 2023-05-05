package dev.eymen.customfont;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.th0rgal.oraxen.OraxenPlugin;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.eymen.ChatBeautifier.instance;

public class CustomFontManager {
    public List<String> customFonts = new ArrayList<>();
    public File[] customFontFiles = instance.custom_fonts.listFiles((d, name) -> name.endsWith(".ttf"));
    private YamlDocument fontConfigurationFile;
    public static String namespace;


    Boolean isIaEnabledPl = Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");
    Boolean isOraxenEnabledPl = Bukkit.getPluginManager().isPluginEnabled("Oraxen");
    Boolean isIaEnabledConfig = instance.config.getBoolean("custom_fonts.auto_resource_pack.itemsadder");
    Boolean isOraxenEnabledConfig = instance.config.getBoolean("custom_fonts.auto_resource_pack.oraxen");

    public void init() {
        customFonts.clear();
        for (File file : customFontFiles) {
            customFonts.add(file.getName().substring(0, file.getName().length() - 4));
        }

        if (isIaEnabledPl && isIaEnabledConfig) {
            generateResourcePack("itemsadder");
            instance.getLogger().info("Generated resource pack for ItemsAdder.");
        }

        if (isOraxenEnabledPl && isOraxenEnabledConfig) {
            generateResourcePack("oraxen");
            instance.getLogger().info("Generated resource pack for Oraxen.");
        }

        generateResourcePack("vanilla");
        instance.getLogger().info("Generated resource pack in 'resource_pack' directory.");
    }

    public void generateResourcePack(String packGenType) {
        if (customFonts.size() == 0) {
            instance.getLogger().severe("Couldn't generate resource pack, because no font was found in 'custom_fonts' directory. Please add .ttf fonts for the custom fonts feature to work.\n\nPlease note that this message may be logged a few times, because resource pack generation can be made for multiple supported plugins.");
            return;
        }

        namespace = instance.config.getString("custom_fonts.namespace");

        File fileNamespace;
        File fileFontNamespace = null;
        File fontFile;
        File packFontConfigurationFile;

        if (packGenType.equals("oraxen")) {
            if (isOraxenEnabledPl && isOraxenEnabledConfig) {
                fileNamespace = new File(OraxenPlugin.get().getDataFolder(), "pack" + File.separator + "assets" + File.separator + namespace);
                if (fileNamespace.exists()) deleteFileContent(fileNamespace);
                if (!fileNamespace.exists()) fileNamespace.mkdirs();
                fileFontNamespace = new File(fileNamespace, "font");
                fileFontNamespace.mkdirs();
            }
        }

        if (packGenType.equals("itemsadder")) {
            if (isIaEnabledPl && isIaEnabledConfig) {
                fileNamespace = new File(Bukkit.getPluginManager().getPlugin("ItemsAdder").getDataFolder(), "contents" + File.separator + namespace);
                if (fileNamespace.exists()) deleteFileContent(fileNamespace);
                if (!fileNamespace.exists()) fileNamespace.mkdirs();
                File iaRespack = new File(fileNamespace, "resourcepack" + File.separator + namespace);
                iaRespack.mkdirs();
                fileFontNamespace = new File(iaRespack, "font");
                fileFontNamespace.mkdirs();
            }
        }

        if (packGenType.equals("vanilla")) {
            fileNamespace = new File(instance.resource_pack_assets, namespace);
            if (fileNamespace.exists()) deleteFileContent(fileNamespace);
            if (!fileNamespace.exists()) fileNamespace.mkdirs();
            fileFontNamespace = new File(fileNamespace, "font");
            fileFontNamespace.mkdirs();
            File packMcmeta = new File(instance.resource_pack, "pack.mcmeta");
            deleteFileContentAsText(packMcmeta);
            JSONObject content = new JSONObject();
            int pack_format;
            if (Bukkit.getServer().getVersion().contains("1.18")) {
                pack_format = 8;
            } else {
                pack_format = 9;
            }
            content.put("description", "This pack is generated by ChatBeautifier.");
            content.put("pack_format", pack_format);
            JSONObject root = new JSONObject();
            root.put("pack", content);
            try (FileWriter writer = new FileWriter(packMcmeta)) {
                writer.write(root.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (File customFontFile : customFontFiles) {
            fontFile = new File(fileFontNamespace, customFontFile.getName());
            try {
                fontConfigurationFile = getFontConfigurationFile(customFontFile.getName());
                if (fontConfigurationFile.getFile().length() == 0) {
                    fontConfigurationFile.set("shift", Arrays.asList(0, 0.5));
                    fontConfigurationFile.set("size", 12.0);
                    fontConfigurationFile.set("oversample", 4.0);
                    fontConfigurationFile.set("skip", "");
                    fontConfigurationFile.save();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            copyFileContent(customFontFile, fontFile);
            packFontConfigurationFile = new File(fileFontNamespace, customFontFile.getName().substring(0, customFontFile.getName().length() - 4) + ".json");
            deleteFileContentAsText(packFontConfigurationFile);
            JSONObject content = new JSONObject();
            content.put("type", "ttf");
            content.put("file", namespace + ":" + customFontFile.getName());
            content.put("shift", fontConfigurationFile.getDoubleList("shift"));
            content.put("size", fontConfigurationFile.getDouble("size"));
            content.put("oversample", fontConfigurationFile.getDouble("oversample"));
            content.put("skip", fontConfigurationFile.getString("skip"));
            JSONArray contentArray = new JSONArray();
            contentArray.put(content);
            JSONObject root = new JSONObject();
            root.put("providers", contentArray);
            try (FileWriter writer = new FileWriter(packFontConfigurationFile)) {
                writer.write(root.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public YamlDocument getFontConfigurationFile(String filename) {
        if (filename.endsWith(".yml")) {
            try {
                return YamlDocument.create(new File(instance.custom_fonts, filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (filename.endsWith(".ttf")) {
            try {
                return YamlDocument.create(new File(instance.custom_fonts, filename.substring(0, filename.length() - 4) + ".yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return YamlDocument.create(new File(instance.custom_fonts, filename + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFileContent(File file) {
        File[] content = file.listFiles();
        if (content != null) {
            for (File contentFile : content) {
                contentFile.delete();
            }
        }
    }

    //Thanks ChatGPT!
    public void copyFileContent(File source, File destination) {
        try {
            FileInputStream sourceInputStream = new FileInputStream(source);
            FileOutputStream destinationInputStream = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];
            int readed;
            while ((readed = sourceInputStream.read(buffer)) > 0) {
                destinationInputStream.write(buffer, 0, readed);
            }

            sourceInputStream.close();
            destinationInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Thanks ChatGPT, again!
    public void deleteFileContentAsText(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Component convert(String font, String input) {
        Component component = Component.text(input, Style.empty().font(Key.key(namespace + ":" + font)));
        return component;
    }
}