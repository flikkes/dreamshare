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
	private String href;
	private boolean publicAccess;

	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public Long getId() {
		return id;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public boolean isPublicAccess() {
		return publicAccess;
	}
	public void setPublicAccess(boolean publicAccess) {
		this.publicAccess = publicAccess;
	}

}
