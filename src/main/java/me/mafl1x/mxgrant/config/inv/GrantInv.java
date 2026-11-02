package me.mafl1x.mxgrant.config.inv;

import de.exlll.configlib.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GrantInv {

    public int rows = 6;
    public String name = "&8Выдача группы";

    public List<MenuItem> menuItems = Arrays.asList(
            new MenuItem(0, "background", " ", "GRAY_STAINED_GLASS_PANE", List.of())
    );

    public List<GroupItem> groupItems = Arrays.asList(
            new GroupItem("king", "&6King", "GOLD_INGOT", Arrays.asList("&7Осталось выдач: &e{amount}")),
            new GroupItem("knight", "&7Knight", "IRON_INGOT", Arrays.asList("&7Осталось выдач: &e{amount}"))
    );

    public List<Integer> groupSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20);

    @Configuration
    public static class MenuItem {
        public int slot = 0;
        public String type = "background";
        public String name = " ";
        public String material = "GRAY_STAINED_GLASS_PANE";
        public List<String> lore = List.of();

        public MenuItem() {}

        public MenuItem(int slot, String type, String name, String material, List<String> lore) {
            this.slot = slot;
            this.type = type;
            this.name = name;
            this.material = material;
            this.lore = lore;
        }

        public int slot() { return slot; }
        public String type() { return type; }
        public String name() { return name; }
        public String material() { return material; }
        public List<String> lore() { return lore; }
    }

    @Configuration
    public static class GroupItem {
        public String group = "";
        public String name = "";
        public String material = "DIAMOND";
        public List<String> lore = List.of();

        public GroupItem() {}

        public GroupItem(String group, String name, String material, List<String> lore) {
            this.group = group;
            this.name = name;
            this.material = material;
            this.lore = lore;
        }

        public String group() { return group; }
        public String name() { return name; }
        public String material() { return material; }
        public List<String> lore() { return lore; }
    }
}
