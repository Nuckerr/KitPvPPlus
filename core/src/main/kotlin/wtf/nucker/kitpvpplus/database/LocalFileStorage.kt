package wtf.nucker.kitpvpplus.database

import org.spongepowered.configurate.BasicConfigurationNode
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.gson.GsonConfigurationLoader
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import wtf.nucker.kitpvpplus.manager.ConfigManager
import wtf.nucker.kitpvpplus.`object`.PlayerData
import wtf.nucker.kitpvpplus.util.KotlinExtensions.logger
import java.util.*

class LocalFileStorage(override val configSettings: DatabaseConfigSettings) : DataStorageMethod {

    private val loader: GsonConfigurationLoader
    private val configNode: BasicConfigurationNode

    init {
        logger.warn("!-----WARNING-----!")
        logger.warn("YOU ARE USING THE LOCAL DATA STORAGE DRIVER FOR KITPVPPLUS.")
        logger.warn("IF YOU ARE USING THIS PLUGIN IN A PRODUCTION ENVIRONMENT, WE ADVISE YOU USE A DATABASE.")
        logger.warn("PAPER LIMITS THE MAX SIZE OF A FILE. SO ONLY USE THIS FOR TESTING AND SMALL SERVERS (IF THAT)")
        logger.warn("YOU HAVE BEEN WARNED")

        logger.info("Loading data.json")
        loader = ConfigManager.retrieveLoaderJson("data.json").also { logger.debug("Loaded data.json") }
        configNode = loader.load().also { logger.debug("Loaded root node") }
        loader.save(configNode)
    }

    override fun getPlayerData(uuid: UUID): PlayerData {
        //if(configNode.node(uuid).get(PlayerData::class.java) == null) this.updatePlayerData(uuid, PlayerData())
        return configNode.node(uuid.toString()).get(PlayerData::class.java)!!
    }

    override fun updatePlayerData(uuid: UUID, data: PlayerData) {
        configNode.node(uuid.toString()).set(data::class.java, data)
        loader.save(configNode)
    }

}