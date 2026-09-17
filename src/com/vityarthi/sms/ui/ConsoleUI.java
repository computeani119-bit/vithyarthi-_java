package com.vityarthi.sms.ui;

import com.vityarthi.sms.exception.InvalidDataException;
import com.vityarthi.sms.exception.StudentNotFoundException;
import com.vityarthi.sms.model.GraduateStudent;
import com.vityarthi.sms.model.Student;
import com.vityarthi.sms.service.GradeAnalyzer;
import com.vityarthi.sms.service.StudentManager;
import com.vityarthi.sms.util.DataExporter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {

    private final StudentManager studentManager;
    private final Scanner scanner;

    public ConsoleUI(StudentManager studentManager) {
        this.studentManager = studentManager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("==================================================================");
        System.out.println("   VITYARTHI STUDENT MANAGEMENT & GRADE ANALYSIS SYSTEM (CLI)     ");
        System.out.println("==================================================================");

        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Enter choice (1-8): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> listAllStudents();
                case "2" -> addStudent();
                case "3" -> searchStudent();
                case "4" -> editStudent();
                case "5" -> deleteStudent();
                case "6" -> showAnalytics();
                case "7" -> exportReport();
                case "8" -> {
                    exit = true;
                    System.out.println("\nSaving data and exiting Vityarthi SMS. Goodbye!");
                }
                default -> System.out.println("Invalid selection. Please enter a number between 1 and 8.");
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("\n---------------- MAIN MENU ----------------");
        System.out.println("1. List All Students");
        System.out.println("2. Add New Student Record");
        System.out.println("3. Search Student by Name / Roll / Dept");
        System.out.println("4. Update Student Details");
        System.out.println("5. Delete Student Record");
        System.out.println("6. Display Class Analytics & Grade Distribution");
        System.out.println("7. Export Academic Summary Report");
        System.out.println("8. Exit Application");
        System.out.println("-------------------------------------------");
    }

    private void listAllStudents() {
        List<Student> students = studentManager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("\n[Info] No student records found.");
            return;
        }
        System.out.println("\n--- ENROLLED STUDENT RECORDS ---");
        System.out.printf("%-10s | %-20s | %-16s | %-6s | %-18s%n",
                "ROLL NO", "NAME", "DEPARTMENT", "CGPA", "CATEGORY");
        System.out.println("-------------------------------------------------------------------------");
        for (Student s : students) {
            String cat = (s instanceof GraduateStudent) ? "Postgraduate" : "Undergraduate";
            System.out.printf("%-10s | %-20s | %-16s | %-6.2f | %-18s%n",
                    s.getRollNumber(), s.getName(), s.getDepartment(), s.getCGPA(), cat);
        }
    }

    private void addStudent() {
        System.out.println("\n--- ADD NEW STUDENT ---");
        try {
            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Email Address: ");
            String email = scanner.nextLine();

            System.out.print("Enter Roll Number (e.g. 21BCE0100): ");
            String roll = scanner.nextLine();

            System.out.print("Enter Department (e.g. Computer Science): ");
            String dept = scanner.nextLine();

            System.out.print("Enter Academic Year (1-5): ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter CGPA (0.0 - 10.0): ");
            double cgpa = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Is Postgraduate / Research Student? (y/n): ");
            String isGrad = scanner.nextLine().trim().toLowerCase();

            String id = UUID.randomUUID().toString().substring(0, 8);
            Student s;
            if (isGrad.startsWith("y")) {
                System.out.print("Enter Research Topic: ");
                String topic = scanner.nextLine();
                System.out.print("Enter Advisor Name: ");
                String advisor = scanner.nextLine();
                s = new GraduateStudent(id, name, email, roll, dept, year, cgpa, topic, advisor);
            } else {
                s = new Student(id, name, email, roll, dept, year, cgpa);
            }

            studentManager.addStudent(s);
            System.out.println("\n[Success] Student record added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid numerical input entered.");
        } catch (InvalidDataException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private void searchStudent() {
        System.out.print("\nEnter search query (Name, Roll, Dept): ");
        String q = scanner.nextLine();
        List<Student> results = studentManager.searchStudents(q);
        if (results.isEmpty()) {
            System.out.println("[Info] No matching records found for query: " + q);
        } else {
            System.out.println("\nFound " + results.size() + " matching record(s):");
            for (Student s : results) {
                System.out.println("  * " + s);
            }
        }
    }

    private void editStudent() {
        System.out.print("\nEnter Roll Number of student to update: ");
        String roll = scanner.nextLine().trim();

        try {
            Student existing = studentManager.getStudentByRollNumber(roll);
            System.out.println("Editing Student: " + existing.getName());

            System.out.print("New Full Name [" + existing.getName() + "]: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) name = existing.getName();

            System.out.print("New Email [" + existing.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (email.trim().isEmpty()) email = existing.getEmail();

            System.out.print("New Department [" + existing.getDepartment() + "]: ");
            String dept = scanner.nextLine();
            if (dept.trim().isEmpty()) dept = existing.getDepartment();

            System.out.print("New CGPA [" + existing.getCGPA() + "]: ");
            String cgpaStr = scanner.nextLine().trim();
            double cgpa = cgpaStr.isEmpty() ? existing.getCGPA() : Double.parseDouble(cgpaStr);

            Student updated = new Student(existing.getId(), name, email, roll, dept, existing.getAcademicYear(), cgpa);
            studentManager.updateStudent(roll, updated);
            System.out.println("\n[Success] Student updated successfully.");
        } catch (StudentNotFoundException | InvalidDataException | NumberFormatException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private void deleteStudent() {
        System.out.print("\nEnter Roll Number of student to delete: ");
        String roll = scanner.nextLine().trim();
        try {
            boolean removed = studentManager.deleteStudent(roll);
            if (removed) {
                System.out.println("[Success] Student record deleted.");
            }
        } catch (StudentNotFoundException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private void showAnalytics() {
        List<Student> students = studentManager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("[Info] No student data to analyze.");
            return;
        }

        System.out.println("\n--- CLASS PERFORMANCE ANALYTICS ---");
        System.out.printf("Total Enrolled Students : %d%n", students.size());
        System.out.printf("Class Average CGPA      : %.2f%n", GradeAnalyzer.calculateClassAverageCGPA(students));
        System.out.printf("Highest CGPA            : %.2f%n", GradeAnalyzer.getHighestCGPA(students));
        System.out.printf("Lowest CGPA             : %.2f%n", GradeAnalyzer.getLowestCGPA(students));
        System.out.println();

        System.out.println("Grade Distribution:");
        Map<String, Integer> dist = GradeAnalyzer.getGradeDistribution(students);
        for (Map.Entry<String, Integer> entry : dist.entrySet()) {
            System.out.printf("  %-15s : %d student(s)%n", entry.getKey(), entry.getValue());
        }
    }

    private void exportReport() {
        List<Student> students = studentManager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("[Info] No student records available to export.");
            return;
        }
        String fileName = "data/Vityarthi_Academic_Report.txt";
        try {
            String path = DataExporter.exportSummaryReport(students, fileName);
            System.out.println("\n[Success] Report exported to: " + path);
        } catch (IOException e) {
            System.out.println("[Error] Failed to export report: " + e.getMessage());
        }
    }
}
