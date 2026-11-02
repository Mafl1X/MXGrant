package me.mafl1x.mxgrant.menu;

import me.mafl1x.mxgrant.MXGrant;
import me.mafl1x.mxgrant.api.VaultAPI;
import me.mafl1x.mxgrant.config.Settings;
import me.mafl1x.mxgrant.config.inv.GrantInv;
import me.mafl1x.mxgrant.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrantMenu implements IMenu {

    private final Player granter;
    private final Player player;
    private final List<ItemMenu> itemMenus = new ArrayList<>();
    private final Inventory inventory;

    public GrantMenu(Player granter, Player player) {
        this.granter = granter;
        this.player = player;
        this.inventory = Bukkit.createInventory(null, getRows() * 9, Utils.hex(getName()));
    }

    public Player getGranter() { return granter; }
    public Player getPlayer() { return player; }

    @Override
    public String getName() {
        return MXGrant.getInstance().getGrantInv().name;
    }

    @Override
    public int getRows() {
        return MXGrant.getInstance().getGrantInv().rows;
    }

    @Override
    public List<ItemMenu> getItemMenus() {
        if (!itemMenus.isEmpty()) return itemMenus;

        GrantInv grantInv = MXGrant.getInstance().getGrantInv();

        for (GrantInv.MenuItem menuItem : grantInv.menuItems) {
            ItemStack itemStack = Utils.getItemStack(menuItem.name(), menuItem.material(), menuItem.lore());
            itemMenus.add(new ItemMenu(menuItem.slot(), itemStack, menuItem.type()));
        }

        List<Integer> slots = grantInv.groupSlots;
        int slotIndex = 0;

        for (Settings.Limit limit : MXGrant.getInstance().getSettings().limits) {
            if (VaultAPI.hasGroup(granter, limit.group())) {
                for (Settings.GroupLimit groupLimit : limit.groupLimits()) {
                    for (GrantInv.GroupItem groupItem : grantInv.groupItems) {
                        if (groupLimit.group().equalsIgnoreCase(groupItem.group())) {
                            int given = MXGrant.getInstance().getGrantAPI().getGroupGiveCount(granter.getUniqueId(), groupItem.group());
                            if (groupLimit.limit() > given && slotIndex < slots.size()) {
                                int left = groupLimit.limit() - given;
                                List<String> lore = groupItem.lore().stream()
                                        .map(s -> s.replace("{amount}", String.valueOf(left)))
                                        .collect(Collectors.toList());
                                ItemStack item = Utils.getItemStack(groupItem.name(), groupItem.material(), lore);
                                itemMenus.add(new ItemMenu(slots.get(slotIndex), item, groupItem.group()));
                                slotIndex++;
                            }
                        }
                    }
                }
                break;
            }
        }

        return itemMenus;
    }

    @Override
    public Inventory getInventory() {
        for (ItemMenu itemMenu : getItemMenus()) {
            inventory.setItem(itemMenu.getSlot(), itemMenu.getItemStack());
        }
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        int clicked = e.getSlot();
        for (ItemMenu itemMenu : getItemMenus()) {
            if (itemMenu.getSlot() != clicked) continue;
            for (Settings.Priority priority : MXGrant.getInstance().getSettings().priorities) {
                if (!itemMenu.getType().equals(priority.group())) continue;
                if (!playerHasGroupHigherPriority(priority.group())) {
                    giveDonate(priority.group());
                } else {
                    granter.sendMessage(Utils.hex(MXGrant.getInstance().getMessages().PLAYER_HAVE_NICE_GROUP));
                }
                granter.closeInventory();
                return;
            }
        }
    }

    private boolean playerHasGroupHigherPriority(String group) {
        String playerGroup = VaultAPI.getGroup(player);
        int p1 = getPriorityGroup(playerGroup);
        int p2 = getPriorityGroup(group);
        return p1 >= p2;
    }

    private int getPriorityGroup(String group) {
        for (Settings.Priority priority : MXGrant.getInstance().getSettings().priorities) {
            if (priority.group().equalsIgnoreCase(group)) return priority.priority();
        }
        return -1;
    }

    private void giveDonate(String group) {
        String prefix = VaultAPI.getGroupPrefix(player.getWorld(), group);
        String msg = Utils.hex(MXGrant.getInstance().getMessages().SUCCESSFULLY_GIVE
                .replace("{granter}", granter.getName())
                .replace("{player}", player.getName())
                .replace("{group}", prefix));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(msg));
        VaultAPI.addGroup(player, group);
        MXGrant.getInstance().getGrantAPI().addPlayerGroup(granter.getUniqueId(), group);
    }
}
