# Advanced Genetics — NeoForge 26.1.2

A native NeoForge port of Advanced Genetics. Collect organic matter from mobs, analyze cells, decode DNA, and inject genes to gain their abilities.

Targets **Minecraft Java 26.1.2**, **NeoForge 26.1.2.109**, and **Java 25**. The release filename is **AdvancedGenetics-1.0.2+neoforge-26.1.jar**; its Minecraft dependency is explicitly 26.1.2.

## Project layout

The repository root is the **NeoForge 26.1.2** project. Its source is in `src/`, and its release builds go to `build/libs/`.

The complete original **Fabric 26.2** project is in `fabric/`, including its source, resources, tests, Gradle wrapper, and development run directory. It builds independently and is not included in the NeoForge build.

```sh
# NeoForge (from the repository root)
./gradlew build

# Fabric (from the repository root)
./fabric/gradlew -p fabric build
```

Fabric builds go to `fabric/build/libs/`. See `fabric/README.md` for Fabric dependencies and test commands.

## Installation

Install NeoForge 26.1.2.109 for Minecraft 26.1.2. Put these two files in the `mods` folder on both clients and servers:

- `AdvancedGenetics-1.0.2+neoforge-26.1.jar`
- `patchouli-neoforge-26.1-94.jar`

Patchouli is a required, separate dependency. The guidebook's content and crafting recipe are included in Advanced Genetics. Craft the **Advanced Genetics Field Guide** with a book and an iron ingot, or find it in the Advanced Genetics creative tab.

Machines accept NeoForge energy and expose NeoForge item automation capabilities. Use a compatible energy source from another mod to power the laboratory.

## Features retained

- All 38 gene abilities plus Basic DNA, 39 original mob sources, and 94 original items.
- Six machines: Cell Analyzer, DNA Extractor, DNA Decrypter, Plasmid Infuser, Blood Purifier, and Plasmid Injector.
- All 54 original recipes, textures, mob colors, tool durability, machine timing and energy costs, plasmid requirements, overclock behavior, and cooldowns.
- Player genetics saved through world reloads, death, and respawn, with client synchronization.
- Original controls: G for Teleport and H for Dragon's Breath; both can be rebound.
- `/gene add`, `/gene remove`, and `/gene clear`, retaining operator permission requirements.
- The original `config/AdvancedGenetics.toml` keys and defaults, including all gene toggles, hard mode, and gene sharing.

The guidebook adds 55 entries across four categories, covering every gene and its mob sources, the complete laboratory workflow, all machines, upgrades, blood rules, and commands.

Existing gameplay quirks are retained for parity. In particular, the original fire-charge interaction checks that Shoot Fireballs is absent or disabled. Matching DNA contributes two plasmid points, Basic DNA contributes one after assignment, and machine completion consumes an extra processing tick. The legacy cloning-machine config section remains present; the source mod did not include a playable cloning machine.

Registry IDs remain under `advancedgenetics` to preserve item and recipe identifiers. Genetics now use native NeoForge attachments; direct migration of player genetics from Fabric/CCA saves has not been implemented or tested. Use a separate world or a backup when switching loaders.

## Build and verification

Use a Java 25 JDK and the included Gradle wrapper:

```sh
./gradlew build
./gradlew runGameTestServer
./gradlew runClientSmoke
```

The playable artifact is written to `build/libs/AdvancedGenetics-1.0.2+neoforge-26.1.jar`. The sources JAR is for development.

Seven resource tests validate the guidebook links and coverage, plus the original 94 item definitions, syringe model states, cell tints, model and texture references, texture atlases, and tag names. Nineteen Advanced Genetics GameTests (plus one vanilla sanity test) exercise the complete machine workflow, timings, energy, transactions, automation, save/load, plasmids, syringes, passive abilities, damage protection, combat, death/respawn, movement, commands, and guidebook crafting.

The client smoke test creates a disposable flat world under `run-neoforge/client-smoke`, opens and reopens the creative inventory, verifies that all 94 mod items and exactly one Field Guide appear in the mod's tab, opens all six machine screens, checks synchronized energy displays and genes, triggers both ability keys, and opens all 55 guide entries. Screenshots are written to `run-neoforge/client-smoke/screenshots`. Development test mods are excluded from the playable JAR. The original Fabric project, including its client tests, is maintained separately under `fabric/`.

For interactive development, use `./gradlew runClient` or `./gradlew runServer`. Each uses a separate directory under `run-neoforge`.

## Credits and license

Original Advanced Genetics: TechnoVision (developer), KirboCabana and TheUnderTaker11 (artists). Code and original assets are licensed under MIT; see `LICENSE`.

[Original project](https://github.com/TechnoVisionDev/Advanced-Genetics) · [NeoForge](https://neoforged.net/) · [Patchouli](https://github.com/VazkiiMods/Patchouli)
