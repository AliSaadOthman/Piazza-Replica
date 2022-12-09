package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserAnswerPoll;
import org.springframework.stereotype.Repository;

@Repository("userAnswerPoll")
public interface UserAnswerPollRepository extends ArangoRepository<UserAnswerPoll, String> {
}
