> This folder contains the original Fabric 26.2 project. The repository root contains the NeoForge 26.1.2 port. From the root, build Fabric with `./fabric/gradlew -p fabric build`; from this folder, use `./gradlew build`.

![lol](https://i.imgur.com/ElQpqvH.png)

[![](https://cf.way2muchnoise.eu/full_advanced-genetics_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/advanced-genetics) [![](https://cf.way2muchnoise.eu/packs/advanced-genetics.svg)](https://www.curseforge.com/minecraft/mc-mods/advanced-genetics) [![](https://cf.way2muchnoise.eu/versions/advanced-genetics.svg)](https://www.curseforge.com/minecraft/mc-mods/advanced-genetics) [![](https://img.shields.io/badge/Discord-TechnoVision-738bd7.svg)](https://discord.gg/m5fjByfrKP)

# Advanced Genetics - Fabric Mod
_Play god by collecting, analyzing, and modifying your genetics! Advanced Genetics allows you to splice your genome with the mobs in your Minecraft world to gain their unique abilities, like shooting fireballs or climbing walls. There are dozens of abilities to discover!_

## Minecraft 26.2

This branch targets **Minecraft Java 26.2**, **Java 25**, Fabric Loader **0.19.5+**, and Fabric API **0.160.0+26.2**.

The port retains the original 38 gene abilities (plus basic DNA), 39 mob sources, 94 items, six machines, 54 recipes, configuration keys/defaults, textures, controls, and `/gene` commands. Machine timings, energy costs, overclock behavior, plasmid requirements, and ability cooldowns retain their original values. The configuration file remains `config/AdvancedGenetics.toml`.

### Install

Build with `./gradlew build`, then place `build/libs/AdvancedGenetics-1.0.4+26.2.jar` and Fabric API for 26.2 in the `mods` directory of a Fabric 26.2 installation. Install the mod on both the server and clients. Team Reborn Energy 5.0.0, Forge Config API Port 26.2.1, and Cardinal Components base/entity 8.0.1 are bundled in the mod JAR. The `-sources.jar` is for development, not installation.

### Development and verification

Use a Java 25 JDK. The included wrapper uses Gradle 9.5.1 and Loom 1.17.20. Minecraft 26.2 uses unobfuscated Mojang names; Yarn mappings and remapping tasks are no longer needed.

```sh
./gradlew build
./gradlew runGameTest
./gradlew runClientGameTest
```

The build runs six resource compatibility tests and 19 server GameTests. Server GameTests cover the complete machine workflow, original timing/energy use, automation, save/load, plasmid and syringe rules, passive effects, damage protection, combat, death/respawn, movement, and command permissions. The client tests open the creative tab and all six machine menus, verify energy displays and gene synchronization, and exercise the teleport/dragon-breath keys and cooldowns. Screenshots are saved under `build/run/clientGameTest/screenshots/`. Test code is kept in separate source sets and is excluded from the playable JAR.

Existing 1.19.2 worlds have not been tested for direct upgrade; the runtime tests check 26.2 save/load and the preserved genetics data fields.

# Downloads

### Recommended Releases

The latest stable releases can always be found on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/advanced-genetics).<br/>
The top file in the list is the latest recommended release!

### Development Builds

If you are looking for the latest bleeding edge build, you can find unstable releases [here](https://github.com/TechnoVisionDev/Advanced-Genetics/releases).</br>
Use these builds with caution, and please do not put these in modpacks. They may contain major bugs!

# Issues and Suggestions
To report issue, add a translation, or make a feature suggestion, please open a new issue in the `Issues` tab. This will require that you create a free GitHub account. **Please always include the version of the Advanced Genetics that you are using!**

# Credits

* TechnoVision - Developer
* KirboCabana - Artist
* TheUnderTaker11 - Artist

# License

Advanced Genetics and all assets are licensed under the **MIT** license.
