package de.hszg.luepke.dreamshare.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(final String username);
}
