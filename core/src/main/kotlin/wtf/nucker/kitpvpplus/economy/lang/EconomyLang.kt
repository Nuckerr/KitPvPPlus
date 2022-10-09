package wtf.nucker.kitpvpplus.economy.lang

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import wtf.nucker.kitpvpplus.util.KotlinExtensions.component

@ConfigSerializable
class EconomyLang {
    val balanceSetTo: Component = "%player%'s balance is now %amount%" component NamedTextColor.GREEN
    val balanceAddedTo: Component = "%player%'s balance is now %amount% after you added $%amount%" component NamedTextColor.GREEN
    val balanceCleared: Component = "%player%'s balance has been cleared" component NamedTextColor.GREEN
    val balanceQueried: Component = "%player% has a balance of %amount%" component NamedTextColor.YELLOW
}