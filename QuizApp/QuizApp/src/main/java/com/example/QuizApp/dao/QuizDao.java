package com.example.QuizApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.QuizApp.model.Quiz;

public interface QuizDao extends JpaRepository<Quiz,Integer>{
    
}
