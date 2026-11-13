package mx.edu.lrcp.act4.catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "materias")
public class Materia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nombre;
    @Column(nullable = false)
    private Integer semestre;
    @Column(length = 500)
    private String descripcion;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    protected Materia() {}
    public Materia(String nombre, Integer semestre, String descripcion, Categoria categoria) {
        this.nombre = nombre;
        this.semestre = semestre;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getSemestre() { return semestre; }
    public String getDescripcion() { return descripcion; }
    public Categoria getCategoria() { return categoria; }
    public void actualizar(String nombre, Integer semestre, String descripcion, Categoria categoria) {
        this.nombre = nombre;
        this.semestre = semestre;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }
}
