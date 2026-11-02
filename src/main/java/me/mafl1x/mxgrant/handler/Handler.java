package me.mafl1x.mxgrant.handler;

import me.mafl1x.mxgrant.menu.IMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.UUID;

public class Handler implements Listener {

    private static final HashMap<UUID, IMenu> playersOpenGrantMenu = new HashMap<>();

    public static HashMap<UUID, IMenu> getPlayersOpenGrantMenu() {
        return playersOpenGrantMenu;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getView().getPlayer();
        UUID uuid = player.getUniqueId();
        if (playersOpenGrantMenu.containsKey(uuid)) {
            e.setCancelled(true);
            IMenu menu = playersOpenGrantMenu.get(uuid);
            menu.onClick(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getType() == InventoryType.CHEST) {
            UUID uuid = e.getPlayer().getUniqueId();
            if (playersOpenGrantMenu.containsKey(uuid)) {
                IMenu iMenu = playersOpenGrantMenu.get(uuid);
                if (e.getInventory().equals(iMenu.getInventory())) {
                    playersOpenGrantMenu.remove(uuid);
                }
            }
        }
    }
}
