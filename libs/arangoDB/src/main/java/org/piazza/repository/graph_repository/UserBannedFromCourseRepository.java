package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserBannedFromCourse;
import org.springframework.stereotype.Repository;

@Repository("userBannedFromCourseEdge")
public interface UserBannedFromCourseRepository extends ArangoRepository<UserBannedFromCourse, String> {
}
