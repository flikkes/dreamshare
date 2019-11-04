package de.hszg.luepke.dreamshare.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserDTO {

	@NotNull
	@NotBlank
	private String password;
	@NotNull
	@NotBlank
	private String matchingPassword;
	@NotNull
	@NotBlank
	private String email;

	public UserDTO() {

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}