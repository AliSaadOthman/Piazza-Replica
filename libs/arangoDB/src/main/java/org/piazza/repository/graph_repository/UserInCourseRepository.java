package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserInCourse;
import org.springframework.stereotype.Repository;

@Repository("userInCourse")
public interface UserInCourseRepository extends ArangoRepository<UserInCourse, String> {
}
