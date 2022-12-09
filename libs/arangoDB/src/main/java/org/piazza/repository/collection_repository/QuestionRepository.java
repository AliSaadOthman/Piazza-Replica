package org.piazza.repository.collection_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.document.Question;
import org.springframework.stereotype.Repository;

@Repository("questionRepository")
public interface QuestionRepository extends ArangoRepository<Question, String> {

}
