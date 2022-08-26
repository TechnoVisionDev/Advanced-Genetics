package com.technovision.advancedgenetics.api.component;

import com.technovision.advancedgenetics.api.genetics.Genes;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.List;
import java.util.Map;

public interface EntityGeneticsComponent extends Component, ServerTickingComponent, AutoSyncedComponent {

    int geneCount();

    List<Genes> getGenes();

    Map<String, Genes> getGenesMap();

    boolean hasGene(Genes gene);

    void addGene(Genes gene);

    void addGenes(List<Genes> genes);

    void removeGene(Genes gene);

    void removeAllGenes();

    void addCooldown(String key, long seconds);

    boolean isOnCooldown(String key);
}
