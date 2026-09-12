package com.technovision.advancedgenetics.gametest;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.AdvancedGenetics;

import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

@Mod("advancedgenetics_test")
public class NeoForgeGameTests {
    public NeoForgeGameTests(IEventBus bus) { bus.addListener(NeoForgeGameTests::register); }
    private static void register(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(id("default"));
        for (var suite : new Object[]{new GeneticsGameTests(), new MachineTimingGameTests(), new GeneAbilityGameTests(), new ScalpelGameTests()}) {
            for (var method : suite.getClass().getMethods()) {
                if (!method.isAnnotationPresent(GameTest.class)) continue;
                var name = id(suite.getClass().getSimpleName().toLowerCase(java.util.Locale.ROOT) + "/" + method.getName().toLowerCase(java.util.Locale.ROOT));
                event.registerTest(name, new FunctionGameTestInstance(ResourceKey.create(Registries.TEST_FUNCTION, name),
                        new TestData<>(environment, id("empty"), 400, 1, true)) {
                    @Override public void run(GameTestHelper helper) {
                        try { method.invoke(suite, helper); }
                        catch (ReflectiveOperationException failure) {
                            Throwable cause = failure.getCause() == null ? failure : failure.getCause();
                            if (cause instanceof RuntimeException runtime) throw runtime;
                            throw new RuntimeException(cause);
                        }
                    }
                });
            }
        }
    }
    private static Identifier id(String path) { return Identifier.fromNamespaceAndPath("advancedgenetics", path); }
}
