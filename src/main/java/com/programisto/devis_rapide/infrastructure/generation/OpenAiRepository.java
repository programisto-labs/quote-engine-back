package com.programisto.devis_rapide.infrastructure.generation;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import com.programisto.devis_rapide.domaine.generation.IDevisRepository;
import com.programisto.devis_rapide.domaine.generation.entity.Autocomplete;
import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.ScenarioChunk;
import com.programisto.devis_rapide.infrastructure.generation.service.GenerationService;
import com.programisto.devis_rapide.infrastructure.generation.service.PromptService;

@Component
public class OpenAiRepository implements IDevisRepository {
    private final ChatModel chatModel;
    private final PromptService promptService;
    private final GenerationService generationService;
    private static final int CHUNK_SIZE = 20;

    OpenAiRepository(PromptService promptService, ChatModel chatModel, GenerationService generationService) {
        this.promptService = promptService;
        this.chatModel = chatModel;
        this.generationService = generationService;
    }

    private String getGenerations(DemandeClient demandeClient) {
        ChatResponse call = chatModel.call(promptService.getDevisFromDemandeClientPrompt(demandeClient));
        return call.getResults().get(0).getOutput().getContent();
    }

    @Override
    public Devis genere(DemandeClient demandeClient) {
        if (demandeClient.getUseCases().size() > CHUNK_SIZE) {
            return generationService.chunk(demandeClient, CHUNK_SIZE).stream()
                    .map(chunk -> Devis.fromJson(getGenerations(chunk))).reduce(Devis::merge).orElseThrow();
        } else {
            return Devis.fromJson(getGenerations(demandeClient));
        }
    }

    private String getSuggestions(DemandeClient demandeClient) {
        ChatResponse call = chatModel.call(promptService.getSuggestionsFromDemandeClientPrompt(demandeClient));
        return call.getResults().get(0).getOutput().getContent();
    }

    private String getSuggestions(ScenarioChunk scenarioChunk) {
        ChatResponse call = chatModel.call(promptService.getSuggestionsFromScenarioChunkPrompt(scenarioChunk));
        return call.getResults().get(0).getOutput().getContent();
    }

    private String getDemande(String rawDemande) {
        ChatResponse call = chatModel.call(promptService.getDemandeFromRawDemandePrompt(rawDemande));
        return call.getResults().get(0).getOutput().getContent();
    }

    @Override
    public Autocomplete autocomplete(DemandeClient demandeClient) {
        return Autocomplete.fromJson(getSuggestions(demandeClient));
    }

    @Override
    public Autocomplete inlineAutocomplete(ScenarioChunk scenarioChunk) {
        return Autocomplete.fromJson(getSuggestions(scenarioChunk));
    }

    @Override
    public DemandeClient extractDemande(String rawDemande) {
        return DemandeClient.fromJson(getDemande(rawDemande));
    }

}
