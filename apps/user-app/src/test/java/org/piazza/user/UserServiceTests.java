package org.piazza.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    BCryptPasswordEncoder encoder;
    @AfterEach
    void tearDown(){
        userService.deleteAll();
//        userRepository.deleteAll();
    }

    @Test
    void getUserByUsernameIfExists() {
//        userService = new UserService(new UsersDataAccessService(new JdbcTemplate()));
        String email = "selim@gmail.com";
        //given
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";
        //When
        userService.addNewUserTest(email, password, firstName, lastName, role);
        User retrieved = userService.getUserByUsername(email);
        //then
        boolean t1 = email.equals(retrieved.getemail());
        boolean t2 = firstName.equals(retrieved.getfirstName());
        boolean t3 = lastName.equals(retrieved.getlastName());
        boolean t4 = role.equals(retrieved.getrole());
        boolean t5 = encoder.matches(password,retrieved.getpassword());
        assertThat(t1 && t2 && t3 && t4 && t5).isEqualTo(true);
    }

//    @Test
//    void load3000() {
//        for(int i = 1; i<3001;i++) {
//
//            String email = "loadtest"+Integer.toString(i)+"@gmail.com";
//            String password = "Password1";
//            String firstName = "load";
//            String lastName = "test";
//            String role = "student";
//            //When
//            userService.addNewUserTest(email, password, firstName, lastName, role);
//        }
//    }


    @Test
    void getUserByUsernameIfNotExists(){
        String email = "selim@gmail.com";
        assertThatThrownBy(() -> userService.getUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in database");
    }

    @Test
    void addNewUser() {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        HashMap<String, Object> response = userService.addNewUserTest(email, password, firstName, lastName, role);
        User retrieved = userService.getUserByUsername(email);
        //then
        boolean t1 = email.equals(retrieved.getemail());
        boolean t2 = firstName.equals(retrieved.getfirstName());
        boolean t3 = lastName.equals(retrieved.getlastName());
        boolean t4 = role.equals(retrieved.getrole());
        boolean t5 = encoder.matches(password,retrieved.getpassword());
        assertThat(t1 && t2 && t3 && t4 && t5).isEqualTo(true);
        Integer status = (Integer) response.get("status");
        assertThat(status).isEqualTo(200);

    }

    @Test
    void addNewUserIfExistsAlready() {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.addNewUserTest(email, password, firstName, lastName, role);
        Integer status = (Integer) response.get("status");
        assertThat(status).isEqualTo(500);
    }

    @Test
    void addNewUserIfUnfitPassword1() {
        String email = "selim@gmail.com";
        String password = "password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        HashMap<String, Object> response = userService.addNewUserTest(email, password, firstName, lastName, role);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(500);
        assertThat(message).isEqualTo("Password must contain a capital (UpperCase) letter");
    }

    @Test
    void addNewUserIfUnfitPassword2() {
        String email = "selim@gmail.com";
        String password = "Password";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        HashMap<String, Object> response = userService.addNewUserTest(email, password, firstName, lastName, role);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(500);
        assertThat(message).isEqualTo("Password must contain a number");
    }

    @Test
    void addNewUserIfUnfitPassword3() throws MalformedURLException {
        String email = "selim@gmail.com";
        String password = "Pord1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        HashMap<String, Object> response = userService.addNewUserTest(email, password, firstName, lastName, role);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(500);
        assertThat(message).isEqualTo("Password must be between 8 and 46 characters long");
    }

    @Test
    void addNewUserIfUnfitPassword4() {
        String email = "selim@gmail.com";
        String password = "Password1fillerfillerfiller" +
                "fillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfiller" +
                "fillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfiller" +
                "fillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfiller" +
                "fillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfiller" +
                "fillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfiller" +
                "fillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfillerfiller";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        HashMap<String, Object> response = userService.addNewUserTest(email, password, firstName, lastName, role);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(500);
        assertThat(message).isEqualTo("Password must be between 8 and 46 characters long");
    }

    @Test
    void deleteUser() {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.deleteUserTest("selim@gmail.com","selim@gmail.com","student");
        assertThatThrownBy(() -> userService.getUserByUsername("selim@gmail.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in database");

        Integer status = (Integer) response.get("status");
        assertThat(status).isEqualTo(200);
    }

    @Test
    void deleteOtherUser() {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.deleteUserTest("selim@gmail.com","mahmoud@gmail.com","student");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(500);
        assertThat(message).isEqualTo("Cannot delete other users account");
    }
    @Test
    void changePassword() {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.changePassword("Password1","Password2",email);
        User x = userService.getUserByUsername(email);
        assertThat(encoder.matches("Password2",x.getpassword()));
        Integer status = (Integer) response.get("status");
        assertThat(status).isEqualTo(200);
    }

    @Test
    void changePasswordWrongOld() {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.changePassword("Password3","Password2",email);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).isEqualTo("Incorrect Password");
    }

    @Test
    void changePasswordUserNotCreated() {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.changePassword("Password3","Password2","mahmoud@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).isEqualTo("Email not registered: Error: User not found in database");
    }

    @Test
    void changePasswordUnfitPassword() {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.changePassword("Password1","password2","selim@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).isEqualTo("Password must contain a capital (UpperCase) letter");
    }

    @Test
    void login() throws IOException {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.logIn("Password1","selim@gmail.com", "http://localhost:8080/api/login");
        Integer status = (Integer) response.get("status");
        String access = (String) response.get("access_token");
        String refresh = (String) response.get("refresh_token");
        assertThat(status).isEqualTo(200);
        assertThat(access.length()>10).isEqualTo(true);
        assertThat(refresh.length()>10).isEqualTo(true);
    }

    @Test
    void incorrectLoginUserDoesNotExist() throws IOException {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.logIn("Password1","test@gmail.com", "http://localhost:8080/api/login");
        Integer status = (Integer) response.get("status");
        assertThat(status).isEqualTo(500);
    }
    @Test
    void incorrectLoginIncorrectPassword() throws IOException {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.logIn("Passsword1","selim@gmail.com", "http://localhost:8080/api/login");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).isEqualTo("Email or Password Incorrect");
    }

    @Test
    void resfreshToken() throws IOException {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.logIn("Password1","selim@gmail.com", "http://localhost:8080/api/login");
        Integer status = (Integer) response.get("status");
        String refresh = (String) response.get("refresh_token");
        assertThat(status).isEqualTo(200);

        HashMap<String, Object> response2 = userService.refreshToken("Bearer "+refresh, "http://localhost:8080/api/login");
        Integer status2 = (Integer) response2.get("status");
        String access2 = (String) response2.get("access_token");
        String refresh2 = (String) response2.get("refresh_token");
        assertThat(status2).isEqualTo(200);
        assertThat(access2.length()>10).isEqualTo(true);
        assertThat(refresh2.length()>10).isEqualTo(true);
    }

    @Test
    void resfreshIncorrectToken() throws IOException {
        String email = "selim@gmail.com";
        String password = "Password1";
        String firstName = "selim";
        String lastName = "Elbindary";
        String role = "student";

        userService.addNewUserTest(email, password, firstName, lastName, role);
        HashMap<String, Object> response = userService.logIn("Password1","selim@gmail.com", "http://localhost:8080/api/login");
        Integer status = (Integer) response.get("status");
        String refresh = (String) response.get("refresh_token");
        assertThat(status).isEqualTo(200);

        HashMap<String, Object> response2 = userService.refreshToken(refresh, "http://localhost:8080/api/login");
        Integer status2 = (Integer) response2.get("status");
        String message = (String) response2.get("message");
        assertThat(status2).isEqualTo(404);
        assertThat(message).isEqualTo("Refresh token is missing");
    }
    @Test
    void connectionTest(){
        userService.testConn();
    }
}