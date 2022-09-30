import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper")
    id("net.minecrell.plugin-yml.bukkit")
}

group = "wtf.nucker"

repositories {
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://jitpack.io")
    maven(url = "https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-gson:4.1.2")
    implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")
    implementation("cloud.commandframework:cloud-core:1.7.1")
    implementation("cloud.commandframework:cloud-paper:1.7.1")
    implementation("cloud.commandframework:cloud-brigadier:1.7.1")
    implementation("cloud.commandframework:cloud-kotlin-extensions:1.7.1")
    implementation("cloud.commandframework:cloud-minecraft-extras:1.7.1")
    implementation("org.incendo.interfaces:interfaces-kotlin:1.0.0-SNAPSHOT")
    implementation("org.incendo.interfaces:interfaces-paper:1.0.0-SNAPSHOT")
    implementation("net.kyori:adventure-extra-kotlin:4.11.0")
    implementation("org.apache.logging.log4j:log4j-api:2.19.0")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation("org.reflections:reflections:0.10.2")
}

tasks {
    runServer {
        jvmArgs("-Dcom.mojang.eula.agree=true")
    }

    shadowJar {
        archiveFileName.set("KitPvPPlus-${project.version}.jar")
    }

    build {
        dependsOn(shadowJar)
    }

    assemble {
        dependsOn(reobfJar)
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    main = "wtf.nucker.kitpvpplus.BukkitEntryPoint"
    apiVersion = "1.19"

    name = rootProject.name
    description = project.description
    version = project.version.toString()
    authors = listOf("Nucker")
    website = "nucker.me"
    prefix = rootProject.name
}