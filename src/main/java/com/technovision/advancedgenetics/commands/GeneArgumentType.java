package com.technovision.advancedgenetics.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.technovision.advancedgenetics.api.genetics.Genes;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public class GeneArgumentType implements ArgumentType<Genes> {

    public static GeneArgumentType gene() {
        return new GeneArgumentType();
    }

    @Override
    public Genes parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        String geneString = reader.getString().substring(argBeginning, reader.getCursor());
        try {
            return Genes.valueOf(geneString.toUpperCase());
        } catch (Exception e) {
            throw new SimpleCommandExceptionType(Text.literal("Invalid gene name")).createWithContext(reader);
        }
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("water_breathing", "resistance", "wither_hit");
    }
}
