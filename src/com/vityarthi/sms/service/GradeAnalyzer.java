package com.vityarthi.sms.service;

import com.vityarthi.sms.model.Student;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeAnalyzer {

    public static double calculateClassAverageCGPA(List<Student> students) {
        if (students == null || students.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Student s : students) {
            sum += s.getCGPA();
        }
        return Math.round((sum / students.size()) * 100.0) / 100.0;
    }

    public static double getHighestCGPA(List<Student> students) {
        if (students == null || students.isEmpty()) return 0.0;
        double max = 0.0;
        for (Student s : students) {
            if (s.getCGPA() > max) max = s.getCGPA();
        }
        return max;
    }

    public static double getLowestCGPA(List<Student> students) {
        if (students == null || students.isEmpty()) return 0.0;
        double min = 10.0;
        for (Student s : students) {
            if (s.getCGPA() < min) min = s.getCGPA();
        }
        return min;
    }

    /**
     * Categorizes CGPA into grade brackets:
     * "9.0 - 10.0 (Outstanding)"
     * "8.0 - 8.9 (Excellent)"
     * "7.0 - 7.9 (Good)"
     * "6.0 - 6.9 (Average)"
     * "< 6.0 (Needs Improvement)"
     */
    public static Map<String, Integer> getGradeDistribution(List<Student> students) {
        Map<String, Integer> dist = new HashMap<>();
        dist.put("9.0 - 10.0", 0);
        dist.put("8.0 - 8.9", 0);
        dist.put("7.0 - 7.9", 0);
        dist.put("6.0 - 6.9", 0);
        dist.put("< 6.0", 0);

        if (students == null) return dist;

        for (Student s : students) {
            double gpa = s.getCGPA();
            if (gpa >= 9.0) dist.put("9.0 - 10.0", dist.get("9.0 - 10.0") + 1);
            else if (gpa >= 8.0) dist.put("8.0 - 8.9", dist.get("8.0 - 8.9") + 1);
            else if (gpa >= 7.0) dist.put("7.0 - 7.9", dist.get("7.0 - 7.9") + 1);
            else if (gpa >= 6.0) dist.put("6.0 - 6.9", dist.get("6.0 - 6.9") + 1);
            else dist.put("< 6.0", dist.get("< 6.0") + 1);
        }
        return dist;
    }

    public static Map<String, Double> getDepartmentAverageCGPA(List<Student> students) {
        Map<String, Double> sumMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        if (students != null) {
            for (Student s : students) {
                String dept = s.getDepartment();
                sumMap.put(dept, sumMap.getOrDefault(dept, 0.0) + s.getCGPA());
                countMap.put(dept, countMap.getOrDefault(dept, 0) + 1);
            }
        }

        Map<String, Double> avgMap = new HashMap<>();
        for (String dept : sumMap.keySet()) {
            double avg = sumMap.get(dept) / countMap.get(dept);
            avgMap.put(dept, Math.round(avg * 100.0) / 100.0);
        }
        return avgMap;
    }

    public static int getPassCount(List<Student> students, double passThreshold) {
        if (students == null) return 0;
        int count = 0;
        for (Student s : students) {
            if (s.getCGPA() >= passThreshold) count++;
        }
        return count;
    }
}
