package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.QuestionHasAnswer;
import org.springframework.stereotype.Repository;

@Repository("questionHasAnswer")
public interface QuestionHasAnswerRepository extends ArangoRepository<QuestionHasAnswer, String> {
}
