package wtf.nucker.kitpvpplus.statistics.lang

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import wtf.nucker.kitpvpplus.util.KotlinExtensions.adaptStringsToComponents

@ConfigSerializable
class StatisticsLang {

    val currency: Char = '$'

    val statisticsMessage: List<Component> = listOf(
        "<yellow>%player%'s statistics:",
        "<yellow>Kills: <gold>%kills%",
        "<yellow>Deaths: <gold>%deaths%",
        "<yellow>Kill to Death ratio: <gold>%kill_death_ratio%",
        "<yellow>Kill Streak: <gold>%kill_streak%",
        "<yellow>Top Kill Streak: <gold>%top_kill_streak%",
        "",
        "<yellow>Money: <gold>%money%",
        "<yellow>Level: <gold>%level%",
        "<yellow>Exp: <gold>%exp%",
        "<yellow>How far to next level: <gold>%level_percentage%",
        ).adaptStringsToComponents()

    val killsMessage: Component = MiniMessage.miniMessage().deserialize("<yellow>%player% have <gold>%kills% <yellow>kills")
    val deathMessage: Component = MiniMessage.miniMessage().deserialize("<yellow>%player% have died <gold>%deaths% <yellow>times")
    val kdrMessage: Component = MiniMessage.miniMessage().deserialize("<yellow>%player% has a KDR of <gold>%kdr%")
    val killStreakMessage: Component = MiniMessage.miniMessage().deserialize(
        "<yellow>%player%'s current kill streak is <gold>%kill_streak% <yellow>kills. <newline>" +
                "%player%'s highest kill streak is <gold>%top_kill_streak% <yellow>kills"
    )

    val moneyMessage: Component = MiniMessage.miniMessage().deserialize("<yellow>You have <gold>%money%")
    val levelMessage: Component = MiniMessage.miniMessage().deserialize(
        "<yellow>%player% is at level %level%. %player% is <gold>%level_percentage% <yellow> of the way to level %next_level%. <newline>" +
                "<yellow>%player% has <gold>%exp% <yellow>exp"
    )
}