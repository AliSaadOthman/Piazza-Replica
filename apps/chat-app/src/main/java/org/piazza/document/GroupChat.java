package org.piazza.document;

import java.util.ArrayList;

public class GroupChat {

    private ArrayList<String> userIds;

    public GroupChat() {
    }
    public GroupChat(
            ArrayList<String> userIds) {
        this.userIds = userIds;
    }
    public ArrayList<String> getUserIds() {
        return userIds;
    }
    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }
}