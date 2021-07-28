package wtf.nucker.kitpvpplus.player;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import wtf.nucker.kitpvpplus.KitPvPPlus;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class FlatFilePlayerData {

    private final Player p;
    private final ConfigurationSection section;
    private final KitPvPPlus core = KitPvPPlus.getInstance();

    public FlatFilePlayerData(Player player) {
        this.p = player;
        if (!(core.getDataManager().getDataYaml().contains("playerdata." + p.getUniqueId()))) {
            core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + ".kills", 0);
            core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + ".deaths", 0);
            core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + ".level", 0);
            core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + ".exp", 0);
            core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + ".killstreak", 0);
            core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + ".highest-killstreak", 0);

            core.getDataManager().getDataConfig().save();
        }
        this.section = core.getDataManager().getDataYaml().getConfigurationSection("playerdata." + p.getUniqueId());
    }

    public int getKills() {
        return section.getInt("kills");
    }

    public int getDeaths() {
        return section.getInt("deaths");
    }

    public int getLevel() {
        return section.getInt("level");
    }

    public int getExp() {
        return section.getInt("exp");
    }

    public int getKillStreak() {
        return section.getInt("killstreak");
    }

    public int getHighestKillStreak() {
        return this.section.getInt("highest-killstreak");
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public int setKills(int newKillCount) {
        section.set("kills", newKillCount);
        core.getDataManager().getDataConfig().save();
        return this.getKills();
    }

    public int setDeaths(int newDeathCount) {
        section.set("deaths", newDeathCount);
        core.getDataManager().getDataConfig().save();
        return this.getKills();
    }

    public int setLevel(int newLevelCount) {
        section.set("level", newLevelCount);
        core.getDataManager().getDataConfig().save();
        return this.getKills();
    }

    public int setExp(int newExp) {
        section.set("exp", newExp);
        core.getDataManager().getDataConfig().save();
        return this.getExp();
    }

    public int setKillStreak(int newKillStreak) {
        section.set("killstreak", newKillStreak);
        if(this.getKillStreak() > this.getHighestKillStreak()) {
            section.set("highest-killstreak", this.getKillStreak());
        }
        core.getDataManager().getDataConfig().save();

        return this.getKillStreak();
    }

    public Player getPlayer() {
        return p;
    }
}
