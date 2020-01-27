package de.hszg.luepke.dreamshare.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class ImageEntity {
    @Id
    private String id;
    private String transformedImageId;
    @JsonIgnore
    private byte[] imageData;
    @JsonIgnore
    private byte[] transformedImageData;
    private String href;
    private String transformedHref;
    private String imageDescription;
    private String commentsHref;
    private boolean publicAccess;
    private long likesBalance;
}
