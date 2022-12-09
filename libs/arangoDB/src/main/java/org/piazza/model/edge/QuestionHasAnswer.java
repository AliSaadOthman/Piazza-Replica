package org.piazza.model.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.piazza.model.BaseModel;
import org.piazza.model.document.Answer;
import org.piazza.model.document.Question;

@Edge("questionHasAnswer")
public class QuestionHasAnswer extends BaseModel {

    @From(lazy = true)
    Question question;
    @To(lazy = true)
    Answer answer;

    public QuestionHasAnswer(Question question, Answer answer) {
        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
