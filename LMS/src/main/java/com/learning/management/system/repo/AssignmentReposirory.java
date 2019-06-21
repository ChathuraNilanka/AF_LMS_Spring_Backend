package com.learning.management.system.repo;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.learning.management.system.model.Assignment;;

public interface AssignmentReposirory extends MongoRepository<Assignment, String> {

//	List<Assignment> findByinstructorIdt(String instructorId);

	List<Assignment> findAll(Query query);
	
}