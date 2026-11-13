package mx.edu.lrcp.act4.catalogo;

import jakarta.validation.Valid;
import java.net.URI;
import mx.edu.lrcp.act4.catalogo.dto.MateriaRequest;
import mx.edu.lrcp.act4.catalogo.dto.MateriaResponse;
import mx.edu.lrcp.act4.catalogo.dto.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    private final MateriaService service;
    public MateriaController(MateriaService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<PageResponse<MateriaResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page >= 0 y size entre 1 y 100");
        }
        return ResponseEntity.ok(service.listar(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<MateriaResponse> crear(@Valid @RequestBody MateriaRequest request) {
        MateriaResponse saved = service.crear(request);
        return ResponseEntity.created(URI.create("/api/materias/" + saved.id())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaResponse> actualizar(@PathVariable Long id,
            @Valid @RequestBody MateriaRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
