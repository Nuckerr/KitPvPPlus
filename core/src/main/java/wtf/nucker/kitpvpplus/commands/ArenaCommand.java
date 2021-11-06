package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandlers.PlayerState;
import wtf.nucker.kitpvpplus.managers.Locations;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
@CommandAlias("join|play")
@Description("Joins the arena")
public class ArenaCommand extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "play";
    }

    @Default
    public void onCommand(Player p) {
        p.teleport(Locations.ARENA.get());
        KitPvPPlus.getInstance().getDataManager().getPlayerData(p).setState(PlayerState.ARENA);
        p.sendMessage(Language.ARENA.get(p));
    }

    @CommandAlias("setarena")
    @CommandPermission("kitpvpplus.setlocations")
    @Description("Set the location for the arena.")
    public void setArena(Player p) {
        Locations.ARENA.set(p.getLocation());
        p.sendMessage(Language.SET_ARENA.get(p));
    }
}
