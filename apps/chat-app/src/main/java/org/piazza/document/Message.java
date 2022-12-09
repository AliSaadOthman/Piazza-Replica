package org.piazza.document;

import com.google.cloud.firestore.FieldValue;

public class Message {
    private String userId;
    private String content;
    private String groupChatId;
    private FieldValue timestamp;

    // [START fs_class_definition]
    // [START firestore_data_custom_type_definition]
    public Message() {
        // Must have a public no-argument constructor
    }
    // Initialize all fields of a city
    public Message(
            String userId,
            String content,
            String groupChatId,
            FieldValue timestamp
            ) {
        this.userId = userId;
        this.content = content;
        this.groupChatId = groupChatId;
        this.timestamp = timestamp;
    }
    public String getUserId() {
        return userId;
    }
    public String getContent() {
        return content;
    }

    public String getGroupChatId() { return groupChatId;}
}