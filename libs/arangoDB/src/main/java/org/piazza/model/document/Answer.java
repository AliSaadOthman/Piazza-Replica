package org.piazza.model.document;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.piazza.model.BaseModel;
import org.piazza.model.edge.UserLikesAnswer;
import org.piazza.model.edge.UserMakeAnswer;

import java.time.ZonedDateTime;
import java.util.Collection;

@Document("answers")
public class Answer extends BaseModel {

    private String title;
    private String description;
    private String mediaLink;
    private ZonedDateTime date;
    private boolean isEndorsed;

    @Relations(edges = UserMakeAnswer.class)
    private User user;

    @Relations(edges = UserLikesAnswer.class, lazy = true)
    private Collection<User> userLikedAnswer;


    public Answer(String title, String description, String mediaLink, ZonedDateTime date) {
        super();
        this.title = title;
        this.description = description;
        this.mediaLink = mediaLink;
        this.date = date;
        this.isEndorsed=false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isEndorsed() {
        return isEndorsed;
    }

    public void setEndorsed(boolean endorsed) {
        isEndorsed = endorsed;
    }

    public Collection<User> getUserLikedAnswer() {
        return userLikedAnswer;
    }

    public void setUserLikedAnswer(Collection<User> userLikedAnswer) {
        this.userLikedAnswer = userLikedAnswer;
    }

    @Override
    public String toString() {
        return "Answer{" + super.toString() +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mediaLink='" + mediaLink + '\'' +
                ", date=" + date +
                ",isEndorsed"+ isEndorsed+
                ", usersLikedAnswer"+userLikedAnswer+
                ", answerOwner"+user+
                '}';
    }
    @Override
    public boolean equals(Object answer){
        Answer curAnswer=(Answer) answer;
        return this.getArangoId().equals(curAnswer.getArangoId());
    }


}
