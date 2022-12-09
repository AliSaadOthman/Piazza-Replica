package org.piazza.model.document;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.piazza.model.BaseModel;
import org.piazza.model.edge.*;

import java.util.Collection;

@Document("users")
public class User extends BaseModel {

    String email;
    String role;

    //one TO many
    @Relations(edges = UserMakeQuestion.class, lazy = true)
    private Collection<Question> createdQuestion;
    @Relations(edges = UserMakeAnswer.class, lazy = true)
    private Collection<Answer> createdAnswers;
    @Relations(edges = UserCreatePoll.class, lazy = true)
    private Collection<Poll> createdPolls;

    //many TO many
    @Relations(edges = UserAnswerPoll.class, lazy = true)
    private Collection<Poll> answeredPolls;
    @Relations(edges = UserLikesQuestion.class, lazy = true)
    private Collection<Question> likedQuestions;

    public User(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collection<Question> getCreatedQuestion() {
        return createdQuestion;
    }

    public Collection<Answer> getCreatedAnswers() {
        return createdAnswers;
    }

    public Collection<Poll> getCreatedPolls() {
        return createdPolls;
    }

    public Collection<Poll> getAnsweredPolls() {
        return answeredPolls;
    }

    public Collection<Question> getLikedQuestions() {
        return likedQuestions;
    }

    @Override
    public boolean equals(Object user){
        User curUser=(User) user;
         return this.getArangoId().equals(curUser.getArangoId());
    }

}
