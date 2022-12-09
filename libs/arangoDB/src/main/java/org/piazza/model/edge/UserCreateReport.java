package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Report;
import org.piazza.model.document.User;
@Edge("userCreateReport")
public class UserCreateReport extends BaseModel {

    @From(lazy = true)
    User user;
    @To(lazy = true)
    Report report;

    public UserCreateReport(User user, Report report) {
        this.user = user;
        this.report = report;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Report getReport() {
        return report;
    }
    public void setReport(Report report) {
        this.report = report;
    }
}
