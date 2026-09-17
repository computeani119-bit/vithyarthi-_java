package com.vityarthi.sms.util;

import com.vityarthi.sms.model.Student;
import com.vityarthi.sms.service.GradeAnalyzer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DataExporter {

    public static String exportSummaryReport(List<Student> students, String targetFilePath) throws IOException {
        File file = new File(targetFilePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("=========================================================");
            bw.newLine();
            bw.write("         VITYARTHI STUDENT ACADEMIC REPORT               ");
            bw.newLine();
            bw.write("=========================================================");
            bw.newLine();
            bw.write("Generated On: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            bw.newLine();
            bw.write("Total Enrolled Students: " + students.size());
            bw.newLine();
            bw.write(String.format("Overall Class Average CGPA: %.2f", GradeAnalyzer.calculateClassAverageCGPA(students)));
            bw.newLine();
            bw.write(String.format("Highest CGPA: %.2f | Lowest CGPA: %.2f", 
                    GradeAnalyzer.getHighestCGPA(students), GradeAnalyzer.getLowestCGPA(students)));
            bw.newLine();
            bw.newLine();

            bw.write("--- GRADE DISTRIBUTION SUMMARY ---");
            bw.newLine();
            Map<String, Integer> dist = GradeAnalyzer.getGradeDistribution(students);
            for (Map.Entry<String, Integer> entry : dist.entrySet()) {
                bw.write(String.format("  * %-15s : %d students", entry.getKey(), entry.getValue()));
                bw.newLine();
            }
            bw.newLine();

            bw.write("--- STUDENT ACADEMIC RECORDS ---");
            bw.newLine();
            bw.write(String.format("%-12s | %-22s | %-12s | %-6s | %-20s", 
                    "ROLL NO", "NAME", "DEPARTMENT", "CGPA", "ACADEMIC STANDING"));
            bw.newLine();
            bw.write("----------------------------------------------------------------------------------");
            bw.newLine();

            for (Student s : students) {
                bw.write(String.format("%-12s | %-22s | %-12s | %-6.2f | %-20s",
                        s.getRollNumber(),
                        truncate(s.getName(), 22),
                        truncate(s.getDepartment(), 12),
                        s.getCGPA(),
                        s.getAcademicStanding()));
                bw.newLine();
            }

            bw.write("=========================================================");
            bw.newLine();
            bw.write("End of Report.");
            bw.newLine();
        }

        return file.getAbsolutePath();
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
