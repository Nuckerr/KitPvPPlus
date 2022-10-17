package wtf.nucker.kitpvpplus.arena

import net.kyori.adventure.text.Component
import wtf.nucker.kitpvpplus.util.BlockRegion

interface Arena {
    val id: String
    var name: Component
    var region: BlockRegion

}