package org.piazza.user;

public record UserSignUpRequest(String firstName,String lastName,String email,String password, String role) {

}