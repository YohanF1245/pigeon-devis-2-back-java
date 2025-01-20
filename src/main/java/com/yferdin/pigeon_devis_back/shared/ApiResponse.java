package com.yferdin.pigeon_devis_back.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String result;    // SUCCESS ou ERROR
    private String message;   // Message de succès ou d'erreur
    private T data;          // Données de retour, null en cas d'erreur
} 