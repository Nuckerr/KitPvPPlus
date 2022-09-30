package wtf.nucker.kitpvpplus

import org.bukkit.plugin.java.JavaPlugin

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 20/09/2022
 */
class BukkitEntryPoint: JavaPlugin() {

    lateinit var plugin: KitPvPPlus

    override fun onEnable() {
        plugin = KitPvPPlus(this)
    }

    override fun onDisable() {
        plugin.onServerShutdown()
    }
}