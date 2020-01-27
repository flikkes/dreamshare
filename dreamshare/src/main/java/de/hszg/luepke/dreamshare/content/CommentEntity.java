package de.hszg.luepke.dreamshare.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
	@Id
	private String id;
	private String commentText;
	private long likesBalance;
    @JsonIgnore
	private ImageEntity image;
}
