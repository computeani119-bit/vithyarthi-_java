package com.vityarthi.sms.service;

import com.vityarthi.sms.exception.InvalidDataException;
import com.vityarthi.sms.model.GraduateStudent;
import com.vityarthi.sms.model.Student;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService implements StorageService {

    private final String filePath;

    public FileStorageService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Student> loadStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return students;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (isHeader && (line.startsWith("Type") || line.startsWith("type"))) {
                    isHeader = false;
                    continue;
                }
                isHeader = false;

                // CSV format: Type,ID,Name,Email,RollNumber,Department,AcademicYear,CGPA,ResearchTopic,Advisor
                String[] tokens = line.split(",", -1);
                if (tokens.length >= 8) {
                    try {
                        String type = tokens[0].trim();
                        String id = tokens[1].trim();
                        String name = tokens[2].trim();
                        String email = tokens[3].trim();
                        String roll = tokens[4].trim();
                        String dept = tokens[5].trim();
                        int year = Integer.parseInt(tokens[6].trim());
                        double cgpa = Double.parseDouble(tokens[7].trim());

                        Student student;
                        if ("GRADUATE".equalsIgnoreCase(type) && tokens.length >= 10) {
                            String topic = tokens[8].trim();
                            String advisor = tokens[9].trim();
                            student = new GraduateStudent(id, name, email, roll, dept, year, cgpa, topic, advisor);
                        } else {
                            student = new Student(id, name, email, roll, dept, year, cgpa);
                        }
                        students.add(student);
                    } catch (InvalidDataException | NumberFormatException e) {
                        System.err.println("Warning: Skipping malformed line in CSV: " + line);
                    }
                }
            }
        }
        return students;
    }

    @Override
    public void saveStudents(List<Student> students) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("Type,ID,Name,Email,RollNumber,Department,AcademicYear,CGPA,ResearchTopic,AdvisorName");
            bw.newLine();

            for (Student s : students) {
                if (s instanceof GraduateStudent grad) {
                    bw.write(String.format("GRADUATE,%s,%s,%s,%s,%s,%d,%.2f,%s,%s",
                            sanitize(grad.getId()),
                            sanitize(grad.getName()),
                            sanitize(grad.getEmail()),
                            sanitize(grad.getRollNumber()),
                            sanitize(grad.getDepartment()),
                            grad.getAcademicYear(),
                            grad.getCGPA(),
                            sanitize(grad.getResearchTopic()),
                            sanitize(grad.getAdvisorName())));
                } else {
                    bw.write(String.format("UG,%s,%s,%s,%s,%s,%d,%.2f,,",
                            sanitize(s.getId()),
                            sanitize(s.getName()),
                            sanitize(s.getEmail()),
                            sanitize(s.getRollNumber()),
                            sanitize(s.getDepartment()),
                            s.getAcademicYear(),
                            s.getCGPA()));
                }
                bw.newLine();
            }
        }
    }

    private String sanitize(String text) {
        if (text == null) return "";
        return text.replace(",", ";");
    }
}
