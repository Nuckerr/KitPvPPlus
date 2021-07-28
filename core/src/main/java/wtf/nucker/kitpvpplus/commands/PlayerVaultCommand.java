package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 28/07/2021
 */
@CommandAlias("playervault|playerv|pvault|pv")
@Description("Open your player vault")
public class PlayerVaultCommand extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "playervault";
    }

    @Default
    public void onCommand(Player player, int page) {
        if(KitPvPPlus.getInstance().getPvManager().openPV(page, player)) {
            player.sendMessage(Language.OPENING_PV.get(player).replace("%page%", String.valueOf(page)));
        }else {
            player.sendMessage(Language.PERMISSION_MESSAGE.get(player));
        }
    }

    @CatchUnknown
    public void onHelp(Player player) {
        player.sendMessage(ChatUtils.translate("&cInvalid usage: /playervault <page>"));
    }
}
