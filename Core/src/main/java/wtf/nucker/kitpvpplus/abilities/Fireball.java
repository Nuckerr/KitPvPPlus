package wtf.nucker.kitpvpplus.abilities;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.utils.ItemUtils;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class Fireball extends Ability {

    public Fireball() {
        super("fireball", ItemUtils.buildItem("&6Fireball shooter", XMaterial.PAPER.parseMaterial(), 10, "&7Shoot it and fireballs appear!", "&7Just right click!"));
        ConfigurationSection section = KitPvPPlus.getInstance().getConfig().getConfigurationSection("abilities.fireball");

        this.setItem(ItemUtils.buildItem(section.getString("displayname"), Material.valueOf(section.getString("material")),
                section.getInt("amount"), section.getStringList("lore").toArray(new String[section.getStringList("lore").size()])));
    }

    @Override
    public void onActivate(Ability ability, ItemStack item, PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_AIR))
            return;

        p.getWorld().spawnEntity(p.getLocation().add(0, 1, 0).add(p.getLocation().getDirection()), EntityType.FIREBALL);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 5, true, false));
        p.playSound(p.getLocation(), XSound.ENTITY_GENERIC_EXPLODE.parseSound(), 1f, 1f);
        if (event.getItem().getAmount() == 1) {
            p.setItemInHand(XMaterial.AIR.parseItem());
            return;
        }
        event.getItem().setAmount(event.getItem().getAmount() - 1);
    }
}
