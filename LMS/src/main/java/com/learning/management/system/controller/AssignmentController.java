package com.learning.management.system.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.learning.management.system.model.Assignment;
import com.learning.management.system.repo.AssignmentReposirory;

@RestController
@RequestMapping("lms/assignments")
public class AssignmentController {
	
	@Autowired
	private AssignmentReposirory assignmentRepo;
	
	@PostMapping("/")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Assignment add(@RequestBody Assignment assignment) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		assignment.setTime_stamp(dateFormat.format(date));
        //return assignmentRepo.save(assingment);//.save(assingment);
		return assignmentRepo.save(assignment);
    }
	
	@GetMapping("/all")
    public String getAll() {
        return "wada";
    }

}
