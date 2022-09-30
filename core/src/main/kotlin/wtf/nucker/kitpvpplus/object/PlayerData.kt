package wtf.nucker.kitpvpplus.`object`

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class PlayerData(
    var exp: Double = 0.0,
    var money: Double = 0.0,
    var kills: Int = 0,
    var deaths: Int = 0
)