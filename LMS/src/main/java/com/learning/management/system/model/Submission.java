package com.learning.management.system.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class Submission {
    private String _id;
    private String courseId;
    private String assigmentName;
    private String marks;
    private String studentId;
    private String createdAt;
    private String updatedAt;
    private byte[] file;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getAssigmentName() {
        return assigmentName;
    }

    public void setAssigmentName(String assigmentName) {
        this.assigmentName = assigmentName;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
