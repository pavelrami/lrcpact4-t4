package mx.edu.lrcp.act4.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import mx.edu.lrcp.act4.auth.dto.AuthResponse;
import mx.edu.lrcp.act4.user.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtEncoder encoder;
    private final String issuer;
    private final long expirationMinutes;

    public JwtService(JwtEncoder encoder, @Value("${app.jwt.issuer}") String issuer,
                      @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {
        this.encoder = encoder;
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    public AuthResponse issueToken(Usuario usuario) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(usuario.getEmail())
                .issuedAt(now)
                .expiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .claim("uid", usuario.getId())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new AuthResponse(token, "Bearer", expirationMinutes * 60,
                usuario.getNombre(), usuario.getEmail());
    }
}
