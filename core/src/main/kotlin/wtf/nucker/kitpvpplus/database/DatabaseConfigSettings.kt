package wtf.nucker.kitpvpplus.database

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
data class DatabaseConfigSettings(
    val driver: DataDriver = DataDriver.LOCAL_STORAGE,
    val host: String? = null,
    val port: Int? = null,
    @Comment("Only needed for SQL-based databases")
    val database: String? = null,
    val authentication: DatabaseAuthenticationSettings = DatabaseAuthenticationSettings()
) {
    @ConfigSerializable
    data class DatabaseAuthenticationSettings(
        val enabled: Boolean = false,
        val username: String = "",
        val password: String = ""
    )

    enum class DataDriver {
        LOCAL_STORAGE, MYSQL, POSTGRES, MONGO
    }
}


