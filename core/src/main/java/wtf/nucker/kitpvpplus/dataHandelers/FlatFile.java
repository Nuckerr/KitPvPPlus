package wtf.nucker.kitpvpplus.dataHandelers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.exceptions.InsufficientBalance;
import wtf.nucker.kitpvpplus.listeners.custom.PlayerStateChangeEvent;
import wtf.nucker.kitpvpplus.managers.DataManager;
import wtf.nucker.kitpvpplus.managers.PlayerBank;
import wtf.nucker.kitpvpplus.objects.Kit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 03/08/2021
 */
public class FlatFile implements PlayerData {

    private final OfflinePlayer p;
    private final KitPvPPlus core = KitPvPPlus.getInstance();

    public FlatFile(OfflinePlayer player) {
        this.p = player;
        if (!(core.getDataManager().getDataYaml().contains("playerdata." + p.getUniqueId()))) {
            this.reset("kills");
            this.reset("deaths");
            this.reset("level");
            this.reset("exp");
            this.reset("killstreak");
            this.reset("highest-killstreak");

            core.getDataManager().getDataConfig().save();
        }
    }

    @Override
    public void updateExp(int newAmount) {
        this.set("exp", newAmount);
    }

    @Override
    public void incrementDeaths() {
        this.set("deaths", this.getDeaths() + 1);
    }

    @Override
    public void incrementKills() {
        this.set("kills", this.getKills() + 1);
    }

    @Override
    public void updateLevel() {
        int exp = this.getExp();
        String levelAsString = String.valueOf(exp);
        if(levelAsString.length() > 2) {
            levelAsString = levelAsString.substring(0, levelAsString.length() - 2);
        }else {
            levelAsString = "";
        }
        if(levelAsString.isEmpty()) levelAsString = "0";
        int level = Integer.parseInt(levelAsString);

        this.set("level", level);
        this.updateExpBar();
    }

    @Override
    public void incrementKillStreak() {
        this.set("killstreak", this.getKillStreak() + 1);
        if(this.getKillStreak() > this.getTopKillStreak()) {
            this.set("highest-killstreak", this.getKillStreak());
        }
    }

    @Override
    public void resetKillStreak() {
        this.reset("killstreak");
    }

    @Override
    public void resetData() {
        this.reset("kills");
        this.reset("deaths");
        this.reset("level");
        this.reset("exp");
        this.reset("killstreak");
        this.reset("highest-killstreak");
    }

    @Override
    public void deleteData() {
        core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId(), null);
    }

    @Override
    public void setState(PlayerState newState) {
        if(!p.isOnline()) return;
        PlayerState oldState = this.getState();
        DataManager.getPlayerStates().remove(p);
        DataManager.getPlayerStates().put(p, newState);
        Bukkit.getPluginManager().callEvent(new PlayerStateChangeEvent(p.getPlayer(), oldState, newState));
    }

    @Override
    public int getExp() {
        return this.get("exp");
    }

    @Override
    public int getDeaths() {
        return this.get("deaths");
    }

    @Override
    public int getKills() {
        return this.get("kills");
    }

    @Override
    public int getLevel() {
        return this.get("level");
    }

    @Override
    public int getKillStreak() {
        return this.get("killstreak");
    }

    @Override
    public int getTopKillStreak() {
        return this.get("highest-killstreak");
    }

    @Override
    public double getKDR() {
        if(this.getDeaths() <= 0 || this.getKills() <= 0) return 0;
        return ((double) this.getKills()) / ((double) this.getDeaths());
    }

    @Override
    public PlayerState getState() {
        return DataManager.getPlayerStates().get(p);
    }

    @Override
    public void updateExpBar() {
        if(!p.isOnline()) return;
        float exp = this.getExp() - (this.getLevel() * 100);
        String modifiedExp = String.valueOf(exp).replace(".", "");
        modifiedExp = "0."+modifiedExp;
        p.getPlayer().setExp(Float.parseFloat(modifiedExp));
        p.getPlayer().setLevel(this.getLevel());
    }

    @Override
    public List<Kit> getOwnedKits() {
        List<Kit> res = new ArrayList<>();
        for (Kit kit : KitPvPPlus.getInstance().getKitManager().getKits()) {
            if (this.ownsKit(kit)) {
                res.add(kit);
            }
        }
        return res;
    }

    @Override
    public boolean ownsKit(Kit kit) {
        if(!p.isOnline()) return false;
        if (p.isOp()) return true;
        if (p.getPlayer().hasPermission(kit.getPermission())) return true;
        return this.getStringList("owned-kits").contains(kit.getId());
    }



    @Override
    public List<Kit> purchaseKit(Kit kit) {
        PlayerBank bank = new PlayerBank(p);
        if (bank.getBal() < kit.getPrice()) {
            throw new InsufficientBalance();
        }
        bank.setBal(bank.getBal() - kit.getPrice());
        List<String> res = this.getStringList("owned-kits");
        res.add(kit.getId());
        this.setStringList("owned-kits", res);
        return this.getOwnedKits();
    }

    @Override
    public OfflinePlayer getPlayer() {
        return this.p;
    }

    private void reset(String path) {
        core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + "." + path, 0);
    }

    private void set(String path, int value) {
        core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + "." + path, value);
        core.getDataManager().getDataConfig().save();
    }

    private int get(String path) {
        return core.getDataManager().getDataYaml().getInt("playerdata." + p.getUniqueId() + "." + path);
    }

    private List<String> getStringList(String path) {
        return core.getDataManager().getDataYaml().getStringList("playerdata." + p.getUniqueId() + "." + path);
    }

    private void setStringList(String path, List<String> val) {
        core.getDataManager().getDataYaml().set("playerdata." + p.getUniqueId() + "." + path, val);
    }
}
