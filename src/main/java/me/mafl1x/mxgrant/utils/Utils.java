package me.mafl1x.mxgrant.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern HEX_PATTERN = Pattern.compile("(#[a-fA-F0-9]{6})");

    public static ItemStack getItemStack(String name, String material, List<String> lore) {
        ItemStack itemStack = new ItemStack(Material.valueOf(material));
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(hex(name));
            List<String> coloredLore = new ArrayList<>();
            for (String line : lore) coloredLore.add(hex(line));
            meta.setLore(coloredLore);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public static String hex(String message) {
        if (message == null) return "";
        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) builder.append("&").append(c);
            message = message.replace(hexCode, builder.toString());
            matcher = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> hexList(List<String> lines) {
        List<String> result = new ArrayList<>();
        for (String s : lines) result.add(hex(s));
        return result;
    }
}
