package org.piazza.requests;

public record AddAnswerRequest(String title,String description,String mediaLink,String courseId,String userEmail,String questionId) {
}
