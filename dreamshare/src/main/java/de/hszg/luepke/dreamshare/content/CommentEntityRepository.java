package de.hszg.luepke.dreamshare.content;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CommentEntityRepository extends CrudRepository<CommentEntity, String> {
	public List<CommentEntity> findAll(Pageable page);
	public List<CommentEntity> findByImageId(String imageId, Pageable page);
}
