package wtf.nucker.kitpvpplus.arena.lang

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import wtf.nucker.kitpvpplus.util.KotlinExtensions.component

@ConfigSerializable
data class ArenaLang(
    val arenaCreated: Component = "You have successfully created %arena%" component NamedTextColor.GREEN,
    val arenaDeleted: Component = "You have successfully deleted %arena%" component NamedTextColor.GREEN,
    val arenaNameUpdated: Component = "You have successfully changed %arena%'s name from %old_name% to %new_name%" component NamedTextColor.GREEN,
    val arenaNowRestricted: Component = "%arena% is now restricted" component NamedTextColor.GREEN,
    val arenaNoLongerRestricted: Component = "%arena% is no longer restricted" component NamedTextColor.GREEN,
    val arenaRegionUpdated: Component = "You have successfully changed %arena%'s region to %region_point_1% to %region_point_2%" component NamedTextColor.GREEN,
    val noArenasCreated: Component = "There are no arenas" component NamedTextColor.RED,
)
