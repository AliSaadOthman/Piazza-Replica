package org.piazza.repository.collection_repository;

import com.arangodb.springframework.repository.ArangoRepository;
import org.piazza.model.document.Report;

public interface ReportRepository extends ArangoRepository<Report, String> {
}
