package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.AbilityManager;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.Logger;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
@CommandAlias("kitpvpdebug")
@Description("A command just used for debugging the plugin. Disabled in production")
@CommandPermission("kitpvpplus.debug")
public class DebugCommand extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "kitpvpdebug";
    }

    @Subcommand("ListAbilities")
    public void onListAbilities(Player p) {
        AbilityManager.getAbilities().forEach((String id, Ability ability) -> {
            p.sendMessage(id);
        });
    }

    @Subcommand("leaderboard")
    public void onLeaderboard(Player p) {
        KitPvPPlus.getInstance().getLeaderBoardManager().getDeathsLeaderboard().getTop(10).forEach(place -> {
            Logger.debug("#" + KitPvPPlus.getInstance().getLeaderBoardManager().getDeathsLeaderboard().getPlace(place.getPlayer()) + " " + place.getValue() + "("
            + place.getPlayer().getName() + ")");
        });
        p.sendMessage("&b#"+KitPvPPlus.getInstance().getLeaderBoardManager().getDeathsLeaderboard().getPlace(p));
    }

    @Subcommand("state")
    @CommandCompletion("@players")
    public void onState(Player p, OnlinePlayer target) {
        p.sendMessage(KitPvPPlus.getInstance().getDataManager().getPlayerData(target.getPlayer()).getState().name());
    }

    @Subcommand("abilities")
    public void getAbilities(Player p) {
        Inventory inv = Bukkit.createInventory(p, 9, "Abilities");
        AbilityManager.getAbilities().forEach((id, ability) -> inv.addItem(ability.getItem()));
        p.openInventory(inv);
    }

    @Subcommand("isability")
    public void isAbility(Player p) {
        p.sendMessage(String.valueOf(AbilityManager.isAbilityItem(p.getItemInHand())));
    }

    @Subcommand("stats increment deaths")
    @CommandCompletion("@players")
    public void incrementDeaths(CommandSender p, @Optional Player target) {
        target = target != null ? target : p instanceof Player ? (Player) p : null;
        if (target == null) {
            p.sendMessage(ChatUtils.translate("&cNo player provided"));
            return;
        }

        KitPvPPlus.getInstance().getDataManager().getPlayerData(target).incrementDeaths();
        target.sendMessage(ChatUtils.translate("&aIncremented " + target.getName() + "'s deaths"));
    }

    @Subcommand("stats increment kills")
    @CommandCompletion("@players")
    public void incrementKills(CommandSender p, @Optional Player t) {
        t = t != null ? t : p instanceof Player ? (Player) p : null;
        if (t == null) {
            p.sendMessage(ChatUtils.translate("&cNo player provided"));
            return;
        }

        KitPvPPlus.getInstance().getDataManager().getPlayerData(t).incrementKills();
        t.sendMessage(ChatUtils.translate("&aIncremented " + t.getName() + "'s kills"));
    }

    @Subcommand("stats increment exp")
    @CommandCompletion("@players")
    public void incrementExp(CommandSender p, @Optional OnlinePlayer t) {
        t = t != null ? t : p instanceof Player ? new OnlinePlayer((Player) p) : null;
        if (t == null) {
            p.sendMessage(ChatUtils.translate("&cNo player provided"));
            return;
        }

        KitPvPPlus.getInstance().getDataManager().getPlayerData(t.getPlayer()).updateExp(1);
        p.sendMessage(ChatUtils.translate("&aSet " + t.getPlayer().getName() + "'s exp to +1"));
    }

    @Subcommand("stats updatelevel")
    @CommandCompletion("@players")
    public void updateLevel(CommandSender p, @Optional Player t) {
        t = t != null ? t : p instanceof Player ? (Player) p : null;
        if (t == null) {
            p.sendMessage(ChatUtils.translate("&cNo player provided"));
            return;
        }

        KitPvPPlus.getInstance().getDataManager().getPlayerData(t).updateLevel();
        t.sendMessage(ChatUtils.translate("&aUpdated " + t.getName() + "'s level to " + KitPvPPlus.getInstance().getDataManager().getPlayerData(t).getLevel()));
    }

    @Subcommand("stats set exp")
    @CommandCompletion("@players")
    public void setExp(CommandSender p, Player t, int newExp) {
        t = t != null ? t : p instanceof Player ? (Player) p : null;
        if (t == null) {
            p.sendMessage(ChatUtils.translate("&cNo player provided"));
            return;
        }

        KitPvPPlus.getInstance().getDataManager().getPlayerData(t).updateExp(newExp);
        t.sendMessage(ChatUtils.translate("&aAdded " + newExp + " to " + t.getName() + "'s exp value"));
    }

    @Subcommand("stats reset")
    @CommandCompletion("@players")
    public void reset(CommandSender p, Player t) {
        t = t != null ? t : p instanceof Player ? (Player) p : null;
        if (t == null) {
            p.sendMessage(ChatUtils.translate("&cNo player provided"));
            return;
        }

        KitPvPPlus.getInstance().getDataManager().getPlayerData(t).resetData();
        t.sendMessage(ChatUtils.translate("&aDone"));
    }


}
