rootProject.name = "KitPvPPlus"

include("core")
pluginManagement {
    plugins {
        kotlin("jvm") version "1.7.10"
        id("com.github.johnrengelman.shadow") version "7.1.2"
        id("io.papermc.paperweight.userdev") version "1.3.8"
        id("xyz.jpenilla.run-paper") version "1.0.6"
        id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    }
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}