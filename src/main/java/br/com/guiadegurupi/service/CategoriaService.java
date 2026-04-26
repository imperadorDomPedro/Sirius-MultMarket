package br.com.guiadegurupi.service;

import br.com.guiadegurupi.dto.response.CategoriaResponse;
import br.com.guiadegurupi.model.Categoria;
import br.com.guiadegurupi.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAllByAtivoTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CategoriaResponse buscarPorSlug(String slug) {
        return categoriaRepository.findBySlug(slug)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada: " + slug));
    }

    private CategoriaResponse toResponse(Categoria c) {
        return new CategoriaResponse(
                String.valueOf(c.getId()),
                c.getNome(),
                c.getSlug(),
                c.getIcone(),
                c.getCor(),
                c.getBgCor(),
                0, // productCount calculado dinamicamente se necessário
                c.getUrlImagem(),
                c.getDescricao()
        );
    }
}
