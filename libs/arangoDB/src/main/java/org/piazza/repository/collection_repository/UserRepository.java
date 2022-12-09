package org.piazza.repository.collection_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.document.User;

public interface UserRepository extends ArangoRepository<User, String> {
}
