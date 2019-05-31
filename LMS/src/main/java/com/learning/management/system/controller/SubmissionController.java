package com.learning.management.system.controller;

import com.learning.management.system.exception.ResourceNotFoundException;
import com.learning.management.system.model.Submission;
import com.learning.management.system.repo.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/lms/submission")
public class SubmissionController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Submission add(@RequestParam("courseID") String coursID,@RequestParam("assignmentName") String assignmentName,@RequestParam("studentID") String studentID,@RequestParam("file") MultipartFile file) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Submission submission = new Submission();
        System.out.println(file.isEmpty());
        submission.setFile(file.getBytes());
        submission.setCourseId(coursID);
        submission.setAssigmentName(assignmentName);
        submission.setStudentId(studentID);

        Date now = new Date();
        submission.setCreatedAt(dateFormat.format(now));
        return submissionRepository.save(submission);
    }

    @GetMapping
    public List<Submission> getAll() {
        return submissionRepository.findAll();
    }

    @GetMapping(value = "/course/{courseID}")
    public List<Submission> getSubmissionByCourseID(@PathVariable String courseID) {
        List<Submission> result = submissionRepository.findByCourseId(courseID);

        if(result != null){
            if ( result.size() == 0)
                throw new ResourceNotFoundException();
        }else{
            throw new ResourceNotFoundException();
        }

        return result;
    }

    @GetMapping("/student/{studentID}")
    public List<Submission> getSubmissionByStudentId(@PathVariable String studentID){
        List<Submission> result = submissionRepository.findByStudentId(studentID);

        if(result != null){
            if ( result.size() == 0)
                throw new ResourceNotFoundException();
        }else{
            throw new ResourceNotFoundException();
        }

        return result;
    }
    @GetMapping("/course/{courseID}/assignment/{assignmentName}")
    public List<Submission> getSubmissionByCourseIDAndAssignmentName(@PathVariable String courseID,@PathVariable String assignmentName){
        List<Submission> result = submissionRepository.findByCourseIdAndAssigmentName(courseID,assignmentName);

        if(result != null){
            if ( result.size() == 0)
                throw new ResourceNotFoundException();
        }else{
            throw new ResourceNotFoundException();
        }

        return result;
    }
    @GetMapping("/getassignment")
    public Submission getSubmissionByStudentID(@RequestParam(name="courseID") String courseID,
                                               @RequestParam(name="assignment") String assignmentName,
                                               @RequestParam(name="studentID") String studentID){
        Submission result =  submissionRepository.findByCourseIdAndAssigmentNameAndStudentId(courseID,assignmentName,studentID);

        if(result==null){
            throw new ResourceNotFoundException();
        }
        return result;
    }
}
