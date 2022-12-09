package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Question;
import org.piazza.model.document.User;

@Edge("questionMentionedUser")
public class QuestionMentionedUser extends BaseModel {

    @From(lazy = true)
    Question question;
    @To(lazy = true)
    User user;

    public QuestionMentionedUser(Question question, User user) {
        this.question = question;
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
