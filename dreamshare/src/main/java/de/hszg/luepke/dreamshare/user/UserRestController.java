package de.hszg.luepke.dreamshare.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserRestController {

	@Autowired
	private UserEntityRepository ueRp;
	@Autowired
	private PasswordEncoder encoder;

	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody UserDTO user) {
		if (!user.getPassword().equals(user.getMatchingPassword())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		final UserEntity userEntity = new UserEntity();
		userEntity.setUsername(user.getEmail());
		userEntity.setPassword(encoder.encode(user.getPassword()));
		ueRp.save(userEntity);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
