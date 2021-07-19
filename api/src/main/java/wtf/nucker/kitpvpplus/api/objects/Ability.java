package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.managers.CooldownManager;

/**
 * @author Nucker
 * Used to add custom abilities to the plugin
 */
public abstract class Ability {

    private final String id;
    private final ItemStack abilityItem;

    public Ability(String id, ItemStack abilityItem) {
        this.id = id;
        this.abilityItem = abilityItem;
    }

    public abstract void onActivate(ItemStack item, Ability ability, PlayerInteractEvent event);

    /**
     * Used to transfer an ability to the ability class in the plugin instance
     * @param ability The API version of the ability you want to transfer
     * @return The transferred version of the ability
     */
    public static wtf.nucker.kitpvpplus.objects.Ability toInstanceAbility(Ability ability) {
        return new wtf.nucker.kitpvpplus.objects.Ability(ability.getId(), ability.getAbilityItem()) {
            @Override
            public void onActivate(wtf.nucker.kitpvpplus.objects.Ability ability1, ItemStack item, PlayerInteractEvent listener) {
                ability1.onActivate(ability1, item, listener);
            }
        };
    }

    /**
     * Used to transfer a plugin instance instance ability to a api instance ability
     * @param ability the plugin version of the ability
     * @return the api verison of the plugin
     */
    public static Ability fromInstanceAbility(wtf.nucker.kitpvpplus.objects.Ability ability) {
        return new Ability(ability.getId(), ability.getItem()) {
            @Override
            public void onActivate(ItemStack item, Ability ability, PlayerInteractEvent event) {
                ability.onActivate(item, ability, event);
            }
        };
    }

    /**
     * @return the id of the ability
     */
    public String getId() {
        return id;
    }

    /**
     * @return the item of the ability
     */
    public ItemStack getAbilityItem() {
        return abilityItem;
    }

    /**
     * Used for deacreasing the amount of the item by one
     * @param item the item you wan to decrease
     */
    protected void decreaseItem(ItemStack item) {
        if(item.getAmount() == 0) {
            item.setType(Material.AIR);
            return;
        }

        item.setAmount(item.getAmount() - 1);
    }

    /**
     * Used for putting a player on cooldown (usually after they use the ability
     * @param player the player your putting on cooldown
     * @param amount the amount of seconds till the cooldown ends
     */
    protected void putOnCooldown(Player player, int amount) {
        CooldownManager.addAbilityCooldown(player, toInstanceAbility(this), amount);
    }
}
