package com.learning.management.system.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.learning.management.system.model.Enrollments;

public interface EnrollRepository extends MongoRepository<Enrollments, String> {

}
