package de.hszg.luepke.dreamshare.content;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class ImageEntity {

	@Id
	@GeneratedValue
	private Long id;
	@Lob
	@JsonIgnore
	private byte[] imageData;
	private String href;
	@Column(columnDefinition = "TEXT")
	private String imageDescription;
	private String commentsHref;
	private boolean publicAccess;
	private long likesBalance;
}
