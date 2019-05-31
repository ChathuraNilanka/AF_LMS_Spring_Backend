package com.learning.management.system.controller;

import com.learning.management.system.exception.ResourceNotFoundException;
import com.learning.management.system.exception.UnprocessableEntityException;
import com.learning.management.system.model.Submission;
import com.learning.management.system.repo.SubmissionRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFSDBFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/lms/submission")
public class SubmissionController {

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private ApplicationContext ctx;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Submission add(@RequestParam("courseID") String courseID, @RequestParam("assignmentName") String assignmentName, @RequestParam("studentID") String studentID, @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty())
            throw new UnprocessableEntityException();

        GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");
        DBObject metaData = new BasicDBObject();
        metaData.put("for", "submission");

        InputStream inputStream = new BufferedInputStream(file.getInputStream());

        //set file name
        String fileName = courseID + "_" + assignmentName + "_" + studentID;

        //get file extention
        String ext = "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        ObjectId id = gridOperations.store(inputStream, fileName + ext, file.getContentType(), metaData);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Submission submission = new Submission();

        submission.setFile(id.toString());
        submission.setCourseId(courseID);
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

        if (result != null) {
            if (result.size() == 0)
                throw new ResourceNotFoundException();
        } else {
            throw new ResourceNotFoundException();
        }

        return result;
    }

    @GetMapping("/student/{studentID}")
    public List<Submission> getSubmissionByStudentId(@PathVariable String studentID) {
        List<Submission> result = submissionRepository.findByStudentId(studentID);

        if (result != null) {
            if (result.size() == 0)
                throw new ResourceNotFoundException();
        } else {
            throw new ResourceNotFoundException();
        }

        return result;
    }

    @GetMapping("/course/{courseID}/assignment/{assignmentName}")
    public List<Submission> getSubmissionByCourseIDAndAssignmentName(@PathVariable String courseID, @PathVariable String assignmentName) {
        List<Submission> result = submissionRepository.findByCourseIdAndAssigmentName(courseID, assignmentName);

        if (result != null) {
            if (result.size() == 0)
                throw new ResourceNotFoundException();
        } else {
            throw new ResourceNotFoundException();
        }

        return result;
    }

    @GetMapping("/getassignment")
    public Submission getSubmissionByStudentID(@RequestParam(name = "courseID") String courseID,
                                               @RequestParam(name = "assignment") String assignmentName,
                                               @RequestParam(name = "studentID") String studentID) {
        Submission result = submissionRepository.findByCourseIdAndAssigmentNameAndStudentId(courseID, assignmentName, studentID);

        if (result == null) {
            throw new ResourceNotFoundException();
        }
        return result;
    }

    @GetMapping("/download/{id}")
    public void getSubmittedFile(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

        GridFSFile file = gridOperations.findOne(new Query().addCriteria(Criteria.where("_id").is(id)));

        if (file == null)
            throw new ResourceNotFoundException();

        try {
            response.setContentType(gridOperations.getResource(file).getContentType());
            response.setContentLength((new Long(file.getLength()).intValue()));
            response.setHeader("content-Disposition", "attachment; filename=" + file.getFilename());// "attachment;filename=test.xls"
            // copy it to response's OutputStream
            IOUtils.copyLarge(gridOperations.getResource(file).getInputStream(), response.getOutputStream());
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PutMapping("/{id}")
    public Submission update(@PathVariable String id, @RequestParam("marks") Double updatedMarks){
        Submission submission = submissionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException());

        if(submission==null)
            throw new ResourceNotFoundException();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();

        submission.setUpdatedAt(dateFormat.format(now));

        submission.setMarks(updatedMarks);

        return submissionRepository.save(submission);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable String id){
        Submission submission = submissionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException());

        String fileID = submission.getFile();
        GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");
        gridOperations.delete(new Query().addCriteria(Criteria.where("_id").is(fileID)));
        submissionRepository.delete(submission);

    }
}