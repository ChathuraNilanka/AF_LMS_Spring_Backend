package com.learning.management.system.model;

import org.bson.types.ObjectId;

public class Submission {
    private String _id;
    private String courseId;
    private String assigmentName;
    private String marks;
    private String studentId;
    private String createdAt;
    private String updatedAt;
    private ObjectId file;

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

    public ObjectId getFile() {
        return file;
    }

    public void setFile(ObjectId file) {
        this.file = file;
    }
}