package de.hszg.luepke.dreamshare.content;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ImageEntity {

	@Id
	@GeneratedValue
	private Long id;
	@Lob
	private byte[] imageData;

	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public Long getId() {
		return id;
	}

}
