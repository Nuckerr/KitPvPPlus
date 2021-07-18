package wtf.nucker.kitpvpplus;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.managers.PlayerBank;
import wtf.nucker.kitpvpplus.player.PlayerData;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class KitPvPPlaceholderExpansion extends PlaceholderExpansion {

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "KitPvPPlus";
    }

    @Override
    public String getAuthor() {
        return KitPvPPlus.getInstance().getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return KitPvPPlus.getInstance().getDescription().getVersion();
    }

    @Override
    public String getRequiredPlugin() {
        return "KitPvPPlus";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        PlayerData data = KitPvPPlus.getInstance().getDataManager().getPlayerData(p.getPlayer());

        switch (params) {
            // Statistics
            case "kpvp_deaths":
                return String.valueOf(data.getDeaths());
            case "kpvp_kills":
                return String.valueOf(data.getKills());
            case "kpvp_exp":
                return String.valueOf(data.getExp());
            case "kpvp_level":
                return String.valueOf(data.getLevel());
            case "kpvp_bal":
                return String.valueOf(new PlayerBank(p).getBal());
            default:
                return null;
        }
    }
}
