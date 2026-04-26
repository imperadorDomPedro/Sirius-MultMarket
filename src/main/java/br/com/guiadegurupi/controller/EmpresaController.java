package br.com.guiadegurupi.controller;

import br.com.guiadegurupi.dto.response.EmpresaResponse;
import br.com.guiadegurupi.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    // GET /api/empresas
    @GetMapping
    public ResponseEntity<List<EmpresaResponse>> listarTodas() {
        return ResponseEntity.ok(empresaService.listarTodas());
    }

    // GET /api/empresas/destaques
    @GetMapping("/destaques")
    public ResponseEntity<List<EmpresaResponse>> listarDestaques() {
        return ResponseEntity.ok(empresaService.listarDestaques());
    }

    // GET /api/empresas/{slug}  — ex: /api/empresas/distribuidora-martins
    @GetMapping("/{slug}")
    public ResponseEntity<EmpresaResponse> buscarPorSlug(@PathVariable String slug) {
        return ResponseEntity.ok(empresaService.buscarPorSlug(slug));
    }

    // GET /api/empresas?categoria=servicos-automotivos
    @GetMapping("/categoria/{categoriaSlug}")
    public ResponseEntity<List<EmpresaResponse>> buscarPorCategoria(
            @PathVariable String categoriaSlug) {
        return ResponseEntity.ok(empresaService.buscarPorCategoria(categoriaSlug));
    }

    // GET /api/empresas/buscar?q=lubrificante
    @GetMapping("/buscar")
    public ResponseEntity<List<EmpresaResponse>> pesquisar(@RequestParam String q) {
        return ResponseEntity.ok(empresaService.pesquisar(q));
    }
}
