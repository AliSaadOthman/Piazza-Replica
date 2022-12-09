package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserReported;
import org.springframework.stereotype.Repository;

@Repository("userReported")
public interface UserReportedRepository extends ArangoRepository<UserReported, String> {
}
