package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.events.KitLoadEvent;
import wtf.nucker.kitpvpplus.exceptions.InsufficientBalance;
import wtf.nucker.kitpvpplus.exceptions.KitAlreadyExistException;
import wtf.nucker.kitpvpplus.exceptions.KitNotExistException;
import wtf.nucker.kitpvpplus.managers.CooldownManager;
import wtf.nucker.kitpvpplus.objects.Kit;
import wtf.nucker.kitpvpplus.player.PlayerData;
import wtf.nucker.kitpvpplus.utils.APIConversion;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.ClockUtils;
import wtf.nucker.kitpvpplus.utils.Language;
import wtf.nucker.kitpvpplus.utils.menuUtils.menuBuilders.KitMenus;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
@CommandAlias("kit|kits")
@Description("Get a kit idiot, what do you think it does")
public class KitCommand extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "kit";
    }

    @Default
    @Description("Get a kit")
    @CommandCompletion("@ownedkits @players")
    public void onCommand(Player p, @Optional Kit kit, @Optional OnlinePlayer target) {
        if(kit == null) {
            if(KitPvPPlus.getInstance().getConfig().getBoolean("enable-guis")) {
                p.sendMessage(Language.KIT_MENU.get(p));
                KitMenus.buildKitGUI(p);
                return;
            }else {
                throw new InvalidCommandArgument(Language.KIT_DOESNT_EXIST.get(p), true);
            }
        }

        if (!KitPvPPlus.getInstance().getDataManager().getPlayerData(p).ownsKit(kit)) {
            p.sendMessage(Language.KIT_NOT_OWNED.get(p).replace("%kitname%", kit.getId()).replace("%permission%", kit.getPermission()));
            return;
        }
        if (CooldownManager.kitCooldown(p, kit)) {
            p.sendMessage(Language.KIT_ON_COOLDOWN.get(p).replace("%kitname%", kit.getId()).replace("%time%", ClockUtils.formatSeconds(kit.getCooldownRunnable().getAmount())));
            return;
        }
        if (target == null) {
            kit.fillInventory(p);
            p.sendMessage(Language.KIT_LOADED.get(p).replace("%kitname%", kit.getId()));
            if (kit.getCooldown() > 0) CooldownManager.addKitCooldown(p, kit, kit.getCooldown());
            Bukkit.getPluginManager().callEvent(new KitLoadEvent(APIConversion.fromInstanceKit(kit), p, p));
            return;
        }
        if (!p.hasPermission("kitpvpplus.givekits")) {
            p.sendMessage(Language.PERMISSION_MESSAGE.get(p).replace("%permission%", "kitpvpplus.givekits"));
            return;
        }
        kit.fillInventory(target.getPlayer());
        p.sendMessage(Language.KIT_SENT.get(p).replace("%target%", target.getPlayer().getName()).replace("%kitname%", kit.getId()));
        target.getPlayer().sendMessage(Language.KIT_GIVEN.get(p).replace("%target%", target.getPlayer().getName()).replace("%kitname%", kit.getId()));
        if (kit.getCooldown() > 0) CooldownManager.addKitCooldown(p, kit, kit.getCooldown());
        Bukkit.getPluginManager().callEvent(new KitLoadEvent(APIConversion.fromInstanceKit(kit), target.getPlayer(), p));
    }

    @Subcommand("purchase|buy")
    @Description("Purchase a kit")
    @CommandCompletion("@notownedkits")
    public void onPurchase(Player p, Kit kit) {
        PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(p);
        if (data.ownsKit(kit)) {
            p.sendMessage(Language.KIT_ALREADY_OWNED.get(p).replace("%kitname%", kit.getId()));
            return;
        }
        if (!kit.getPermission().equals("")) {
            p.sendMessage(Language.PERMISSION_MESSAGE.get(p));
            return;
        }

        if(KitPvPPlus.getInstance().getConfig().getBoolean("enable-guis")) {
            KitMenus.buildPurchaseMenu(p, kit);
        }else {
            try {
                KitPvPPlus.getInstance().getDataManager().getPlayerData(p).purchaseKit(kit);
                p.sendMessage(Language.KIT_PURCHASED.get(p).replace("%kit%", kit.getId()));
            } catch (InsufficientBalance e) {
                p.playSound(p.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1f, 1f);
                p.sendMessage(Language.INSUFFICIENT_BAL.get(p).replace("%kitname%", kit.getId()));
            }
        }
    }

    @Subcommand("create")
    @Description("Creates a kit")
    @CommandPermission("kitspvp.kits.create")
    public void createKit(Player p, String id) {
        try {
            KitPvPPlus.getInstance().getKitManager().createKit(id);
            p.sendMessage(Language.KIT_CREATED.get(p).replace("%kitname%", id));
        } catch (KitAlreadyExistException e) {
            p.sendMessage(Language.KIT_ALREADY_EXISTS.get(p).replace("%kitname%", id));
            e.printStackTrace();
        }
    }

    @Subcommand("delete")
    @Description("Delete a kit")
    @CommandCompletion("@kits")
    @CommandPermission("kitspvpplus.kits.delete")
    public void deleteKit(Player p, Kit kit) {
        String id = kit.getId();
        KitPvPPlus.getInstance().getKitManager().deleteKit(id);
        p.sendMessage(Language.KIT_DELETED.get(p).replace("%kitname%", id));
    }

    @Subcommand("edit")
    @Description("Edit help command")
    @CommandPermission("kitpvpplus.kits.edit")
    public void onEdit(Player p) {
        List<String> message = Language.KIT_ADMIN_HELP.getAsStringList();
        message = ChatUtils.replaceInList(message, "%bar%", ChatUtils.CHAT_BAR);
        p.sendMessage(message.toArray(new String[message.size()]));
    }


    @Subcommand("edit displayname")
    @Description("Edits the displayname of a kit")
    @CommandCompletion("@kits")
    @CommandPermission("kitspvp.kits.edit")
    public void editDisplayname(Player p, Kit kit, String newdisplayname) {
        try {
            kit.setDisplayname(newdisplayname);
            p.sendMessage(Language.KIT_EDIT_DISPLAYNAME.get(p).replace("%kitname%", kit.getId()).replace("%newname%", newdisplayname));
        } catch (KitNotExistException e) {
            p.sendMessage(Language.KIT_DOESNT_EXIST.get(p));
        }
    }

    @Subcommand("edit icon")
    @Description("Edits the icon of a kit")
    @CommandCompletion("@kits")
    @CommandPermission("kitspvp.kits.edit")
    public void setIcon(Player p, Kit kit) {
        try {
            kit.setIcon(p.getItemInHand().getType());
            if (p.getItemInHand().getItemMeta() != null && p.getItemInHand().getItemMeta().getLore() != null) {
                kit.setLore(p.getItemInHand().getItemMeta().getLore());
            }
            p.sendMessage(Language.KIT_EDIT_ICON.get(p).replace("%kitname%", kit.getId()).replace("%itemname%", p.getItemInHand().getType().name().toLowerCase()));
        } catch (KitNotExistException e) {
            p.sendMessage(Language.KIT_DOESNT_EXIST.get(p).replace("%kitname%", kit.getId()));
        }
    }

    @Subcommand("edit contents")
    @Description("Edits the displayname of a kit")
    @CommandCompletion("@kits")
    @CommandPermission("kitpvpplus.kits.edit")
    public void editContents(Player p, Kit kit) {
        try {
            kit.setInventory(p.getInventory());
            p.sendMessage(Language.KIT_EDIT_CONTENTS.get(p).replace("%kitname%", kit.getId()));
        } catch (KitNotExistException e) {
            p.sendMessage(Language.KIT_DOESNT_EXIST.get(p).replace("%kitname%", kit.getId()));
        }
    }

    @Subcommand("edit permission")
    @Description("Edits the permission of a kit")
    @CommandCompletion("@kits @nothing")
    @CommandPermission("kitpvpplus.kits.edit")
    public void editPerm(Player p, Kit kit, @Optional String permission) {
        try {
            if(permission == null) {
                kit.setPermission("");
                p.sendMessage(Language.KIT_EDIT_PERMISSION.get(p).replace("%permission%", "\"\"").replace("%kitname%", kit.getId()));
                return;
            }

            kit.setPermission(permission);
            p.sendMessage(Language.KIT_EDIT_PERMISSION.get(p).replace("%kitname%", kit.getId()).replace("%permission%", permission));
        } catch (KitNotExistException e) {
            p.sendMessage(Language.KIT_DOESNT_EXIST.get(p).replace("%kitname%", kit.getId()));
        }
    }


    @Subcommand("edit price")
    @Description("Edits the price of a kit")
    @CommandCompletion("@kits")
    @CommandPermission("kitpvpplus.kits.edit")
    public void editPrice(Player p, Kit kit, int price) {
        try {
            kit.setPrice(price);
            p.sendMessage(Language.KIT_EDIT_PRICE.get(p).replace("%kitname%", kit.getId()).replace("%price%", String.valueOf(price)));
        } catch (KitNotExistException e) {
            p.sendMessage(Language.KIT_DOESNT_EXIST.get(p).replace("%kitname%", kit.getId()));
        }
    }

    @Subcommand("edit cooldown")
    @Description("Edits the cooldown of a kit")
    @CommandCompletion("@kits")
    @CommandPermission("kitpvpplus.kits.edit")
    public void editCooldown(Player p, Kit kit, int cooldown) {
        try {
            kit.setCooldown(cooldown);
            p.sendMessage(Language.KIT_EDIT_COOLDOWN.get(p).replace("%kitname%", kit.getId()).replace("%cooldown%", String.valueOf(cooldown)));
        } catch (KitNotExistException e) {
            p.sendMessage(Language.KIT_DOESNT_EXIST.get(p).replace("%kitname%", kit.getId()));
        }
    }

    // Aliases for kit subcommands

    @Subcommand("set")
    @CommandPermission("kitpvpplus.kits.edit")
    public void onSet(Player p) {
        this.onEdit(p);
    }

    @Subcommand("set displayname")
    @CommandPermission("kitpvpplus.kits.edit")
    @CommandCompletion("@kits")
    public void onSetDisplayname(Player p, Kit kit, String name) {
        this.editDisplayname(p, kit, name);
    }

    @Subcommand("set icon")
    @CommandPermission("kitpvpplus.kits.edit")
    @CommandCompletion("@kits")
    public void setIconAlias(Player p, Kit kit) {
        this.setIcon(p, kit);
    }

    @Subcommand("set contents")
    @CommandPermission("kitpvpplus.kits.edit")
    @CommandCompletion("@kits")
    public void setContents(Player p, Kit kit) {
        this.editContents(p, kit);
    }

    @Subcommand("set permission")
    @CommandPermission("kitpvpplus.kits.edit")
    @CommandCompletion("@kits")
    public void setPerm(Player p, Kit kit, @Optional String permission) {
        this.editPerm(p, kit, permission);
    }

    @Subcommand("set price")
    @CommandPermission("kitpvpplus.kits.edit")
    @CommandCompletion("@kits")
    public void onSetPrice(Player p, Kit kit, int price) {
        this.editPrice(p, kit, price);
    }

    @Subcommand("set cooldown")
    @CommandPermission("kitpvpplus.kits.edit")
    @CommandCompletion("@kits")
    public void setCooldown(Player p, Kit kit, int cooldown) {
        this.editCooldown(p, kit, cooldown);
    }

    @Subcommand("remove")
    @CommandPermission("kitpvpplus.kits.delete")
    @CommandCompletion("@kits")
    public void onRemove(Player p, Kit kit) {
        this.deleteKit(p, kit);
    }


    @HelpCommand
    @Description("See all the commands of the /kit command")
    public void onHelp(CommandSender p) {
        if (p.hasPermission("kitpvpplus.kits.edit")) {
            List<String> message = Language.KIT_HELP_COMMAND.getAsStringList();

            message = ChatUtils.replaceInList(message, "%bar%", ChatUtils.CHAT_BAR);
            message = ChatUtils.replaceInList(message, "%player%", p.getName());

            p.sendMessage(message.toArray(new String[message.size()]));
        } else {
            p.sendMessage(ChatUtils.translate("&c/kit <kitId> [target]"));
        }
    }
}
