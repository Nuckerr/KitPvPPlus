package wtf.nucker.kitpvpplus.abilities;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.CooldownManager;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.utils.ItemUtils;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class Fireman extends Ability {

    public Fireman() {
        super("fireman", ItemUtils.buildItem("&aFireman", XMaterial.PAPER.parseMaterial(), 1, "&7If your on fire, put your self out by right clicking this"));
        ConfigurationSection section = KitPvPPlus.getInstance().getConfig().getConfigurationSection("abilities.fireman");
        this.setItem(ItemUtils.buildItem(section.getString("displayname"), Material.valueOf(section.getString("material")),
                section.getInt("amount"), section.getStringList("lore").toArray(new String[0])));
    }

    @Override
    public void onActivate(final Ability ability, final ItemStack item, final PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;
        if (CooldownManager.abilityCooldown(event.getPlayer(), ability)) {
            event.getPlayer().sendMessage(Language.ON_COOLDOWN.get());
            return;
        }
        Player p = event.getPlayer();
        p.setFireTicks(0);
        p.playSound(p.getLocation(), XSound.ENTITY_SILVERFISH_AMBIENT.parseSound(), 1f, 1f);
        CooldownManager.addAbilityCooldown(p, ability, 120);
        p.sendMessage(Language.FIREMAN_ACTIVATION.get(p));
    }
}
