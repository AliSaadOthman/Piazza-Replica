package org.piazza.model.wrappers;

import org.piazza.model.document.Answer;
import org.piazza.model.document.User;

import java.util.ArrayList;
import java.util.Collection;

public class AnswerWrapper {
    private String id;

    private String title;
    private String description;
    private String mediaLink;
    private String date;
    private boolean isEndorsed;
    private String user;
    private ArrayList<String> likedUsers;
    private int likes;

    private boolean liked;
    public AnswerWrapper(Answer answer,String userEmail){
        this.id=answer.getArangoId();
        this.title=answer.getTitle();
        this.description=answer.getDescription();
        this.date=answer.getDate().toString();
        this.isEndorsed=answer.isEndorsed();
        this.user=answer.getUser()!=null?answer.getUser().getEmail():"Deleted user";
        Collection<User> userLikes=answer.getUserLikedAnswer();
        likes=userLikes==null?0:userLikes.size();
        likedUsers=new ArrayList<String>();
        if(answer.getUserLikedAnswer()!=null) {
            for (User user : answer.getUserLikedAnswer()) {
                if (user != null) {
                    if (userEmail.equals(user.getEmail())) {
                        liked = true;
                    }
                    likedUsers.add(user.getEmail());
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

    public ArrayList<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(ArrayList<String> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
