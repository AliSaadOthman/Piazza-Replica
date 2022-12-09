package org.piazza.requests;

public record EndorseAnswerRequest(String answerId,String userEmail,String courseId) {
}
