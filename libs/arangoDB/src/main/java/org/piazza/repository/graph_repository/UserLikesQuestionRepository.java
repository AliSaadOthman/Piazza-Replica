package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserLikesQuestion;
import org.springframework.stereotype.Repository;

@Repository("userLikesQuestion")
public interface UserLikesQuestionRepository extends ArangoRepository<UserLikesQuestion, String> {
}
