package wtf.nucker.kitpvpplus.arena.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import wtf.nucker.kitpvpplus.arena.Arena

@ConfigSerializable
data class ArenaConfig(
    var arenas: MutableMap<String, Arena> = mutableMapOf()
)