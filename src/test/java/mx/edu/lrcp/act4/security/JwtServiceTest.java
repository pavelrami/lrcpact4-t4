package mx.edu.lrcp.act4.security;

import java.util.Base64;
import mx.edu.lrcp.act4.user.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    @Test
    void emiteUnJwtFirmadoVerificableConVencimiento() throws Exception {
        byte[] key = new byte[32];
        for (int i = 0; i < key.length; i++) key[i] = (byte)(i + 1);
        JwtConfig cfg = new JwtConfig();
        var secret = cfg.jwtSecretKey(Base64.getEncoder().encodeToString(key));
        var decoder = cfg.jwtDecoder(secret, "https://lrcp-act4.invalid");
        var issuer = new JwtService(cfg.jwtEncoder(secret), "https://lrcp-act4.invalid", 60);
        Usuario usuario = new Usuario("Alumno", "alumno@example.com", "hash-solo-de-prueba");
        var field = Usuario.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(usuario, 1L);
        String token = issuer.issueToken(usuario).accessToken();
        Jwt claims = decoder.decode(token);
        assertEquals("alumno@example.com", claims.getSubject());
        assertEquals("https://lrcp-act4.invalid", claims.getIssuer().toString());
        assertTrue(claims.getExpiresAt().isAfter(claims.getIssuedAt()));
    }

    @Test
    void rechazaClavesMenoresA32Bytes() {
        JwtConfig cfg = new JwtConfig();
        assertThrows(IllegalStateException.class,
                () -> cfg.jwtSecretKey(Base64.getEncoder().encodeToString(new byte[20])));
    }
}
