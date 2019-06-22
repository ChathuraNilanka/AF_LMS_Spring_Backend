package com.learning.management.system.controller;

import com.learning.management.system.model.Assignment;
import com.learning.management.system.repo.AssignmentReposirory;
import com.learning.management.system.exception.ResourceNotFoundException;
import com.learning.management.system.exception.UnprocessableEntityException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpStatus;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("lms/assignments")
public class AssignmentController {
	
	@Autowired
	private AssignmentReposirory assignmentRepo;
	@Autowired
	private ApplicationContext ctx;
	
//	@PostMapping("/")
//    @ResponseStatus(code = HttpStatus.CREATED)
//    public Assignment add(@RequestBody Assignment assignment) {
//		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//		Date date = new Date();
//		assignment.setTime_stamp(dateFormat.format(date));
//        //return assignmentRepo.save(assingment);//.save(assingment);
//		return assignmentRepo.save(assignment);
//    }
	
	@GetMapping("/")
    public List<Assignment> getAllByInsructorId( ) {
        return assignmentRepo.findAll();
    }
	
	@PostMapping("/")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Assignment add(@RequestParam("assgnmentName") String assgnmentName, @RequestParam("courseName") String courseName, 
    		              @RequestParam("deadLine") String deadLine, @RequestParam("instructorId") String instructorId, 
    		              @RequestParam("file") MultipartFile file, @RequestParam("description") String description) throws IOException {

        if (file.isEmpty())
        	throw new UnprocessableEntityException();

        GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");
        DBObject metaData = new BasicDBObject();
        metaData.put("for", "submission");

        InputStream inputStream = new BufferedInputStream(file.getInputStream());

        //set file name
        String fileName = courseName + "_" + assgnmentName;

        String ext = "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        ObjectId id = gridOperations.store(inputStream, fileName + ext, file.getContentType(), metaData);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Assignment assignment = new Assignment();

        assignment.setDoc(id.toString());
        assignment.setAssgnmentName(assgnmentName);
        assignment.setCourseName(courseName);
        assignment.setInstructorId(instructorId);
        assignment.setDeadLine(deadLine);
        
        Date now = new Date();
        assignment.setTime_stamp(dateFormat.format(now));
        return assignmentRepo.save(assignment);

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

}
