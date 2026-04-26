package br.com.guiadegurupi.repository;

import br.com.guiadegurupi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findAllByEmpresaIdAndAtivoTrue(Long empresaId);

    List<Produto> findAllByDestaqueAndAtivoTrue(boolean destaque);

    List<Produto> findAllByCategoriaSlugAndAtivoTrue(String categoriaSlug);

    @Query("""
        SELECT p FROM Produto p
        WHERE p.ativo = true
        AND (
            LOWER(p.nome) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :q, '%'))
        )
        ORDER BY p.destaque DESC, p.rating DESC
    """)
    List<Produto> search(@Param("q") String query);

    // Produtos sazonais disponíveis agora — chave do cerrado!
    @Query("""
        SELECT p FROM Produto p
        WHERE p.ativo = true
        AND p.epocaDisponivel IS NOT NULL
        ORDER BY p.destaque DESC
    """)
    List<Produto> findProdutosSazonais();
}
