package wtf.nucker.kitpvpplus.api.managers;

import wtf.nucker.kitpvpplus.api.objects.Kit;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public interface KitManager {

    public Kit getKitById(String id);

    public List<Kit> getKits();
}
