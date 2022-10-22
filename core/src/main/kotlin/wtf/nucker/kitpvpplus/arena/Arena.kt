package wtf.nucker.kitpvpplus.arena

import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import wtf.nucker.kitpvpplus.util.BlockRegion

interface Arena {
    val id: String
    var name: Component
    var region: BlockRegion
    var restrictedAccess: Boolean

    /**
     * @return if player has permission. Will always return true if [restrictedAccess] is false
     */
    fun hasPermission(player: CommandSender): Boolean = if(restrictedAccess) player.hasPermission("kitpvpplus.arena.$id") else true

    /**
     * @return if the player is in the arena's region and if they have permission ([hasPermission]) to the arena
     */
    operator fun contains(player: Player): Boolean {
        return hasPermission(player) && player in region
    }
}