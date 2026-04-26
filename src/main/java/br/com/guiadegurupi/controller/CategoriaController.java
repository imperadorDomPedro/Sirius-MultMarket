package br.com.guiadegurupi.controller;

import br.com.guiadegurupi.dto.response.CategoriaResponse;
import br.com.guiadegurupi.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // GET /api/categorias
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    // GET /api/categorias/produtos-cerrado
    @GetMapping("/{slug}")
    public ResponseEntity<CategoriaResponse> buscarPorSlug(@PathVariable String slug) {
        return ResponseEntity.ok(categoriaService.buscarPorSlug(slug));
    }
}
