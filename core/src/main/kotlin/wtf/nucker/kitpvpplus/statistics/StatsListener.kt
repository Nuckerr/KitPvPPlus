package wtf.nucker.kitpvpplus.statistics

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import wtf.nucker.kitpvpplus.config.SettingsConfig
import wtf.nucker.kitpvpplus.statistics.lang.DeathLang
import wtf.nucker.kitpvpplus.util.KotlinExtensions.editPlayerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.placeholder
import wtf.nucker.kitpvpplus.util.KotlinExtensions.playBukkitSound
import wtf.nucker.kitpvpplus.util.KotlinExtensions.playerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.sendTo

class StatsListener(private val lang: DeathLang, private val settings: SettingsConfig): Listener {

    @EventHandler
    fun playerDeathEvent(event: PlayerDeathEvent) {
        val killer = event.player.killer ?: return
        // TODO: Check if they are in an arena

        // Killed Player
        event.player.editPlayerData {
            it.deaths++
            it.killStreak = 0
            it.money += settings.economySettings.deathPayment
            it.exp += settings.economySettings.deathExp
        }
        lang.killedMessage
            .placeholder("deaths", event.player.playerData.deaths)
            .placeholder("kills", event.player.playerData.kills)
            .placeholder("killer_kills", killer.playerData.kills)
            .sendTo(event.player, target = killer)

        // Killer
        killer.editPlayerData {
            it.kills++
            it.killStreak++
            it.money += settings.economySettings.killPayment
            it.exp += settings.economySettings.killExp
        }
        if(killer.playerData.topKillStreak == killer.playerData.killStreak) {
            killer.showTitle(lang.newKillStreak.placeholder("killstreak", killer.playerData.killStreak))
        }
        if(lang.newKillStreakSound != null) {
            killer.playBukkitSound(lang.newKillStreakSound)
        }

        lang.killMessage
            .placeholder("kills", killer.playerData.kills)
            .placeholder("deaths", killer.playerData.deaths)
            .placeholder("player_deaths", event.player.playerData.deaths)
            .sendTo(killer, target = event.player)

        if(settings.enableDeathMessages) {
            val deathMessage = lang.deathMessages.random()
                .placeholder("player", event.player.name)
                .placeholder("killer", killer.name)
                .placeholder("player_deaths", event.player.playerData.deaths)
                .placeholder("player_kills", event.player.playerData.kills)
                .placeholder("killer_kills", killer.playerData.kills)
                .placeholder("killer_deaths", killer.playerData.deaths)
            Bukkit.broadcast(deathMessage)
        }
    }
}