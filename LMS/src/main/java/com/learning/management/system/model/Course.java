package com.learning.management.system.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "courses")
public class Course {
	
	@Id
	private String id;
	@Indexed(unique = true)
	private String course_id;
	@Indexed(unique = true)
	private String name;
	private String description;
	private String enroll_key;
	private int credits;
	private String faculty;
	private String department;
	private String instructor_id;
	private String status;
	private String time_stamp;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEnroll_key() {
		return enroll_key;
	}
	public void setEnroll_key(String enroll_key) {
		this.enroll_key = enroll_key;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public String getFaculty() {
		return faculty;
	}
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getInstructor_id() {
		return instructor_id;
	}
	public void setInstructor_id(String instructor_id) {
		this.instructor_id = instructor_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime_stamp() {
		return time_stamp;
	}
	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}
	
	
	
}
