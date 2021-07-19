package wtf.nucker.kitpvpplus.listeners;

import com.cryptomorin.xseries.XSound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.Locations;
import wtf.nucker.kitpvpplus.player.PlayerState;
import wtf.nucker.kitpvpplus.utils.Language;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class SignListeners implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if(KitPvPPlus.getInstance().getConfig().getBoolean("enable-signs")) {
            if(!e.getPlayer().hasPermission("kitpvpplus.signs.create")) {
                e.getPlayer().sendMessage(Language.PERMISSION_MESSAGE.get(e.getPlayer()));
                return;
            }
            List<String> contents;
            switch (e.getLine(0).toLowerCase()) {
                case "[kitpvp-spawn]": /* Spawn sign */
                    contents = Language.SPAWN_SIGN.getAsStringList();
                    for (int i = 0; i < contents.size(); i++) {
                        e.setLine(i, contents.get(i));
                    }
                    e.getBlock().setMetadata("kpvp", new FixedMetadataValue(KitPvPPlus.getInstance(), "spawn"));
                    KitPvPPlus.getInstance().getSignManager().addSign(e.getBlock(), "spawn");
                    e.getPlayer().sendMessage(Language.SIGN_SET.get(e.getPlayer()).replace("%sign_type%", "spawn"));
                    e.getPlayer().playSound(e.getPlayer().getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1f, 250f);
                    break;
                case "[kitpvp-arena]": /* Spawn sign */
                    contents = Language.ARENA_SIGN.getAsStringList();
                    for (int i = 0; i < contents.size(); i++) {
                        e.setLine(i, contents.get(i));
                    }
                    e.getBlock().setMetadata("kpvp", new FixedMetadataValue(KitPvPPlus.getInstance(), "arena"));
                    KitPvPPlus.getInstance().getSignManager().addSign(e.getBlock(), "arena");
                    e.getPlayer().sendMessage(Language.SIGN_SET.get(e.getPlayer()).replace("%sign_type%", "arena"));
                    e.getPlayer().playSound(e.getPlayer().getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1f, 250f);
                    break;
            }
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        if(e.getClickedBlock().getType().name().contains("SIGN")) {
            if(e.getClickedBlock().getMetadata("kpvp") == null || e.getClickedBlock().getMetadata("kpvp").isEmpty()) return;
            switch (e.getClickedBlock().getMetadata("kpvp").get(0).asString()) {
                case "spawn":
                    e.getPlayer().teleport(Locations.SPAWN.get());
                    KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).setState(PlayerState.SPAWN);
                    break;
                case "arena":
                    e.getPlayer().teleport(Locations.ARENA.get());
                    KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).setState(PlayerState.ARENA);
                    break;
            }
        }
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {
        if(e.getBlock() == null || !e.getBlock().getType().name().contains("SIGN")) return;
        if(e.getBlock().getMetadata("kpvp") == null || e.getBlock().getMetadata("kpvp").isEmpty()) return;

        if(!e.getPlayer().hasPermission("kitpvpplus.signs.remove")) {
            e.getPlayer().sendMessage(Language.PERMISSION_MESSAGE.get(e.getPlayer()));
            return;
        }

        KitPvPPlus.getInstance().getSignManager().removeSign(e.getBlock());
        e.getPlayer().sendMessage(Language.SIGN_DELETED.get(e.getPlayer()));
    }
}
