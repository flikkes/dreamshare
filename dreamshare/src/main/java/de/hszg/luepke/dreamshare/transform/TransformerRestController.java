package de.hszg.luepke.dreamshare.transform;

import de.hszg.luepke.dreamshare.content.ImageEntity;
import de.hszg.luepke.dreamshare.content.ImageEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("transform")
public class TransformerRestController {

    @Autowired
    private ImageEntityRepository imageEntityRepository;

    @PostMapping(value = "{id}")
    public ResponseEntity<?> transformImage(@PathVariable final String id) {
        final ImageEntity img = imageEntityRepository.findById(id).get();
        if (img.getTransformedHref() == null) {
            final RestTemplate template = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            final MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
            ByteArrayResource contentsAsResource = new ByteArrayResource(img.getImageData()) {
                @Override
                public String getFilename() {
                    return "ImageFromFlikkesService"; // Filename has to be returned in order to be able to post.
                }
            };
            bodyMap.add("image", contentsAsResource);
            final HttpEntity<MultiValueMap<String, Object>> reqEntity = new HttpEntity<>(bodyMap, headers);
            try {
                final ResponseEntity<String> responseEntity = template
                        .postForEntity("http://141.46.136.238:5000/image", reqEntity, String.class);
                img.setTransformedImageId(responseEntity.getBody());
                imageEntityRepository.save(img);
                return ResponseEntity.ok(responseEntity.getBody());
            } catch (final HttpClientErrorException e) {
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
            }
        }
        return ResponseEntity.ok(img.getTransformedImageId());
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getTransformedImage(@PathVariable final String id) throws IOException {
        final ImageEntity img = imageEntityRepository.findById(id).get();
        if (img.getTransformedImageId() != null && img.getTransformedHref() == null) {
            final RestTemplate template = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            try {
                final ResponseEntity<byte[]> responseEntity = template
                        .getForEntity("http://141.46.136.238:5000/image/" + img.getTransformedImageId(),
                                byte[].class);
                if (responseEntity.getHeaders().getContentType().equals(MediaType.IMAGE_JPEG)) {
                    img.setTransformedImageData(responseEntity.getBody());
                    img.setTransformedHref(ServletUriComponentsBuilder.fromCurrentRequest().toUriString() + "/" + id);
                    imageEntityRepository.save(img);
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img.getTransformedImageData());
                } else {
                    return ResponseEntity.ok(img.getTransformedImageId());
                }
            } catch (final HttpClientErrorException e) {
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
            }
        }
        if (img.getTransformedImageData() != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img.getTransformedImageData());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("You didn't request a deep dream transformation of this image yet!");
    }
}
