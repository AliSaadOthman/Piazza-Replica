package org.piazza.requests;

public record LikeAnswerRequest(String answerId,String userEmail,String courseId) {
}
