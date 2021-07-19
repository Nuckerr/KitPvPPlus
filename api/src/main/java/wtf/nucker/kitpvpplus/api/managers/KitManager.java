package wtf.nucker.kitpvpplus.api.managers;

import wtf.nucker.kitpvpplus.KitPvPPlus;
import wtf.nucker.kitpvpplus.api.objects.Kit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nucker
 * Used for getting and managing kits
 */
public class KitManager {

    private static KitManager instance;
    private final wtf.nucker.kitpvpplus.managers.KitManager manager;

    private KitManager() {
        this.manager = KitPvPPlus.getInstance().getKitManager();
    }

    public Kit getKitById(String id) {
        return Kit.fromInstanceKit(manager.getKit(id));
    }

    public List<Kit> getKits() {
        List<Kit> res = new ArrayList<>();
        manager.getKits().forEach(kit -> res.add(Kit.fromInstanceKit(kit)));

        return res;
    }

    public static KitManager getInstance() {
        if(instance == null) instance = new KitManager();
        return instance;
    }
}
