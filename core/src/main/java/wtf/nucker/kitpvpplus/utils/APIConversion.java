package wtf.nucker.kitpvpplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.api.events.KitLoadEvent;
import wtf.nucker.kitpvpplus.api.objects.Kit;
import wtf.nucker.kitpvpplus.api.objects.PlayerState;
import wtf.nucker.kitpvpplus.objects.Ability;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public class APIConversion {

    public static Ability toInstanceAbility(wtf.nucker.kitpvpplus.api.objects.Ability ability) {
        return new Ability(ability.getId(), ability.getAbilityItem()) {
            @Override
            public void onActivate(Ability ability, ItemStack item, PlayerInteractEvent listener) {
                ability.onActivate(ability, item, listener);
            }
        };
    }

    public static Kit fromInstanceKit(wtf.nucker.kitpvpplus.objects.Kit kit) {
        return new Kit() {
            @Override
            public String getId() {
                return kit.getId();
            }

            @Override
            public int getPrice() {
                return kit.getPrice();
            }

            @Override
            public boolean isFree() {
                return kit.isFree();
            }

            @Override
            public int getCooldown() {
                return kit.getCooldown();
            }

            @Override
            public Material getIcon() {
                return kit.getIcon();
            }

            @Override
            public List<String> getLore() {
                return kit.getLore();
            }

            @Override
            public String getDisplayname() {
                return kit.getDisplayname();
            }

            @Override
            public String getPermission() {
                return kit.getPermission();
            }

            @Override
            public void loadKit(Player receiver, Player loader) {
                kit.fillInventory(receiver);
                Bukkit.getServer().getPluginManager().callEvent(new KitLoadEvent(this, receiver, loader));
            }

            @Override
            public void loadKit(Player player) {
                kit.fillInventory(player);
                Bukkit.getServer().getPluginManager().callEvent(new KitLoadEvent(this, player, player));
            }
        };
    }

    public static wtf.nucker.kitpvpplus.api.objects.Ability fromInstanceAbility(Ability ability) {
        return new wtf.nucker.kitpvpplus.api.objects.Ability(ability.getId(), ability.getItem()) {
            @Override
            public void onActivate(ItemStack item, wtf.nucker.kitpvpplus.api.objects.Ability ability, PlayerInteractEvent event) {
                ability.onActivate(item, ability, event);
            }
        };
    }

    public static PlayerState fromInstanceState(wtf.nucker.kitpvpplus.player.PlayerState state) {
        switch (state) {
            case SPAWN:
                return PlayerState.SPAWN;
            case ARENA:
                return PlayerState.ARENA;
            case PROTECTED:
                return PlayerState.PROTECTED;
            default:
                return null;
        }
    }

    public static wtf.nucker.kitpvpplus.player.PlayerState toInstanceState(PlayerState state) {
        switch (state) {
            case SPAWN:
                return wtf.nucker.kitpvpplus.player.PlayerState.SPAWN;
            case ARENA:
                return wtf.nucker.kitpvpplus.player.PlayerState.ARENA;
            case PROTECTED:
                return wtf.nucker.kitpvpplus.player.PlayerState.PROTECTED;
            default:
                return null;
        }
    }
}
