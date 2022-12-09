package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Poll;
import org.piazza.model.document.User;

@Edge("userCreatePoll")
public class UserCreatePoll extends BaseModel {

    @From(lazy = true)
    User user;
    @To(lazy = true)
    Poll poll;

    public UserCreatePoll(User user, Poll poll) {
        this.user = user;
        this.poll = poll;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
