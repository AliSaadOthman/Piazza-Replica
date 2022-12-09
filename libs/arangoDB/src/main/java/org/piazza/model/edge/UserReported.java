package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Report;
import org.piazza.model.document.User;

@Edge("userReported")
public class UserReported extends BaseModel {

    @From(lazy = true)
    Report report;
    @To(lazy = true)
    User user;

    public UserReported(Report report, User user) {
        this.report = report;
        this.user = user;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
