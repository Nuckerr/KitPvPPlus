package wtf.nucker.kitpvpplus.flags;

import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 15/07/2021
 */
public class SpawnFlag extends StateFlag {

    public SpawnFlag() {
        super("kpvp-spawn", false);
    }
}
