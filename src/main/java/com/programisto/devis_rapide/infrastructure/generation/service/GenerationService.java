package com.programisto.devis_rapide.infrastructure.generation.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.google.common.collect.Lists;
import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;

@Service
public class GenerationService {
    public List<DemandeClient> chunk(DemandeClient demandeClient, int chunkSize) {
        return Lists.partition(demandeClient.getUseCases(), chunkSize).stream()
                .map(demandeClient::withUseCases).toList();
    }
}
