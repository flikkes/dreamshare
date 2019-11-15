package de.hszg.luepke.dreamshare.content;

public class ImageDTO {
	private final Long id;
	private final String href;
	private final boolean publicAccess;
	
	public ImageDTO(final Long id, final String href, final boolean publicAccess) {
		this.id = id;
		this.href = href;
		this.publicAccess = publicAccess;
	}

	public Long getId() {
		return id;
	}

	public String getHref() {
		return href;
	}

	public boolean isPublicAccess() {
		return publicAccess;
	}
}
