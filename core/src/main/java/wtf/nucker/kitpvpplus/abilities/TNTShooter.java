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
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.dataHandelers.PlayerState;
import wtf.nucker.kitpvpplus.objects.Ability;
import wtf.nucker.kitpvpplus.utils.ItemUtils;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class TNTShooter extends Ability {

    private final ConfigurationSection section = KitPvPPlus.getInstance().getConfig().getConfigurationSection("abilities.tnt-shooter");

    public TNTShooter() {
        super("tnt_shooter", ItemUtils.buildItem("&cTNT Shooter", XMaterial.PAPER.parseMaterial(), 10, "&7Shoot oppenents with primed tnt", "&7Just right click"));
        this.setItem(ItemUtils.buildItem(section.getString("displayname"), Material.valueOf(section.getString("material")),
                section.getInt("amount"), section.getStringList("lore").toArray(new String[0])));
    }

    @Override
    public void onActivate(Ability ability, ItemStack item, PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_AIR))
            return;
        if (!KitPvPPlus.getInstance().getDataManager().getPlayerData(p).getState().equals(PlayerState.ARENA))
            return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.getClickedBlock().getWorld().spawnEntity(
                    event.getClickedBlock().getLocation().add(event.getBlockFace().getModX(), event.getBlockFace().getModY(), event.getBlockFace().getModZ()),
                    EntityType.PRIMED_TNT
            );
        } else {
            p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
        }
        p.playSound(p.getLocation(), XSound.UI_BUTTON_CLICK.parseSound(), 1f, 1f);

        if (event.getItem().getAmount() == 1) {
            p.setItemInHand(XMaterial.AIR.parseItem());
            return;
        }
        event.getItem().setAmount(event.getItem().getAmount() - 1);
    }
}
