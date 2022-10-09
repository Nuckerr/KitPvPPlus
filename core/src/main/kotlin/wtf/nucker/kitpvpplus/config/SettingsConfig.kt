package wtf.nucker.kitpvpplus.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import wtf.nucker.kitpvpplus.database.DatabaseConfigSettings
import wtf.nucker.kitpvpplus.statistics.config.EconomySettings

@ConfigSerializable
class SettingsConfig {

    val database: DatabaseConfigSettings = DatabaseConfigSettings()

    val economySettings: EconomySettings = EconomySettings()
    val enableDeathMessages: Boolean = true
}