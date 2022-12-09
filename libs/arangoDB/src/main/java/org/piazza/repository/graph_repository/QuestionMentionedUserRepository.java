package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.QuestionMentionedUser;
import org.springframework.stereotype.Repository;

@Repository("questionMentionedUser")
public interface QuestionMentionedUserRepository extends ArangoRepository<QuestionMentionedUser, String> {

}
