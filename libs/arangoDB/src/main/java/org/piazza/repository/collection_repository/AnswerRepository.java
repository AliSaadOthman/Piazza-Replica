package org.piazza.repository.collection_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.document.Answer;

public interface AnswerRepository extends ArangoRepository<Answer, String> {
}
