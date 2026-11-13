package mx.edu.lrcp.act4.auth;

import java.util.Locale;
import mx.edu.lrcp.act4.auth.dto.AuthResponse;
import mx.edu.lrcp.act4.auth.dto.LoginRequest;
import mx.edu.lrcp.act4.auth.dto.RegisterRequest;
import mx.edu.lrcp.act4.exception.DuplicateResourceException;
import mx.edu.lrcp.act4.security.JwtService;
import mx.edu.lrcp.act4.user.Usuario;
import mx.edu.lrcp.act4.user.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwords;
    private final JwtService tokens;

    public AuthService(UsuarioRepository usuarios, PasswordEncoder passwords, JwtService tokens) {
        this.usuarios = usuarios;
        this.passwords = passwords;
        this.tokens = tokens;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = normalize(request.email());
        if (usuarios.existsByEmail(email)) {
            throw new DuplicateResourceException("El correo ya esta registrado");
        }
        try {
            Usuario user = usuarios.saveAndFlush(new Usuario(request.nombre().trim(), email,
                    passwords.encode(request.password())));
            return tokens.issueToken(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("El correo ya esta registrado");
        }
    }

    public AuthResponse login(LoginRequest request) {
        Usuario user = usuarios.findByEmail(normalize(request.email()))
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));
        if (!passwords.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales invalidas");
        }
        return tokens.issueToken(user);
    }

    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
