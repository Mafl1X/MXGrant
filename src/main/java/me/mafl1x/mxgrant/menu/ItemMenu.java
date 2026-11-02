package me.mafl1x.mxgrant.menu;

import org.bukkit.inventory.ItemStack;

public class ItemMenu {
    private final int slot;
    private final ItemStack itemStack;
    private final String type;

    public ItemMenu(int slot, ItemStack itemStack, String type) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.type = type;
    }

    public int getSlot() { return slot; }
    public ItemStack getItemStack() { return itemStack; }
    public String getType() { return type; }
}
