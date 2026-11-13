package mx.edu.lrcp.act4.catalogo.dto;
public record MateriaResponse(Long id, String nombre, Integer semestre,
                              String descripcion, CategoriaResponse categoria) {}
