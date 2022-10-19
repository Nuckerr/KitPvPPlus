package wtf.nucker.kitpvpplus.arena

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import wtf.nucker.kitpvpplus.KitPvPPlus
import wtf.nucker.kitpvpplus.arena.command.ArenaCommands
import wtf.nucker.kitpvpplus.arena.config.ArenaConfig
import wtf.nucker.kitpvpplus.manager.ConfigManager
import wtf.nucker.kitpvpplus.util.BlockRegion
import wtf.nucker.kitpvpplus.util.KotlinExtensions.component

class ArenaManager(plugin: KitPvPPlus) {

    val lang = plugin.langConfig.arena
    private val config: ArenaConfig = ConfigManager.loadConfig("arenas.json")
    val arenas: List<Arena>
        get() = config.arenas.values.toList()


    init {
        ArenaCommands(plugin.commandManager, this)
        ConfigManager.loadConfig<ArenaConfig>("arenas.json")
    }
    
    fun createArena(id: String, region: BlockRegion): Arena {
        if(config.arenas.keys.contains(id)) throw IllegalArgumentException("An arena with the id $id already exists")
        return object : Arena {
            override var name: Component = id component NamedTextColor.WHITE
            override val id: String = id
            override var region: BlockRegion = region
        }.also {
            config.arenas[it.id] = it
            ConfigManager.reloadConfig("arenas.json", config)
        }
    }

    fun getArena(id: String): Arena? = config.arenas[id]

    fun editArena(id: String, consumer: (arena: Arena) -> Unit) {
        val arena = getArena(id)!!
        consumer.invoke(arena)
        config.arenas[id] = arena
        ConfigManager.reloadConfig("arenas.json", config)
    }

    fun deleteArena(id: String) {
        config.arenas.remove(id)
        ConfigManager.reloadConfig("arenas.json", config)
    }

}