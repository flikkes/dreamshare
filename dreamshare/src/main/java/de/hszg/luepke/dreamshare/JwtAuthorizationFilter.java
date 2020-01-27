package de.hszg.luepke.dreamshare;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private String jwtSecret;
    private String jwtIssuer;
    private String jwtAudience;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, String jwtAudience, String jwtIssuer,
                                  String jwtSecret) {
        super(authenticationManager);
        this.jwtAudience = jwtAudience;
        this.jwtIssuer = jwtIssuer;
        this.jwtSecret = jwtSecret;
    }

    private UsernamePasswordAuthenticationToken parseToken(HttpServletRequest request) {
        String rawToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (rawToken != null && rawToken.startsWith("Bearer ")) {
            String token = rawToken.replace("Bearer ", "");
            try {
                Algorithm algorithm = Algorithm.HMAC512(this.jwtSecret);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer(this.jwtIssuer)
                        .withAudience(this.jwtAudience)
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);
                final String username = jwt.getSubject();
                if (username == null || username.trim().isEmpty()) {
                    return null;
                }
                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            } catch (JWTVerificationException exception) {
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = parseToken(request);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
