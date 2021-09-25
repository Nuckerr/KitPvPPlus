package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.managers.PlayerBank;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
@Description("Commands for economy")
public class EconomyCommands extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "balance";
    }

    @CommandAlias("bal|balance")
    @Description("Sends your balance")
    public void onBal(Player p) {
        p.sendMessage(Language.BALANCE_MESSAGE.get(p).replace("%balance%", String.valueOf(new PlayerBank(p).getBal())));
    }

    @CommandAlias("otherbal|otherbalance|playersbal|playersbalance")
    @Description("Sends other persons balance")
    @CommandPermission("kitpvpplus.balance.other")
    @CommandCompletion("@players")
    public void onOtherBal(CommandSender p, OnlinePlayer target) {
        p.sendMessage(Language.OTHER_BAL_MESSAGE.get().replace("%target%", target.getPlayer().getName())
                .replace("%balance%", String.valueOf(new PlayerBank(target.getPlayer()).getBal())));
    }

    @CommandAlias("pay|sendmoney")
    @Description("Send money to other people")
    @CommandCompletion("@players")
    public void onPay(Player p, OnlinePlayer target, double amount) {
        PlayerBank playerBank = new PlayerBank(p);
        PlayerBank targetBank = new PlayerBank(target.getPlayer());
        playerBank.setBal(playerBank.getBal() - amount);
        targetBank.setBal(targetBank.getBal() + amount);
        if (p.getUniqueId().equals(target.getPlayer().getUniqueId())) {
            p.sendMessage(Language.MONEY_SENT_TO_SELF.get(p));
            return;
        }
        p.sendMessage(Language.PAYMENT_SENT.get(p).replace("%balance%", String.valueOf(playerBank.getBal())).replace("%amount%", String.valueOf(amount)).replace("%target%", target.getPlayer().getName()));
        target.getPlayer().sendMessage(Language.PAID_MESSAGE.get(p).replace("%amount%", String.valueOf(amount)).replace("%payer%", p.getName()).replace("target%", target.getPlayer().getName()));
    }

}
