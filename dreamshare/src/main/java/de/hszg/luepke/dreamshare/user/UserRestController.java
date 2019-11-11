package de.hszg.luepke.dreamshare.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserRestController {

	@Autowired
	private UserEntityRepository ueRp;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO user) {
		if (!user.getPassword().equals(user.getMatchingPassword())) {
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_ACCEPTABLE);
		}
		final UserEntity userEntity = new UserEntity();
		userEntity.setUsername(user.getEmail());
		userEntity.setPassword(user.getPassword());
		ueRp.save(userEntity);
		return new ResponseEntity<UserDTO>(user, HttpStatus.CREATED);
	}
}
