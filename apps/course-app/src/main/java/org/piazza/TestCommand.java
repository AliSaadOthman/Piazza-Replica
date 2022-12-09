package org.piazza;

import org.piazza.repository.collection_repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TestCommand {
    @Autowired
    QuestionRepository questionRepository;

    public void testing(){
//                Question question2 = new Question("monto1", "monto2", "monto3", ZonedDateTime.now());
//
//        questionRepository.save(question2);

    }
}
