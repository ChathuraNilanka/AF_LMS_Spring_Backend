package com.learning.management.system.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.learning.management.system.model.Exams;

public interface ExamRepository extends MongoRepository< Exams, String >{

}