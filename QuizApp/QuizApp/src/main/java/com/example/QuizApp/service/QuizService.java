package com.example.QuizApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.QuizApp.dao.QuestionDao;
import com.example.QuizApp.dao.QuizDao;
import com.example.QuizApp.model.Question;
import com.example.QuizApp.model.Quiz;
import com.example.QuizApp.model.QuizWrapper;
import com.example.QuizApp.model.Response;

@Service
public class QuizService {
    
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category,int numQ,String title){

        List<Question> questions = questionDao.findRandomQuestionByCategory(category,numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title); 
        quiz.setQuestions(questions); 
        quizDao.save(quiz);

        return new ResponseEntity<>("Success",HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuizWrapper>> getQuiz(Integer id){
        Optional<Quiz> quiz = quizDao.findById(id);  

        List<Question> questionsFromDB = quiz.get().getQuestions(); 
        List<QuizWrapper> questionsForUser = new ArrayList<>(); 
        for (Question q:questionsFromDB){
            QuizWrapper qw = new QuizWrapper(q.getId(),q.getQuestionTitle(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
            questionsForUser.add(qw);
        }

        return new ResponseEntity<>(questionsForUser,HttpStatus.OK); 
    }

    public ResponseEntity<Integer> calcScore(Integer id, List<Response> responses){

        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();
        Integer right = 0;
        int i=0;
        for (Response response:responses){
            if (response.getResponse().equals(questions.get(i).getRightAnswer())){
                right++;
            }
            i++;
        }
        return new ResponseEntity<>(right,HttpStatus.OK);
    }   
}