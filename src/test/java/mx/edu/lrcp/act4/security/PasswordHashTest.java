package mx.edu.lrcp.act4.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class PasswordHashTest {
    @Test
    void bcryptNoAlmacenaLaContrasenaEnTextoPlano() {
        var encoder = new SecurityConfig().passwordEncoder();
        String raw = "ContrasenaSeguraDePrueba123";
        String encoded = encoder.encode(raw);
        assertNotEquals(raw, encoded);
        assertTrue(encoder.matches(raw, encoded));
        assertFalse(encoder.matches("incorrecta", encoded));
    }
}
