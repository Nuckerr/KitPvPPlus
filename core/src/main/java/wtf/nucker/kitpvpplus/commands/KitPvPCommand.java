package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.PlayerBank;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.ItemUtils;
import wtf.nucker.kitpvpplus.utils.Language;
import wtf.nucker.kitpvpplus.utils.menuUtils.menuBuilders.AbilityMenus;

import java.util.List;
import java.util.UUID;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
@CommandAlias("kitpvp|kpvp|kitpvpcore|kpvpcore|kitpvpc|kpvpc|kitpvpplus|kpvpvplus|kpvpp|kitpvpp")
@Description("Provides information on the plugin and allows you to control it if you are an admin")
public class KitPvPCommand extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "kitpvpplus";
    }

    @Subcommand("reload config|rl config|reloadconfig|rlconfig")
    @Description("Reloads just the config files")
    @CommandPermission("kitpvpplus.admin")
    public void onReloadConfig(CommandSender p) {
        KitPvPPlus.getInstance().reloadConfigs();
        p.sendMessage(Language.PLUGIN_RELOADED.get());
    }

    @Subcommand("reload database|rl database|reloaddatabase|rldatabase")
    @Description("Reloads just the databases")
    @CommandPermission("kitpvpplus.admin")
    public void onReloadDB(CommandSender p) {
        KitPvPPlus.getInstance().reloadDatabase();
        p.sendMessage(Language.PLUGIN_RELOADED.get());
    }

    @Subcommand("reload|rl")
    @Description("Reloads everything")
    @CommandPermission("kitpvppplus.admin")
    public void onReload(CommandSender p) {
        KitPvPPlus.getInstance().reloadConfigs();
        KitPvPPlus.getInstance().reloadDatabase();
        KitPvPPlus.getInstance().reloadSB();
        p.sendMessage(Language.PLUGIN_RELOADED.get());
    }

    @Subcommand("credits")
    @Description("Sends the credits")
    public void onCredits(CommandSender p) {
        p.sendMessage(ChatUtils.translate(new String[]{
                ChatColor.AQUA + ChatUtils.CHAT_BAR,
                "&7KitPvP Core developed by Nucker",
                "&eSpigot: https://spigotmc.org/resource/id",
                "&8Github: https://github.com/Nuckerr/KitPvPPlus",
                "&9Support server: http://nckr.link/support",
                ChatColor.AQUA + ChatUtils.CHAT_BAR
        }));
    }
    @Subcommand("abilities|ability")
    @Description("Get the itemstacks of the registered abilities")
    @CommandPermission("kitpvpplus.admin")
    @CommandCompletion("@abilities")
    public void onAbility(Player player, @Optional Ability ability) {
        if(ability == null) {
            AbilityMenus.buildAbilitiesMenu(player);
            return;
        }
        player.getInventory().addItem(ItemUtils.copyItem(ability.getItem()));
    }

    @Subcommand("debug")
    @Private
    public void onDebugCommand(Player player) {
        if(player.getUniqueId().equals(UUID.fromString("68f34c4f-d00c-40fb-858d-b5a876601072"))) {
            player.sendMessage(ChatUtils.translate(new String[] {
                    "&e" + ChatUtils.CHAT_BAR,
                    "&eThis server uses KitPvPPlus",
                    "&eVersion: &b" + KitPvPPlus.getInstance().getDescription().getVersion(),
                    "&eSending metrics: &b" + KitPvPPlus.getInstance().getMetrics().isEnabled(),
                    "&eWorldguard integration: &b" + KitPvPPlus.getInstance().isWGEnabled(),
                    "&ePlaceholder intergration: &b" + (Bukkit.getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null),
                    "&eStorage system: &b" + KitPvPPlus.getInstance().getDataManager().getStorageType(),
                    "&eBank Storage type: &b" + PlayerBank.getStorageType(),
                    "&eDebug mode: &b" + KitPvPPlus.DEBUG,
                    "&eLegacy Version: &b" + KitPvPPlus.getInstance().getVerManager().needsUpdating(),
                    "&e" + ChatUtils.CHAT_BAR
            }));
        }else {
            if (player.hasPermission("kitpvpplus.admin")) {
                List<String> message = Language.CORE_HELP.getAsStringList();

                message = ChatUtils.replaceInList(message, "%bar%", ChatUtils.CHAT_BAR);
                message = ChatUtils.replaceInList(message, "%player%", player.getName());
                player.sendMessage(message.toArray(new String[0]));
            } else {
                player.sendMessage(ChatUtils.translate(new String[]{
                        ChatColor.AQUA + ChatUtils.CHAT_BAR,
                        "&7KitPvP Core developed by Nucker",
                        "&eSpigot: https://spigotmc.org/resource/id",
                        "&8Github: https://github.com/Nuckerr/KitPvPPlus",
                        "&9Support server: http://nckr.link/support",
                        ChatColor.AQUA + ChatUtils.CHAT_BAR
                }));
            }
        }
    }

    @HelpCommand
    public void onHelp(Player p) {
        if (p.hasPermission("kitpvpplus.admin")) {
            List<String> message = Language.CORE_HELP.getAsStringList();

            message = ChatUtils.replaceInList(message, "%bar%", ChatUtils.CHAT_BAR);
            message = ChatUtils.replaceInList(message, "%player%", p.getName());
            p.sendMessage(message.toArray(new String[0]));
        } else {
            p.sendMessage(ChatUtils.translate(new String[]{
                    ChatColor.AQUA + ChatUtils.CHAT_BAR,
                    "&7KitPvP Core developed by Nucker",
                    "&eSpigot: https://spigotmc.org/resource/id",
                    "&8Github: https://github.com/Nuckerr/KitPvPPlus",
                    "&9Support server: http://nckr.link/support",
                    ChatColor.AQUA + ChatUtils.CHAT_BAR
            }));
        }
    }

}
