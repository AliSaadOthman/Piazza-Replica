package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Course;
import org.piazza.model.document.Question;

@Edge("courseQuestion")
public class CourseQuestionEdge extends BaseModel {

    @From(lazy = true)
    Course course;
    @To(lazy = true)
    Question question;

    public CourseQuestionEdge(Course course, Question question) {
        this.course = course;
        this.question = question;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
