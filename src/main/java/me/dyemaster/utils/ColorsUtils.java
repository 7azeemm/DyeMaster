package me.dyemaster.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorsUtils {

    // Regular expression for a valid hex color code
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6})$");

    private static final String COLOR_CODE_PATTERN = "&[0-9a-fk-or]";
    private static final String COLOR_PLACEHOLDER_PATTERN = "(?i)\\[color(?::(#[0-9a-fA-F]{6}))?\\]"; // Case-insensitive pattern for [color] and [color:#RRGGBB]

    public static ArrayList<String> loreProcess(String rawLore, String color) {
        rawLore = color != null ? replaceColorPlaceholders(rawLore, color) : replaceColorPlaceholders(rawLore, null);
        String[] words = rawLore.split("\\\\n", -1);
        String[] newLore = processWords(words);
        return new ArrayList<>(List.of(newLore));
    }

    public static String replaceColorPlaceholders(String message, String color) {
        Matcher matcher = Pattern.compile(COLOR_PLACEHOLDER_PATTERN).matcher(message);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String hexColor = matcher.group(1); // This will be null if [color] is used without hex
            if (hexColor != null) {
                String minecraftColor = convertHexToMinecraftColor(hexColor);
                matcher.appendReplacement(result, minecraftColor);
            } else {
                if (color != null) {
                    String minecraftColor = convertHexToMinecraftColor(color);
                    matcher.appendReplacement(result, minecraftColor);
                } else {
                    matcher.appendReplacement(result, "[color]"); // Retain [color] if no default hex provided
                }
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public static String convertHexToMinecraftColor(String hexColor) {
        StringBuilder minecraftColor = new StringBuilder("&x");
        for (char c : hexColor.substring(1).toCharArray()) {
            minecraftColor.append("&").append(c);
        }
        return minecraftColor.toString();
    }

    public static String[] processWords(String[] words) {
        String[] coloredWords = new String[words.length];
        boolean previousWordHadColor = false;

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String coloredWord = ChatColor.translateAlternateColorCodes('&', word);

            // Check if the word contains a color code
            boolean currentWordHasColor = containsColorCode(coloredWord);

            if (!previousWordHadColor && !currentWordHasColor) {
                coloredWord = ChatColor.GRAY + coloredWord;
            }

            coloredWords[i] = coloredWord;
            previousWordHadColor = currentWordHasColor;
        }

        return coloredWords;
    }

    public static boolean containsColorCode(String text) {
        Matcher matcher = Pattern.compile(COLOR_CODE_PATTERN).matcher(text);
        return matcher.find();
    }

    public static int hexToRGB(String hex) {
        return Integer.parseInt(hex.substring(1), 16);
    }

    public static boolean isValidHexColor(String hexColor) {
        if (hexColor == null) {
            return false;
        }
        return HEX_COLOR_PATTERN.matcher(hexColor).matches();
    }

}
