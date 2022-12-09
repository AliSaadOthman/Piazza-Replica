package org.piazza.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.piazza.utils.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Service("userService")
public class UserService implements UserDetailsService, Receiver {

    private final UserDao userDao;
    BCryptPasswordEncoder encoder;
    @Autowired
    KafkaTemplate<String,Map<String,Object>> kafkaTemplate;
    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
        encoder = new BCryptPasswordEncoder();
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.getUser(s);
        if(user == null){
            throw new UsernameNotFoundException("User not found in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getrole()));
        return new org.springframework.security.core.userdetails.User(user.getemail(), user.getpassword(), authorities);
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException{
        try {
            User user = userDao.getUser(username);
            if(user == null){
                throw new UsernameNotFoundException("User not found in database");
            }
            return user;
        }
        catch (Exception e){
            throw new UsernameNotFoundException("Error: User not found in database");
        }
    }
    public List<User> getUsers() throws SQLException {
        List<User> users = userDao.selectUsers();
        for (int i = 0; i < users.size(); i++) {

            // Print all elements of List
            System.out.println(users.get(i).getemail());
        }

        return users;
    }

    public HashMap<String, Object> addNewUserTest(String email, String password, String firstName, String lastName, String role) {
        HashMap<String, Object> response = new HashMap<>();
        User user = User.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build();
        if (!((user.getpassword().length() >= 8) && (user.getpassword().length() <= 46))) {
            response.put("status", 500);
            response.put("message", "Password must be between 8 and 46 characters long");
            return response;
        }
        if (true) {
            int count = 0;
            // checking capital letters
            for (int i = 65; i <= 90; i++) {
                // type casting
                char c = (char)i;
                String str1 = Character.toString(c);
                if (user.getpassword().contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                response.put("status", 500);
                response.put("message", "Password must contain a capital (UpperCase) letter");
                return response;
            }
        }
        if (true) {
            int count = 0;
            for (int i = 0; i <= 9; i++) {
                String str1 = Integer.toString(i);

                if (user.getpassword().contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                response.put("status", 500);
                response.put("message", "Password must contain a number");
                return response;
            }
        }
        String encoded = encoder.encode(user.getpassword());
        user.setPassword(encoded);

        try {
            int result = userDao.insertUser(user);
            if (result != 1) {
                response.put("status", 500);
                response.put("message", "Email already registered");
                return response;
            }
            else{
                response.put("status", 200);
                response.put("data","User Registered Successfully");
                return response;
            }
        }
        catch(Exception e){
            response.put("status", 500);
            response.put("message", e.getMessage());
            return response;
        }
    }

    public HashMap<String, Object> addNewUser(String email, String password, String firstName, String lastName, String role) {
        HashMap<String, Object> response = new HashMap<>();
        User user = User.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build();
        if (!((user.getpassword().length() >= 8) && (user.getpassword().length() <= 46))) {
            response.put("status", 500);
            response.put("message", "Password must be between 8 and 46 characters long");
            return response;
        }
        if (true) {
            int count = 0;
            // checking capital letters
            for (int i = 65; i <= 90; i++) {
                // type casting
                char c = (char)i;
                String str1 = Character.toString(c);
                if (user.getpassword().contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                response.put("status", 500);
                response.put("message", "Password must contain a capital (UpperCase) letter");
                return response;
            }
        }
        if (true) {
            int count = 0;
            for (int i = 0; i <= 9; i++) {
                String str1 = Integer.toString(i);

                if (user.getpassword().contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                response.put("status", 500);
                response.put("message", "Password must contain a number");
                return response;
            }
        }
        String encoded = encoder.encode(user.getpassword());
        user.setPassword(encoded);

        try {
            int result = userDao.insertUser(user);
            if (result != 1) {
                response.put("status", 500);
                response.put("message", "Email already registered");
                return response;
            }
            else{
                Map<String, Object> objectMap = new Hashtable<>();
                Map<String, Object> jsonObject = new HashMap<>();
                jsonObject.put("email",email);
                jsonObject.put("role",role);
                objectMap.put("body",jsonObject);
                objectMap.put("commandName", "AddUserCommand");
                kafkaTemplate.send("course.topic",objectMap);
                response.put("status", 200);
                response.put("data","User Registered Successfully");
                return response;
            }
        }
        catch(Exception e){
            response.put("status", 500);
            response.put("message", e.getMessage());
            return response;
        }
    }

    //for testing
    public void deleteAll(){
        userDao.deleteAll();
    }

    public HashMap<String,Object> changePassword(String oldPassword, String newPassword, String username) {
        HashMap<String,Object> response = new HashMap<>();
        User user;
        try {
            user = getUserByUsername(username);
        } catch (Exception e) {
            response.put("status",422);
            response.put("message","Email not registered: " + e.getMessage());
            return response;
        }
        String password = user.getpassword();
        if (encoder.matches(oldPassword,password)) {
            if (!((newPassword.length() >= 8) && (newPassword.length() <= 46))) {
                response.put("status",422);
                response.put("message","Password must be between 8 and 46 characters long");
                return response;
            }
            if (true) {
                int count = 0;
                // checking capital letters
                for (int i = 65; i <= 90; i++) {
                    // type casting
                    char c = (char) i;
                    String str1 = Character.toString(c);
                    if (newPassword.contains(str1)) {
                        count = 1;
                    }
                }
                if (count == 0) {
                    response.put("status",422);
                    response.put("message","Password must contain a capital (UpperCase) letter");
                    return response;
                }
            }
            if (true) {
                int count = 0;
                for (int i = 0; i <= 9; i++) {
                    String str1 = Integer.toString(i);

                    if (newPassword.contains(str1)) {
                        count = 1;
                    }
                }
                if (count == 0) {
                    response.put("status",422);
                    response.put("message","Password must contain a number");
                    return response;
                }
            }
            String encoded = encoder.encode(newPassword);
            user.setPassword(encoded);
            try {
                userDao.changePassword(encoded, username);
            } catch (Error e) {
                response.put("status",422);
                response.put("message","Issue with email");
                return response;
            }
            response.put("status",200);
            return response;
        } else {
            response.put("status",422);
            response.put("message","Incorrect Password");
            return response;
        }
    }

    public HashMap<String,Object> deleteUser(String email, String username, String role){
        HashMap<String,Object> response = new HashMap<>();
        if(email.equals(username)) {
            int result = userDao.deleteUser(email);
            if (result != 1) {
                response.put("status",404);
                response.put("message","Email not registered");
                return response;
            } else {
                Map<String, Object> objectMap = new Hashtable<>();
                Map<String, Object> jsonObject = new HashMap<>();
                jsonObject.put("email",email);
                objectMap.put("body",jsonObject);
                objectMap.put("commandName", "DeleteUserCommand");
                kafkaTemplate.send("course.topic",objectMap);
                response.put("status",200);
                return response;
            }
        }
        else{
            response.put("status",500);
            response.put("message","Cannot delete other users account");
            return response;
        }
    }

    public HashMap<String,Object> deleteUserTest(String email, String username, String role){
        HashMap<String,Object> response = new HashMap<>();
        if(email.equals(username)) {
            int result = userDao.deleteUser(email);
            if (result != 1) {
                response.put("status",404);
                response.put("message","Email not registered");
                return response;
            } else {
                response.put("status",200);
                return response;
            }
        }
        else{
            response.put("status",500);
            response.put("message","Cannot delete other users account");
            return response;
        }
    }

    public HashMap<String, Object> logIn(String password, String email, String url) throws IOException {

        HashMap<String, Object> response = new HashMap<>();
        try {
            User user = userDao.getUser(email);
            if(encoder.matches(password,user.getpassword())){
                Algorithm algorithm = Algorithm.HMAC256("secretpiazza".getBytes());
                String access_token = JWT.create()
                        .withSubject(user.getemail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                        .withIssuer(url)
                        .withClaim("role",user.getrole())
                        .sign(algorithm);

                String refresh_token = JWT.create()
                        .withSubject(user.getemail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 120 * 60 * 1000))
                        .withIssuer(url)
                        .sign(algorithm);

                response.put("status", 200);
                response.put("access_token", access_token);
                response.put("refresh_token", refresh_token);
                return response;
            }
            else{
                response.put("status",422);
                response.put("message","Email or Password Incorrect");
                return response;
            }
        }
        catch (Exception e){
            response.put("status",500);
            response.put("message",e.getMessage());
            return response;
        }

    }
    public HashMap<String,Object> refreshToken(String authorizationHeader, String url){
        HashMap<String,Object> response=new HashMap<>();
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secretpiazza".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = getUserByUsername(username);

                String access_token = JWT.create()
                        .withSubject(user.getemail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(url)
                        .withClaim("role",user.getrole())
                        .sign(algorithm);
                response.put("status", 200);
                response.put("access_token", access_token);
                response.put("refresh_token", refresh_token);
                return response;
            }
            catch(Exception e){
                response.put("status",500);
                response.put("message",e.getMessage());
                return response;
            }
        }
        else{
            response.put("status",404);
            response.put("message","Refresh token is missing");
            return response;
        }
    }

    public void testConn() {
        try {
            userDao.testConn();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}