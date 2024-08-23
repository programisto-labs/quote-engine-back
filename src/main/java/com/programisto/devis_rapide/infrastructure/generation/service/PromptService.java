package com.programisto.devis_rapide.infrastructure.generation.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;
import com.programisto.devis_rapide.domaine.generation.entity.ScenarioChunk;
import com.programisto.devis_rapide.infrastructure.generation.error.OpenAiError;

@Service
public class PromptService {

    ////////////////////////////////////////////////////////////////
    //
    // NOTE: Il existe un system de template pour les prompts,
    // si les prompts deviennent plus complexes, il faudra utiliser
    // https://docs.spring.io/spring-ai/reference/api/prompt.html
    //
    ////////////////////////////////////////////////////////////////

    private static final String PROMPT_FOLDER = "src/main/resources";

    private PromptService() {
    }

    public enum PromptFile {
        SYSTEM_PROMPT(PROMPT_FOLDER + "/system_prompt.txt"),
        AUTOCOMPLETE_PROMPT(PROMPT_FOLDER + "/autocomplete_prompt.txt"),
        RAW_DEMANDE_PROMPT(PROMPT_FOLDER + "/raw_demande_prompt.txt"),
        AUTOCOMPLETE_CHUNK_PROMPT(PROMPT_FOLDER + "/autocomplete_chunk_prompt.txt");

        private final String filePath;

        PromptFile(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    ObjectMapper objectMapper = new ObjectMapper();

    private String readPromptFile(PromptFile promptFile) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(promptFile.getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            throw new OpenAiError("Error while reading prompt file", e);
        }
        return data.toString();
    }

    public Prompt getDevisFromDemandeClientPrompt(DemandeClient demandeClient) {
        return new Prompt(List.of(
                new SystemMessage(readPromptFile(PromptFile.SYSTEM_PROMPT)),
                new UserMessage(DemandeClient.toJson(demandeClient))));
    }

    public Prompt getSuggestionsFromDemandeClientPrompt(DemandeClient demandeClient) {
        return new Prompt(List.of(
                new SystemMessage(readPromptFile(PromptFile.AUTOCOMPLETE_PROMPT)),
                new UserMessage(DemandeClient.toJson(demandeClient))));
    }

    public Prompt getSuggestionsFromScenarioChunkPrompt(ScenarioChunk scenarioChunk) {
        return new Prompt(List.of(
                new SystemMessage(readPromptFile(PromptFile.AUTOCOMPLETE_CHUNK_PROMPT)),
                new UserMessage(ScenarioChunk.toJson(scenarioChunk))));
    }

    public Prompt getDemandeFromRawDemandePrompt(String rawDemande) {
        return new Prompt(List.of(
                new SystemMessage(readPromptFile(PromptFile.RAW_DEMANDE_PROMPT)),
                new UserMessage(rawDemande)));
    }

}
