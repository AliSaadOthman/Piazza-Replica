package org.piazza;

import com.arangodb.springframework.core.template.ArangoTemplate;
import org.piazza.model.document.User;
import org.piazza.repository.collection_repository.BaseRepositoryImpl;
import org.piazza.repository.collection_repository.CourseRepository;
import org.piazza.repository.collection_repository.QuestionRepository;
import org.piazza.repository.graph_repository.CourseQuestionEdgeRepository;
import org.piazza.repository.graph_repository.EdgeBaseRepositoryImpl;
import org.piazza.repository.query.EdgeDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("org.piazza")
public class CrudRunner implements CommandLineRunner {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseQuestionEdgeRepository courseQuestionEdgeRepository;
    @Autowired
    private ArangoTemplate arangoTemplate;
    @Autowired
    BaseRepositoryImpl baseRepository;
    @Autowired
    EdgeBaseRepositoryImpl edgeBaseRepository;


    @Override
    public void run(String... args) throws Exception {
//        Question question = new Question("adham", "adham2", "adham3", ZonedDateTime.now());
////        Question question1 = new Question("sam", "sam1", "sam3", ZonedDateTime.now());
////        Question question2 = new Question("monto1", "monto2", "monto3", ZonedDateTime.now());
////
//        questionRepository.save(question);
////        questionRepository.save(question1);
////        questionRepository.save(question2);
//        Course course=new Course("CS");
//        courseRepository.save(course);
//
//        FilterProperty filterProperty = new FilterProperty("_id", "courses/40233", FilterCondition.EQUAL);
//        FilterProperty filterPropertyuser=new FilterProperty("title","rawan",FilterCondition.EQUAL);
//        Collection<FilterProperty> filterProperties = new ArrayList<>();
//        filterProperties.add(filterProperty);


        Iterable<User> courseQuestionEdge =edgeBaseRepository.findAll1(User.class, "courses/40233", "userReported",EdgeDirection.OUTBOUND,"v",1,1);
//          Iterable<Course> courseQuestionEdge =baseRepository.findBy(Course.class, "courses", filterProperties,ConditionSeparator.AND);

//        for (CourseQuestionEdge edge : courseQuestionEdge) {
//          System.out.println(edge);
//
//        }
//
//        for (Course edge : courseQuestionEdge) {
//            Collection<Question> testing=edge.getQuestions();
//            for(Question question: testing){
//                System.out.println(question.getArangoId());
//            }
//
//        }
//        for (Course course:courses ){
//            courseQuestionEdgeRepository.save(new CourseQuestionEdge(course,question1));
//            courseQuestionEdgeRepository.save(new CourseQuestionEdge(course,question2));
//
//
//        }
//        Collection<FilterProperty> verticesFilterProperties = new ArrayList<>();
//        verticesFilterProperties.add(filterPropertyuser);
//        Iterable<Question> iterable = edgeBaseRepository.findBy(Question.class,"courses/40233","courseQuestionEdge", EdgeDirection.OUTBOUND,new ArrayList<FilterProperty>(),verticesFilterProperties,null, ConditionSeparator.AND);
//
//        String query= "FOR v,e  IN OUTBOUND @value courseQuestionEdge RETURN v";
//        HashMap<String,Object> test=new HashMap<String,Object>();
//        test.put("value","courses/40233");
//        Iterable<Question> returned= arangoTemplate.query(query,test,Question.class);


//        FilterProperty filterProperty = new FilterProperty("mediaLink", "monto3", FilterCondition.EQUAL);
//        Collection<FilterProperty> filterProperties = new ArrayList<>();
//        filterProperties.add(filterProperty);
//        Iterable<Question> iterable = questionRepository.findBy(Question.class, "questions", filterProperties, "AND");
//        for (Question value : iterable) {
//            System.out.println(value);
//        }
    }
}