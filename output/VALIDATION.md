# Advanced Genetics NeoForge release validation

Target: Minecraft 26.1.2, NeoForge 26.1.2.109, Java 25, Patchouli 26.1-94.

- Clean rebuild under the Advanced Genetics name: passed.
- Release and sources JARs: no references to the accidental former name; release metadata, creative tab, and field guide use Advanced Genetics.
- Existing Fabric release and sources JARs: verified to retain Advanced Genetics branding.
- Seven resource tests: passed.
- Nineteen Advanced Genetics GameTests plus one vanilla sanity test: all 20 passed.
- Real client: all six machine menus and their energy displays passed.
- Player gene synchronization and both ability keys: passed.
- Patchouli: four categories and all 55 entries loaded and opened; recipe links verified.
- Original content: all 94 items, 39 mob sources, 38 abilities plus Basic DNA retained.
- Original 54 recipes and 79 textures: unchanged.
- Release JAR: contains NeoForge metadata and the guidebook; contains no Fabric metadata, former Fabric library references, or development test classes.

Craft the guide with a book and an iron ingot. Install both release JARs listed in INSTALL.txt. SHA-256 checksums are in SHA256SUMS.txt.

The original gameplay quirks and cross-loader save limitations are documented in the repository README.md.

## Inventory crash fix — September 12, 2026

Removed the manual Field Guide insertion from the creative tab; Patchouli adds it through the book definition. The expanded client test reproduced the original duplicate-item crash before the fix. After the fix, the build and all seven resource tests passed, and the real client opened and reopened the creative inventory with all 94 mod items and exactly one Field Guide. The same client run also passed all six machine screens, gene synchronization, both ability keys, and all 55 guide entries. The release JAR and SHA-256 checksums in this folder have been refreshed.
