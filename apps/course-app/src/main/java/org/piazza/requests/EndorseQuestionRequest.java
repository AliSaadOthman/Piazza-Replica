package org.piazza.requests;

public record EndorseQuestionRequest(String questionId,String userEmail,String courseId) {
}
