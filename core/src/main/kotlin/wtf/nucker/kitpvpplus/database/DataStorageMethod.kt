package wtf.nucker.kitpvpplus.database

import org.bukkit.entity.Player
import wtf.nucker.kitpvpplus.`object`.PlayerData
import java.util.*

interface DataStorageMethod {

    val configSettings: DatabaseConfigSettings

    fun getPlayerData(player: Player): PlayerData = getPlayerData(player.uniqueId)

    fun getPlayerData(uuid: UUID): PlayerData

    fun updatePlayerData(player: Player, data: PlayerData) = updatePlayerData(player.uniqueId, data)

    fun updatePlayerData(uuid: UUID, data: PlayerData)
}