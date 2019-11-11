package de.hszg.luepke.dreamshare.content;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
			ieRp.save(imageEntity);
			return new ResponseEntity<ImageEntity>(imageEntity, HttpStatus.CREATED);
		} catch (final IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Couldn't read image", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("image/{id}")
	public ResponseEntity<?> getImage(@PathVariable final Long id) {
		final Optional<ImageEntity> opt = ieRp.findById(id);
		if (!opt.isPresent()) {
			return new ResponseEntity<String>("Image not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ImageEntity>(opt.get(), HttpStatus.OK);
	}
}
