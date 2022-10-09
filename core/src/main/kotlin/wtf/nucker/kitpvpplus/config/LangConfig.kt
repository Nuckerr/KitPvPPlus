package wtf.nucker.kitpvpplus.config

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import wtf.nucker.kitpvpplus.statistics.DeathLang
import wtf.nucker.kitpvpplus.statistics.StatisticsLang
import wtf.nucker.kitpvpplus.util.KotlinExtensions.component

@ConfigSerializable
class LangConfig {

    val missingPermission: Component = "You don't have permission" component NamedTextColor.RED
    val mustBeInGame: Component = "You must be in game to run this command" component NamedTextColor.RED

    val statistics: StatisticsLang = StatisticsLang()
    val deathMessages: DeathLang = DeathLang()
}