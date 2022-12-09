package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserMakeQuestion;
import org.springframework.stereotype.Repository;

@Repository("userMakeQuestion")
public interface UserMakeQuestionRepository extends ArangoRepository<UserMakeQuestion, String> {
}
