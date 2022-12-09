package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserLikesAnswer;
import org.springframework.stereotype.Repository;

@Repository("userLikesAnswer")
public interface UserLikesAnswerRepository extends ArangoRepository<UserLikesAnswer, String> {
}
