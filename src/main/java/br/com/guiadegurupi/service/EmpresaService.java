package br.com.guiadegurupi.service;

import br.com.guiadegurupi.dto.response.EmpresaResponse;
import br.com.guiadegurupi.model.Empresa;
import br.com.guiadegurupi.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public List<EmpresaResponse> listarTodas() {
        return empresaRepository.findAllByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EmpresaResponse> listarDestaques() {
        return empresaRepository.findAllByDestaqueAndAtivoTrue(true)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EmpresaResponse buscarPorSlug(String slug) {
        return empresaRepository.findBySlug(slug)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + slug));
    }

    public List<EmpresaResponse> buscarPorCategoria(String categoriaSlug) {
        return empresaRepository.findByCategoriaSlug(categoriaSlug)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EmpresaResponse> pesquisar(String query) {
        return empresaRepository.search(query)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Mapeia Empresa → EmpresaResponse com contrato idêntico ao Bolt/React
    private EmpresaResponse toResponse(Empresa e) {
        var categorySlugs = e.getCategorias()
                .stream()
                .map(c -> c.getSlug())
                .collect(Collectors.toList());

        return new EmpresaResponse(
                String.valueOf(e.getId()),
                e.getNome(),
                e.getSlug(),
                e.getTagline(),
                e.getDescricao(),
                e.getUrlLogo(),
                e.getUrlBanner(),
                e.getNomeResponsavel(),
                e.getRating(),
                e.getTotalAvaliacoes(),
                e.getTotalSeguidores(),
                e.getProdutos().size(),
                categorySlugs,
                e.getLocalizacao(),
                e.getCriadoEm() != null ? e.getCriadoEm().toLocalDate().toString() : null,
                e.getVerificado(),
                e.getDestaque(),
                e.getTempoResposta(),
                new EmpresaResponse.PoliciesResponse(
                        e.getPoliticaDevolucao(),
                        e.getPoliticaEntrega()
                ),
                e.getWhatsapp()
        );
    }
}
