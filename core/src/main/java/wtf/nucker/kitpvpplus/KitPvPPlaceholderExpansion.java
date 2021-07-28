package wtf.nucker.kitpvpplus;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.utils.Language;

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
        return Language.formatPlaceholders(p, params);
    }
}
