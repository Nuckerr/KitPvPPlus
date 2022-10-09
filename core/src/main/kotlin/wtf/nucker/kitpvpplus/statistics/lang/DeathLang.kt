package wtf.nucker.kitpvpplus.statistics.lang

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Sound
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import wtf.nucker.kitpvpplus.util.KotlinExtensions.component

@ConfigSerializable
class DeathLang {

    val killMessage: Component = "You killed %player%" component NamedTextColor.GREEN
    val killedMessage: Component = "You were killed by %player%" component NamedTextColor.RED

    val newKillStreak: Title = Title.title(
        "New high score!" component NamedTextColor.YELLOW,
        "Your top killstreak is now %killstreak%" component NamedTextColor.GOLD
    )

    @Suppress("RedundantNullableReturnType")
    @Comment("Remove for no sound")
    val newKillStreakSound: Sound? = Sound.UI_TOAST_CHALLENGE_COMPLETE

    val deathMessages: List<Component> = listOf(
        "%player% was killed by %killer%" component NamedTextColor.YELLOW
    )
}