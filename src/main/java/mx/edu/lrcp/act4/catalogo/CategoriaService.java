package mx.edu.lrcp.act4.catalogo;

import java.util.List;
import mx.edu.lrcp.act4.catalogo.dto.CategoriaRequest;
import mx.edu.lrcp.act4.catalogo.dto.CategoriaResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;
    public CategoriaService(CategoriaRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return repository.findAll(Sort.by("nombre")).stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion()))
                .toList();
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria c = repository.saveAndFlush(new Categoria(request.nombre().trim(), request.descripcion()));
        return new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion());
    }
}
