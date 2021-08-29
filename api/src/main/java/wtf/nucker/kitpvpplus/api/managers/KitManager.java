package wtf.nucker.kitpvpplus.api.managers;

import wtf.nucker.kitpvpplus.api.objects.Kit;

import java.util.List;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 21/07/2021
 */
public interface KitManager {

    /**
     * @param id The id of the kit you are trying to get (not case-sensitive)
     * @return API instance of that kit (null if the kit cannot be found)
     */
    public Kit getKitById(String id);

    /**
     * @return a list of all the kits on the server
     */
    public List<Kit> getKits();

    /**
     * Creates a kit
     * @param id The id of the kit
     * @return an API instance of the created kit
     */
    public Kit createKit(String id);
}
