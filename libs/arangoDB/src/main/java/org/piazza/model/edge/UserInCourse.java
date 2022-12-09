package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Course;
import org.piazza.model.document.User;

@Edge("userInCourse")
public class UserInCourse extends BaseModel {

    @From(lazy = true)
    User user;
    @To(lazy = true)
    Course course;

    String role;

    public UserInCourse(String role, Course course, User user) {
        this.role = role;
        this.course = course;
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
