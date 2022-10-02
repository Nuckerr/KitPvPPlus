package wtf.nucker.kitpvpplus.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import wtf.nucker.kitpvpplus.database.DatabaseConfigSettings

@ConfigSerializable
class SettingsConfig {

    val database: DatabaseConfigSettings = DatabaseConfigSettings()
}