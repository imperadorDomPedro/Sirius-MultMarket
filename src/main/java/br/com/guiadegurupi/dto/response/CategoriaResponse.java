package br.com.guiadegurupi.dto.response;

// Contrato idêntico ao interface Category do Bolt/React
public record CategoriaResponse(
        String id,
        String name,
        String slug,
        String icon,
        String color,
        String bgColor,
        int productCount,
        String image,
        String description
) {}
