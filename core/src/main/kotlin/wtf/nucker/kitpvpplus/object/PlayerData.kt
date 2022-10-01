package wtf.nucker.kitpvpplus.`object`

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.*

@Entity("PlayerData")
@ConfigSerializable
data class PlayerData(
    @Id val uuid: String? = null /*** UUID must not be null for databases. Only for local storage. */,
    var exp: Double = 0.0,
    var money: Double = 0.0,
    var kills: Int = 0,
    var deaths: Int = 0
)