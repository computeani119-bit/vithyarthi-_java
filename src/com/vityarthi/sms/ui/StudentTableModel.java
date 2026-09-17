package com.vityarthi.sms.ui;

import com.vityarthi.sms.model.GraduateStudent;
import com.vityarthi.sms.model.Student;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StudentTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Roll Number", "Name", "Email", "Department", "Year", "CGPA", "Standing", "Category"};
    private List<Student> students;

    public StudentTableModel() {
        this.students = new ArrayList<>();
    }

    public void setStudents(List<Student> students) {
        this.students = (students != null) ? students : new ArrayList<>();
        fireTableDataChanged();
    }

    public Student getStudentAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            return students.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student s = students.get(rowIndex);
        switch (columnIndex) {
            case 0: return s.getRollNumber();
            case 1: return s.getName();
            case 2: return s.getEmail();
            case 3: return s.getDepartment();
            case 4: return "Year " + s.getAcademicYear();
            case 5: return String.format("%.2f", s.getCGPA());
            case 6: return s.getAcademicStanding();
            case 7: return (s instanceof GraduateStudent) ? "Postgraduate" : "Undergraduate";
            default: return null;
        }
    }
}
