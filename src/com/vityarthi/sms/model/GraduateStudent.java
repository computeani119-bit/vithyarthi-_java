package com.vityarthi.sms.model;

import com.vityarthi.sms.exception.InvalidDataException;

/**
 * Specialization of Student demonstrating Polymorphism & Method Overriding.
 */
public class GraduateStudent extends Student {
    private static final long serialVersionUID = 1L;

    private String researchTopic;
    private String advisorName;

    public GraduateStudent(String id, String name, String email, String rollNumber,
                           String department, int academicYear, double cGPA,
                           String researchTopic, String advisorName) throws InvalidDataException {
        super(id, name, email, rollNumber, department, academicYear, cGPA);
        this.researchTopic = researchTopic != null ? researchTopic.trim() : "TBD";
        this.advisorName = advisorName != null ? advisorName.trim() : "Unassigned";
    }

    public String getResearchTopic() {
        return researchTopic;
    }

    public void setResearchTopic(String researchTopic) {
        this.researchTopic = researchTopic;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    @Override
    public String getAcademicStanding() {
        // Graduate students require higher minimum CGPA (6.0)
        if (getCGPA() >= 9.2) return "High Honors (Postgraduate)";
        if (getCGPA() >= 8.5) return "Honors (Postgraduate)";
        if (getCGPA() >= 6.0) return "Satisfactory Progress";
        return "Probation / Review Required";
    }

    @Override
    public String getRoleTitle() {
        return "Postgraduate / Research Student";
    }

    @Override
    public String toString() {
        return String.format("GradStudent [Roll: %s, Name: %s, Dept: %s, CGPA: %.2f, Topic: %s]",
                getRollNumber(), getName(), getDepartment(), getCGPA(), researchTopic);
    }
}
