package com.technovision.advancedgenetics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.technovision.advancedgenetics.api.genetics.Entities;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBootstrap;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.properties.conditional.ComponentMatches;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.component.CustomData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Checks resource formats with the target game's codecs and resolves packaged asset references. */
class ResourceCompatibilityTest {
    private static final Path RESOURCES = Path.of("src/main/resources");
    private static final Path ASSETS = RESOURCES.resolve("assets/advancedgenetics");

    @BeforeAll
    static void bootstrapMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        ClientBootstrap.bootstrap();
    }

    @Test
    void everyRegisteredItemHasAValidMinecraft26_2Definition() throws IOException {
        Set<String> expected = new HashSet<>(List.of("cell_analyzer", "dna_extractor", "dna_decrypter",
                "plasmid_infuser", "blood_purifier", "plasmid_injector", "metal_scalpel", "diamond_scalpel",
                "netherite_scalpel", "syringe", "overclocker", "crowbar", "dna_helix", "plasmid",
                "antiplasmid", "dragon_health_crystal"));
        for (Entities entity : Entities.values()) {
            expected.add(entity.getName() + "_cell");
            expected.add(entity.getName() + "_matter");
        }
        Set<String> actual = new HashSet<>();
        for (Path path : jsonFiles(ASSETS.resolve("items"))) {
            String filename = path.getFileName().toString();
            actual.add(filename.substring(0, filename.length() - 5));
            assertDoesNotThrow(() -> ClientItem.CODEC.parse(JsonOps.INSTANCE, readJson(path)).getOrThrow(),
                    path.toString());
        }
        assertEquals(94, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    void filledSyringeModelTracksThePersistedBloodFlag() throws IOException {
        JsonElement model = readJson(ASSETS.resolve("items/syringe.json")).getAsJsonObject().get("model");
        ComponentMatches condition = ComponentMatches.MAP_CODEC.codec().parse(JsonOps.INSTANCE, model).getOrThrow();
        var predicate = condition.predicate().predicate();
        assertFalse(predicate.matches(DataComponentMap.EMPTY));
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("filled", false);
        assertFalse(predicate.matches(DataComponentMap.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(tag)).build()));
        tag.putBoolean("filled", true);
        assertTrue(predicate.matches(DataComponentMap.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(tag)).build()));
        tag.putBoolean("purified", true);
        tag.put("genes", new CompoundTag());
        assertTrue(predicate.matches(DataComponentMap.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(tag)).build()));
    }

    @Test
    void cellTintsPreserveEveryOriginalEntityColor() throws IOException {
        for (Entities entity : Entities.values()) {
            JsonObject model = readJson(ASSETS.resolve("items/" + entity.getName() + "_cell.json"))
                    .getAsJsonObject().getAsJsonObject("model");
            JsonObject tint = model.getAsJsonArray("tints").get(0).getAsJsonObject();
            assertEquals("minecraft:constant", tint.get("type").getAsString());
            assertEquals(0xff000000 | entity.getColor(), tint.get("value").getAsInt(), entity.getName());
        }
    }

    @Test
    void allJsonParsesAndEveryModelAndTextureReferenceResolves() throws IOException {
        for (Path path : jsonFiles(RESOURCES)) {
            JsonElement json = assertDoesNotThrow(() -> readJson(path), path.toString());
            if (path.startsWith(ASSETS.resolve("models")) || path.startsWith(ASSETS.resolve("items"))
                    || path.startsWith(ASSETS.resolve("blockstates"))) {
                checkModelReferences(json, path);
            }
        }
    }

    @Test
    void legacyTextureFoldersAreIncludedInTheModernAtlases() throws IOException {
        for (String directory : List.of("items", "blocks")) {
            JsonObject atlas = readJson(RESOURCES.resolve("assets/minecraft/atlases/" + directory + ".json")).getAsJsonObject();
            boolean found = false;
            for (JsonElement source : atlas.getAsJsonArray("sources")) {
                JsonObject entry = source.getAsJsonObject();
                found |= entry.get("type").getAsString().equals("minecraft:directory")
                        && entry.get("source").getAsString().equals(directory)
                        && entry.get("prefix").getAsString().equals(directory + "/");
            }
            assertTrue(found, "Missing atlas directory for the original " + directory + " textures");
        }
    }

    @Test
    void customItemTagsHaveDisplayNames() throws IOException {
        JsonObject language = readJson(ASSETS.resolve("lang/en_us.json")).getAsJsonObject();
        Path tags = RESOURCES.resolve("data/advancedgenetics/tags/item");
        for (Path path : jsonFiles(tags)) {
            String name = tags.relativize(path).toString().replace('\\', '/').replace('/', '.');
            String key = "tag.item.advancedgenetics." + name.substring(0, name.length() - 5);
            assertTrue(language.has(key), "Missing tag translation: " + key);
            assertFalse(language.get(key).getAsString().isBlank());
        }
    }

    private static void checkModelReferences(JsonElement element, Path origin) {
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) checkModelReferences(child, origin);
        } else if (element.isJsonObject()) {
            for (var entry : element.getAsJsonObject().entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if ((key.equals("model") || key.equals("parent")) && value.isJsonPrimitive()) {
                    assertResource(value.getAsString(), "models", ".json", origin);
                } else if (key.equals("textures") && value.isJsonObject()) {
                    for (var texture : value.getAsJsonObject().entrySet()) {
                        String reference = texture.getValue().getAsString();
                        if (!reference.startsWith("#")) assertResource(reference, "textures", ".png", origin);
                    }
                } else {
                    checkModelReferences(value, origin);
                }
            }
        }
    }

    private static void assertResource(String id, String folder, String suffix, Path origin) {
        String[] parts = id.contains(":") ? id.split(":", 2) : new String[]{"minecraft", id};
        String resource = "assets/" + parts[0] + "/" + folder + "/" + parts[1] + suffix;
        boolean exists = Files.isRegularFile(RESOURCES.resolve(resource))
                || ResourceCompatibilityTest.class.getClassLoader().getResource(resource) != null;
        assertTrue(exists, origin + " refers to missing resource " + resource);
    }

    private static List<Path> jsonFiles(Path directory) throws IOException {
        try (var files = Files.walk(directory)) {
            return files.filter(path -> path.toString().endsWith(".json")).sorted().toList();
        }
    }

    private static JsonElement readJson(Path path) throws IOException {
        try (var reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader);
        }
    }
}
