package wtf.nucker.kitpvpplus.managers;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.objects.Kit;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.ClockUtils;
import wtf.nucker.kitpvpplus.utils.Language;
import wtf.nucker.kitpvpplus.utils.Logger;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class CooldownManager {

    private static HashMap<UUID, Ability> abilityCooldowns;
    private static HashMap<UUID, String> kitCooldowns;

    public static void setup() {
        CooldownManager.kitCooldowns = new HashMap<>();
        CooldownManager.abilityCooldowns = new HashMap<>();
    }

    public static boolean abilityCooldown(Player p, Ability ability) {
        Logger.debug(abilityCooldowns == null);
        return abilityCooldowns.containsKey(p.getUniqueId()) && abilityCooldowns.containsValue(ability);
    }

    public static boolean kitCooldown(Player p, Kit kit) {
        //TODO: look at bug in 1.16
        return (kitCooldowns.containsKey(p.getUniqueId()) && kitCooldowns.containsValue(kit.getId()));
    }

    public static HashMap<UUID, Ability> getAbilityCooldowns() {
        return abilityCooldowns;
    }

    public static HashMap<UUID, String> getKitCooldowns() {
        return kitCooldowns;
    }

    public static void addAbilityCooldown(Player player, Ability ability, int amount) {
        abilityCooldowns.put(player.getUniqueId(), ability);
        ClockUtils.countDown(amount, runnable -> {
            if (KitPvPPlus.DEBUG) {
                player.sendMessage(String.valueOf(runnable.getAmount()));
            }
        }, runnable -> {
            abilityCooldowns.remove(player.getUniqueId(), ability);
            player.sendMessage(ChatUtils.translate(Language.NO_COOLDOWN_NOW.get().replace("%name%", ChatColor.stripColor(ability.getId()))));
        });
    }

    public static void addKitCooldown(Player player, Kit kit, int amount) {
        kitCooldowns.put(player.getUniqueId(), kit.getId());
        ClockUtils.countDown(amount, runnable -> {
            kit.setCooldownRunnable(runnable);
            if (KitPvPPlus.DEBUG) {
                player.sendMessage(String.valueOf(runnable.getAmount()));
            }
        }, runnable -> {
            kitCooldowns.remove(player.getUniqueId(), kit.getId());
            player.sendMessage(Language.NO_COOLDOWN_NOW.get(player).replace("%name%", ChatColor.stripColor(kit.getId())));
            kit.setCooldownRunnable(null);
        });
    }

    public static void addKitCooldown(OfflinePlayer player, Kit kit, int amount) {
        kitCooldowns.put(player.getUniqueId(), kit.getId());
        ClockUtils.countDown(amount, runnable -> {
            kit.setCooldownRunnable(runnable);
            if (player.isOnline()) {
                if (KitPvPPlus.DEBUG) {
                    player.getPlayer().sendMessage(String.valueOf(runnable.getAmount()));
                }
            }
        }, runnable -> {
            kitCooldowns.remove(player.getUniqueId(), kit.getId());
            kit.setCooldownRunnable(null);
            if (player.isOnline()) {
                player.getPlayer().sendMessage(Language.NO_COOLDOWN_NOW.get(player.getPlayer()).replace("%name%", ChatColor.stripColor(kit.getId())));
            }
        });
    }

}
