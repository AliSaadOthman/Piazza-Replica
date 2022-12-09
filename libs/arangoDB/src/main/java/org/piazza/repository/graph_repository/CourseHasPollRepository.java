package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.CourseHasPoll;
import org.springframework.stereotype.Repository;

@Repository("courseHasPoll")
public interface CourseHasPollRepository extends ArangoRepository<CourseHasPoll, String> {
}

