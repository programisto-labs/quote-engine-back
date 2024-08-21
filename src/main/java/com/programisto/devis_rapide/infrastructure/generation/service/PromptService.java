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

    private PromptService() {
    }

    ObjectMapper objectMapper = new ObjectMapper();

    private String getSystemText() {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/system_prompt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            throw new OpenAiError("Error while reading system prompt");
        }
        return data.toString();
    }

    private String getSuggestionsText() {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/autocomplete_prompt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            throw new OpenAiError("Error while reading system prompt");
        }
        return data.toString();
    }

    private String getSuggestionsTextFromChunk() {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new FileReader("src/main/resources/autocomplete_chunk_prompt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            throw new OpenAiError("Error while reading system prompt");
        }
        return data.toString();
    }

    private PromptTemplate getUserPromptTemplate() {
        String userText = """
                ```json
                {clientRequest}
                ```
                """;
        return new PromptTemplate(userText);
    }

    private Prompt getUserPrompt(DemandeClient demandeClient) {
        return getUserPromptTemplate().create(Map.of("clientRequest", DemandeClient.toJson(demandeClient)));
    }

    private UserMessage getUserMessage(DemandeClient demandeClient) {
        return new UserMessage(getUserPrompt(demandeClient).getContents());
    }

    private SystemMessage getSystemMessage() {
        return new SystemMessage(getSystemText());
    }

    private SystemMessage getSuggestionsMessage() {
        return new SystemMessage(getSuggestionsText());
    }

    private SystemMessage getSuggestionsMessageFromChunk() {
        return new SystemMessage(getSuggestionsTextFromChunk());
    }

    public Prompt getDevisFromDemandeClientPrompt(DemandeClient demandeClient) {
        return new Prompt(List.of(getSystemMessage(), getUserMessage(demandeClient)));
    }

    public Prompt getSuggestionsFromDemandeClientPrompt(DemandeClient demandeClient) {
        return new Prompt(List.of(getSuggestionsMessage(), getUserMessage(demandeClient)));
    }

    public Prompt getSuggestionsFromScenarioChunkPrompt(ScenarioChunk scenarioChunk) {
        return new Prompt(
                List.of(getSuggestionsMessageFromChunk(), new UserMessage(ScenarioChunk.toJson(scenarioChunk))));
    }

}
