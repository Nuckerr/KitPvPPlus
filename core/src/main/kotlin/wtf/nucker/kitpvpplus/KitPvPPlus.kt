package wtf.nucker.kitpvpplus

import cloud.commandframework.kotlin.extension.buildAndRegister
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.incendo.interfaces.paper.PaperInterfaceListeners
import wtf.nucker.kitpvpplus.config.LangConfig
import wtf.nucker.kitpvpplus.config.SettingsConfig
import wtf.nucker.kitpvpplus.database.*
import wtf.nucker.kitpvpplus.economy.EconomyModule
import wtf.nucker.kitpvpplus.manager.CommandManager
import wtf.nucker.kitpvpplus.manager.ConfigManager
import wtf.nucker.kitpvpplus.statistics.StatisticsModule
import wtf.nucker.kitpvpplus.util.KotlinExtensions
import wtf.nucker.kitpvpplus.util.KotlinExtensions.editPlayerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.logger
import wtf.nucker.kitpvpplus.util.KotlinExtensions.playerData

/**
 *
 * @project KitPvPPlus
 * @author Nucker
 * @date 20/09/2022
 */
class KitPvPPlus(val bukkit: Plugin) {

    val server: Server = bukkit.server
    private val pluginManager: PluginManager = bukkit.server.pluginManager
    val commandManager: CommandManager = CommandManager(bukkit)
    val database: DataStorageMethod

    val settingsConfig: SettingsConfig = ConfigManager.loadConfig("settings.yml")
    val langConfig: LangConfig = ConfigManager.loadConfig("lang.yml")

    init {
        KotlinExtensions.plugin = this

        // Load database
        database = when(settingsConfig.database.driver) {
            DatabaseConfigSettings.DataDriver.LOCAL_STORAGE -> LocalFileStorage(settingsConfig.database)
            DatabaseConfigSettings.DataDriver.MYSQL -> MySQLDataStorage(settingsConfig.database)
            DatabaseConfigSettings.DataDriver.POSTGRES -> PostgresDataStorage(settingsConfig.database)
            DatabaseConfigSettings.DataDriver.MONGO -> MongoDataStorage(settingsConfig.database)
        }

        PaperInterfaceListeners.install(bukkit)
        commandManager.buildAndRegister("test") {
            senderType(Player::class)
            handler {
                val player = it.sender as Player
                player.editPlayerData { data ->
                    data.kills++
                }
                it.sender.sendMessage(player.playerData.kills.toString())
            }
        }

        StatisticsModule(this)
        EconomyModule(this)
    }

    fun onServerShutdown() {
        logger.info("KitPvPPlus is now shutting down")
        database.disconnect()
    }

    fun registerEvent(listener: Listener) {
        pluginManager.registerEvents(listener, bukkit)
    }
}