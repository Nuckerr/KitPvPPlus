package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandlers.PlayerData;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.Language;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
@CommandAlias("stats|statistics")
@Description("View your stats")
public class StatsCommand extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "statistics";
    }

    @Default
    @CommandCompletion("@players")
    public void onCommand(Player p, @Optional OnlinePlayer t) {
        if (t == null) t = new OnlinePlayer(p);
        else {
            if (!p.hasPermission("kitpvpplus.stats.other")) {
                p.sendMessage(Language.PERMISSION_MESSAGE.get(p));
                return;
            }
        }

        List<String> message = Language.STATS.getAsStringList();
        PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(t.getPlayer());
        message = ChatUtils.replaceInList(message, "%bar%", ChatUtils.CHAT_BAR);
        message = ChatUtils.replaceInList(message, "%kills%", String.valueOf(data.getKills()));
        message = ChatUtils.replaceInList(message, "%deaths%", String.valueOf(data.getDeaths()));
        message = ChatUtils.replaceInList(message, "%exp%", String.valueOf(data.getExp()));
        message = ChatUtils.replaceInList(message, "%level%", String.valueOf(data.getLevel()));
        message = ChatUtils.replaceInList(message, "%killstreak%", String.valueOf(data.getKillStreak()));
        message = ChatUtils.replaceInList(message, "%kdr%", String.valueOf(data.getKDR()));
        message = ChatUtils.replaceInList(message, "%top_killstreak%", String.valueOf(data.getTopKillStreak()));
        p.sendMessage(message.toArray(new String[0]));
    }
}
