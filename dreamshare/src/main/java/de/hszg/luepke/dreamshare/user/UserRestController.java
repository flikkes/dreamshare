package de.hszg.luepke.dreamshare.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("user")
public class UserRestController {

    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private DaoAuthenticationProvider authProvider;
    @Autowired
    private DreamUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt.audience}")
    private String jwtAudience;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO user) {
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getEmail());
        userEntity.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestParam final String username, @RequestParam final String password) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
            try {
                Authentication auth = authProvider.authenticate(authReq);
                if (auth.isAuthenticated()) {
                    DreamUserPrincipal user = (DreamUserPrincipal) auth.getPrincipal();
                    Algorithm algorithm = Algorithm.HMAC512(this.jwtSecret);
                    String token = JWT.create()
                            .withIssuer(this.jwtIssuer)
                            .withAudience(this.jwtAudience)
                            .withSubject(user.getUsername())
                            .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                            .sign(algorithm);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    return ResponseEntity.ok(token);
                }
            } catch (final AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Couldn't authenticate: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Couldn't authenticate");
    }
}
