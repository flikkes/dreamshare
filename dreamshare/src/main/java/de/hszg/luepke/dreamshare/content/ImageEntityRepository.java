package de.hszg.luepke.dreamshare.content;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ImageEntityRepository extends CrudRepository<ImageEntity, String> {
	public List<ImageEntity> findAll(Pageable page);
}
