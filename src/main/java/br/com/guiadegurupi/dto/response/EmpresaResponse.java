package br.com.guiadegurupi.dto.response;

import java.util.List;

// DTO idêntico ao interface Store do Bolt/React
public record EmpresaResponse(
        String id,          // convertemos Long → String para o React
        String name,
        String slug,
        String tagline,
        String description,
        String logo,
        String banner,
        String ownerName,
        double rating,
        int reviewCount,
        int followers,
        int productCount,
        List<String> categories,
        String location,
        String joinedDate,
        boolean verified,
        boolean featured,
        String responseTime,
        PoliciesResponse policies,
        String whatsapp     // campo extra — diferencial do Guia de Gurupi
) {
    public record PoliciesResponse(String returns, String shipping) {}
}
