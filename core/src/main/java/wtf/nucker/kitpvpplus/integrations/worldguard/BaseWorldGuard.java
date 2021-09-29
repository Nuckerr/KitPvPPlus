package wtf.nucker.kitpvpplus.integrations.worldguard;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Nucker
 * @project KitPvPPlus
 * @date 29/09/2021
 */
public interface BaseWorldGuard {

    void subscribeListeners();

    void registerFlags() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
