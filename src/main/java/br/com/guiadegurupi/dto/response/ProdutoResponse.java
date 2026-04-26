package br.com.guiadegurupi.dto.response;

import java.util.List;
import java.util.Map;

// Contrato idêntico ao interface Product do Bolt/React
public record ProdutoResponse(
        String id,
        String name,
        String description,
        double price,
        Double originalPrice,
        List<String> images,
        String categoryId,
        String storeId,
        double rating,
        int reviewCount,
        int stock,
        List<String> tags,
        boolean isService,
        boolean featured,
        Map<String, String> specs,
        String deliveryTime,
        String epocaDisponivel  // campo extra para sazonalidade do cerrado
) {}
