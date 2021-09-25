package wtf.nucker.kitpvpplus.api.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.api.managers.CooldownManager;

/**
 * @author Nucker
 * Used to add custom abilities to the plugin
 */
public abstract class Ability {

    private final String id;
    private final ItemStack abilityItem;

    /**
     * Used to create a custom ability
     * @param id The id of your ability (MUST BE UNIQUE)
     * @param abilityItem the item your ability is based off of
     */
    public Ability(String id, ItemStack abilityItem) {
        this.id = id;
        this.abilityItem = abilityItem;
    }

    public abstract void onActivate(ItemStack item, Ability ability, PlayerInteractEvent event);


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
        CooldownManager.addAbilityCooldown(player, this, amount);
    }
}
