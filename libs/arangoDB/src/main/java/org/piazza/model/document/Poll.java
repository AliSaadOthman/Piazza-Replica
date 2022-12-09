package org.piazza.model.document;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.piazza.model.BaseModel;
import org.piazza.model.edge.UserAnswerPoll;
import org.piazza.model.edge.UserCreatePoll;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

@Document("polls")
public class Poll extends BaseModel {

    String title;
    Collection<String> options;

    ZonedDateTime date;

    Date expiryDate;



    @Relations(edges = UserAnswerPoll.class, lazy = true)
    private Collection<User> answeredUsers;

    @Relations(edges = UserCreatePoll.class, lazy = true)
    private User owner;

    public Poll(String title, Collection<String> options,ZonedDateTime date,Date expiryDate) {
        this.title = title;
        this.options = options;
        this.date=date;
        this.expiryDate=expiryDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<String> getOptions() {
        return options;
    }

    public void setOptions(Collection<String> options) {
        this.options = options;
    }

    public Collection<User> getAnsweredUsers() {
        return answeredUsers;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public void setAnsweredUsers(Collection<User> answeredUsers) {
        this.answeredUsers = answeredUsers;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object poll){
        Poll curPoll=(Poll) poll;
        return this.getArangoId().equals(curPoll.getArangoId());
    }

}
