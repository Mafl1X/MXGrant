package me.mafl1x.mxgrant.command;

import me.mafl1x.mxgrant.MXGrant;
import me.mafl1x.mxgrant.handler.Handler;
import me.mafl1x.mxgrant.menu.GrantMenu;
import me.mafl1x.mxgrant.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GrantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.hex(MXGrant.getInstance().getMessages().NOT_A_PLAYER));
            return true;
        }

        Player granter = (Player) sender;

        if (!granter.hasPermission("MXGrant.grant")) {
            granter.sendMessage(Utils.hex(MXGrant.getInstance().getMessages().DO_NOT_HAVE_PERMISSIONS));
            return true;
        }

        if (args.length < 1) {
            granter.sendMessage(Utils.hex(MXGrant.getInstance().getMessages().USAGE));
            return true;
        }

        String targetName = args[0];

        if (targetName.equalsIgnoreCase(granter.getName())) {
            granter.sendMessage(Utils.hex(MXGrant.getInstance().getMessages().CANT_GIVE_YOURSELF));
            return true;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            granter.sendMessage(Utils.hex(MXGrant.getInstance().getMessages().PLAYER_IS_NULL));
            return true;
        }

        GrantMenu grantMenu = new GrantMenu(granter, target);

        if (grantMenu.getItemMenus().stream().noneMatch(i -> {
            for (me.mafl1x.mxgrant.config.Settings.Priority p : MXGrant.getInstance().getSettings().priorities) {
                if (i.getType().equals(p.group())) return true;
            }
            return false;
        })) {
            granter.sendMessage(Utils.hex(MXGrant.getInstance().getMessages().NO_GRANTS_AVAILABLE));
            return true;
        }

        Handler.getPlayersOpenGrantMenu().put(granter.getUniqueId(), grantMenu);
        granter.openInventory(grantMenu.getInventory());
        return true;
    }
}
