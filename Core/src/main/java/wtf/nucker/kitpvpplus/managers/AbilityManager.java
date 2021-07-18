package wtf.nucker.kitpvpplus.managers;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.objects.Ability;

import java.util.HashMap;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class AbilityManager {

    private static HashMap<String, Ability> abilities;

    public AbilityManager() {
        AbilityManager.abilities = new HashMap<>();
    }

    public void registerAbility(Ability ability) {
        if (getAbilities().containsValue(ability)) return;
        getAbilities().put(ability.getId(), ability);
    }

    public static boolean isAbilityItem(ItemStack item) {
        return new NBTItem(item).hasKey("ability");
    }

    public static HashMap<String, Ability> getAbilities() {
        return abilities;
    }

    public static Ability getAbility(String name) {
        return abilities.get(name);
    }

    public static Ability getAbility(ItemStack item) {
        if (!isAbilityItem(item)) return null;
        return getAbility(new NBTItem(item).getString("ability").replace("\"", ""));
    }
}
