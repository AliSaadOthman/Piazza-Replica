package org.piazza.repository.collection_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.document.Poll;

public interface PollRepository extends ArangoRepository<Poll, String> {
}
