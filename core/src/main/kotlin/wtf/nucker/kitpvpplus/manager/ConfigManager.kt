package wtf.nucker.kitpvpplus.manager

import org.bukkit.Bukkit
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.gson.GsonConfigurationLoader
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import wtf.nucker.kitpvpplus.adapter.*
import wtf.nucker.kitpvpplus.arena.config.ArenaConfig
import wtf.nucker.kitpvpplus.util.KotlinExtensions.logger
import java.nio.file.Path


object ConfigManager {
    private const val FOLDER_NAME = "KitPvPPlus"

    private val serializers: TypeSerializerCollection = TypeSerializerCollection.builder()
        .register(LocationAdapter())
        .register(BlockRegionAdapter())
        .register(ComponentAdapter.INSTANCE)
        .register(TitleAdapter.INSTANCE)
        .register(LocaleAdapter.INSTANCE)
        .register(ArenaAdapter())
        .build()

    inline fun <reified T : Any> loadConfig(name: String): T {
        logger.info("Loading $name")
        val loader = if(name.endsWith(".json")) retrieveLoaderJson(name) else retrieveLoaderYaml(name)

        val node = loader.load()
        val config = node.get(T::class.java)!!
        reloadConfig(name, config)
        return config
    }

    fun <T : Any> reloadConfig(name: String, configClass: T) {
        logger.info("Saving $name with ${configClass::class.qualifiedName}#${configClass.hashCode()}")
        val loader = if(name.endsWith(".json")) retrieveLoaderJson(name) else retrieveLoaderYaml(name)

        val node: ConfigurationNode = loader.load()

        node.set(configClass::class.java, configClass)
        loader.save(node)
    }

    fun retrieveLoaderYaml(name: String): YamlConfigurationLoader = YamlConfigurationLoader.builder()
        .path(Path.of(Bukkit.getPluginsFolder().absolutePath, FOLDER_NAME, name))
        .defaultOptions {
            it.shouldCopyDefaults(true)
            it.serializers { builder ->
                builder.registerAll(serializers)
            }
        }
        .indent(4)
        .nodeStyle(NodeStyle.BLOCK)
        .build()

    fun retrieveLoaderJson(name: String): GsonConfigurationLoader = GsonConfigurationLoader.builder()
        .path(Path.of(Bukkit.getPluginsFolder().absolutePath, FOLDER_NAME, name))
        .defaultOptions {
            it.shouldCopyDefaults(true)
            it.serializers { builder ->
                builder.registerAll(serializers)
            }
        }
        .indent(4)
        .build()
}