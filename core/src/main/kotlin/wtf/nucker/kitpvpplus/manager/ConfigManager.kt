package wtf.nucker.kitpvpplus.manager

import org.bukkit.Bukkit
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.gson.GsonConfigurationLoader
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import wtf.nucker.kitpvpplus.adapter.ComponentAdapter
import wtf.nucker.kitpvpplus.adapter.LocaleAdapter
import wtf.nucker.kitpvpplus.adapter.TitleAdapter
import wtf.nucker.kitpvpplus.adapter.register
import wtf.nucker.kitpvpplus.util.KotlinExtensions.logger
import java.nio.file.Path


object ConfigManager {
    private const val FOLDER_NAME = "KitPvPPlus"

    private val serializers: TypeSerializerCollection = TypeSerializerCollection.builder()
        .register(ComponentAdapter.INSTANCE)
        .register(LocaleAdapter.INSTANCE)
        .register(TitleAdapter.INSTANCE)
        .build()

    inline fun <reified T : Any> loadConfig(name: String): T {
        logger.info("Loading $name")
        val loader: YamlConfigurationLoader = retrieveLoaderYaml(name)

        val node: CommentedConfigurationNode = loader.load()
        val config = node.get(T::class.java)!!
        reloadConfig(name, config)
        return config
    }

    fun <T : Any> reloadConfig(name: String, configClass: T) {
        logger.info("Saving $name with ${configClass::class.qualifiedName}#${configClass.hashCode()}")
        val loader: YamlConfigurationLoader = retrieveLoaderYaml(name)

        val node: CommentedConfigurationNode = loader.load()
        val config = node.get(configClass::class.java)!!

        node.set(configClass::class.java, config)
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