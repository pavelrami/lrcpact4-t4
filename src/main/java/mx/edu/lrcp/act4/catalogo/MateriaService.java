package mx.edu.lrcp.act4.catalogo;

import mx.edu.lrcp.act4.catalogo.dto.CategoriaResponse;
import mx.edu.lrcp.act4.catalogo.dto.MateriaRequest;
import mx.edu.lrcp.act4.catalogo.dto.MateriaResponse;
import mx.edu.lrcp.act4.catalogo.dto.PageResponse;
import mx.edu.lrcp.act4.exception.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MateriaService {
    private final MateriaRepository materias;
    private final CategoriaRepository categorias;
    public MateriaService(MateriaRepository materias, CategoriaRepository categorias) {
        this.materias = materias;
        this.categorias = categorias;
    }

    @Transactional(readOnly = true)
    public PageResponse<MateriaResponse> listar(int page, int size) {
        return PageResponse.from(materias.findAll(PageRequest.of(page, size, Sort.by("id"))).map(this::map));
    }

    @Transactional(readOnly = true)
    public MateriaResponse obtener(Long id) { return map(encontrar(id)); }

    @Transactional
    public MateriaResponse crear(MateriaRequest req) {
        Materia materia = new Materia(req.nombre().trim(), req.semestre(), req.descripcion(),
                encontrarCategoria(req.categoriaId()));
        return map(materias.saveAndFlush(materia));
    }

    @Transactional
    public MateriaResponse actualizar(Long id, MateriaRequest req) {
        Materia materia = encontrar(id);
        materia.actualizar(req.nombre().trim(), req.semestre(), req.descripcion(),
                encontrarCategoria(req.categoriaId()));
        return map(materias.saveAndFlush(materia));
    }

    @Transactional
    public void eliminar(Long id) { materias.delete(encontrar(id)); }

    private Materia encontrar(Long id) {
        return materias.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Materia no encontrada: " + id));
    }
    private Categoria encontrarCategoria(Long id) {
        return categorias.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Categoria no encontrada: " + id));
    }
    private MateriaResponse map(Materia m) {
        Categoria c = m.getCategoria();
        return new MateriaResponse(m.getId(), m.getNombre(), m.getSemestre(), m.getDescripcion(),
                new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion()));
    }
}
