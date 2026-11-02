package me.mafl1x.mxgrant.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface IMenu {
    String getName();
    int getRows();
    List<ItemMenu> getItemMenus();
    Inventory getInventory();
    void onClick(InventoryClickEvent e);
}
