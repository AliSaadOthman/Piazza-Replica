package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserCreatePoll;
import org.springframework.stereotype.Repository;

@Repository("userCreatePoll")
public interface UserCreatePollRepository extends ArangoRepository<UserCreatePoll, String> {
}
