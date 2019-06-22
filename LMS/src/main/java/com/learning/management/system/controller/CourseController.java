package com.learning.management.system.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.learning.management.system.exception.ResourceNotFoundException;
import com.learning.management.system.model.Course;
import com.learning.management.system.repo.CourseRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/lms/course")
public class CourseController {
	
	@Autowired
	private CourseRepository courseRepository;
	
	@PostMapping("/add")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Course add(@RequestBody Course course) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		course.setTime_stamp(dateFormat.format(date));
        return courseRepository.save(course);
    }
	
	@GetMapping("/all")
    public List<Course> getAll() {
        return courseRepository.findAll();
    }
	
	@GetMapping(value = "/getbyid/{id}")
    public Course getOne(@PathVariable String id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    }

    @PutMapping(value = "/update/{id}")
    public Course update(@PathVariable String id, @RequestBody Course updatedCourse) {
    	Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    	
    	course.setCourse_id(updatedCourse.getCourse_id());
    	course.setName(updatedCourse.getName());
    	course.setDescription(updatedCourse.getDescription());
    	course.setEnroll_key(updatedCourse.getEnroll_key());
    	course.setCredits(updatedCourse.getCredits());
    	course.setFaculty(updatedCourse.getFaculty());
    	course.setDepartment(updatedCourse.getDepartment());
    	course.setInstructor_id(updatedCourse.getInstructor_id());
    	course.setStatus(updatedCourse.getStatus());
    	
        return courseRepository.save(course);
    }

    @DeleteMapping(value = "/remove/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable String id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
        courseRepository.delete(course);
    }
    
}
