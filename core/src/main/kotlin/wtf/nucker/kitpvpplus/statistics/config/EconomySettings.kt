package wtf.nucker.kitpvpplus.statistics.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
class EconomySettings {

    val deathPayment: Double = 0.0
    val killPayment: Double = 5.0

    val deathExp: Double = 0.0
    val killExp: Double = 0.10
}