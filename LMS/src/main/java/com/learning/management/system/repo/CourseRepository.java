package com.learning.management.system.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.learning.management.system.model.Course;

public interface CourseRepository extends MongoRepository<Course, String> {

//	List<Course> filterByDepartment(String department);
	
}
