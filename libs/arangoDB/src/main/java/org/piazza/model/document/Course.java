package org.piazza.model.document;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.piazza.model.BaseModel;
import org.piazza.model.edge.CourseHasPoll;
import org.piazza.model.edge.CourseQuestionEdge;
import org.piazza.model.edge.UserInCourse;

import java.util.Collection;

@Document("courses")
public class Course extends BaseModel {

    private String name;

    @Relations(edges = CourseQuestionEdge.class, lazy = true)
    private Collection<Question> questions;
    @Relations(edges = CourseHasPoll.class, lazy = true)
    private Collection<Poll> polls;

    @Relations(edges = UserInCourse.class, lazy = true)
    private Collection<User> usersInCourse;






    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public Collection<Poll> getPolls() {
        return polls;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = questions;
    }

    public void setPolls(Collection<Poll> polls) {
        this.polls = polls;
    }

    public Collection<User> getUsersInCourse() {
        return usersInCourse;
    }

    public void setUsersInCourse(Collection<User> usersInCourse) {
        this.usersInCourse = usersInCourse;
    }



}
