package com.vityarthi.sms.model;

import java.io.Serializable;

public class CourseGrade implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseCode;
    private String courseName;
    private int credits;
    private double gradePoint; // 0.0 - 10.0

    public CourseGrade(String courseCode, String courseName, int credits, double gradePoint) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.gradePoint = gradePoint;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public String getLetterGrade() {
        if (gradePoint >= 9.0) return "S";
        if (gradePoint >= 8.0) return "A";
        if (gradePoint >= 7.0) return "B";
        if (gradePoint >= 6.0) return "C";
        if (gradePoint >= 5.0) return "D";
        if (gradePoint >= 4.0) return "E";
        return "F";
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %d Credits | Grade: %.1f [%s]", 
            courseCode, courseName, credits, gradePoint, getLetterGrade());
    }
}
