package wtf.nucker.kitpvpplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandelers.PlayerState;
import wtf.nucker.kitpvpplus.managers.Locations;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
@CommandAlias("spawn|stuck|lobby|l")
@Description("Sends you back to spawn!")
public class SpawnCommand extends BaseCommand {

    @Override
    public String getExecCommandLabel() {
        return "spawn";
    }

    @Default
    public void onCommand(Player p) {
        p.teleport(Locations.SPAWN.get());
        KitPvPPlus.getInstance().getDataManager().getPlayerData(p.getPlayer()).setState(PlayerState.SPAWN);
        p.sendMessage(Language.SPAWN.get(p));
    }

    @CommandAlias("setspawn")
    @CommandPermission("kitpvpplus.setlocations")
    public void setSpawn(Player p) {
        Locations.SPAWN.set(p.getLocation());
        p.sendMessage(Language.SET_SPAWN.get(p));
    }
}
