package org.piazza.repository.collection_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.document.Course;

public interface CourseRepository extends ArangoRepository<Course, String> {
}
