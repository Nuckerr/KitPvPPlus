package wtf.nucker.kitpvpplus.abilities;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.managers.CooldownManager;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.utils.ChatUtils;
import wtf.nucker.kitpvpplus.utils.ItemUtils;
import wtf.nucker.kitpvpplus.utils.Language;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class Sonic extends Ability {

    public Sonic() {
        super("sonic", ItemUtils.buildItem("&9SONIC!!", XMaterial.PAPER.parseMaterial(), 1, "&9I AM SPEEEEEEEEEED!!!", "&7Right click to give speed!"));
        ConfigurationSection section = KitPvPPlus.getInstance().getConfig().getConfigurationSection("abilities.sonic");

        this.setItem(ItemUtils.buildItem(section.getString("displayname"), Material.valueOf(section.getString("material")),
                section.getInt("amount"), section.getStringList("lore").toArray(new String[section.getStringList("lore").size()])));
    }

    @Override
    public void onActivate(Ability ability, ItemStack item, PlayerInteractEvent event) {
        if (CooldownManager.abilityCooldown(event.getPlayer(), ability)) {
            event.getPlayer().sendMessage(Language.ON_COOLDOWN.get());
            return;
        }

        Player p = event.getPlayer();
        p.sendMessage(ChatUtils.translate(Language.SONIC_ACTIVATION.get()));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 1, true, false));
        CooldownManager.addAbilityCooldown(p, ability, 120);
    }
}
