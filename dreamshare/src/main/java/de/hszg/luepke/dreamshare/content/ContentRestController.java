package de.hszg.luepke.dreamshare.content;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("content")
public class ContentRestController {

	@Autowired
	private ImageEntityRepository ieRp;

	@PostMapping("image")
	public ResponseEntity<?> uploadImage(@RequestBody final MultipartFile image) {
		try {
			final byte[] bytes = image.getBytes();
			final ImageEntity imageEntity = new ImageEntity();
			imageEntity.setImageData(bytes);
			imageEntity.setPublicAccess(false);
			ieRp.save(imageEntity);
			imageEntity.setHref(ServletUriComponentsBuilder.fromCurrentRequest().toUriString()+"/"+imageEntity.getId());
			ieRp.save(imageEntity);
			final ImageDTO imageDTO = new ImageDTO(imageEntity.getId(), imageEntity.getHref(), imageEntity.isPublicAccess());
			return ResponseEntity.status(HttpStatus.CREATED).body(imageDTO);
		} catch (final IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Couldn't read image", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("image/{id}")
	public ResponseEntity<?> getImage(@PathVariable final Long id) {
		final Optional<ImageEntity> opt = ieRp.findById(id);
		if (!opt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(opt.get().getImageData());
	}
}
