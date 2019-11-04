package de.hszg.luepke.dreamshare.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class UserEntity {

	@Id
	@GeneratedValue
	private Long id;

	private String username;
	private String password;

	/**
	 * 
	 */
	private static final long serialVersionUID = -172730024812928125L;



	public void setUsername(String username) {
		this.username = username;
	}


	public void setPassword(String password) {
		this.password = password;
	}


}
