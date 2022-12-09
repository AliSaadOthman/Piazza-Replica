package org.piazza.services;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.DbName;
import com.arangodb.springframework.core.template.ArangoTemplate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.internal.util.collections.Iterables;
import org.piazza.model.document.*;
import org.piazza.model.edge.*;
import org.piazza.model.wrappers.AnswerWrapper;
import org.piazza.model.wrappers.PollWrapper;
import org.piazza.model.wrappers.QuestionWrapper;
import org.piazza.model.wrappers.ReportWrapper;
import org.piazza.repository.collection_repository.*;
import org.piazza.repository.graph_repository.*;
import org.piazza.repository.query.*;
import org.piazza.utils.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.time.ZonedDateTime;
import java.util.concurrent.locks.Condition;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service("courseService")
@Slf4j
public class CourseService implements Receiver {

    @Autowired
    UserAnswerPollRepository userAnswerPollRepository;
    @Autowired
    QuestionHasAnswerRepository questionHasAnswerRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionMentionedUserRepository questionMentionedUserRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserLikesQuestionRepository userLikesQuestionRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CourseQuestionEdgeRepository courseQuestionEdgeRepository;
    @Autowired
    UserMakeQuestionRepository userMakeQuestionRepository;
    @Autowired
    BaseRepositoryImpl baseRepository;
    @Autowired
    EdgeBaseRepositoryImpl edgeBaseRepository;
    @Autowired
    UserMakeAnswerRepository userMakeAnswerRepository;
    @Autowired
    UserLikesAnswerRepository userLikesAnswerRepository;
    @Autowired
    CourseHasPollRepository courseHasPollRepository;
    @Autowired
    UserCreatePollRepository userCreatePollRepository;
    @Autowired
    PollRepository pollRepository;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    UserInCourseRepository userInCourseRepository;
    @Autowired
    ArangoTemplate arangoTemplate;
    @Autowired
    UserBannedFromCourseRepository userBannedFromCourseRepository;
    @Autowired
    UserCreateReportRepository userCreateReportRepository;
    @Autowired
    UserReportedRepository userReportedRepository;
    @Autowired
    private ArangoDB arangoDB;

    final String secret = "Secret Piazza";
    final String dbName = "piazza";

    @PostConstruct
    public void init() {
        if (!arangoDB.db(DbName.of(dbName)).exists())
            arangoDB.db(DbName.of(dbName)).create();
    }

    public HashMap<String, Object> addAnswer(String title, String description, String mediaLink, String userEmail, String questionId) throws IOException {

        HashMap<String, Object> response = new HashMap<>();
        try {
            Answer newAnswer = new Answer(title, description, mediaLink, ZonedDateTime.now());
            Optional<Question> questionOptional = questionRepository.findById(questionId);
            if (!questionOptional.isPresent()) {
                response.put("status", 404);
                response.put("message", "question not found!");
                return response;
            }
            Question question = questionOptional.get();
            answerRepository.save(newAnswer);

            questionHasAnswerRepository.save(new QuestionHasAnswer(question, newAnswer));
            FilterProperty userFilter = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering = new ArrayList<>();
            userFiltering.add(userFilter);
            Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
            User user = users.iterator().next();
            userMakeAnswerRepository.save(new UserMakeAnswer(user, newAnswer));
            response.put("status", 200);
            response.put("data",new AnswerWrapper(newAnswer,userEmail));
            return response;
        } catch (Exception error) {
            System.out.println(error.getMessage());
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }
    }

    public HashMap<String, Object> endorseAnswer(String answerId) throws IOException, JSONException {
        HashMap<String, Object> response = new HashMap<>();
        try {


            Optional<Answer> result = answerRepository.findById(answerId);
            if (result.isEmpty()) {
                response.put("status", 404);
                response.put("message", "question not found!");
                return response;
            }
            Answer answer = result.get();
            answer.setEndorsed(true);
            answerRepository.save(answer);
            response.put("status", 200);
            return response;

        } catch (Exception error) {
            System.out.println(error.getMessage());
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }
    }

    public HashMap<String, Object> likeAnswer(String userEmail, String answerId) throws JSONException {
        HashMap<String, Object> response = new HashMap<>();


        try {
            FilterProperty filterProperty = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering = new ArrayList<>();
            userFiltering.add(filterProperty);
            Iterable<User> userFound = new ArrayList<User>();
            if (userLikesAnswerRepository.count() != 0) {
                userFound = edgeBaseRepository.findBy(User.class, answerId, "userLikesAnswer", EdgeDirection.INBOUND, new ArrayList<FilterProperty>(), userFiltering, ConditionSeparator.AND, ConditionSeparator.AND, "v",1,1);


                if (userFound.iterator().hasNext()) {
                    response.put("status", 422);
                    response.put("message", "user already liked this answer!");
                    return response;
                }
            }
            userFiltering.add(filterProperty);
            Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
            User user = users.iterator().next();
            Optional<Answer> answerFound = answerRepository.findById(answerId);
            if (!answerFound.isPresent()) {
                response.put("status", 404);
                response.put("message", "answer not found!");
                return response;
            }
            Answer answer = answerFound.get();
            userLikesAnswerRepository.save(new UserLikesAnswer(user, answer));
            response.put("status", 200);
            return response;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            response.put("status", 500);
            response.put("message", ex.getMessage());
            return response;

        }


    }

    public HashMap<String, Object> deleteAnswer(String answerId) throws IOException, JSONException {
        HashMap<String, Object> response = new HashMap<>();
        try {

            Optional<Answer> answerOptional = answerRepository.findById(answerId);
            if (answerOptional.isEmpty()) {
                response.put("status", 404);
                response.put("message", "Answer not found!");
                return response;
            }
            Answer answer = answerOptional.get();
            Iterable<UserLikesAnswer> userLikesAnswers = edgeBaseRepository.findAll1(UserLikesAnswer.class, answerId, "userLikesAnswer", EdgeDirection.INBOUND, "e",1,1);
            userLikesAnswerRepository.deleteAll(userLikesAnswers);
            Iterable<UserMakeAnswer> userMakeAnswer = edgeBaseRepository.findAll1(UserMakeAnswer.class, answerId, "userMakeAnswer", EdgeDirection.INBOUND, "e",1,1);
            userMakeAnswerRepository.deleteAll(userMakeAnswer);
            Iterable<QuestionHasAnswer> questionHasAnswer = edgeBaseRepository.findAll1(QuestionHasAnswer.class, answerId, "questionHasAnswer", EdgeDirection.INBOUND, "e",1,1);
            questionHasAnswerRepository.deleteAll(questionHasAnswer);

            answerRepository.delete(answer);
            response.put("status", 200);
            return response;
        } catch (Exception error) {
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }

    }

    public HashMap<String,Object> getPolls(String courseId) throws JSONException, IOException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            Optional<Course> course = courseRepository.findById(courseId);
            if (!course.isPresent()) {
                log.info("Course with id " + courseId + " not found");
                response.put("status", 404);
                response.put("message", "Answer not found!");
                return response;
            }
            Collection<Poll> coursePolls = course.get().getPolls();
            ArrayList<PollWrapper> pollWrappers = new ArrayList<>();
            for (Poll poll : coursePolls) {
                pollWrappers.add(new PollWrapper(poll));
            }
            response.put("status",200);
            response.put("data",pollWrappers);
            return response;

        }catch(Exception error){
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }

    }

    public HashMap<String, Object> getPoll(String pollId, String userEmail) throws JSONException, IOException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            Optional<Poll> poll = pollRepository.findById(pollId);


            if (!poll.isPresent()) {

                log.info("Poll with id " + pollId + " not found");
                response.put("status",404);
                response.put("message","Poll not found");
                return response;
            }
            Iterator<UserAnswerPoll> userAnswerPollIterator = edgeBaseRepository.findAll1(UserAnswerPoll.class, pollId, "userAnswerPoll", EdgeDirection.INBOUND, "e",1,1);
            PollWrapper pollWrapper = new PollWrapper(poll.get(), userAnswerPollIterator, userEmail);
            response.put("status",200);
            response.put("data",pollWrapper);
            return response;

        }catch(Exception error){
            response.put("status",500);
            response.put("message",error.getMessage());
            return response;


        }
    }


    public HashMap<String,Object> createPoll(String title,ArrayList<String> options,String date,String userEmail,String courseId) throws JSONException, ParseException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            Optional<Course> courseOptional=courseRepository.findById(courseId);
            if(!courseOptional.isPresent()){
                response.put("status",404);
                response.put("message","Course not found");
                return response;
            }
            Course course=courseOptional.get();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            Poll newPoll = new Poll(title, options, ZonedDateTime.now(), simpleDateFormat.parse(date));

            pollRepository.save(newPoll);
            courseHasPollRepository.save(new CourseHasPoll(course, newPoll));
            FilterProperty userFilter = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering = new ArrayList<>();
            userFiltering.add(userFilter);
            Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
            User user = users.iterator().next();
            userCreatePollRepository.save(new UserCreatePoll(user, newPoll));
            response.put("status",200);
            response.put("data",new PollWrapper(newPoll));
            return response;
        }catch(Exception error){
            response.put("status",500);
            response.put("message",error.getMessage());
            return response;
        }
    }

    public HashMap<String,Object> votePoll(String pollId,String option,String courseId,String userEmail) throws IOException, JSONException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            Optional<Poll> pollOptional = pollRepository.findById(pollId);
            if (!pollOptional.isPresent()) {
                response.put("status",404);
                response.put("message","Poll not found");
                return response;
            }
            Poll poll = pollOptional.get();
            Collection<String> avaliableOptions = poll.getOptions();
            if (!avaliableOptions.contains(option)) {
               response.put("status",422);
               response.put("message","Option not valid");
               return response;
            }
            Date expiryDate = poll.getExpiryDate();
            Date todayDate = new Date();
            if (todayDate.after(expiryDate)) {
                response.put("status",422);
                response.put("message","The poll has expired!");
                return response;
            }
            FilterProperty userFilter = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering = new ArrayList<>();
            userFiltering.add(userFilter);
            Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
            User user = users.iterator().next();
            UserAnswerPoll userAnswerPoll = new UserAnswerPoll(option, poll, user);
            userAnswerPollRepository.save(userAnswerPoll);
            response.put("status",200);
            return response;
        }catch (Exception error){
            response.put("status",500);
            response.put("message",error.getMessage());
            return response;
        }
    }


    public HashMap<String,Object> addQuestion(String title,String mediaLink,String description,boolean anonymous,String[] taggedUsers,String courseId,boolean isPublic,String userEmail) throws IOException {
        HashMap<String,Object> response=new HashMap<>();

        try {

            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (!courseOptional.isPresent()) {
              response.put("status",404);
              response.put("message","course not found!");
              return response;
            }
            if(anonymous&&!isPublic){
                response.put("status",422);
                response.put("message","question cannot be anonymous and private");
                return response;
            }
            Course course = courseOptional.get();
            Question newQuestion = new Question(title, description, mediaLink, ZonedDateTime.now(), isPublic, anonymous);
            questionRepository.save(newQuestion);

            courseQuestionEdgeRepository.save(new CourseQuestionEdge(course, newQuestion));
            FilterProperty userFilter = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering = new ArrayList<>();
            userFiltering.add(userFilter);
            Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
            User user = users.iterator().next();
            userMakeQuestionRepository.save(new UserMakeQuestion(user, newQuestion));
            if (taggedUsers != null) {
                FilterProperty taggedFilterProperty = new FilterProperty("email", taggedUsers, FilterCondition.IN);
                Collection<FilterProperty> taggedFilterProperties = new ArrayList<>();
                taggedFilterProperties.add(taggedFilterProperty);
                System.out.println(taggedUsers[0]);
                Iterable<User> validUserToBeTagged = edgeBaseRepository.findBy(User.class, courseId, "userInCourse", EdgeDirection.INBOUND, new ArrayList<>(), taggedFilterProperties, ConditionSeparator.AND, ConditionSeparator.AND, "v",1,1); // filter the users that were sent that only are enrolled in this course
                System.out.println(validUserToBeTagged.iterator().hasNext());
                ArrayList<QuestionMentionedUser> mentionedUsersEdges = new ArrayList<>();
                for (User curUser : validUserToBeTagged) {
                    mentionedUsersEdges.add(new QuestionMentionedUser(newQuestion, curUser));
                }
                questionMentionedUserRepository.saveAll(mentionedUsersEdges);
            }
            response.put("status",200);
            response.put("data",new QuestionWrapper(newQuestion,true,userEmail));
            return response;
        } catch (Exception error) {
           response.put("status",500);
           response.put("message",error.getMessage());
           System.out.println(error.getMessage());
           return response;
        }
    }

    public HashMap<String,Object> endorseQuestion(String questionId) throws IOException, JSONException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            Optional<Question> result = questionRepository.findById(questionId);
            if (result.isEmpty()) {
               response.put("status",404);
               response.put("message","Question not found");
               return response;
            }
            Question question = result.get();
            if (question.isEndorsed()) {
                response.put("status",422);
                response.put("message","Question already endorsed");
                return response;
            }
            question.setEndorsed(true);
            questionRepository.save(question);
            response.put("status",200);
            return response;
        }catch(Exception error){

            response.put("status",500);
            response.put("message",error.getMessage());
            return response;
        }
        }


    public boolean checkUserInCourse(HashMap<String, Object> request) throws JSONException {
        String courseId = (String) request.get("courseId");
        String userEmail = (String) request.get("userEmail");
        FilterProperty userFilterProperty = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
        Collection<FilterProperty> userFilterProperties = new ArrayList<FilterProperty>();
        userFilterProperties.add(userFilterProperty);
        Iterable<UserInCourse> userInCourseEdge = edgeBaseRepository.findBy(UserInCourse.class, courseId, "userInCourse", EdgeDirection.INBOUND, new ArrayList<FilterProperty>(), userFilterProperties, ConditionSeparator.AND, ConditionSeparator.AND, "e",1,1);
        Iterator<UserInCourse> userInCourseEdges = userInCourseEdge.iterator();
        if (userInCourseEdges.hasNext()) {
            UserInCourse userInCourse = userInCourseEdges.next();
            request.put("userRole", userInCourse.getRole());
            return true;
        } else {
            return false;
        }

    }

    public boolean isUserBannedFromCourse(HashMap<String, Object> request) throws JSONException {
        String courseId = (String) request.get("courseId");
        String userEmail = (String) request.get("userEmail");
        FilterProperty userFilterProperty = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
        Collection<FilterProperty> filterProperties = new ArrayList<FilterProperty>();
        filterProperties.add(userFilterProperty);
        Iterable<UserBannedFromCourse> userBannedFromCourse = edgeBaseRepository.findBy(UserBannedFromCourse.class, courseId, "userBannedFromCourseEdge", EdgeDirection.INBOUND, new ArrayList<FilterProperty>(), filterProperties, ConditionSeparator.AND, ConditionSeparator.AND, "e",1,1);

        if (userBannedFromCourse.iterator().hasNext()) {
            UserBannedFromCourse userBan = userBannedFromCourse.iterator().next();
            if (userBan.isPermenant()) {
                return true;
            }
            Date currentDate = new Date();
            if (userBan.getUnbanDate().before(currentDate)) {
                userBannedFromCourseRepository.delete(userBan);
                return false;
            }
        }

        return false;


    }

    public HashMap<String,Object> likeQuestion(String userEmail,String questionId) throws JSONException {
        HashMap<String,Object> response=new HashMap<>();
        try{
        FilterProperty filterProperty = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
        Collection<FilterProperty> userFiltering = new ArrayList<>();
        userFiltering.add(filterProperty);
        Iterable<User> userFound = new ArrayList<User>();
        if(userLikesQuestionRepository.count()>0) {
            userFound = edgeBaseRepository.findBy(User.class, questionId, "userLikesQuestion", EdgeDirection.INBOUND, new ArrayList<FilterProperty>(), userFiltering, ConditionSeparator.AND, ConditionSeparator.AND, "v",1,1);
            System.out.println(userFound);


            if (userFound.iterator().hasNext()) {
                response.put("status", 422);
                response.put("message", "User already liked question");
                return response;
            }
        }
            userFiltering.add(filterProperty);
            Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
            User user = users.iterator().next();
            Optional<Question> questionFound = questionRepository.findById(questionId);
            if (!questionFound.isPresent()) {
                response.put("status",404);
                response.put("message","Question not found");
                return response;
            }
            Question question = questionFound.get();
            userLikesQuestionRepository.save(new UserLikesQuestion(user, question));
            response.put("status",200);
            return response;

        } catch (Exception ex) {

            response.put("status",500);
            response.put("message",ex.getMessage());
            System.out.println(ex.getMessage());
            return response;
        }

    }

    public HashMap<String,Object> deleteQuestion(String questionId) throws IOException, JSONException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            Optional<Question> result = questionRepository.findById(questionId);
            if (result.isEmpty()) {
               response.put("status",404);
               response.put("message","Question not found");
               return response;
            }
            Question question = result.get();
            Collection<Answer> answers = question.getAnswers();
            for (Answer answer : answers) {
                String answerId = answer.getArangoId();
                Iterable<UserLikesAnswer> userLikesAnswers = edgeBaseRepository.findAll1(UserLikesAnswer.class, answerId, "userLikesAnswer", EdgeDirection.INBOUND, "e",1,1);
                userLikesAnswerRepository.deleteAll(userLikesAnswers);
                Iterable<UserMakeAnswer> userMakeAnswer = edgeBaseRepository.findAll1(UserMakeAnswer.class, answerId, "userMakeAnswer", EdgeDirection.INBOUND, "e",1,1);
                userMakeAnswerRepository.deleteAll(userMakeAnswer);
                Iterable<QuestionHasAnswer> questionHasAnswer = edgeBaseRepository.findAll1(QuestionHasAnswer.class, answerId, "questionHasAnswer", EdgeDirection.INBOUND, "e",1,1);
                questionHasAnswerRepository.deleteAll(questionHasAnswer);
                answerRepository.delete(answer);
            }
            Iterable<UserLikesQuestion> userLikeQuestion = edgeBaseRepository.findAll1(UserLikesQuestion.class, question.getArangoId(), "userLikesQuestion", EdgeDirection.INBOUND, "e",1,1);
            userLikesQuestionRepository.deleteAll(userLikeQuestion);
            Iterable<QuestionMentionedUser> questionMentionedUsers = edgeBaseRepository.findAll1(QuestionMentionedUser.class, question.getArangoId(), "questionMentionedUser", EdgeDirection.OUTBOUND, "e",1,1);
            questionMentionedUserRepository.deleteAll(questionMentionedUsers);
            questionRepository.delete(question);
            Iterable<UserMakeQuestion> userMakeQuestions = edgeBaseRepository.findAll1(UserMakeQuestion.class, question.getArangoId(), "questionMentionedUser", EdgeDirection.OUTBOUND, "e",1,1);
            userMakeQuestionRepository.deleteAll(userMakeQuestions);

            response.put("status",200);
            return response;
        }catch(Exception error){
            response.put("status",500);
            response.put("message",error.getMessage());
            return response;
        }
    }

    public HashMap<String,Object> getCourseQuestions(String courseId,String userEmail,String userRole) throws JSONException, IOException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            Optional<Course> course = courseRepository.findById(courseId);
            if (!course.isPresent()) {
                response.put("status",404);
                response.put("message","Course not found");
                log.info("Course with id " + courseId + " not found");
                return response;
            }
            Collection<Question> courseQuestions = course.get().getQuestions();
            ArrayList<QuestionWrapper> questions = new ArrayList<>();
            for (Question question : courseQuestions) {
                if (question != null) {

                    if (!question.get_public()) {
                        if (userRole.equals("instructor") || (question.getUser() != null && question.getUser().getEmail().equals(userEmail))) {
                            questions.add(new QuestionWrapper(question, false, userEmail));
                        }
                    } else {
                        questions.add(new QuestionWrapper(question, false, userEmail));

                    }
                }
            }
            response.put("status",200);
            response.put("data",questions);
            return response;
        }catch(Exception error){

            response.put("status",500);
            response.put("message",error.getMessage());
            return response;
        }
    }

    public HashMap<String,Object> searchForQuestion(String courseId,String searchQuery,String userRole,String userEmail) throws JSONException, IOException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            FilterProperty questionTitleFilterProperty = new FilterProperty("title", "%" + searchQuery + "%", FilterCondition.LIKE);
            FilterProperty questionDescriptionFilterProperty = new FilterProperty("description", "%" + searchQuery + "%", FilterCondition.LIKE);
            Collection<FilterProperty> questionFilterProperties = new ArrayList<>();
            questionFilterProperties.add(questionTitleFilterProperty);
            questionFilterProperties.add(questionDescriptionFilterProperty);
            Iterator<Question> questionIterator = edgeBaseRepository.findBySort(Question.class, courseId, "courseQuestion", EdgeDirection.OUTBOUND, new ArrayList<>(), questionFilterProperties, null, ConditionSeparator.OR, "date", SortDirection.DESC, "v", "v",1,1);
            ArrayList<QuestionWrapper> questions = new ArrayList<>();
            for (Iterator<Question> it = questionIterator; it.hasNext(); ) {
                Question question = it.next();
                if (question != null) {

                    if (!question.get_public()) {
                        if (!userRole.equals("instructor") || (question.getUser() != null && question.getUser().getEmail().equals(userEmail))) {
                            questions.add(new QuestionWrapper(question, false, userEmail));
                        }
                    } else {
                        questions.add(new QuestionWrapper(question, false, userEmail));

                    }
                }
            }
            System.out.println(questions);
            response.put("status",200);
            response.put("data",questions);
            return response;
        }catch(Exception error){

            response.put("status",500);
            response.put("message",error.getMessage());
            return response;
        }

    }

    public HashMap<String, Object> suggestQuestion(String title, String description, String courseId, String userEmail, String userRole) throws JSONException, IOException {
        HashMap<String,Object> response=new HashMap<>();

        try {
            FilterProperty questionTitleFilterProperty = new FilterProperty("title", "%" + title + "%", FilterCondition.LIKE);
            FilterProperty questionDescriptionFilterProperty = new FilterProperty("description", "%" + title + "%", FilterCondition.LIKE);
            FilterProperty questionDescriptionToTitleFilterProperty = new FilterProperty("title", "%" + description + "%", FilterCondition.LIKE);
            FilterProperty questionDescriptionToDescriptionFilterProperty = new FilterProperty("description", "%" + description + "%", FilterCondition.LIKE);
            Collection<FilterProperty> questionFilterProperties = new ArrayList<>();
            questionFilterProperties.add(questionTitleFilterProperty);
            questionFilterProperties.add(questionDescriptionFilterProperty);
            questionFilterProperties.add(questionDescriptionToTitleFilterProperty);
            questionFilterProperties.add(questionDescriptionToDescriptionFilterProperty);
            Iterator<Question> questionIterator = edgeBaseRepository.findBySort(Question.class, courseId, "courseQuestion", EdgeDirection.OUTBOUND, new ArrayList<>(), questionFilterProperties, null, ConditionSeparator.OR, "date", SortDirection.DESC, "v", "v",1,1);
            ArrayList<QuestionWrapper> questions = new ArrayList<>();
            for (Iterator<Question> it = questionIterator; it.hasNext(); ) {
                Question question = it.next();
                if (question != null) {

                    if (!question.get_public()) {
                        if (!userRole.equals("instructor") || (question.getUser() != null && question.getUser().getEmail().equals(userEmail))) {
                            questions.add(new QuestionWrapper(question, false, userEmail));
                        }
                    } else {
                        questions.add(new QuestionWrapper(question, false, userEmail));

                    }
                }
            }


            response.put("status",200);
            response.put("data",questions);
            return response;
        }catch(Exception error){
            response.put("status",500);
            response.put("message",error.getMessage());
            return response;

        }

    }

    public HashMap<String,Object> getQuestion(String questionId,String userEmail,String userRole) throws JSONException, IOException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            Optional<Question> questionOptional = questionRepository.findById(questionId);
            if (!questionOptional.isPresent()) {
                response.put("status",404);
                response.put("message","Question not found");
                return response;
            }
            Question question = questionOptional.get();
            if (!question.get_public()) {
                if (userRole.equals("instructor") || (question.getUser() != null && question.getUser().getEmail().equals(userEmail))) {
                    QuestionWrapper questionWrapper = new QuestionWrapper(question, true, userEmail);
                    response.put("status",200);
                    response.put("data",questionWrapper);
                    return response;

                } else {
                   response.put("status",401);
                   response.put("message","Not Authorized");
                   return response;
                }
            } else {
                QuestionWrapper questionWrapper = new QuestionWrapper(question, true, userEmail);
                response.put("status",200);
                response.put("data",questionWrapper);
                return response;


            }
        }catch(Exception error){
                response.put("status",500);
                response.put("message",error.getMessage());
                return response;
        }


    }

    public HashMap<String, Object> generateCourseLink(String courseId) throws JSONException, IOException {

        HashMap<String, Object> response = new HashMap<>();
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            String courseInviteLink = JWT.create()
                    .withSubject(courseId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // expiry date one day
                    .sign(algorithm);

            response.put("data", courseInviteLink);
            response.put("status", 200);
            return response;

        } catch (Exception error) {
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }
    }

    public HashMap<String, Object> enrollUsingCourseLink(String userEmail, String courseInviteLink) throws JSONException, IOException {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(courseInviteLink);
            String courseId = decodedJWT.getSubject();
            FilterProperty userFilter = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering = new ArrayList<>();
            userFiltering.add(userFilter);
            Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
            if (!users.iterator().hasNext()) {
                log.info("User does not exist");
                response.put("status", 404);
                response.put("message", "User does not exist");
                return response;

            }
            User user = users.iterator().next();
            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (!courseOptional.isPresent()) {
                log.info("Course does not exist");
                response.put("status", 404);
                response.put("message", "Course does not exist");
                return response;

            }
            Course course = courseOptional.get();
            FilterProperty courseFilter = new FilterProperty("_id", courseId, FilterCondition.EQUAL);
            ArrayList<FilterProperty> filterProperties = new ArrayList<>();
            filterProperties.add(courseFilter);

            Iterator<Course> courseIterator = edgeBaseRepository.findBy(Course.class, user.getArangoId(), "userInCourse", EdgeDirection.OUTBOUND, new ArrayList<FilterProperty>(), filterProperties, ConditionSeparator.AND, ConditionSeparator.AND, "v",1,1);
            if (courseIterator.hasNext()) {
                response.put("status", 400);
                response.put("message", "User already enrolled in course");
                return response;
            }
            UserInCourse userInCourse = new UserInCourse(user.getRole(), course, user);
            userInCourseRepository.save(userInCourse);
            log.info("User " + user.getEmail() + " enrolled to course " + course.getArangoId() + " Using an invite link");
            response.put("status", 200);
            return response;
        } catch (TokenExpiredException error) {
            response.put("status", 422);
            response.put("message", "Invite link has expired");
            return response;

        } catch (Exception error) {
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }
    }


    public HashMap<String, Object> getReports(String courseId) throws JSONException, IOException {
        HashMap<String, Object> response = new HashMap<>();
        try {
            FilterProperty courseFilterProperty = new FilterProperty("courseId", courseId, FilterCondition.EQUAL);
            Collection<FilterProperty> courseFilterProperties = new ArrayList<FilterProperty>();
            courseFilterProperties.add(courseFilterProperty);
            Iterator<Report> reportsIterator = baseRepository.findBy(Report.class, "reports", courseFilterProperties, ConditionSeparator.AND);
            ArrayList<ReportWrapper> reportWrappers = new ArrayList<>();
            while (reportsIterator.hasNext()) {
                reportWrappers.add(new ReportWrapper(reportsIterator.next()));
            }
            response.put("status", 200);
            response.put("data", reportWrappers);
            return response;

        } catch (Exception error) {

            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }


    }

    public HashMap<String, Object> courseRecommendation(String email, String role) throws IOException {
        HashMap<String, Object> request = new HashMap<>();
        try {
            FilterProperty userFilter = new FilterProperty("email", email, FilterCondition.EQUAL);
            FilterProperty userFilter2 = new FilterProperty("role", role, FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering = new ArrayList<>();
            userFiltering.add(userFilter);
            userFiltering.add(userFilter2);
            Iterable<org.piazza.model.document.User> users = baseRepository.findBy(org.piazza.model.document.User.class, "users", userFiltering, ConditionSeparator.AND);

            String id = Iterables.firstOf(users).getArangoId();
            Iterator<Course> courses= edgeBaseRepository.findAll1(Course.class,id,"userInCourse",EdgeDirection.OUTBOUND,"v",1,2);
            ArrayList<String> coursesIds=new ArrayList<>();
            for (Iterator<Course> it = courses; it.hasNext(); ) {
                Course course = it.next();
                coursesIds.add(course.getArangoId());


            }
            FilterProperty courseFilter=new FilterProperty("_class",Course.class,FilterCondition.EQUAL);
            FilterProperty courseFilter2=new FilterProperty("_id",coursesIds,FilterCondition.NOT_IN);
            FilterProperty edgeFilter=new FilterProperty("_from",id,FilterCondition.NOT_EQUAL);
            ArrayList<FilterProperty> filterProperties=new ArrayList<>();
            ArrayList<FilterProperty> edgeProperty=new ArrayList<>();
            filterProperties.add(courseFilter);
            filterProperties.add(courseFilter2);
            edgeProperty.add(edgeFilter);
            Iterator<Course> result=edgeBaseRepository.findBy(Course.class,id,"userInCourse",EdgeDirection.ANY,edgeProperty,filterProperties,null,ConditionSeparator.AND,"v",2,5);

            ArrayList<String> x=new ArrayList<>();
            for (Iterator<Course> it = result; it.hasNext(); ) {
                Course course = it.next();
                x.add(course.getName());

            }
            TreeMap<String, Integer> tmap = new TreeMap<String, Integer>();
            for (String t : x) {
                Integer c = tmap.get(t);
                tmap.put(t, (c == null) ? 1 : c + 1);
            }
            NavigableSet<String> keys = tmap.descendingKeySet();
            request.put("data", keys.toString());
            request.put("status", 200);
            return request;

        } catch (Exception error) {
            request.put("status", 500);
            request.put("message", error.getMessage());
            return request;
        }
    }

    public HashMap<String, Object> registerUser(String courseId, String email) throws IOException {
        HashMap<String, Object> response = new HashMap<>();
        try {
            FilterProperty courseFilter = new FilterProperty("_id", courseId, FilterCondition.EQUAL);
            Collection<FilterProperty> courseFiltering = new ArrayList<>();
            courseFiltering.add(courseFilter);
            Iterable<Course> courses = baseRepository.findBy(Course.class, "courses", courseFiltering, ConditionSeparator.AND);
            if (courses.iterator().hasNext()) {
                Course course = courses.iterator().next();
                FilterProperty userFilter = new FilterProperty("email", email, FilterCondition.EQUAL);
                Collection<FilterProperty> userFiltering = new ArrayList<>();
                userFiltering.add(userFilter);
                Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
                if (users.iterator().hasNext()) {
                    if (userIsInCourseId(email, courseId)) {
                        log.info("Student already in course");
                        response.put("status", 422);
                        response.put("message","Student already in course");
                        return response;
                    } else {
                        User user = users.iterator().next();
                        if (user.getRole().equals("student")) {
                            userInCourseRepository.save(new UserInCourse("student", course, user));
                            log.info("Student Registered");
                            response.put("status", 200);
                            return response;
                        } else {
                            log.info("Instructor must be added by course instructor(s)");
                            response.put("status", 401);
                            response.put("message", "Instructor must be added by course instructor(s)");
                            return response;

                        }
                    }
                } else {
                    log.info("Student does not exist");
                    response.put("status", 404);
                    response.put("message", "Student does not exist");
                    return response;
                }
            } else {
                log.info("Course does not exist");
                response.put("status", 404);
                response.put("message", "Course does not exist");
                return response;
            }
        } catch (Exception error) {
            response.put("status", 500);
            response.put("message", error.getMessage());
            return response;
        }
    }

    public HashMap<String, Object> banUser(String instructorEmail, String courseId, int duration, boolean isPermenant, String userEmail) throws IllegalAccessException, IOException {
        HashMap<String, Object> response = new HashMap<>();
        try {
            FilterProperty userFilterI = new FilterProperty("email", instructorEmail, FilterCondition.EQUAL);
            Collection<FilterProperty> userFilteringI = new ArrayList<>();
            userFilteringI.add(userFilterI);
            Iterable<User> usersI = baseRepository.findBy(org.piazza.model.document.User.class, "users", userFilteringI, ConditionSeparator.AND);
            if (usersI.iterator().hasNext()) {
                User userI = usersI.iterator().next();
                if (userI.getRole().equals("instructor")) {
//            String instId = getUserId(instructorEmail);
                    if (courseExistsId(courseId)) {
                        if (userIsInCourseId(instructorEmail, courseId)) {
                            FilterProperty courseFilter = new FilterProperty("_id", courseId, FilterCondition.EQUAL);
                            Collection<FilterProperty> courseFiltering = new ArrayList<>();
                            courseFiltering.add(courseFilter);
                            Iterable<Course> courses = baseRepository.findBy(org.piazza.model.document.Course.class, "courses", courseFiltering, ConditionSeparator.AND);
                            Course course = courses.iterator().next();
                            FilterProperty userFilter = new FilterProperty("email", userEmail, FilterCondition.EQUAL);
                            Collection<FilterProperty> userFiltering = new ArrayList<>();
                            userFiltering.add(userFilter);
                            Iterable<User> users = baseRepository.findBy(org.piazza.model.document.User.class, "users", userFiltering, ConditionSeparator.AND);
                            if (users.iterator().hasNext()) {
                                if (userIsInCourseId(userEmail, courseId)) {
                                    User user = users.iterator().next();
                                    Date date = null;
                                    if (!isPermenant) {
                                        ZoneId defaultZoneId = ZoneId.systemDefault();
                                        LocalDate newDate = LocalDate.now().plusDays(duration);
                                        date = Date.from(newDate.atStartOfDay(defaultZoneId).toInstant());
                                        UserBannedFromCourse userBannedFromCourse = new UserBannedFromCourse(user, course, date, false);
                                        System.out.println(userBannedFromCourse.getUser().toString());
                                        System.out.println(userBannedFromCourse.getCourse().toString());
                                        System.out.println(userBannedFromCourse.isPermenant());
                                        System.out.println(userBannedFromCourse.getUnbanDate());
                                        userBannedFromCourseRepository.save(userBannedFromCourse);
                                    } else {
                                        userBannedFromCourseRepository.save(new UserBannedFromCourse(user, course, null, true));
                                    }
                                    log.info("User Banned");
                                    response.put("status", 200);
                                    return response;
                                } else {
                                    log.info("User not in Course");
                                    response.put("status" ,422);
                                    response.put("message", "User not in Course");
                                    return response;
                                }
                            } else {
                                log.info("User does not exist");
                                response.put("status" ,404);
                                response.put("message", "User does not exist");
                                return response;
                            }
                        } else {
                            log.info("Instructor not in Course");
                            response.put("status" ,401);
                            response.put("message", "Instructor not in Course");
                            return response;
                        }
                    } else {
                        log.info("Course Does Not Exist");
                        response.put("status" ,404);
                        response.put("message", "Course Does Not Exists");
                        return response;
                    }
                } else {
                    log.info("Not authorised");
                    response.put("status" ,401);
                    response.put("message", "Not Authorised");
                    return response;
                }
            } else {
                log.info("Instructor does not exist");
                response.put("status" ,404);
                response.put("message", "Not Instructor does not exist");
                return response;
            }
        } catch (Exception error) {
            response.put("status" ,500);
            response.put("message", error.getMessage());
            return response;

        }
    }

    public HashMap<String, Object> addCourse(String email, String name) throws IOException {
//        String courseId = body.getString("CourseId");
//        String userEmail = body.getString("UserEmail");
//        String userId = authenticationParameters.getString("UserEmail");
        HashMap<String, Object> response = new HashMap<>();

        try {

            FilterProperty courseFilter = new FilterProperty("name", name, FilterCondition.EQUAL);
            Collection<FilterProperty> courseFilterList = new ArrayList<>();
            courseFilterList.add(courseFilter);
            FilterProperty userFilter = new FilterProperty("email", email, FilterCondition.EQUAL);
            FilterProperty userFilter2 = new FilterProperty("role", "instructor", FilterCondition.EQUAL);
            Collection<FilterProperty> userFilterList = new ArrayList<FilterProperty>();
            userFilterList.add(userFilter);
            userFilterList.add(userFilter2);
            Iterable<Course> courses = baseRepository.findBy(org.piazza.model.document.Course.class, "courses", courseFilterList, ConditionSeparator.AND);
            if (!courses.iterator().hasNext()) {
                Iterable<User> users = baseRepository.findBy(org.piazza.model.document.User.class, "users", userFilterList, ConditionSeparator.AND);
                if (users.iterator().hasNext()) {
                    User user = users.iterator().next();
                    Course newCourse = new Course(name);
                    courseRepository.save(newCourse);
                    userInCourseRepository.save(new UserInCourse(user.getRole(), newCourse, user));
                    response.put("status", 200);
                    return response;
                } else {

                    log.info("Unauthorized Access");
                    response.put("status", 401);
                    response.put("message", "Unauthorized Access");
                    return response;
                }
            } else {
                log.info("Course Exists");
                response.put("status", 422);
                response.put("message", "Course Exists");
                return response;
            }
        } catch (Exception e) {
            log.info("error");
            response.put("status", 500);
            response.put("message", e.getMessage());
            return response;
        }

    }

    public HashMap<String, Object> reportStudent(String email, String userToBeReported, String reason, String courseId) throws IOException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            FilterProperty userFiltered = new FilterProperty("email", userToBeReported, FilterCondition.EQUAL);
            FilterProperty stfilter = new FilterProperty("role", "student", FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering = new ArrayList<>();
            userFiltering.add(userFiltered);
            userFiltering.add(stfilter);
            Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);

            FilterProperty userFiltered2 = new FilterProperty("email", email, FilterCondition.EQUAL);
            Collection<FilterProperty> userFiltering2 = new ArrayList<>();
            userFiltering2.add(userFiltered2);
            Iterable<User> users2 = baseRepository.findBy(User.class, "users", userFiltering2, ConditionSeparator.AND);


            if (!users.iterator().hasNext() || !users2.iterator().hasNext()) {
                log.info("Student or user Does Not Exist");
                response.put("status",404);
                response.put("message","Student or user Does Not Exist");
                return response;
            } else {
                if (userIsInCourseId(email, courseId) && userIsInCourseId(userToBeReported, courseId)) {
                    Report report = new Report(reason, courseId);
                    reportRepository.save(report);
                    UserCreateReport userCreateReport = new UserCreateReport(users2.iterator().next(), report);
                    UserReported userReported = new UserReported(report, users.iterator().next());
                    userCreateReportRepository.save(userCreateReport);
                    userReportedRepository.save(userReported);
                    response.put("status",200);
                    return response;
                } else {
                    log.info("Student to be reported and user must be in same course");
                    response.put("status",401);
                    response.put("message","Student to be reported and user must be in same course");
                    return response;
                }
            }
        } catch (Exception e) {
            response.put("status",500);
            response.put("message",e.getMessage());
            return response;
        }
    }



    public HashMap<String,Object> assignInstructorToCourse(String email, String instructorEmail, String courseId) throws IOException {
        HashMap<String,Object> response=new HashMap<>();
        try {
            if (isInstructor(email) && userIsInCourseId(email, courseId)) {
                FilterProperty userFiltered = new FilterProperty("email", instructorEmail, FilterCondition.EQUAL);
                FilterProperty stfilter = new FilterProperty("role", "instructor", FilterCondition.EQUAL);
                FilterProperty courseFiltered = new FilterProperty("_id", courseId, FilterCondition.EQUAL);
                Collection<FilterProperty> usersfilters = new ArrayList<>();
                Collection<FilterProperty> coursefilters = new ArrayList<>();
                usersfilters.add(userFiltered);
                usersfilters.add(stfilter);
                coursefilters.add(courseFiltered);

                    Iterable<User> users = baseRepository.findBy(User.class, "users", usersfilters, ConditionSeparator.AND);
                    if (!users.iterator().hasNext()) {
                        log.info("Instructor Does Not Exist");
                        response.put("status",404);
                        response.put("message","Instructor Does Not Exist");
                        return response;
                    } else {
                        User user = users.iterator().next();
                        Course course = baseRepository.findBy(Course.class, "courses", coursefilters, ConditionSeparator.AND).iterator().next();
                        userInCourseRepository.save(new UserInCourse(user.getRole(), course, user));
                        response.put("status",200);
                        return response;
                    }

            } else {
                log.info("Instructor must add instructor and be in course");
                response.put("status",401);
                response.put("message","Instructor must add instructor and be in course");
                return response;                  }
        }catch(Exception error){
            response.put("status",500);
            response.put("message",error.getMessage());
            return response;
        }
    }

    public HashMap<String,Object> addStudentsManually(String instructorEmail, String studentEmail, String courseId) throws IOException {
//        String studentMail = body.getString("email");
//        String courseId = body.getString("courseId");
//        String userId = authenticationParameters.getString("userEmail");
        HashMap<String,Object> response=new HashMap<>();
        try {
            if (isInstructor(instructorEmail) && userIsInCourseId(instructorEmail, courseId)) {
                FilterProperty userFilter = new FilterProperty("email", studentEmail, FilterCondition.EQUAL);
                FilterProperty stfilter = new FilterProperty("role", "student", FilterCondition.EQUAL);
                Collection<FilterProperty> userFiltering = new ArrayList<>();
                userFiltering.add(userFilter);
                userFiltering.add(stfilter);

                Iterable<User> users = baseRepository.findBy(User.class, "users", userFiltering, ConditionSeparator.AND);
                if (users.iterator().hasNext()) {
                    User user = users.iterator().next();
                    FilterProperty courseFilter = new FilterProperty("_id", courseId, FilterCondition.EQUAL);
                    Collection<FilterProperty> courseFiltering = new ArrayList<>();
                    courseFiltering.add(courseFilter);
                    Course course = baseRepository.findBy(Course.class, "courses", courseFiltering, ConditionSeparator.AND).next();
                    if (course.equals(null)) {
                        log.info("No such course exists");
                        response.put("status", 404);
                        response.put("message", "No such course exists");
                        return response;
                    } else {
                        if (userIsInCourseId(studentEmail, courseId)) {
                            log.info("Student already in course");
                            response.put("status", 422);
                            response.put("message", "Student already in course");
                            return response;
                        } else {
                            userInCourseRepository.save(new UserInCourse(user.getRole(), course, user));
                            response.put("status", 200);
                            return response;
                        }
                    }
                } else {
                    log.info("Student Does Not Exist");
                    response.put("status", 404);
                    response.put("message", "Student Does Not Exist");
                    return response;
                }
            } else {
                log.info("Instructor must add student and be in course");
                response.put("status", 401);
                response.put("message", "Instructor must add student and be in course");
                return response;            }
        }catch (Exception e) {
            response.put("status", 500);
            response.put("message", e.getMessage());
            return response;
        }
    }

    public boolean isInstructor(String email) throws IOException {
//        String id = getUserId(email);
        FilterProperty userFilter = new FilterProperty("email", email, FilterCondition.EQUAL);
        FilterProperty userFilter2 = new FilterProperty("role", "instructor", FilterCondition.EQUAL);
        Collection<FilterProperty> userFilterList = new ArrayList<>();
        userFilterList.add(userFilter);
        userFilterList.add(userFilter2);

        try {
            Iterable<User> users = baseRepository.findBy(org.piazza.model.document.User.class, "users", userFilterList, ConditionSeparator.AND);
            if(!users.iterator().hasNext()){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            log.info("error");
            throw new IOException("Error: "+e.getMessage());
        }
    }

    public boolean courseExists(String name) throws IOException {//for testing
        FilterProperty courseFilter = new FilterProperty("name", name, FilterCondition.EQUAL);
        Collection<FilterProperty> courseFilterList = new ArrayList<>();
        courseFilterList.add(courseFilter);
        try {
            Iterable<Course> courses = baseRepository.findBy(org.piazza.model.document.Course.class, "courses", courseFilterList, ConditionSeparator.AND);
            if(!courses.iterator().hasNext()){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            log.info("error");
            throw new IOException("Error: "+e.getMessage());
        }
    }

    public boolean courseExistsId(String courseId) throws IOException {//for testing
        FilterProperty courseFilter = new FilterProperty("_id", courseId, FilterCondition.EQUAL);
        Collection<FilterProperty> courseFilterList = new ArrayList<>();
        courseFilterList.add(courseFilter);
        try {
            Iterable<Course> courses = baseRepository.findBy(org.piazza.model.document.Course.class, "courses", courseFilterList, ConditionSeparator.AND);
            if(!courses.iterator().hasNext()){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            log.info("error");
            throw new IOException("Error: "+e.getMessage());
        }
    }

    public String getCourseId(String name) throws IOException {//for testing
        FilterProperty courseFilter = new FilterProperty("name", name, FilterCondition.EQUAL);
        Collection<FilterProperty> courseFilterList = new ArrayList<>();
        courseFilterList.add(courseFilter);
        try {
            Iterable<Course> courses = baseRepository.findBy(org.piazza.model.document.Course.class, "courses", courseFilterList, ConditionSeparator.AND);
            if(!courses.iterator().hasNext()){
                log.info("error");
                throw new IOException("Error");
            }else{
                return courses.iterator().next().getArangoId();
            }
        } catch (Exception e) {
            log.info("error");
            throw new IOException("Error: "+e.getMessage());
        }
    }

    public String getUserId(String email) throws IOException {
        FilterProperty userFilter = new FilterProperty("email", email, FilterCondition.EQUAL);
        Collection<FilterProperty> userFilterList = new ArrayList<>();
        userFilterList.add(userFilter);
        try {
            Iterable<User> users = baseRepository.findBy(org.piazza.model.document.User.class, "users", userFilterList, ConditionSeparator.AND);
            if(!users.iterator().hasNext()){
                log.info("error");
                throw new IOException("Error");
            }else{
                return users.iterator().next().getArangoId();
            }
        } catch (Exception e) {
            log.info("error");
            throw new IOException("Error: "+e.getMessage());
        }
    }
// for testing
    public Question getQuestionFromCourse(String courseId){
       Course course= courseRepository.findById(courseId).get();
       Iterable<Question> questions= course.getQuestions();
       return questions.iterator().next();
    }
    // for testing
    public Question getQuestionWithId(String questionId){
        return questionRepository.findById(questionId).get();
    }

    // for testing

    public User getUserWithEmail(String Email){
        FilterProperty userFilter=new FilterProperty("email",Email,FilterCondition.EQUAL);
        ArrayList<FilterProperty> filterProperties=new ArrayList<>();
        filterProperties.add(userFilter);
        Iterator<User> users=baseRepository.findBy(User.class,"users",filterProperties, ConditionSeparator.AND);
        return users.next();
    }

    public boolean userIsInCourse(String email, String name) throws IOException {
        String id = getUserId(email);
        String courseId = getCourseId(name);

        FilterProperty userInCourseFilter = new FilterProperty("_from", id, FilterCondition.EQUAL);
        FilterProperty userInCourseFilter2 = new FilterProperty("_to", courseId, FilterCondition.EQUAL);
        Collection<FilterProperty> userInCourseFilterList = new ArrayList<>();
        userInCourseFilterList.add(userInCourseFilter);
        userInCourseFilterList.add(userInCourseFilter2);
        try {
            Iterable<UserInCourse> userInCourses = baseRepository.findBy(org.piazza.model.edge.UserInCourse.class, "userInCourse", userInCourseFilterList, ConditionSeparator.AND);
            if(!userInCourses.iterator().hasNext()){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            log.info("error");
            throw new IOException("Error:" +e.getMessage());
        }
    }

    public boolean userIsInCourseId(String email, String courseId) throws IOException {
        String id = getUserId(email);

        FilterProperty userInCourseFilter = new FilterProperty("_from", id, FilterCondition.EQUAL);
        FilterProperty userInCourseFilter2 = new FilterProperty("_to", courseId, FilterCondition.EQUAL);
        Collection<FilterProperty> userInCourseFilterList = new ArrayList<>();
        userInCourseFilterList.add(userInCourseFilter);
        userInCourseFilterList.add(userInCourseFilter2);
        try {
            Iterable<UserInCourse> userInCourses = baseRepository.findBy(org.piazza.model.edge.UserInCourse.class, "userInCourse", userInCourseFilterList, ConditionSeparator.AND);
            if(!userInCourses.iterator().hasNext()){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            log.info("error");
            throw new IOException("Error: "+e.getMessage());
        }
    }
    public boolean userReportedUser(String email, String reportedEmail, String courseName) throws IOException {
        String id1 = getUserId(email);
        String id2 = getUserId(reportedEmail);
        String courseId =getCourseId(courseName);
        FilterProperty reporFilter = new FilterProperty("courseId", courseId, FilterCondition.EQUAL);
        Collection<FilterProperty> reportFilterList = new ArrayList<>();
        reportFilterList.add(reporFilter);
        try{
            Iterable<Report> reports = baseRepository.findBy(org.piazza.model.document.Report.class, "reports", reportFilterList, ConditionSeparator.AND);
            if(reports.iterator().hasNext()) {
                Report report = reports.iterator().next();
                FilterProperty userIsReported = new FilterProperty("_from", report.getArangoId(), FilterCondition.EQUAL);
                FilterProperty userIsReportedFilter2 = new FilterProperty("_to", id2, FilterCondition.EQUAL);
                Collection<FilterProperty> userIsReportedFilterList = new ArrayList<>();
                userIsReportedFilterList.add(userIsReported);
                userIsReportedFilterList.add(userIsReportedFilter2);
                Iterable<UserReported> userIsReports = baseRepository.findBy(org.piazza.model.edge.UserReported.class, "userReported", userIsReportedFilterList, ConditionSeparator.AND);
                if(userIsReports.iterator().hasNext()){
                    FilterProperty userCreateReportFilter = new FilterProperty("_from", id1, FilterCondition.EQUAL);
                    FilterProperty userCreateReportFilter2 = new FilterProperty("_to", report.getArangoId(), FilterCondition.EQUAL);
                    Collection<FilterProperty> userCreateReportFilterList = new ArrayList<>();
                    userCreateReportFilterList.add(userCreateReportFilter);
                    userCreateReportFilterList.add(userCreateReportFilter2);
                    Iterable<UserCreateReport> userCreateReports = baseRepository.findBy(org.piazza.model.edge.UserCreateReport.class, "userCreateReport", userCreateReportFilterList, ConditionSeparator.AND);
                    if(userCreateReports.iterator().hasNext()){
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        } catch (Exception e) {
            log.info("error");
            throw new IOException("Error: "+e.getMessage());
        }
    }

    public Poll getPollById(String pollId){ // for testing
        return pollRepository.findById(pollId).get();
    }
    public boolean pollInCourse(String courseId,Poll poll){ // for testing
        Course course=courseRepository.findById(courseId).get();
        return course.getPolls().contains(poll);
    }
    public String userVoteInPoll(String userEmail,String pollId){ // for testing
         FilterProperty filterProperty=new FilterProperty("email",userEmail,FilterCondition.EQUAL);
         ArrayList<FilterProperty> filterProperties=new ArrayList<>();
         filterProperties.add(filterProperty);
         Iterator<UserAnswerPoll> userAnswerPollIterator=edgeBaseRepository.findBy(UserAnswerPoll.class,pollId,"userAnswerPoll",EdgeDirection.INBOUND,new ArrayList<>(),filterProperties,null,null,"e",1,1);
         return  userAnswerPollIterator.next().getOption();

    }



    public boolean questionInCourse(String questionId,String courseId){
        Optional<Question> questionOptional=questionRepository.findById(questionId);
        if(questionOptional.isEmpty()){
            return false;
        }
        Question question=questionOptional.get();
        return question.getCourse().getArangoId().equals(courseId);

    }
    public Answer getAnswerById(String answerId){
        Optional<Answer> answer=answerRepository.findById(answerId);
        if(answer.isPresent()){
            return answer.get();
        }
        return null;
    }

    public HashMap<String, Object> addUser(String email, String role) {
        HashMap<String, Object> response = new HashMap<>();
        FilterProperty userFilter = new FilterProperty("email", email, FilterCondition.EQUAL);
        FilterProperty userFilter2 = new FilterProperty("role", role, FilterCondition.EQUAL);
        Collection<FilterProperty> userFiltering = new ArrayList<>();
        userFiltering.add(userFilter);
        userFiltering.add(userFilter2);
        org.piazza.model.document.User userArango = new org.piazza.model.document.User(email, role);
        Iterable<org.piazza.model.document.User> users = baseRepository.findBy(org.piazza.model.document.User.class, "users", userFiltering, ConditionSeparator.AND);
        if (com.google.common.collect.Iterables.size(users) == 0) {
            userRepository.save(userArango);
            response.put("status", 200);
            return response;
        }
        else{
            userRepository.save(userArango);
            response.put("status", 422);
            response.put("message", "User already exists");
            return response;
        }
    }

    public HashMap<String, Object> deleteUser(String email) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            String id = getUserId(email);
            userRepository.deleteById(id);
            response.put("status", 200);
            return response;
        } catch (IOException e) {
            response.put("status", 500);
            response.put("message", e.getMessage());
            return response;
        }
    }



    public void deleteAll(){
        userRepository.deleteAll();
        courseRepository.deleteAll();
        userInCourseRepository.deleteAll();
        userCreateReportRepository.deleteAll();
        userReportedRepository.deleteAll();
        reportRepository.deleteAll();
        userCreateReportRepository.deleteAll();
        userReportedRepository.deleteAll();
        userBannedFromCourseRepository.deleteAll();
        questionRepository.deleteAll();
        userMakeAnswerRepository.deleteAll();
        userLikesQuestionRepository.deleteAll();
        questionMentionedUserRepository.deleteAll();
        pollRepository.deleteAll();
        userAnswerPollRepository.deleteAll();
        userLikesAnswerRepository.deleteAll();
        answerRepository.deleteAll();

    }
}
