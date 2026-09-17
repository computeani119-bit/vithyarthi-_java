package com.vityarthi.sms.model;

import com.vityarthi.sms.exception.InvalidDataException;
import com.vityarthi.sms.util.ValidationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main Student model demonstrating Inheritance from Person and encapsulation.
 */
public class Student extends Person {
    private static final long serialVersionUID = 1L;

    private String rollNumber;
    private String department;
    private int academicYear;
    private double cGPA;
    private List<CourseGrade> courseGrades;

    public Student(String id, String name, String email, String rollNumber, 
                   String department, int academicYear, double cGPA) throws InvalidDataException {
        super(id, name, email);
        ValidationUtils.validateRollNumber(rollNumber);
        ValidationUtils.validateDepartment(department);
        ValidationUtils.validateGPA(cGPA);

        this.rollNumber = rollNumber.trim().toUpperCase();
        this.department = department.trim();
        this.academicYear = academicYear;
        this.cGPA = cGPA;
        this.courseGrades = new ArrayList<>();
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) throws InvalidDataException {
        ValidationUtils.validateRollNumber(rollNumber);
        this.rollNumber = rollNumber.trim().toUpperCase();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) throws InvalidDataException {
        ValidationUtils.validateDepartment(department);
        this.department = department.trim();
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public double getCGPA() {
        return cGPA;
    }

    public void setCGPA(double cGPA) throws InvalidDataException {
        ValidationUtils.validateGPA(cGPA);
        this.cGPA = cGPA;
    }

    public List<CourseGrade> getCourseGrades() {
        return Collections.unmodifiableList(courseGrades);
    }

    public void addCourseGrade(CourseGrade grade) {
        if (grade != null) {
            this.courseGrades.add(grade);
            recalculateCGPA();
        }
    }

    public void recalculateCGPA() {
        if (courseGrades.isEmpty()) return;
        double totalPoints = 0;
        int totalCredits = 0;
        for (CourseGrade cg : courseGrades) {
            totalPoints += (cg.getGradePoint() * cg.getCredits());
            totalCredits += cg.getCredits();
        }
        if (totalCredits > 0) {
            this.cGPA = Math.round((totalPoints / totalCredits) * 100.0) / 100.0;
        }
    }

    public String getAcademicStanding() {
        if (cGPA >= 9.0) return "First Class with Distinction";
        if (cGPA >= 8.0) return "First Class";
        if (cGPA >= 6.5) return "Second Class";
        if (cGPA >= 5.0) return "Pass";
        return "Academic Warning";
    }

    @Override
    public String getRoleTitle() {
        return "Undergraduate Student";
    }

    @Override
    public String toString() {
        return String.format("Student [Roll: %s, Name: %s, Dept: %s, Year: %d, CGPA: %.2f - %s]",
                rollNumber, getName(), department, academicYear, cGPA, getAcademicStanding());
    }
}
