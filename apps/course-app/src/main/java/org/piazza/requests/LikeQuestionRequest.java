package org.piazza.requests;

public record LikeQuestionRequest(String questionId,String courseId,String userEmail) {
}
