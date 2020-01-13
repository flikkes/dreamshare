package de.hszg.luepke.dreamshare.transform;

import de.hszg.luepke.dreamshare.content.ImageEntity;
import de.hszg.luepke.dreamshare.content.ImageEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("transform")
public class TransformerRestController {

    @Autowired
    private ImageEntityRepository imageEntityRepository;

    @PostMapping(value = "{id}")
    public ResponseEntity<?> transformImage(@PathVariable final Long id) {
        final ImageEntity img = imageEntityRepository.findById(id).get();
        final RestTemplate template = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        final MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        ByteArrayResource contentsAsResource = new ByteArrayResource(img.getImageData()) {
            @Override
            public String getFilename() {
                return "MyImage"; // Filename has to be returned in order to be able to post.
            }
        };
        bodyMap.add("image", contentsAsResource);
        final HttpEntity<MultiValueMap<String, Object>> reqEntity = new HttpEntity<>(bodyMap, headers);
        try {
            final ResponseEntity<String> responseEntity = template
                    .postForEntity("http://141.46.136.238:5000/image", reqEntity, String.class);
            return ResponseEntity.ok(responseEntity.getBody());
        } catch (final HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }


    }
}
