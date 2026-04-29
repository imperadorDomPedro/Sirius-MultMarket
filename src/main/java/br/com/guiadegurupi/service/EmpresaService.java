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

    private EmpresaResponse toResponse(Empresa e) {
        List<String> categoriasSlugs = e.getCategorias()
                .stream()
                .map(c -> c.getSlug())
                .collect(Collectors.toList());

        return EmpresaResponse.builder()
                .id(e.getId())
                .slug(e.getSlug())
                .nome(e.getNome())
                .tagline(e.getTagline())
                .descricao(e.getDescricao())
                .urlLogo(e.getUrlLogo())
                .urlBanner(e.getUrlBanner())
                .nomeResponsavel(e.getNomeResponsavel())
                .whatsapp(e.getWhatsapp())
                .localizacao(e.getLocalizacao())
                .tempoResposta(e.getTempoResposta())
                .politicaEntrega(e.getPoliticaEntrega())
                .politicaDevolucao(e.getPoliticaDevolucao())
                .rating(e.getRating())
                .totalAvaliacoes(e.getTotalAvaliacoes())
                .totalSeguidores(e.getTotalSeguidores())
                .verificado(e.getVerificado())
                .destaque(e.getDestaque())
                .ativo(e.getAtivo())
                .planoNome(e.getPlano() != null ? e.getPlano().getNome() : null)
                .categoriasSlugs(categoriasSlugs)
                .criadoEm(e.getCriadoEm())
                .atualizadoEm(e.getAtualizadoEm())
                .build();
    }
}
