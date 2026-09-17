package com.vityarthi.sms.service;

import com.vityarthi.sms.exception.InvalidDataException;
import com.vityarthi.sms.exception.StudentNotFoundException;
import com.vityarthi.sms.model.Student;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentManager {

    private final List<Student> students;
    private final StorageService storageService;

    public StudentManager(StorageService storageService) {
        this.storageService = storageService;
        this.students = new ArrayList<>();
        loadDataSilently();
    }

    private void loadDataSilently() {
        if (storageService != null) {
            try {
                this.students.clear();
                this.students.addAll(storageService.loadStudents());
            } catch (IOException e) {
                System.err.println("Notice: Failed to auto-load student records: " + e.getMessage());
            }
        }
    }

    public synchronized void addStudent(Student student) throws InvalidDataException {
        if (student == null) {
            throw new InvalidDataException("Student object cannot be null.");
        }
        // Check duplicate roll number
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(student.getRollNumber())) {
                throw new InvalidDataException("A student with roll number '" + student.getRollNumber() + "' already exists.");
            }
        }
        students.add(student);
        saveDataSilently();
    }

    public synchronized void updateStudent(String rollNumber, Student updated) throws StudentNotFoundException, InvalidDataException {
        Student existing = getStudentByRollNumber(rollNumber);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setDepartment(updated.getDepartment());
        existing.setAcademicYear(updated.getAcademicYear());
        existing.setCGPA(updated.getCGPA());
        saveDataSilently();
    }

    public synchronized boolean deleteStudent(String rollNumber) throws StudentNotFoundException {
        Student s = getStudentByRollNumber(rollNumber);
        boolean removed = students.remove(s);
        if (removed) {
            saveDataSilently();
        }
        return removed;
    }

    public Student getStudentByRollNumber(String rollNumber) throws StudentNotFoundException {
        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            throw new StudentNotFoundException("Invalid roll number provided.");
        }
        Optional<Student> opt = students.stream()
                .filter(s -> s.getRollNumber().equalsIgnoreCase(rollNumber.trim()))
                .findFirst();

        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new StudentNotFoundException("No student found with roll number: " + rollNumber);
        }
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public List<Student> searchStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllStudents();
        }
        String q = query.trim().toLowerCase();
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(q) ||
                             s.getRollNumber().toLowerCase().contains(q) ||
                             s.getDepartment().toLowerCase().contains(q) ||
                             s.getEmail().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Student> filterByDepartment(String dept) {
        if (dept == null || dept.trim().isEmpty() || "All".equalsIgnoreCase(dept)) {
            return getAllStudents();
        }
        return students.stream()
                .filter(s -> s.getDepartment().equalsIgnoreCase(dept.trim()))
                .collect(Collectors.toList());
    }

    public List<Student> getTopPerformers(int limit) {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getCGPA).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void saveDataSilently() {
        if (storageService != null) {
            try {
                storageService.saveStudents(students);
            } catch (IOException e) {
                System.err.println("Warning: Failed to save student records: " + e.getMessage());
            }
        }
    }
}
