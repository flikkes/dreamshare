package de.hszg.luepke.dreamshare.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
