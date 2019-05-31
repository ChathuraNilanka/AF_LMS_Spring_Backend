package com.learning.management.system.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.learning.management.system.exception.ResourceNotFoundException;
import com.learning.management.system.model.User;
import com.learning.management.system.repo.UserRepository;


@RestController
@RequestMapping("/lms/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public User add(@RequestBody User user) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		user.setCreated_at(dateFormat.format(date));
		user.setUpdated_at(dateFormat.format(date));
		user.setPassword(passwordEncoder.encode(user.getPassword()).toString());
		
        return userRepository.save(user);
    }
	
	@GetMapping("/all")
    public List<User> getAll() {
        return userRepository.findAll();
    }
	
	@GetMapping(value = "/getbyid/{id}")
    public User getOne(@PathVariable String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    }

    @PutMapping(value = "/update/{id}")
    public User update(@PathVariable String id, @RequestBody User updatedUser) {
    	User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
    	
    	user.setFullname(updatedUser.getFullname());
    	user.setPassword(updatedUser.getPassword());
    	user.setPhone(updatedUser.getPhone());
    	user.setGender(updatedUser.getGender());
    	user.setFaculty(updatedUser.getFaculty());
    	user.setStatus(updatedUser.getStatus());
        user.setConfirmed(updatedUser.getConfirmed());
        user.setConfirm_code(updatedUser.getConfirm_code());
    	user.setUpdated_at(dateFormat.format(date));
    	
    	System.out.println(updatedUser.getStatus());
    	
        return userRepository.save(user);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable String id) {
    	User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    	userRepository.delete(user);
    }
    
}
