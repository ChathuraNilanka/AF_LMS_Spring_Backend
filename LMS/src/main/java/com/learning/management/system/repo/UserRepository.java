package com.learning.management.system.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.learning.management.system.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	
}
