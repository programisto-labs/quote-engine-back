package com.programisto.devis_rapide.infrastructure.generation.entity;

import java.util.List;

import lombok.Data;

@Data
public class DemandeClientEntity {
    private String coreBusiness;
    private String concept;
    private List<String> useCases;
}
