package com.learning.management.system.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.learning.management.system.model.Assignment;;

public interface AssignmentReposirory extends MongoRepository<Assignment, String> {

//	List<Course> filterByDepartment(String department);
	
}