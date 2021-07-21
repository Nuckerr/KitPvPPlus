# Contributing
**I will not be accepting pull-requests that add new features to the plugin until `v1.0` is out**

Since KitPvPPlus is an open-source project, you are welcome to help fix bugs, optimise and improve the plugin. This guide will cover
how the codebase works, how to compile the plugin and good code-readability rules you should follow.

## How the codebase works
The code is split up into two modules. `Core` is the actual bukkit plugin. This is where the plugin enables, where the commands
and listeners are written as well as the WorldGuard flags, PlaceHolderAPI expansion, default abilities & data manegment.  
The `api` module is for the external-api. This is whats used when a developer wants to intergrate with (unless the specifcly specify the
core module in their depdency).  

Most of the time you will probably be working in the `core` module however I will go over the structure of each module

### `api`

**Packages:**  
  *(main dir)* `Classes that are not in packages`  
  `events` - The custom bukkit events called when certain things happen within the plugin  
  `exceptions` - Custom exceptions thrown in api methods  
  `managers` - All the managers used to get information for parts of the plugins such as kits  
  `objects` - Objects that can be used to get information about a singular thing (such as a kit)  

The api works with interfaces. The interfaces are defined in the `core` module where the actual code runs

### `core`
Though there is a basic api in here, it is 100% undocumented and not reccomended. This module is used for the actual backend of the plugin.

**Packages:**  
*main dir* `The main class as well as the PlaceHolderAPI Expansion class`  
`abilities` - The classes for the default ablities  
`commands` - The commands registerd to the plugin  
`exceptions`- Custom exceptions thrown in the plugin  
`flags` - The classes for the custom flags registerd to world guard  
`listeners` - The bukkit event listeners  
`managers` - The managers for certain parts of the plugin (kits, data etc)  
`objects` - Objects that allow to easily work with things in java  
`player` - Player data type classes  
`utils` - Utilities used for the plugin to make creating it easier. (A lot where borrowed from [here](https://github.com/Nuckerr/Utilities)  
`utils/menuUtils` - The menu system as well as menu builder methods  

## Compiling
This is a short guide on how to build the plugin for testing.

The plugin uses maven for dependencies and building. We can simply run `mvn clean package` to build the plugin.  
**If you dont have maven installed (the mvn command) you can use intellij's maven feature and click on the `M` and type `clean package`**
<!--- Insert iamge example --->
  
