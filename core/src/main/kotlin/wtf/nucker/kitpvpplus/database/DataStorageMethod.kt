package wtf.nucker.kitpvpplus.database

import org.bukkit.OfflinePlayer
import wtf.nucker.kitpvpplus.`object`.PlayerData
import java.util.*

interface DataStorageMethod {

    val configSettings: DatabaseConfigSettings

    fun getPlayerData(player: OfflinePlayer): PlayerData = getPlayerData(player.uniqueId)

    fun getPlayerData(uuid: UUID): PlayerData

    fun updatePlayerData(player: OfflinePlayer, data: PlayerData) = updatePlayerData(player.uniqueId, data)

    fun updatePlayerData(uuid: UUID, data: PlayerData)

    fun disconnect()
}