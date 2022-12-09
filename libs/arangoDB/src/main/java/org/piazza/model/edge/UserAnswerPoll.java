package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Poll;
import org.piazza.model.document.User;

@Edge("userAnswerPoll")
public class UserAnswerPoll extends BaseModel {

    @From(lazy = true)
    User user;
    @To(lazy = true)
    Poll poll;

    String option;

    public UserAnswerPoll(String option, Poll poll, User user) {
        this.option = option;
        this.poll = poll;
        this.user = user;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
