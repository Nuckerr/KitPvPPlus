package wtf.nucker.kitpvpplus.statistics

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.bukkit.parsers.PlayerArgument
import cloud.commandframework.kotlin.extension.buildAndRegister
import org.bukkit.entity.Player
import wtf.nucker.kitpvpplus.KitPvPPlus
import wtf.nucker.kitpvpplus.util.KotlinExtensions.placeholder
import wtf.nucker.kitpvpplus.util.KotlinExtensions.playerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.sendTo

class StatisticsModule(plugin: KitPvPPlus) {

    private val commandManager = plugin.commandManager
    private val messages = plugin.langConfig.statistics

    init {
        commandManager.buildAndRegister(
            "stats",
            ArgumentDescription.of("View your statistics"),
            arrayOf("statistics")
        ) {
            argument(PlayerArgument.optional("target"))
            handler {
                if(it.sender !is Player && it.getOptional<Player>("target").isEmpty) {
                    it.sender.sendMessage(plugin.langConfig.mustBeInGame)
                    return@handler
                }
                val target: Player = it.getOrDefault<Player>("target", null) ?: (it.sender as Player)
                val data = target.playerData

                messages.statisticsMessage
                    .placeholder("kills", data.kills)
                    .placeholder("deaths", data.deaths)
                    .placeholder("top_kill_streak", data.topKillStreak)
                    .placeholder("kill_streak", data.killStreak)
                    .placeholder("kill_death_ratio", data.killDeathRatio)
                    .placeholder("exp", data.exp)
                    .placeholder("level", data.level)
                    .placeholder("level_percentage", "${data.nextLevelPercentage}%")
                    .placeholder("level_percentage_raw", data.nextLevelPercentage)
                    .placeholder("money", "${messages.currency}${data.money}").sendTo(it.sender, target = target)
            }
        }
        plugin.registerEvent(StatsListener(plugin.langConfig.deathMessages, plugin.settingsConfig))
    }
}