package com.technovision.advancedgenetics.api.components;

import com.technovision.advancedgenetics.api.genetics.Genes;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.List;
import java.util.Map;

public interface EntityGeneticsComponent extends Component, ServerTickingComponent {

    int geneCount();

    List<Genes> getGenes();

    Map<String, Genes> getGenesMap();

    boolean containsGene(Genes gene);

    void addGene(Genes gene);

    void addGenes(List<Genes> genes);

    void removeGene(Genes gene);
}
