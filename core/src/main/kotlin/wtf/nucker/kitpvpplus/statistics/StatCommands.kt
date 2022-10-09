package wtf.nucker.kitpvpplus.statistics

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument
import cloud.commandframework.kotlin.extension.buildAndRegister
import net.kyori.adventure.text.Component
import org.bukkit.OfflinePlayer
import wtf.nucker.kitpvpplus.manager.CommandManager
import wtf.nucker.kitpvpplus.statistics.lang.StatisticsLang
import wtf.nucker.kitpvpplus.util.KotlinExtensions.argumentOrSender
import wtf.nucker.kitpvpplus.util.KotlinExtensions.placeholder
import wtf.nucker.kitpvpplus.util.KotlinExtensions.playerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.sendTo

class StatCommands(private val commandManager: CommandManager, private val messages: StatisticsLang) {

    init {
        allStatsCommand()
        registerIndividualStatCommand("kills", "View your kills") {
            messages.killsMessage.placeholder("kills", it.playerData.kills)
        }
        registerIndividualStatCommand("deaths", "View your deaths") {
            messages.deathMessage.placeholder("deaths", it.playerData.deaths)
        }
        registerIndividualStatCommand("kdr", "View your kill-death ratio") {
            messages.kdrMessage.placeholder("kdr", it.playerData.killDeathRatio)
        }
        registerIndividualStatCommand("killstreak", "View your killstreak") {
            messages.killStreakMessage
                .placeholder("kill_streak", it.playerData.killStreak)
                .placeholder("top_kill_streak", it.playerData.topKillStreak)
        }
        registerIndividualStatCommand("money", "View your kill-death ratio", arrayOf("bal", "balance")) {
            messages.moneyMessage.placeholder("money", "${messages.currency}${it.playerData.money}")
        }
        registerIndividualStatCommand("level", "View your kill-death ratio", arrayOf("lvl", "exp", "experience")) {
            messages.levelMessage
                .placeholder("level", it.playerData.level)
                .placeholder("next_level", it.playerData.level + 1)
                .placeholder("level_percentage", "${it.playerData.nextLevelPercentage}%")
                .placeholder("exp", it.playerData.exp)
        }
    }

    private fun allStatsCommand() {
        commandManager.buildAndRegister(
            "stats",
            ArgumentDescription.of("View your statistics"),
            arrayOf("statistics")
        ) {
            argument(OfflinePlayerArgument.optional("target"))
            handler {
                if(!it.argumentOrSender()) return@handler
                val target: OfflinePlayer = it.getOrDefault<OfflinePlayer>("target", null) ?: (it.sender as OfflinePlayer)
                val data = target.playerData

                messages.statisticsMessage
                    .placeholder("kills", data.kills)
                    .placeholder("deaths", data.deaths)
                    .placeholder("top_kill_streak", data.topKillStreak)
                    .placeholder("kill_streak", data.killStreak)
                    .placeholder("kill_death_ratio", data.killDeathRatio)
                    .placeholder("exp", data.exp)
                    .placeholder("level", data.level)
                    .placeholder("next_level", data.level + 1)
                    .placeholder("level_percentage", "${data.nextLevelPercentage}%")
                    .placeholder("level_percentage_raw", data.nextLevelPercentage)
                    .placeholder("money", "${messages.currency}${data.money}").sendTo(it.sender, target = target)
            }
        }
    }

    private fun registerIndividualStatCommand(name: String, description: String, aliases: Array<String> = emptyArray(), message: (OfflinePlayer) -> Component) {
        commandManager.buildAndRegister(
            name,
            ArgumentDescription.of(description),
            aliases) {
            argument(OfflinePlayerArgument.optional("target"))
            handler {
                if(!it.argumentOrSender()) return@handler
                val target: OfflinePlayer = it.getOrDefault<OfflinePlayer>("target", null) ?: (it.sender as OfflinePlayer)
                message.invoke(target).sendTo(it.sender, target = target)
            }
        }
    }
}