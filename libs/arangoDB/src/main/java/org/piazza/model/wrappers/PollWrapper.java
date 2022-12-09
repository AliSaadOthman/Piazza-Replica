package org.piazza.model.wrappers;

import org.piazza.model.document.Poll;
import org.piazza.model.edge.UserAnswerPoll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PollWrapper {
    private String title;
    private Collection<String> options;

    private String date;

    private String expiryDate;

    private String id;

    private Collection<VoteWrapper> votes;
    private String userVote;



    public PollWrapper(Poll poll){
        this.id=poll.getArangoId();
        this.title=poll.getTitle();
        this.options=poll.getOptions();
        this.date=poll.getDate().toString();
        this.expiryDate=poll.getExpiryDate().toString();

    }
    public PollWrapper(Poll poll, Iterator<UserAnswerPoll> userAnswerPollIterator,String userEmail){
        this.id=poll.getArangoId();
        this.title=poll.getTitle();
        this.options=poll.getOptions();
        this.date=poll.getDate().toString();
        this.expiryDate=poll.getExpiryDate().toString();
        this.votes=new ArrayList<>();
        for (Iterator<UserAnswerPoll> it = userAnswerPollIterator; it.hasNext(); ) {
            UserAnswerPoll userAnswerPoll = it.next();
            if(userAnswerPoll.getUser()!=null && userAnswerPoll.getUser().getEmail().equals(userEmail)){
                this.userVote=userAnswerPoll.getOption();
            }
            votes.add(new VoteWrapper(userAnswerPoll));
        }


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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<VoteWrapper> getVotes() {
        return votes;
    }

    public void setVotes(Collection<VoteWrapper> votes) {
        this.votes = votes;
    }

    public String getUserVote() {
        return userVote;
    }

    public void setUserVote(String userVote) {
        this.userVote = userVote;
    }
}
