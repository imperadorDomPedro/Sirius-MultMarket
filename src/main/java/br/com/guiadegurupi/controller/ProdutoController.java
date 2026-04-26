package br.com.guiadegurupi.controller;

import br.com.guiadegurupi.dto.response.ProdutoResponse;
import br.com.guiadegurupi.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarDestaques() {
        return ResponseEntity.ok(produtoService.listarDestaques());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorEmpresa(
            @PathVariable Long empresaId) {
        return ResponseEntity.ok(produtoService.listarPorEmpresa(empresaId));
    }

    @GetMapping("/categoria/{categoriaSlug}")
    public ResponseEntity<List<ProdutoResponse>> listarPorCategoria(
            @PathVariable String categoriaSlug) {
        return ResponseEntity.ok(produtoService.listarPorCategoria(categoriaSlug));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> pesquisar(@RequestParam String q) {
        return ResponseEntity.ok(produtoService.pesquisar(q));
    }

    // Endpoint especial: produtos sazonais do cerrado!
    @GetMapping("/sazonais")
    public ResponseEntity<List<ProdutoResponse>> listarSazonais() {
        return ResponseEntity.ok(produtoService.listarSazonais());
    }
}
