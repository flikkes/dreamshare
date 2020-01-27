package de.hszg.luepke.dreamshare.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("content")
public class ContentRestController {

    @Autowired
    private ImageEntityRepository ieRp;
    @Autowired
    private CommentEntityRepository ceRp;

    @PostMapping("image")
    public ResponseEntity<?> uploadImage(@RequestBody final MultipartFile image) {
        try {
            final byte[] bytes = image.getBytes();
            final ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageData(bytes);
            imageEntity.setPublicAccess(false);
            ieRp.save(imageEntity);
            imageEntity.setHref(
                    ServletUriComponentsBuilder.fromCurrentRequest().toUriString() + "/static/" + imageEntity.getId());
            imageEntity.setCommentsHref(
                    ServletUriComponentsBuilder.fromCurrentRequest().toUriString().replace("/image", "") + "/comment/"
                            + imageEntity.getId() + "/of/0");
            ieRp.save(imageEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(imageEntity);
        } catch (final IOException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Couldn't read image", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("image/{id}")
    public ResponseEntity<?> getImageEntity(@PathVariable String id) {
        final Optional<ImageEntity> opt = ieRp.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opt.get());
    }

    @GetMapping("image/static/{id}")
    public ResponseEntity<?> getImage(@PathVariable final String id) {
        final Optional<ImageEntity> opt = ieRp.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(opt.get().getImageData());
    }

    @GetMapping("image/of/{page}")
    public ResponseEntity<?> getAllOfPage(@PathVariable final int page) {
        final List<ImageEntity> images = ieRp.findAll(PageRequest.of(page, 10));
        return ResponseEntity.ok(images);
    }

    @PatchMapping("image/{id}")
    public ResponseEntity<?> setAccessibility(@PathVariable final String id,
                                              @RequestBody final ImageEntity imageEntity) {
        final ImageEntity img = ieRp.findById(id).get();
        img.setPublicAccess(imageEntity.isPublicAccess());
        ieRp.save(img);
        return ResponseEntity.ok().build();
    }

    @PostMapping("comment/{imageId}")
    public ResponseEntity<?> postComment(@PathVariable final String imageId, final String text) {
        final Optional<ImageEntity> imgOpt = ieRp.findById(imageId);
        if (!imgOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final ImageEntity ie = imgOpt.get();
        final CommentEntity comment = new CommentEntity(null, text, 0, ie);
        ceRp.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("comment/{id}")
    public ResponseEntity<?> getComment(@PathVariable final String id) {
        final Optional<CommentEntity> ceOpt = ceRp.findById(id);
        if (!ceOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ceOpt.get());
    }

    @GetMapping("comment/{imageId}/of/{page}")
    public ResponseEntity<?> getCommentsOfImage(@PathVariable final String imageId, @PathVariable final int page) {
        final List<CommentEntity> comments = ceRp.findByImageId(imageId, PageRequest.of(page, 10));
        return ResponseEntity.ok(comments);
    }
}
