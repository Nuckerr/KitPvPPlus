package wtf.nucker.kitpvpplus.listeners;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.events.AbilityActivateEvent;
import wtf.nucker.kitpvpplus.listeners.custom.PlayerStateChangeEvent;
import wtf.nucker.kitpvpplus.managers.AbilityManager;
import wtf.nucker.kitpvpplus.managers.Locations;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.player.PlayerData;
import wtf.nucker.kitpvpplus.player.PlayerState;
import wtf.nucker.kitpvpplus.utils.APIConversion;
import wtf.nucker.kitpvpplus.utils.Logger;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class PlayerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).updateExpBar();
        KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).setState(PlayerState.SPAWN);
        e.getPlayer().teleport(Locations.SPAWN.get());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null || e.getItem().getType().equals(XMaterial.AIR.parseMaterial())) return;
        if(KitPvPPlus.DEBUG) {
            NBTItem item = new NBTItem(e.getItem());
            item.getKeys().forEach(key -> {
                Logger.debug(key + ": "+item.getString(key));
            });
        }
        if (KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).getState().equals(PlayerState.SPAWN))
            return;
        if (AbilityManager.isAbilityItem(e.getItem())) {
            Ability ability = AbilityManager.getAbility(e.getItem());
            ability.onActivate(ability, e.getItem(), e);
            Bukkit.getServer().getPluginManager().callEvent(new AbilityActivateEvent(APIConversion.fromInstanceAbility(ability), e.getItem(), e));
        }
    }

    @EventHandler
    public void onStateChange(PlayerStateChangeEvent e) {
        KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer()).updateLevel();

        boolean pass = e.getOldState() == null;
        if(!pass) pass = !(e.getOldState().equals(e.getNewState()));

        if(pass) {
            if (e.getNewState().equals(PlayerState.SPAWN)) {
                KitPvPPlus.getInstance().getSbManager().getSpawnBoard(e.getPlayer());
            } else if (e.getNewState().equals(PlayerState.ARENA)) {
                KitPvPPlus.getInstance().getSbManager().getArenaBoard(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(e.getPlayer());
        data.setState(data.getState());
        if (KitPvPPlus.getInstance().getConfig().getStringList("scoreboard.disabled-worlds").contains(e.getPlayer().getWorld().getName())) {
            KitPvPPlus.getInstance().getSbManager().clearBoard(e.getPlayer());
        }
    }
}
