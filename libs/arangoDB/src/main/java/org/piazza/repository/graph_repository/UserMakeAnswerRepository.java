package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserMakeAnswer;
import org.springframework.stereotype.Repository;

@Repository("userMakeAnswer")
public interface UserMakeAnswerRepository extends ArangoRepository<UserMakeAnswer, String> {
}
