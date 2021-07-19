package wtf.nucker.kitpvpplus.api.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import wtf.nucker.kitpvpplus.api.objects.Ability;
import wtf.nucker.kitpvpplus.api.objects.Kit;
import wtf.nucker.kitpvpplus.api.objects.PlayerState;
import wtf.nucker.kitpvpplus.listeners.custom.AbilityActivateEvent;
import wtf.nucker.kitpvpplus.listeners.custom.KitLoadEvent;
import wtf.nucker.kitpvpplus.listeners.custom.PlayerStateChangeEvent;

/**
 * @author Nucker
 * Used to listen to the plugin instance events
 */
public class Listener implements org.bukkit.event.Listener {

    /**
     * @deprecated <b>DO NOT USE THIS CLASS. THIS IS FOR CALLING CUSTOM EVENTS</b>
     */
    public Listener() {}

    @EventHandler
    public void onStateChange(PlayerStateChangeEvent e) {
        Bukkit.getPluginManager().callEvent(new StateChangeEvent(e.getPlayer(), PlayerState.fromInstanceState(e.getOldState()), PlayerState.fromInstanceState(e.getNewState())));
    }

    @EventHandler
    public void onAbilityActivate(AbilityActivateEvent e) {
        Bukkit.getPluginManager().callEvent(new wtf.nucker.kitpvpplus.api.events.AbilityActivateEvent(Ability.fromInstanceAbility(e.getAbility()), e.getItem(), e.getInteractEvent()));
    }

    @EventHandler
    public void onKitLoad(KitLoadEvent e) {
        Bukkit.getPluginManager().callEvent(new wtf.nucker.kitpvpplus.api.events.KitLoadEvent(Kit.fromInstanceKit(e.getKit()), e.getPlayer(), e.getLoader()));
    }
}
