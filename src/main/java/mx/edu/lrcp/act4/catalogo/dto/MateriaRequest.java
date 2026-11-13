package mx.edu.lrcp.act4.catalogo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MateriaRequest(
    @NotBlank @Size(max = 120) String nombre,
    @NotNull @Min(1) @Max(12) Integer semestre,
    @Size(max = 500) String descripcion,
    @NotNull Long categoriaId
) {}
