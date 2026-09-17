# Vityarthi Student Management & Grade Analysis System (Java)

An end-to-end Object-Oriented Java project built for the **Vityarthi Flipped Course Evaluation**.

---

## 🌟 Key Features

1. **Object-Oriented Architecture**
   - **Encapsulation**: Domain models (`Person`, `Student`, `GraduateStudent`, `CourseGrade`) enforce data integrity via private fields, getters/setters, and regex input validation (`ValidationUtils`).
   - **Inheritance**: Abstract base class `Person` extended by `Student`, which is further specialized by `GraduateStudent`.
   - **Polymorphism**: Overridden `getAcademicStanding()` and `getRoleTitle()` methods in subclasses.
   - **Abstraction & Interface Segregation**: `StorageService` interface allowing pluggable persistence (CSV File Storage).
   - **Custom Exceptions**: `StudentNotFoundException` and `InvalidDataException`.

2. **Dual User Interface Modes**
   - **Swing Graphical User Interface (Desktop GUI)**:
     - Search & department filter.
     - Live data table (`JTable`) with custom table model (`StudentTableModel`).
     - Modal dialogs for adding/editing student details (`AddEditStudentDialog`).
     - Custom `Graphics2D` bar chart analytics rendering grade distributions and statistics cards (`AnalyticsPanel`).
     - Export to text reports.
   - **Command Line Interface (CLI)**:
     - Full interactive terminal menu launched with `--cli` flag.

3. **Data Persistence & File I/O**
   - Automatic loading and saving of records to `data/students.csv`.
   - Report generation and export capability (`DataExporter`).

---

## 📁 Directory Structure

```
StudentManagementSystem/
├── src/
│   └── com/vityarthi/sms/
│       ├── Main.java                        # Main Application Entrypoint
│       ├── exception/
│       │   ├── InvalidDataException.java     # Custom validation exception
│       │   └── StudentNotFoundException.java # Custom lookup exception
│       ├── model/
│       │   ├── Person.java                  # Abstract base person model
│       │   ├── Student.java                 # Undergraduate student class
│       │   ├── GraduateStudent.java         # Postgraduate/Research student class
│       │   └── CourseGrade.java             # Individual course grade model
│       ├── service/
│       │   ├── StorageService.java          # Storage contract interface
│       │   ├── FileStorageService.java      # CSV File I/O implementation
│       │   ├── StudentManager.java          # Core CRUD & search service
│       │   └── GradeAnalyzer.java           # Statistical analytics engine
│       ├── ui/
│       │   ├── MainFrame.java               # Main Swing GUI window
│       │   ├── StudentTableModel.java       # Swing table model
│       │   ├── AddEditStudentDialog.java    # Swing modal form dialog
│       │   ├── AnalyticsPanel.java          # Custom Graphics2D chart panel
│       │   └── ConsoleUI.java               # Interactive terminal CLI UI
│       └── util/
│           ├── DataExporter.java            # Academic summary report exporter
│           └── ValidationUtils.java         # Field regex & range validator
├── data/
│   └── students.csv                         # Auto-persisted dataset
├── build.bat                                # Windows compilation script
├── run.bat                                  # Windows execution script
└── README.md                                # Project documentation
```

---

## 🚀 How to Build and Run

### 1. Build the Project
Open PowerShell or Command Prompt in the `StudentManagementSystem` directory and run:
```cmd
.\build.bat
```

### 2. Run in Graphical UI Mode (Swing GUI)
```cmd
.\run.bat
```

### 3. Run in Command Line Mode (CLI Terminal)
```cmd
.\run.bat --cli
```

---

## 📊 Sample Output & Verification

The project automatically initializes with a set of sample student records across various departments (Computer Science, Data Science, Mechanical, ECE) so that evaluation features can be tested immediately upon launch.

---

## 🎓 Evaluation Alignment for Vityarthi Course
This project satisfies all requirements for Java course submission:
- Multi-file modular package layout (`com.vityarthi.sms.*`).
- Full implementation of OOP Pillar concepts.
- File handling (reading/writing CSV datasets).
- Graphical UI + CLI alternatives.
- Clean Exception Handling.
