package org.piazza.repository.graph_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.edge.UserCreateReport;
import org.springframework.stereotype.Repository;

@Repository("userCreateReport")
public interface UserCreateReportRepository extends ArangoRepository<UserCreateReport, String> {
}
