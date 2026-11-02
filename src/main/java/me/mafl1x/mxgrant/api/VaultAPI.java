package me.mafl1x.mxgrant.api;

import me.mafl1x.mxgrant.utils.Utils;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAPI {

    private static Chat chat;

    public static boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) return false;
        chat = rsp.getProvider();
        return chat != null;
    }

    public static boolean hasGroup(Player player, String group) {
        if (chat == null) return false;
        String[] groups = chat.getPlayerGroups(player);
        if (groups == null) return false;
        for (String g : groups) {
            if (g.equalsIgnoreCase(group)) return true;
        }
        return false;
    }

    public static void addGroup(Player player, String group) {
        LuckPermsAPI.addPermission(player, "group." + group);
    }

    public static String getGroup(Player player) {
        if (chat == null) return "";
        return chat.getPrimaryGroup(player);
    }

    public static String getGroupPrefix(World world, String group) {
        if (chat == null) return group;
        return Utils.hex(chat.getGroupPrefix(world, group));
    }
}
