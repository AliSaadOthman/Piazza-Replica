package org.piazza.user;

public record UserChangePasswordRequest(String oldPassword, String newPassword) {

}
