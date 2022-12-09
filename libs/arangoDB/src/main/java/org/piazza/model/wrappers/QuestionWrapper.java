package org.piazza.model.wrappers;

import org.piazza.model.document.Answer;
import org.piazza.model.document.Question;
import org.piazza.model.document.User;

import java.util.ArrayList;
import java.util.Collection;

public class QuestionWrapper {

    private String id;
    private String title;

    private String description;
    private String mediaLink;
    private String date;
    private Boolean _public;
    private boolean isEndorsed;
    private boolean anonymous;
    private String user;
    private ArrayList<String> taggedUsers;

    private ArrayList<String> likedUsers;

    private ArrayList<AnswerWrapper> answers;

    private int likes;
    private boolean liked;

    public QuestionWrapper(Question question, boolean allDetails,String userEmail){

        id=question.getArangoId();
        title=question.getTitle();
        date=question.getDate().toString();
        mediaLink=question.getMediaLink();
        isEndorsed=question.isEndorsed();
        anonymous=true;
        _public=question.get_public();
        description=question.getDescription();

        if (!anonymous) {
           User curUser=question.getUser();
           if(curUser!=null) {
               user = curUser.getEmail();
           }else{
               user="Deleted User";
           }
        } else {
            user="Anonymous";
        }
        Collection<User> allLikes=question.getLikedUsers();
        likes=allLikes==null?0:allLikes.size();
        if(allDetails){
            likedUsers=new ArrayList<>();
            taggedUsers=new ArrayList<>();
            answers=new ArrayList<>();
            Collection<Answer> questionAnswers=question.getAnswers();
            Collection<User> questionTaggedUsers=question.getMentionedUsers();
            if(allLikes!=null) {
                int counter = 0;
                for (User user : allLikes) {
                    if(counter>20){
                        break;
                    }
                    if (user != null) {
                        if (userEmail.equals(user.getEmail())) {
                            liked = true;
                        }
                        counter ++;
                        likedUsers.add(user.getEmail());
                    }
                }
            }
            if(questionTaggedUsers!=null) {
                for (User user : questionTaggedUsers) {
                    int counter = 0;
                    if(counter>20){
                        break;
                    }
                    if (user != null) {
                        counter ++;
                        taggedUsers.add(user.getEmail());
                    }
                }
            }
            if(questionAnswers!=null) {
                int counter = 0;
                for (Answer answer : questionAnswers) {
                    if(counter>20){
                        break;
                    }
                    if (answer != null) {
                        counter ++;
                        answers.add(new AnswerWrapper(answer, userEmail));
                    }
                }
            }
        }

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean get_public() {
        return _public;
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


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ArrayList<String> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(ArrayList<String> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public ArrayList<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(ArrayList<String> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public ArrayList<AnswerWrapper> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<AnswerWrapper> answers) {
        this.answers = answers;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}








