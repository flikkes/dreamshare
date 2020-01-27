package de.hszg.luepke.dreamshare.user;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
public class UserEntity {

	@Id
	private String id;
	private String username;
	private String password;


}
