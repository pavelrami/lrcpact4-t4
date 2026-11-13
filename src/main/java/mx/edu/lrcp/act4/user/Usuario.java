package mx.edu.lrcp.act4.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, unique = true, length = 190)
    private String email;
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    protected Usuario() {}
    public Usuario(String nombre, String email, String passwordHash) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
}
