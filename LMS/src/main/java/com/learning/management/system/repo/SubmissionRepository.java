package com.learning.management.system.repo;

import com.learning.management.system.model.Submission;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubmissionRepository extends MongoRepository<Submission,String> {
    List<Submission> findByStudentId(String studentID, Sort sort);
    List<Submission> findByCourseIdAndAssigmentName(String courseID,String assignmentName);
    Submission findByCourseIdAndAssigmentNameAndStudentId(String courseID,String assignmentName,String studentID);
}
