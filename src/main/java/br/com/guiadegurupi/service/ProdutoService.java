package br.com.guiadegurupi.service;

import br.com.guiadegurupi.dto.response.ProdutoResponse;
import br.com.guiadegurupi.model.Produto;
import br.com.guiadegurupi.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<ProdutoResponse> listarDestaques() {
        return produtoRepository.findAllByDestaqueAndAtivoTrue(true)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProdutoResponse> listarPorEmpresa(Long empresaId) {
        return produtoRepository.findAllByEmpresaIdAndAtivoTrue(empresaId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProdutoResponse> listarPorCategoria(String categoriaSlug) {
        return produtoRepository.findAllByCategoriaSlugAndAtivoTrue(categoriaSlug)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProdutoResponse> pesquisar(String query) {
        return produtoRepository.search(query)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProdutoResponse> listarSazonais() {
        return produtoRepository.findProdutosSazonais()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProdutoResponse buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
    }

    // Mapeia Produto → ProdutoResponse usando @Builder (padrão clean code do projeto)
    private ProdutoResponse toResponse(Produto p) {
        return ProdutoResponse.builder()
                .id(p.getId())
                .nome(p.getNome())
                .descricao(p.getDescricao())
                .preco(p.getPreco() != null ? p.getPreco().doubleValue() : null)
                .precoOriginal(p.getPrecoOriginal() != null ? p.getPrecoOriginal().doubleValue() : null)
                .imagens(p.getImagens())
                .categoriaId(p.getCategoria() != null ? p.getCategoria().getId() : null)
                .categoriaSlug(p.getCategoria() != null ? p.getCategoria().getSlug() : null)
                .empresaId(p.getEmpresa() != null ? p.getEmpresa().getId() : null)
                .empresaSlug(p.getEmpresa() != null ? p.getEmpresa().getSlug() : null)
                .empresaNome(p.getEmpresa() != null ? p.getEmpresa().getNome() : null)
                .rating(p.getRating())
                .totalAvaliacoes(p.getTotalAvaliacoes())
                .estoque(p.getEstoque())
                .tags(p.getTags())
                .especificacoes(p.getSpecs())
                .eServico(p.getEServico())
                .destaque(p.getDestaque())
                .ativo(p.getAtivo())
                .tempoEntrega(p.getTempoEntrega())
                .epocaDisponivel(p.getEpocaDisponivel())
                .build();
    }
}
