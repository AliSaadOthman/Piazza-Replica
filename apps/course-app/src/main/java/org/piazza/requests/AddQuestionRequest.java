package org.piazza.requests;

public record AddQuestionRequest(String description,String mediaLink,String userEmail,boolean isPublic,String title,String courseId,String[] tags) {

}
