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

    private ProdutoResponse toResponse(Produto p) {
        return new ProdutoResponse(
                String.valueOf(p.getId()),
                p.getNome(),
                p.getDescricao(),
                p.getPreco().doubleValue(),
                p.getPrecoOriginal() != null ? p.getPrecoOriginal().doubleValue() : null,
                p.getImagens(),
                String.valueOf(p.getCategoria().getId()),
                String.valueOf(p.getEmpresa().getId()),
                p.getRating(),
                p.getTotalAvaliacoes(),
                p.getEstoque(),
                p.getTags(),
                p.getEServico(),
                p.getDestaque(),
                p.getSpecs(),
                p.getTempoEntrega(),
                p.getEpocaDisponivel()
        );
    }
}
