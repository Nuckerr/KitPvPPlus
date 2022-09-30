package wtf.nucker.kitpvpplus

import cloud.commandframework.kotlin.extension.buildAndRegister
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.incendo.interfaces.paper.PaperInterfaceListeners
import wtf.nucker.kitpvpplus.config.LangConfig
import wtf.nucker.kitpvpplus.config.SettingsConfig
import wtf.nucker.kitpvpplus.manager.CommandManager
import wtf.nucker.kitpvpplus.manager.ConfigManager
import wtf.nucker.kitpvpplus.util.KotlinExtensions.logger

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 20/09/2022
 */
class KitPvPPlus(val bukkit: Plugin) {

    val server: Server = bukkit.server
    val pluginManager: PluginManager = bukkit.server.pluginManager
    val commandManager: CommandManager = CommandManager(bukkit)

    val settingsConfig: SettingsConfig = ConfigManager.loadConfig("settings.yml")
    val langConfig: LangConfig = ConfigManager.loadConfig("lang.yml")

    init {
        PaperInterfaceListeners.install(bukkit)
        //logger.info(langConfig.locale.toLanguageTag())
        commandManager.buildAndRegister("test") {
            handler {
                it.sender.sendMessage(langConfig.permissionMessage)
            }
        }
    }



    fun onServerShutdown() {
        logger.info("KitPvPPlus is now shutting down")
    }
}