package org.piazza.model.document;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.piazza.model.BaseModel;
import org.piazza.model.edge.UserCreateReport;
import org.piazza.model.edge.UserReported;

@Document("reports")

public class Report extends BaseModel {

    String reason;
    String courseId;

    public Report(String reason, String courseId) {
        this.reason = reason;
        this.courseId = courseId;
    }
    @Relations(edges = UserCreateReport.class)
    private User UserCreatedReport;

    @Relations(edges = UserReported.class)
    private User UserReported;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public User getUserCreatedReport() {
        return UserCreatedReport;
    }

    public void setUserCreatedReport(User userCreatedReport) {
        UserCreatedReport = userCreatedReport;
    }

    public User getUserReported() {
        return UserReported;
    }

    public void setUserReported(User userReported) {
        UserReported = userReported;
    }
}
