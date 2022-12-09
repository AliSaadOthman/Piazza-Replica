package org.piazza.model.document;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import com.google.gson.JsonObject;
import org.piazza.model.BaseModel;
import org.piazza.model.edge.*;

import java.time.ZonedDateTime;
import java.util.Collection;

@Document("questions")
public class Question extends BaseModel {

    private String title;
    private String description;
    private String mediaLink;
    private ZonedDateTime date;
    private Boolean _public;
    private boolean isEndorsed;
    private boolean anonymous;

    @Relations(edges = QuestionHasAnswer.class, lazy = true)
    private Collection<Answer> answers;
    @Relations(edges = QuestionMentionedUser.class, lazy = true)
    private Collection<User> mentionedUsers;
    @Relations(edges = UserLikesQuestion.class, lazy = true)
    private Collection<User> likedUsers;
    @Relations(edges = UserMakeQuestion.class)
    private User user;

    @Relations(edges= CourseQuestionEdge.class)
    private Course course;








    public Question(String title, String description, String mediaLink, ZonedDateTime date,boolean _public,boolean anonymous) {
        super();
        this.title = title;
        this.description = description;
        this.mediaLink = mediaLink;
        this.date = date;
        this._public=_public;
        if(!_public){
            this.anonymous=false;
        }else {
            this.anonymous = anonymous;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
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

    public Boolean getPublic() {
        return _public;
    }

    public void setPublic(Boolean _public) {
        this._public = _public;
    }





    public Boolean get_public() {
        return _public;
    }

    public Collection<Answer> getAnswers() {
        return answers;
    }

    public Collection<User> getMentionedUsers() {
        return mentionedUsers;
    }

    public Collection<User> getLikedUsers() {
        return likedUsers;
    }

    public void set_public(Boolean _public) {
        this._public = _public;
    }

    public boolean isEndorsed() {
        return isEndorsed;
    }

    public void setEndorsed(boolean endorsed) {
        isEndorsed = endorsed;
    }

    public void setAnswers(Collection<Answer> answers) {
        this.answers = answers;
    }

    public void setMentionedUsers(Collection<User> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }

    public void setLikedUsers(Collection<User> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mediaLink='" + mediaLink + '\'' +
                ", date=" + date +
                ", _public=" + _public +
                ", isEndorsed=" + isEndorsed +
                ", answers=" + answers +
                ", mentionedUsers=" + mentionedUsers +
                ", likedUsers=" + likedUsers +
                '}';
    }

    public JsonObject toJson(){

        JsonObject question=new JsonObject();
        question.addProperty("ID",getArangoId());
        question.addProperty("title",title);
        question.addProperty("date", String.valueOf(date));
        question.addProperty("mediaLink",mediaLink);
        question.addProperty("isEndorsed",isEndorsed);
        question.addProperty("anonymous",anonymous);
        question.addProperty("public",_public);

        if (!anonymous) {
            question.addProperty("User", user.toString());
        } else {
            question.addProperty("User", "Anonymous");
        }
        question.addProperty("Likes",likedUsers.size());
        return question;



    }

}
