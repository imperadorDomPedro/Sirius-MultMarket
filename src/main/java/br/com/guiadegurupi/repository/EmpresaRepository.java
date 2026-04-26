package br.com.guiadegurupi.repository;

import br.com.guiadegurupi.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findBySlug(String slug);

    List<Empresa> findAllByAtivoTrue();

    List<Empresa> findAllByDestaqueAndAtivoTrue(boolean destaque);

    @Query("""
        SELECT DISTINCT e FROM Empresa e
        JOIN e.categorias c
        WHERE c.slug = :categoriaSlug
        AND e.ativo = true
        ORDER BY e.destaque DESC, e.rating DESC
    """)
    List<Empresa> findByCategoriaSlug(@Param("categoriaSlug") String categoriaSlug);

    @Query("""
        SELECT e FROM Empresa e
        WHERE e.ativo = true
        AND (
            LOWER(e.nome) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(e.tagline) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(e.descricao) LIKE LOWER(CONCAT('%', :q, '%'))
        )
        ORDER BY e.destaque DESC, e.rating DESC
    """)
    List<Empresa> search(@Param("q") String query);
}
