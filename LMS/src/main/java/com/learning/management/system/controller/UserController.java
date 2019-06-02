package com.learning.management.system.controller;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.mongodb.core.query.Query;

import com.learning.management.system.exception.ResourceNotFoundException;
import com.learning.management.system.exception.UnprocessableEntityException;
import com.learning.management.system.model.User;
import com.learning.management.system.repo.UserRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;


@RestController
@RequestMapping("/lms/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private ApplicationContext ctx;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public User add(@RequestBody User user) {    
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		user.setCreated_at(dateFormat.format(date));
		user.setUpdated_at(dateFormat.format(date));
		user.setPassword(passwordEncoder.encode(user.getPassword()).toString());
		//user.setImage(imageId.toString());
		
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
    
    @PutMapping(value = "image/{id}")
    public User updateImage(@PathVariable String id, @RequestBody MultipartFile file) throws IOException {
    	User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    	
    	if (file.isEmpty())
            throw new UnprocessableEntityException();
		
		GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");
        DBObject metaData = new BasicDBObject();
        metaData.put("for", "userImage");
        
        InputStream inputStream = new BufferedInputStream(file.getInputStream());
        
        String fileName = user.getUsername();
        
        String ext = "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        
        ObjectId imageId = gridOperations.store(inputStream, fileName + ext, file.getContentType(), metaData);
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
    	
		user.setImage(imageId.toString());
		user.setUpdated_at(dateFormat.format(date));
    	
        return userRepository.save(user);
    }
    
    
    @GetMapping("/image/get/{id}")
    public void retrieveImageFile(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException{
    	
    	GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");
    	
    	GridFSFile file = gridOperations.findOne(new Query().addCriteria(Criteria.where("_id").is(id)));
    	
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
    

    @DeleteMapping(value = "/remove/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable String id) {
    	User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    	userRepository.delete(user);
    }
    
}
