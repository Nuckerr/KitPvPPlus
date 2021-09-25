package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.managers.PlayerBank;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.Language;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
@CommandAlias("eco|economy")
@Description("View your balance")
public class EconomyAdminCommand extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "economy";
    }

    @Subcommand("set")
    @CommandPermission("kitpvpplus.economy.admin")
    @CommandCompletion("@players")
    public void onSet(CommandSender p, OnlinePlayer target, double newAmount) {
        PlayerBank bank = new PlayerBank(target.getPlayer());
        bank.setBal(newAmount);
        p.sendMessage(Language.BALANCE_SET.get(p instanceof Player ? (Player) p : target.getPlayer()).replace("%balance%", String.valueOf(bank.getBal())).replace("%target%", target.getPlayer().getName()));
    }

    @Subcommand("reset")
    @CommandPermission("kitpvpplus.economy.admin")
    @CommandCompletion("@players")
    public void onReset(CommandSender p, OnlinePlayer target) {
        PlayerBank bank = new PlayerBank(target.getPlayer());
        bank.setBal(0.0);
        p.sendMessage(Language.RESET_BALANCE.get(p instanceof Player ? (Player) p : target.getPlayer()).replace("%target%", target.getPlayer().getName()).replace("%balance%", String.valueOf(0.0)));
    }

    @Subcommand("give")
    @CommandPermission("kitpvpplus.economy.admin")
    @CommandCompletion("@players")
    public void onGive(CommandSender p, OnlinePlayer target, double amount) {
        PlayerBank bank = new PlayerBank(target.getPlayer());
        bank.setBal(bank.getBal() + amount);
        p.sendMessage(Language.BALANCE_GIVEN.get(p instanceof Player ? (Player) p : target.getPlayer()).replace("%target%", target.getPlayer().getName()).replace("%givenAmount%", String.valueOf(amount)).replace("%balance%", String.valueOf(bank.getBal())));
    }

    @HelpCommand
    @Description("Understand the eco command")
    public void onHelp(CommandSender p) {
        List<String> message = Language.ECO_HELP.getAsStringList();

        message = ChatUtils.replaceInList(message, "%bar%", ChatUtils.CHAT_BAR);
        message = ChatUtils.replaceInList(message, "%player%", p.getName());

        p.sendMessage(message.toArray(new String[0]));
    }
}
