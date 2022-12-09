package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Course;
import org.piazza.model.document.Poll;

@Edge("courseHasPoll")
public class CourseHasPoll extends BaseModel {

    @From(lazy = true)
    Course course;
    @To(lazy = true)
    Poll poll;

    public CourseHasPoll(Course course, Poll poll) {
        this.course = course;
        this.poll = poll;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
