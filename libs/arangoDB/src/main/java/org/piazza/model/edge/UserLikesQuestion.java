package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Question;
import org.piazza.model.document.User;


@Edge("userLikesQuestion")
public class UserLikesQuestion extends BaseModel {

    @From(lazy = true)
    User user;
    @To(lazy = true)
    Question question;

    public UserLikesQuestion(User user, Question question) {
        this.user = user;
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
