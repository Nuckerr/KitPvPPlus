package wtf.nucker.kitpvpplus.api;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.api.objects.Ability;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 17/07/2021
 */
public class ExampleAbility extends Ability {

    public ExampleAbility() {
        super("example_ability", new ItemStack(Material.APPLE));
    }

    @Override
    public void onActivate(ItemStack item, Ability ability, PlayerInteractEvent event) {

    }
}
