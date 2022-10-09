package wtf.nucker.kitpvpplus.statistics

import wtf.nucker.kitpvpplus.KitPvPPlus

class StatisticsModule(private val plugin: KitPvPPlus) {

    private val commandManager = plugin.commandManager
    private val messages = plugin.langConfig.statistics

    init {
        StatCommands(commandManager, messages)
        plugin.registerEvent(StatsListener(plugin.langConfig.deathMessages, plugin.settingsConfig))
    }
}