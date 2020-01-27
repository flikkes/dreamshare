package de.hszg.luepke.dreamshare;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import de.hszg.luepke.dreamshare.user.DreamUserPrincipal;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private String jwtAudience;
    private String jwtIssuer;
    private String jwtSecret;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   String jwtAudience, String jwtIssuer,
                                   String jwtSecret) {
        this.jwtAudience = jwtAudience;
        this.jwtIssuer = jwtIssuer;
        this.jwtSecret = jwtSecret;
        this.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login");
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication authentication) {
        DreamUserPrincipal user = (DreamUserPrincipal) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC512(this.jwtSecret);
        String token = JWT.create()
                .withIssuer(this.jwtIssuer)
                .withAudience(this.jwtAudience)
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(algorithm);
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
