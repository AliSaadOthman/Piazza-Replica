package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.CourseQuestionEdge;
import org.springframework.stereotype.Repository;

@Repository("courseQuestion")
public interface CourseQuestionEdgeRepository extends ArangoRepository<CourseQuestionEdge, String>{
}
