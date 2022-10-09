package wtf.nucker.kitpvpplus.statistics.lang

import net.kyori.adventure.text.Component
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
}