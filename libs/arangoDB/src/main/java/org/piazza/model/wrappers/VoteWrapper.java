package org.piazza.model.wrappers;

import org.piazza.model.edge.UserAnswerPoll;

public class VoteWrapper {
    private String id;
    private String user;
    private String option;

    public VoteWrapper(UserAnswerPoll userAnswerPoll){
        this.id=userAnswerPoll.getId();
        if(userAnswerPoll.getUser()!=null){
            this.user=userAnswerPoll.getUser().getEmail();
        }else{
            this.user="Deleted User";
        }
        this.option=userAnswerPoll.getOption();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
