package com.vityarthi.sms;

import com.vityarthi.sms.exception.InvalidDataException;
import com.vityarthi.sms.model.GraduateStudent;
import com.vityarthi.sms.model.Student;
import com.vityarthi.sms.service.FileStorageService;
import com.vityarthi.sms.service.StorageService;
import com.vityarthi.sms.service.StudentManager;
import com.vityarthi.sms.ui.ConsoleUI;
import com.vityarthi.sms.ui.MainFrame;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

public class Main {

    private static final String DATA_FILE = "data" + File.separator + "students.csv";

    public static void main(String[] args) {
        // Initialize storage & service
        StorageService storageService = new FileStorageService(DATA_FILE);
        StudentManager studentManager = new StudentManager(storageService);

        // Pre-populate initial dummy records if empty for immediate evaluation demo
        if (studentManager.getAllStudents().isEmpty()) {
            populateSampleData(studentManager);
        }

        boolean cliMode = Arrays.asList(args).contains("--cli");

        if (cliMode) {
            ConsoleUI consoleUI = new ConsoleUI(studentManager);
            consoleUI.start();
        } else {
            // Launch Swing GUI
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                    // Fall back to default look and feel
                }
                MainFrame frame = new MainFrame(studentManager);
                frame.setVisible(true);
            });
        }
    }

    private static void populateSampleData(StudentManager manager) {
        try {
            manager.addStudent(new Student("STU101", "Aarav Sharma", "aarav.sharma@vityarthi.edu", "21BCE0101", "Computer Science", 3, 9.42));
            manager.addStudent(new Student("STU102", "Ananya Verma", "ananya.verma@vityarthi.edu", "21BCE0105", "Computer Science", 3, 8.85));
            manager.addStudent(new Student("STU103", "Rohan Mehta", "rohan.mehta@vityarthi.edu", "21BIT0204", "Information Technology", 3, 7.60));
            manager.addStudent(new Student("STU104", "Priya Nair", "priya.nair@vityarthi.edu", "22ECE0310", "Electronics & Comm", 2, 9.10));
            manager.addStudent(new Student("STU105", "Devansh Patel", "devansh.patel@vityarthi.edu", "22MEC0412", "Mechanical", 2, 6.40));
            manager.addStudent(new GraduateStudent("PG101", "Dr. Kavita Joshi", "kavita.j@vityarthi.edu", "20MSC0501", "Data Science", 4, 9.65, "Deep Learning for Genomic Sequences", "Dr. R. Ramanujan"));
        } catch (InvalidDataException e) {
            System.err.println("Sample data initialization notice: " + e.getMessage());
        }
    }
}
