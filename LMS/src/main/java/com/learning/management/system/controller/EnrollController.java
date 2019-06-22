package com.learning.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.learning.management.system.exception.ResourceNotFoundException;
import com.learning.management.system.model.Enrollments;
import com.learning.management.system.repo.EnrollRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/lms/enrollment")
public class EnrollController {

	@Autowired
	private EnrollRepository enrollRepository;
	
	@PostMapping("/enroll")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Enrollments add(@RequestBody Enrollments enroll) {
		return enrollRepository.save(enroll);
	}
	
	@DeleteMapping(value = "/unenroll/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable String id) {
		Enrollments enroll = enrollRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
        enrollRepository.delete(enroll);
    }
}
