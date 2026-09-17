================================================================================
          VITYARTHI STUDENT MANAGEMENT & GRADE ANALYSIS SYSTEM (JAVA)
================================================================================

1. PROJECT OVERVIEW
-------------------
The Student Management & Grade Analysis System is a comprehensive, multi-layered 
Java desktop and CLI application built specifically for the Vityarthi Flipped 
Course Evaluation. 

The application enables educational institutions to maintain student academic 
records, calculate Grade Point Averages (CGPA), perform class-wide analytics, 
generate statistical distributions, and export official summary reports.

2. KEY FEATURES & HIGHLIGHTS
----------------------------
* Dual Interface Architecture:
  - Graphical User Interface (Swing Desktop App) with search, filter, data table,
    dialog forms, and dynamic Graphics2D grade distribution bar charts.
  - Interactive Command Line Interface (CLI) launched with `--cli`.

* Object-Oriented Principles (OOP Core):
  - Encapsulation: Strict field validation for Email, Roll Number, and CGPA bounds.
  - Inheritance: Abstract base class `Person` extended by `Student` and `GraduateStudent`.
  - Polymorphism: Dynamic academic standing rules and role descriptions.
  - Abstraction: Pluggable storage architecture via `StorageService` interface.
  - Custom Exceptions: `StudentNotFoundException` and `InvalidDataException`.

* Data Persistence & File I/O:
  - Auto-saves student records in CSV format (`data/students.csv`).
  - Report Exporter generates formatted `.txt` academic summaries.

3. HOW TO BUILD AND RUN
-----------------------
Directory: StudentManagementSystem/

[Step 1] Open Command Prompt / PowerShell in project folder.

[Step 2] Compile the project:
   .\build.bat

[Step 3] Run Graphical UI (Swing Desktop App):
   .\run.bat

[Step 4] Run Command Line CLI Mode:
   .\run.bat --cli

4. FILE & PACKAGE STRUCTURE
---------------------------
com.vityarthi.sms/
  ├── Main.java                        (Launcher Entrypoint)
  ├── model/
  │   ├── Person.java                  (Abstract Base Class)
  │   ├── Student.java                 (Undergraduate Class)
  │   ├── GraduateStudent.java         (Postgraduate Class)
  │   └── CourseGrade.java             (Course Grade Model)
  ├── service/
  │   ├── StorageService.java          (Persistence Contract Interface)
  │   ├── FileStorageService.java      (CSV Data Storage Implementation)
  │   ├── StudentManager.java          (Core CRUD & Search Service)
  │   └── GradeAnalyzer.java           (Statistical Analytics Engine)
  ├── ui/
  │   ├── MainFrame.java               (Swing GUI Window)
  │   ├── StudentTableModel.java       (JTable Data Model)
  │   ├── AddEditStudentDialog.java    (Swing Input Modal Form)
  │   ├── AnalyticsPanel.java          (Graphics2D Bar Chart Dashboard)
  │   └── ConsoleUI.java               (Interactive CLI Menu)
  └── util/
      ├── DataExporter.java            (Summary Report Exporter)
      └── ValidationUtils.java         (Regex Validator Utilities)

================================================================================
GitHub Repository: https://github.com/computeani119-bit/vithyarthi-_java
================================================================================
