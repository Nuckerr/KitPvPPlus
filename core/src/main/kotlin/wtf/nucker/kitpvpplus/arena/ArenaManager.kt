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

    init {
        ArenaCommands(plugin.commandManager, this)
        ConfigManager.loadConfig<ArenaConfig>("arenas.json")
    }

    private val arenas: MutableMap<String, Arena>
        get() = ConfigManager.loadConfig<ArenaConfig>("arenas.json").arenas
    
    fun createArena(id: String, region: BlockRegion): Arena {
        if(arenas.keys.contains(id)) throw IllegalArgumentException("An arena with the id $id already exists")
        return object : Arena {
            override var name: Component = id component NamedTextColor.WHITE
            override val id: String = id
            override var region: BlockRegion = region
        }.also {
            arenas[it.id] = it
            ConfigManager.reloadConfig("arenas.json", ArenaConfig(arenas = arenas))
        }
    }

    fun getArena(id: String): Arena? = arenas[id]

    fun editArena(id: String, consumer: (arena: Arena) -> Unit) {
        val arena = getArena(id)!!
        consumer.invoke(arena)
        arenas[id] = arena
        ConfigManager.reloadConfig("arenas.json", ArenaConfig(arenas = arenas))
    }

    fun deleteArena(id: String) {
        arenas.remove(id)
    }
}