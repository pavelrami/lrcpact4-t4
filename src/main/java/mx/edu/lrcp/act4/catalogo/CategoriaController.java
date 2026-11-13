package mx.edu.lrcp.act4.catalogo;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import mx.edu.lrcp.act4.catalogo.dto.CategoriaRequest;
import mx.edu.lrcp.act4.catalogo.dto.CategoriaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }
    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse created = service.crear(request);
        return ResponseEntity.created(URI.create("/api/categorias/" + created.id())).body(created);
    }
}
