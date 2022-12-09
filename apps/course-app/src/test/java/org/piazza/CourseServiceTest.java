package org.piazza;

import com.arangodb.DbName;
import com.arangodb.springframework.core.template.ArangoTemplate;
import com.google.common.collect.Iterables;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.piazza.model.document.Answer;
import org.piazza.model.document.Poll;
import org.piazza.model.document.Question;
import org.piazza.model.document.User;
import org.piazza.model.wrappers.AnswerWrapper;
import org.piazza.model.wrappers.PollWrapper;
import org.piazza.model.wrappers.QuestionWrapper;
import org.piazza.model.wrappers.ReportWrapper;
import org.piazza.repository.collection_repository.BaseRepositoryImpl;
import org.piazza.repository.collection_repository.UserRepository;
import org.piazza.repository.query.ConditionSeparator;
import org.piazza.repository.query.FilterCondition;
import org.piazza.repository.query.FilterProperty;
import org.piazza.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BaseRepositoryImpl baseRepository;
    @Autowired
    ArangoTemplate arangoTemplate;

    @BeforeAll
    void addStart() {
        if (!arangoTemplate.driver().getDatabases().contains("piazza"))
            arangoTemplate.driver().createDatabase(DbName.of("piazza"));
        helperAddUser("selim@gmail.com", "student");
        helperAddUser("ahmed@gmail.com", "student");
        helperAddUser("john@gmail.com", "student");
        helperAddUser("sam@gmail.com", "student");
        helperAddUser("eve@gmail.com", "student");
        helperAddUser("bob@gmail.com", "student");
        helperAddUser("mike@gmail.com", "student");
        helperAddUser("ali@gmail.com", "student");
        helperAddUser("mohamed@gmail.com", "student");
        helperAddUser("wael@gmail.com", "student");
        helperAddUser("alaa@gmail.com", "student");
        helperAddUser("avd@gmail.com", "student");
        helperAddUser("mico@gmail.com", "student");
        helperAddUser("mohsen@gmail.com", "student");
        helperAddUser("reem@gmail.com", "instructor");
        helperAddUser("malak@gmail.com", "instructor");
    }

    void helperAddUser(String email, String role) {
        FilterProperty userFilter = new FilterProperty("email", email, FilterCondition.EQUAL);
        FilterProperty userFilter2 = new FilterProperty("role", role, FilterCondition.EQUAL);
        Collection<FilterProperty> userFiltering = new ArrayList<>();
        userFiltering.add(userFilter);
        userFiltering.add(userFilter2);
        org.piazza.model.document.User userArango = new org.piazza.model.document.User(email, role);
        Iterable<org.piazza.model.document.User> users = baseRepository.findBy(org.piazza.model.document.User.class, "users", userFiltering, ConditionSeparator.AND);
        if (Iterables.size(users) == 0) {
            userRepository.save(userArango);
        }
    }

//    @Test
//    @Order(3)
//    void loadtest() throws IOException {
//        String courseId = courseService.getCourseId("CS");
//        for(int i = 1; i<3001;i++) {
//            String email = "loadtest" + Integer.toString(i) + "@gmail.com";
//            org.piazza.model.document.User userArango = new org.piazza.model.document.User(email, "student");
//            userRepository.save(userArango);
//            courseService.registerUser(courseId, email);
//        }
//    }

    @AfterAll
    void deleteEnd() {
        courseService.deleteAll();
    }

    @Test
    @Order(1)
    void CreateCourse() throws IOException {
        courseService.addCourse("reem@gmail.com", "CS");
        courseService.addCourse("reem@gmail.com", "NLP");
        courseService.addCourse("malak@gmail.com", "Scalable");
        boolean t1 = courseService.courseExists("CS");
        boolean t2 = courseService.courseExists("NLP");
        boolean t3 = courseService.courseExists("Scalable");
        boolean t4 = courseService.userIsInCourse("reem@gmail.com", "CS");
        boolean t5 = courseService.userIsInCourse("reem@gmail.com", "NLP");
        boolean t6 = courseService.userIsInCourse("malak@gmail.com", "Scalable");

        assertThat(t1 && t2 && t3 && t4 && t5 && t6).isEqualTo(true);
    }

    @Test
    @Order(2)
    void StudentCreateCourse() throws IOException {
        HashMap<String, Object> response = courseService.addCourse("selim@gmail.com", "Deep Learning");
        Integer status = (Integer) response.get("status");
        assertThat(status).isEqualTo(401);
    }

    @Test
    @Order(2)
    void InstructorCreateTakenCourse() throws IOException {
        HashMap<String, Object> response = courseService.addCourse("reem@gmail.com", "CS");

        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).contains("Course Exists");
    }

    @Test
    @Order(2)
    void addInstructorToCourse() throws IOException {
        String courseId = courseService.getCourseId("NLP");
        courseService.assignInstructorToCourse("reem@gmail.com", "malak@gmail.com", courseId);
        assertThat(courseService.userIsInCourse("malak@gmail.com", "NLP")).isEqualTo(true);
    }

    @Test
    @Order(3)
    void registerStudentToCourse() throws IOException {
        String courseId = courseService.getCourseId("CS");
        courseService.registerUser(courseId, "selim@gmail.com");
        courseService.registerUser(courseId, "ahmed@gmail.com");
        courseService.registerUser(courseId, "john@gmail.com");
        courseService.registerUser(courseId, "sam@gmail.com");
        courseService.registerUser(courseId, "eve@gmail.com");
        courseService.registerUser(courseId, "bob@gmail.com");
        courseService.registerUser(courseId, "mike@gmail.com");
        courseService.registerUser(courseId, "ali@gmail.com");

//        courseService.registerUser(courseId, "selim@gmail.com");
//        courseService.registerUser(courseId, "ahmed@gmail.com");
//        courseService.registerUser(courseId, "john@gmail.com");
//        courseService.registerUser(courseId, "sam@gmail.com");
//        courseService.registerUser(courseId, "eve@gmail.com");
//        courseService.registerUser(courseId, "bob@gmail.com");
//
//        courseService.registerUser(courseId, "mike@gmail.com");
//        courseService.registerUser(courseId, "ali@gmail.com");
//        courseService.registerUser(courseId, "mohamed@gmail.com");
//        courseService.registerUser(courseId, "wael@gmail.com");
//        courseService.registerUser(courseId, "alaa@gmail.com");
//
//        courseService.registerUser(courseId, "avd@gmail.com");
//        courseService.registerUser(courseId, "mico@gmail.com");
//        courseService.registerUser(courseId, "mohsen@gmail.com");


        boolean t1 = courseService.userIsInCourse("selim@gmail.com", "CS");
        boolean t2 = courseService.userIsInCourse("ahmed@gmail.com", "CS");
        boolean t3 = courseService.userIsInCourse("john@gmail.com", "CS");
        boolean t4 = courseService.userIsInCourse("sam@gmail.com", "CS");
        boolean t5 = courseService.userIsInCourse("eve@gmail.com", "CS");
        boolean t6 = courseService.userIsInCourse("bob@gmail.com", "CS");
        boolean t7 = courseService.userIsInCourse("mike@gmail.com", "CS");
        boolean t8 = courseService.userIsInCourse("ali@gmail.com", "CS");
        assertThat(t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8).isEqualTo(true);
    }

    @Test
    @Order(4)
    void registerStudentAlreadyInCourse() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.registerUser(courseId, "selim@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).contains("Student already in course");


    }

    @Test
    @Order(4)
    void registerStudentInCourseDoesNotExist() throws IOException {
        HashMap<String, Object> response = courseService.registerUser("courseId", "selim@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(message).contains("Course does not exist");
    }

    @Test
    @Order(4)
    void registerStudentDoesNotExistInCourse() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.registerUser(courseId, "sagdsdsg@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(message).contains("Student does not exist");
    }

    @Test
    @Order(4)
    void registerInstructorAsStudentInCourse() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.registerUser(courseId, "malak@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(401);
        assertThat(message).isEqualTo("Instructor must be added by course instructor(s)");
    }

    @Test
    @Order(4)
    void addManuallyStudentAlreadyInCourse() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.addStudentsManually("reem@gmail.com", "selim@gmail.com", courseId);

        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).isEqualTo("Student already in course");
    }

    @Test
    @Order(4)
    void addManuallyStudentDoesNotExist() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.addStudentsManually("reem@gmail.com", "asfh@gmail.com", courseId);

        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(message).isEqualTo("Student Does Not Exist");
    }

    @Test
    @Order(4)
    void addManuallyStudentByStudent() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.addStudentsManually("bob@gmail.com", "wael@gmail.com", courseId);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(401);
        assertThat(message).isEqualTo("Instructor must add student and be in course");

    }

    @Test
    @Order(4)
    void addManuallyStudentByInstructorNotInCourse() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.addStudentsManually("malak@gmail.com", "wael@gmail.com", courseId);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(401);
        assertThat(message).isEqualTo("Instructor must add student and be in course");


    }

    @Test
    @Order(4)
    void addStudentManually1() throws IOException {
        String courseId = courseService.getCourseId("Scalable");
        courseService.addStudentsManually("malak@gmail.com", "selim@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "ahmed@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "john@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "sam@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "eve@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "bob@gmail.com", courseId);
        boolean t1 = courseService.userIsInCourse("selim@gmail.com", "Scalable");
        boolean t2 = courseService.userIsInCourse("ahmed@gmail.com", "Scalable");
        boolean t3 = courseService.userIsInCourse("john@gmail.com", "Scalable");
        boolean t4 = courseService.userIsInCourse("sam@gmail.com", "Scalable");
        boolean t5 = courseService.userIsInCourse("eve@gmail.com", "Scalable");
        boolean t6 = courseService.userIsInCourse("bob@gmail.com", "Scalable");
        assertThat(t1 && t2 && t3 && t4 && t5 && t6).isEqualTo(true);
    }

    @Test
    @Order(5)
    void addStudentManually2() throws IOException {
        String courseId = courseService.getCourseId("NLP");
        courseService.addStudentsManually("malak@gmail.com", "mike@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "ali@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "mohamed@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "wael@gmail.com", courseId);
        courseService.addStudentsManually("malak@gmail.com", "alaa@gmail.com", courseId);
        boolean t1 = courseService.userIsInCourse("mike@gmail.com", "NLP");
        boolean t2 = courseService.userIsInCourse("ali@gmail.com", "NLP");
        boolean t3 = courseService.userIsInCourse("mohamed@gmail.com", "NLP");
        boolean t4 = courseService.userIsInCourse("wael@gmail.com", "NLP");
        boolean t5 = courseService.userIsInCourse("alaa@gmail.com", "NLP");
        assertThat(t1 && t2 && t3 && t4 && t5).isEqualTo(true);
    }

    @Test
    @Order(6)
    void addStudentManually3() throws IOException {
        String courseId = courseService.getCourseId("CS");
        courseService.addStudentsManually("reem@gmail.com", "avd@gmail.com", courseId);
        courseService.addStudentsManually("reem@gmail.com", "mico@gmail.com", courseId);
        courseService.addStudentsManually("reem@gmail.com", "mohsen@gmail.com", courseId);
        boolean t1 = courseService.userIsInCourse("avd@gmail.com", "CS");
        boolean t2 = courseService.userIsInCourse("mico@gmail.com", "CS");
        boolean t3 = courseService.userIsInCourse("mohsen@gmail.com", "CS");
        assertThat(t1 && t2 && t3).isEqualTo(true);
    }

    @Test
    @Order(7)
    void reportStudent() throws IOException {
        String courseId = courseService.getCourseId("CS");
        courseService.reportStudent("selim@gmail.com", "eve@gmail.com", "Insulting other users", courseId);
        assertThat(courseService.userReportedUser("selim@gmail.com", "eve@gmail.com", "CS")).isEqualTo(true);
    }

    @Test
    @Order(8)
    void reportStudentThatDoesNotExist() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.reportStudent("john@gmail.com", "sav@gmail.com", "Filler Reason", courseId);


        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(message).isEqualTo("Student or user Does Not Exist");
    }

    @Test
    @Order(8)
    void reportedByUserThatDoesNotExist() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.reportStudent("jov@gmail.com", "sam@gmail.com", "Filler Reason", courseId);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(message).isEqualTo("Student or user Does Not Exist");
    }

    @Test
    @Order(8)
    void reportStudentThatIsNotInCourse() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.reportStudent("john@gmail.com", "wael@gmail.com", "Filler Reason", courseId);

        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(401);
        assertThat(message).isEqualTo("Student to be reported and user must be in same course");
    }

    @Test
    @Order(8)
    void reportedByUserNotInCourse() throws IOException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.reportStudent("wael@gmail.com", "sam@gmail.com", "Filler Reason", courseId);
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(401);
        assertThat(message).isEqualTo("Student to be reported and user must be in same course");

    }

    @Test
    @Order(9)
    void banStudent() throws IOException, IllegalAccessException {
        String courseId = courseService.getCourseId("CS");
        String userId = "eve@gmail.com";
        courseService.banUser("reem@gmail.com", courseId, 20, true, userId);
    }

    @Test
    @Order(10)
    void banStudentNotInCourse() throws IOException, IllegalAccessException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.banUser("reem@gmail.com", courseId, 10, false, "wael@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).isEqualTo("User not in Course");
    }

    @Test
    @Order(10)
    void studentBanStudent() throws IOException, IllegalAccessException {
        String courseId = courseService.getCourseId("NLP");
        HashMap<String, Object> response = courseService.banUser("alaa@gmail.com", courseId, 10, false, "wael@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(401);
        assertThat(message).isEqualTo("Not Authorised");
    }

    @Test
    @Order(10)
    void banInCourseThatDoesNotExist() throws IOException, IllegalAccessException {
        HashMap<String, Object> response = courseService.banUser("reem@gmail.com", "users/12358", 10, false, "wael@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(message).isEqualTo("Course Does Not Exists");

    }

    @Test
    @Order(10)
    void banStudentInstructorNotInCourse() throws IOException, IllegalAccessException {
        String courseId = courseService.getCourseId("Scalable");
        HashMap<String, Object> response = courseService.banUser("reem@gmail.com", courseId, 10, false, "selim@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(401);
        assertThat(message).isEqualTo("Instructor not in Course");

    }

    @Test
    @Order(10)
    void banStudentDoesNotExist() throws IOException, IllegalAccessException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> response = courseService.banUser("reem@gmail.com", courseId, 10, false, "asdfg@gmail.com");
        Integer status = (Integer) response.get("status");
        String message = (String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(message).isEqualTo("User does not exist");
    }

    @Test
    @Order(11)
    void checkBannedStudentIsBanned() throws IOException, JSONException {
        String courseId = courseService.getCourseId("CS");
        HashMap<String, Object> request = new HashMap<>();
        request.put("userEmail", "eve@gmail.com");
        request.put("courseId", courseId);
        Boolean userBanned = courseService.isUserBannedFromCourse(request);
        assertThat(userBanned).isEqualTo(true);
    }

    @Test
    @Order(12)
    void addQuestionTest() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",false,null,courseId,true,"selim@gmail.com");
        int status= (int) response.get("status");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        assertThat(status).isEqualTo(200);
        assertThat(courseService.questionInCourse(newQuestion.getId(),courseId)).isEqualTo(true);

    }
    @Test
    @Order(12)
    void addQuestionToInvalidCourse() throws IOException, JSONException {
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",false,null,"invalid",true,"selim@gmail.com");
        int status= (int) response.get("status");
        String message = (String) response.get("message");

        assertThat(status).isEqualTo(404);
        assertThat(message).isEqualTo("course not found!");

    }
    @Test
    @Order(13)
    void addQuestionAnonymousAndPrivate() throws IOException, JSONException {
        String courseId=courseService.getCourseId("NLP");
        HashMap<String,Object> response=courseService.addQuestion("test question ano and private",null,"will it be anonymous?",true,null,courseId,false,"selim@gmail.com");
        int status= (int) response.get("status");
        String message = (String) response.get("message");

        assertThat(status).isEqualTo(422);

        assertThat(message).isEqualTo("question cannot be anonymous and private");
    }
    @Test
    @Order(13)
    void addQuestionAnonymous() throws IOException {
        String courseId=courseService.getCourseId("NLP");
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",true,null,courseId,true,"selim@gmail.com");
        int status= (int) response.get("status");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        assertThat(status).isEqualTo(200);
        assertThat(courseService.questionInCourse(newQuestion.getId(),courseId)).isTrue();
        assertThat(newQuestion.getUser()).isEqualTo("Anonymous");

    }
    @Test
    @Order(13)
    void addQuestionPrivate() throws IOException {
        String courseId=courseService.getCourseId("NLP");
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",false,null,courseId,false,"selim@gmail.com");
        int status= (int) response.get("status");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        assertThat(status).isEqualTo(200);
        assertThat(courseService.questionInCourse(newQuestion.getId(),courseId)).isTrue();
        assertThat(newQuestion.get_public()).isFalse();
    }

    @Test
    @Order(14)
    void endorseQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",false,null,courseId,true,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        Question question=courseService.getQuestionWithId(newQuestion.getId());

         response=courseService.endorseQuestion(question.getArangoId());
         int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        question=courseService.getQuestionWithId(question.getArangoId());
        assertThat(question.isEndorsed()).isTrue();
    }
    @Test
    @Order(14)
    void likeQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",false,null,courseId,true,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        Question question=courseService.getQuestionWithId(newQuestion.getId());
         response=courseService.likeQuestion("selim@gmail.com",question.getArangoId());
        User user=courseService.getUserWithEmail("selim@gmail.com");
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        question=courseService.getQuestionWithId(question.getArangoId());
        assertThat(question.getLikedUsers().contains(user)).isTrue();


    }

    @Test
    @Order(15)
    void likeQuestionTwice() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",false,null,courseId,true,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        Question question=courseService.getQuestionWithId(newQuestion.getId());
        courseService.likeQuestion("selim@gmail.com",question.getArangoId());
        response=courseService.likeQuestion("selim@gmail.com",question.getArangoId());
        User user=courseService.getUserWithEmail("selim@gmail.com");
        int status= (int) response.get("status");
        String message=(String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).isEqualTo("User already liked question");


    }

    @Test
    @Order(16)
    public void createQuestionWithTaggedUsers() throws IOException {
        String courseId=courseService.getCourseId("CS");
        String[] taggedUsers=new String[]{"ahmed@gmail.com","john@gmail.com"};
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",false,taggedUsers,courseId,true,"selim@gmail.com");
        User ahmed=courseService.getUserWithEmail("ahmed@gmail.com");
        User john=courseService.getUserWithEmail("john@gmail.com");
        int status= (int) response.get("status");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        assertThat(status).isEqualTo(200);
        assertThat(courseService.questionInCourse(newQuestion.getId(),courseId)).isEqualTo(true);

        Question question=courseService.getQuestionWithId(newQuestion.getId());
        assertThat(question.getMentionedUsers().contains(ahmed)).isTrue();
        assertThat(question.getMentionedUsers().contains(john)).isTrue();


    }

    @Test
    @Order(16)
    public void createQuestionWithTaggedUserNotInCourse() throws IOException {
        String courseId=courseService.getCourseId("CS");
        String[] taggedUsers=new String[]{"ahmed@gmail.com","malak@gmail.com"};
        HashMap<String,Object> response=courseService.addQuestion("test question",null,"what is life?",false,taggedUsers,courseId,true,"selim@gmail.com");
        User ahmed=courseService.getUserWithEmail("ahmed@gmail.com");
        User malak=courseService.getUserWithEmail("malak@gmail.com");
        int status= (int) response.get("status");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        assertThat(status).isEqualTo(200);
        assertThat(courseService.questionInCourse(newQuestion.getId(),courseId)).isEqualTo(true);

        Question question=courseService.getQuestionWithId(newQuestion.getId());
        assertThat(question.getMentionedUsers().contains(ahmed)).isTrue();
        assertThat(question.getMentionedUsers().contains(malak)).isFalse();


    }
    @Order(17)
    @Test
    public void searchForQuestionTitle() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> response=courseService.addQuestion("title search",null,"what is life?",false, null,courseId,true,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        HashMap<String,Object> searchResponse=courseService.searchForQuestion(courseId,"title","student","selim@gmail.com");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(200);
        ArrayList<QuestionWrapper> result= (ArrayList<QuestionWrapper>) searchResponse.get("data");
        assertThat(result.size()).isEqualTo(1);
        QuestionWrapper questionWrapper=result.get(0);
        assertThat(questionWrapper.getId()).isEqualTo(newQuestion.getId());

    }
    @Order(17)
    @Test
    public void searchForQuestionDescription() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> response=courseService.addQuestion("...",null,"description is life?",false, null,courseId,true,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        HashMap<String,Object> searchResponse=courseService.searchForQuestion(courseId,"description","student","selim@gmail.com");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(200);
        ArrayList<QuestionWrapper> result= (ArrayList<QuestionWrapper>) searchResponse.get("data");
        assertThat(result.size()).isEqualTo(1);
        QuestionWrapper questionWrapper=result.get(0);
        assertThat(questionWrapper.getId()).isEqualTo(newQuestion.getId());

    }
    @Order(17)
    @Test
    public void searchForNonExistentQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> searchResponse=courseService.searchForQuestion(courseId,"isdjaoidhaudiohaduhaui","student","selim@gmail.com");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(200);
        ArrayList<QuestionWrapper> result= (ArrayList<QuestionWrapper>) searchResponse.get("data");
        assertThat(result.size()).isEqualTo(0);

    }
    @Order(18)
    @Test
    public void SuggestDuplicateQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> response=courseService.addQuestion("duplicate question",null,"this is a duplicate question",false,null,courseId,true,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        HashMap<String,Object> searchResponse=courseService.suggestQuestion("duplicate question","this is a duplicate question",courseId,"selim@gmail.com","student");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(200);
        ArrayList<QuestionWrapper> result= (ArrayList<QuestionWrapper>) searchResponse.get("data");
        assertThat(result.size()).isEqualTo(1);
        QuestionWrapper questionWrapper=result.get(0);
        assertThat(questionWrapper.getId()).isEqualTo(newQuestion.getId());


    }

    @Order(18)
    @Test
    public void SuggestDuplicateOnNonDuplicateQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> searchResponse=courseService.suggestQuestion("a non duplicate question","this is a brand new unique question",courseId,"selim@gmail.com","student");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(200);
        ArrayList<QuestionWrapper> result= (ArrayList<QuestionWrapper>) searchResponse.get("data");
        assertThat(result.size()).isEqualTo(0);

    }
    @Order(19)
    @Test
    public void getQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("Scalable");
        HashMap<String,Object> response=courseService.addQuestion("public",null,"this is a public question",false,null,courseId,true,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        HashMap<String,Object> searchResponse=courseService.getQuestion(newQuestion.getId(), "bob@gmail.com","student");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(200);
        QuestionWrapper result= (QuestionWrapper) searchResponse.get("data");
        assertThat(result.getId()).isEqualTo(newQuestion.getId());

    }
    @Order(19)
    @Test
    public void studentGetPrivateQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("Scalable");
        HashMap<String,Object> response=courseService.addQuestion("private",null,"this is a private question",false,null,courseId,false,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        HashMap<String,Object> searchResponse=courseService.getQuestion(newQuestion.getId(), "bob@gmail.com","student");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(401);
        String result= (String) searchResponse.get("message");
        assertThat(result).isEqualTo("Not Authorized");


    }

    @Order(19)
    @Test
    public void ownerStudentGetPrivateQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("Scalable");
        HashMap<String,Object> response=courseService.addQuestion("private",null,"this is a private question",false,null,courseId,false,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        HashMap<String,Object> searchResponse=courseService.getQuestion(newQuestion.getId(), "selim@gmail.com","student");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(200);
        QuestionWrapper result= (QuestionWrapper) searchResponse.get("data");
        assertThat(result.getId()).isEqualTo(newQuestion.getId());


    }

    @Order(19)
    @Test
    public void instructorGetPrivateQuestion() throws IOException, JSONException {
        String courseId=courseService.getCourseId("Scalable");
        HashMap<String,Object> response=courseService.addQuestion("private",null,"this is a private question",false,null,courseId,false,"selim@gmail.com");
        QuestionWrapper newQuestion= (QuestionWrapper) response.get("data");
        HashMap<String,Object> searchResponse=courseService.getQuestion(newQuestion.getId(), "malak@gmail.com","instructor");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(200);
        QuestionWrapper result= (QuestionWrapper) searchResponse.get("data");
        assertThat(result.getId()).isEqualTo(newQuestion.getId());


    }
    @Order(20)
    @Test
    public void getNonExistentQuestion() throws IOException, JSONException {
        HashMap<String,Object> searchResponse=courseService.getQuestion("dasdadad", "malak@gmail.com","instructor");
        int status= (int) searchResponse.get("status");
        assertThat(status).isEqualTo(404);
        String result= (String) searchResponse.get("message");
        assertThat(result).isEqualTo("Question not found");
    }
    @Order(21)
    @Test
    public void studentGetQuestions() throws IOException, JSONException {
        String courseId=courseService.getCourseId("Scalable");
        HashMap<String,Object> result=courseService.getCourseQuestions(courseId,"bob@gmail.com","student");
        ArrayList<QuestionWrapper> questions= (ArrayList<QuestionWrapper>) result.get("data");
        int status= (int) result.get("status");
        assertThat(status).isEqualTo(200);
        assertThat(questions.size()).isEqualTo(1);


    }
    @Order(21)
    @Test
    public void privateQuestionStudentGetQuestions() throws IOException, JSONException {
        String courseId=courseService.getCourseId("Scalable");
        HashMap<String,Object> result=courseService.getCourseQuestions(courseId,"selim@gmail.com","student");
        ArrayList<QuestionWrapper> questions= (ArrayList<QuestionWrapper>) result.get("data");
        int status= (int) result.get("status");
        assertThat(status).isEqualTo(200);
        assertThat(questions.size()).isEqualTo(4);


    }
    @Order(21)
    @Test
    public void instructorGetCourseQuestions() throws IOException, JSONException {
        String courseId=courseService.getCourseId("Scalable");
        HashMap<String,Object> result=courseService.getCourseQuestions(courseId,"malak@gmail.com","instructor");
        ArrayList<QuestionWrapper> questions= (ArrayList<QuestionWrapper>) result.get("data");
        int status= (int) result.get("status");
        assertThat(status).isEqualTo(200);
        assertThat(questions.size()).isEqualTo(4);


    }
    @Order(22)
    @Test
    public void deleteQuestion() throws JSONException, IOException {
        String courseId=courseService.getCourseId("Scalable");
        Question question=courseService.getQuestionFromCourse(courseId);
        HashMap<String,Object> response=courseService.deleteQuestion(question.getArangoId());
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        assertThat(courseService.questionInCourse(question.getArangoId(),courseId)).isFalse();
    }
    @Order(23)
    @Test
    public void addAnswer() throws IOException {
        String courseId=courseService.getCourseId("CS");
        Question question=courseService.getQuestionFromCourse(courseId);
        HashMap<String,Object> response=courseService.addAnswer("new answer","aaaaaaa",null,"selim@gmail.com",question.getArangoId());
        User user=courseService.getUserWithEmail("selim@gmail.com");
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        AnswerWrapper answerWrapper= (AnswerWrapper) response.get("data");
        Answer answer=courseService.getAnswerById(answerWrapper.getId());
        question=courseService.getQuestionWithId(question.getArangoId());
        assertThat(question.getAnswers().contains(answer)).isTrue();

    }
    @Order(23)
    @Test
    public void addAnswerToInvalidQuestion() throws IOException {
        HashMap<String,Object> response=courseService.addAnswer("new answer","aaaaaaa",null,"selim@gmail.com","ASdandsiad");
        int status= (int) response.get("status");
        String data=(String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(data).isEqualTo("question not found!") ;

    }
    @Test
    @Order(25)
    void endorseAnswer() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        Question question=courseService.getQuestionFromCourse(courseId);
        HashMap<String,Object> response=courseService.addAnswer("new answer","aaaaaaa",null,"selim@gmail.com",question.getArangoId());
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        AnswerWrapper answerWrapper= (AnswerWrapper) response.get("data");
        Answer answer=courseService.getAnswerById(answerWrapper.getId());
        courseService.endorseAnswer(answer.getArangoId());
        assertThat(status).isEqualTo(200);
        answer=courseService.getAnswerById(answer.getArangoId());
        assertThat(answer.isEndorsed()).isTrue();
    }
    @Test
    @Order(26)
    void likeAnswer() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        Question question=courseService.getQuestionFromCourse(courseId);
        HashMap<String,Object> response=courseService.addAnswer("new answer","aaaaaaa",null,"selim@gmail.com",question.getArangoId());
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        AnswerWrapper answerWrapper= (AnswerWrapper) response.get("data");
        Answer answer=courseService.getAnswerById(answerWrapper.getId());
        HashMap<String,Object> likeResponse=courseService.likeAnswer("selim@gmail.com",answer.getArangoId());
        User user=courseService.getUserWithEmail("selim@gmail.com");
         status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        answer=courseService.getAnswerById(answer.getArangoId());
        assertThat(answer.getUserLikedAnswer().contains(user)).isTrue();

    }

    @Test
    @Order(27)
    void likeAnswerTwice() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        Question question=courseService.getQuestionFromCourse(courseId);
        HashMap<String,Object> response=courseService.addAnswer("new answer","aaaaaaa",null,"selim@gmail.com",question.getArangoId());
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        AnswerWrapper answerWrapper= (AnswerWrapper) response.get("data");
        Answer answer=courseService.getAnswerById(answerWrapper.getId());
        response=courseService.likeAnswer("selim@gmail.com",answer.getArangoId());
        User user=courseService.getUserWithEmail("selim@gmail.com");
        status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        answer=courseService.getAnswerById(answer.getArangoId());
        assertThat(answer.getUserLikedAnswer().contains(user)).isTrue();

        response=courseService.likeAnswer("selim@gmail.com",answer.getArangoId());
        status= (int) response.get("status");
        String message=(String) response.get("message");
        assertThat(status).isEqualTo(422);
        assertThat(message).isEqualTo("user already liked this answer!");
    }

    @Test
    @Order(28)
    void deleteAnswer() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        Question question=courseService.getQuestionFromCourse(courseId);
        HashMap<String,Object> response=courseService.addAnswer("new answer","aaaaaaa",null,"selim@gmail.com",question.getArangoId());
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        AnswerWrapper answerWrapper= (AnswerWrapper) response.get("data");
        Answer answer=courseService.getAnswerById(answerWrapper.getId());
         response=courseService.deleteAnswer(answer.getArangoId());
         status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        answer=courseService.getAnswerById(answer.getArangoId());
        assertThat(answer).isNull();


    }
    @Test
    @Order(29)
    void deleteNonExistentAnswer() throws JSONException, IOException {
        HashMap<String,Object> response=courseService.deleteAnswer("sadada");
        int status= (int) response.get("status");
        String data=(String) response.get("message");
        assertThat(status).isEqualTo(404);
        assertThat(data).isEqualTo("Answer not found!") ;

    }
    @Test
    @Order(30)
    void generateLinkAndEnrollInCourseUsingLink() throws IOException, JSONException {
        String courseId = courseService.getCourseId("NLP");
        HashMap<String, Object> response = courseService.generateCourseLink(courseId);
        int status = (int) response.get("status");
        String data = (String) response.get("data");
        assertThat(status).isEqualTo(200);
        boolean t1=courseService.userIsInCourseId("mico@gmail.com",courseId);
        assertThat(t1).isFalse();
        response=courseService.enrollUsingCourseLink("mico@gmail.com",data);
        status = (int) response.get("status");
        assertThat(status).isEqualTo(200);
        boolean t2=courseService.userIsInCourseId("mico@gmail.com",courseId);
        assertThat(t2).isTrue();

    }
    @Test
    @Order(31)
    public void addPoll() throws IOException, JSONException, ParseException {
        String courseId=courseService.getCourseId("CS");
        ArrayList<String> options=new ArrayList<String>();
        options.add("yes");
        options.add("no");
        options.add("maybe");
        Date today=new Date();
        Date tomorrow=new Date(today.getTime()+(1000*60*60*24));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate=simpleDateFormat.format(tomorrow);
        HashMap<String,Object> response=courseService.createPoll("Test poll",options,strDate,"reem@gmail.com",courseId);
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        PollWrapper pollWrapper= (PollWrapper) response.get("data");
        Poll poll=courseService.getPollById(pollWrapper.getId());
        boolean t1=courseService.pollInCourse(courseId,poll);
        assertThat(t1).isTrue();
    }
    @Test
    @Order(32)
    public void votePoll() throws IOException, JSONException, ParseException {
        String courseId=courseService.getCourseId("CS");
        ArrayList<String> options=new ArrayList<String>();
        options.add("yes");
        options.add("no");
        options.add("maybe");
        Date today=new Date();
        Date tomorrow=new Date(today.getTime()+(1000*60*60*24));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate=simpleDateFormat.format(tomorrow);
        HashMap<String,Object> response=courseService.createPoll("Test poll",options,strDate,"reem@gmail.com",courseId);
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        PollWrapper pollWrapper= (PollWrapper) response.get("data");
        Poll poll=courseService.getPollById(pollWrapper.getId());
        response=courseService.votePoll(poll.getArangoId(),"yes",courseId,"selim@gmail.com");
        status=(int) response.get("status");
        assertThat(status).isEqualTo(200);
        String userOption=courseService.userVoteInPoll("selim@gmail.com",poll.getArangoId());
        assertThat(userOption).isEqualTo("yes");
    }
    @Test
    @Order(33)
    public void voteAfterExpirationPoll() throws IOException, JSONException, ParseException {
        String courseId=courseService.getCourseId("CS");
        ArrayList<String> options=new ArrayList<String>();
        options.add("yes");
        options.add("no");
        options.add("maybe");
        Date today=new Date();
        Date tomorrow=new Date(today.getTime()-(1000*60*60*24));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate=simpleDateFormat.format(tomorrow);
        HashMap<String,Object> response=courseService.createPoll("Test poll",options,strDate,"reem@gmail.com",courseId);
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        PollWrapper pollWrapper= (PollWrapper) response.get("data");
        Poll poll=courseService.getPollById(pollWrapper.getId());
        response=courseService.votePoll(poll.getArangoId(),"yes",courseId,"selim@gmail.com");
        status=(int) response.get("status");
        assertThat(status).isEqualTo(422);
        String data= (String) response.get("message");
        assertThat(data).isEqualTo("The poll has expired!");
    }
    @Test
    @Order(33)
    public void voteInvalidOptionPoll() throws IOException, JSONException, ParseException {
        String courseId=courseService.getCourseId("CS");
        ArrayList<String> options=new ArrayList<String>();
        options.add("yes");
        options.add("no");
        options.add("maybe");
        Date today=new Date();
        Date tomorrow=new Date(today.getTime()+(1000*60*60*24));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate=simpleDateFormat.format(tomorrow);
        HashMap<String,Object> response=courseService.createPoll("Test poll",options,strDate,"reem@gmail.com",courseId);
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        PollWrapper pollWrapper= (PollWrapper) response.get("data");
        Poll poll=courseService.getPollById(pollWrapper.getId());
        response=courseService.votePoll(poll.getArangoId(),"idk",courseId,"selim@gmail.com");
        status=(int) response.get("status");
        assertThat(status).isEqualTo(422);
        String data= (String) response.get("message");
        assertThat(data).isEqualTo("Option not valid");
    }


    @Test
    @Order(33)
    public void getPoll() throws IOException, JSONException, ParseException {
        String courseId=courseService.getCourseId("CS");
        ArrayList<String> options=new ArrayList<String>();
        options.add("yes");
        options.add("no");
        options.add("maybe");
        Date today=new Date();
        Date tomorrow=new Date(today.getTime()+(1000*60*60*24));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate=simpleDateFormat.format(tomorrow);
        HashMap<String,Object> response=courseService.createPoll("Test poll",options,strDate,"reem@gmail.com",courseId);
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        PollWrapper pollWrapper= (PollWrapper) response.get("data");
        Poll poll=courseService.getPollById(pollWrapper.getId());
        response=courseService.getPoll(poll.getArangoId(),"selim@gmail.com");
        status=(int) response.get("status");
        assertThat(status).isEqualTo(200);
        PollWrapper data= (PollWrapper) response.get("data");
        assertThat(data.getId()).isEqualTo(poll.getArangoId());
    }
    @Test
    @Order(34)
    public void getInvalidPoll() throws JSONException, IOException {
        HashMap<String,Object> response=courseService.getPoll("teasdada","selim@gmail.com");
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(404);

    }
    @Test
    @Order(35)
    public void getCoursePoll() throws IOException, JSONException, ParseException {
        String courseId=courseService.getCourseId("Scalable");
        ArrayList<String> options=new ArrayList<String>();
        options.add("yes");
        options.add("no");
        options.add("maybe");
        Date today=new Date();
        Date tomorrow=new Date(today.getTime()+(1000*60*60*24));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate=simpleDateFormat.format(tomorrow);
        HashMap<String,Object> response=courseService.createPoll("Test poll",options,strDate,"reem@gmail.com",courseId);
        int status= (int) response.get("status");
        assertThat(status).isEqualTo(200);
        PollWrapper pollWrapper= (PollWrapper) response.get("data");
        Poll poll=courseService.getPollById(pollWrapper.getId());
        response=courseService.getPolls(courseId);
        status=(int) response.get("status");
        assertThat(status).isEqualTo(200);
        ArrayList<PollWrapper> data= (ArrayList<PollWrapper>) response.get("data");
        assertThat(data.size()).isEqualTo(1);
    }

    @Test
    @Order(36)
    public void getReports() throws IOException, JSONException {
        String courseId=courseService.getCourseId("CS");
        HashMap<String,Object> response=courseService.getReports(courseId);
        int status=(int) response.get("status");
        assertThat(status).isEqualTo(200);
        ArrayList<ReportWrapper> reportWrappers= (ArrayList<ReportWrapper>) response.get("data");
        assertThat(reportWrappers.size()).isEqualTo(1);
        ReportWrapper reportWrapper=reportWrappers.get(0);
        assertThat(reportWrapper.getUserReported()).isEqualTo("eve@gmail.com");
        assertThat(reportWrapper.getUserCreatedReport()).isEqualTo("selim@gmail.com");
        assertThat(reportWrapper.getReason()).isEqualTo("Insulting other users");
    }
}


