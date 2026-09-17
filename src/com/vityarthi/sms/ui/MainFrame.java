package com.vityarthi.sms.ui;

import com.vityarthi.sms.exception.InvalidDataException;
import com.vityarthi.sms.exception.StudentNotFoundException;
import com.vityarthi.sms.model.Student;
import com.vityarthi.sms.service.StudentManager;
import com.vityarthi.sms.util.DataExporter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {

    private final StudentManager studentManager;
    private StudentTableModel tableModel;
    private JTable studentTable;
    private JTextField searchTxt;
    private JComboBox<String> deptFilterCombo;
    private AnalyticsPanel analyticsPanel;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;

    public MainFrame(StudentManager studentManager) {
        this.studentManager = studentManager;

        setTitle("Vityarthi Student Management & Grade Analysis System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 620);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);

        initUI();
        refreshData();
    }

    private void initUI() {
        // Top Toolbar / Controls Panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.setBackground(new Color(240, 243, 246));

        // Action buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionsPanel.setOpaque(false);

        JButton addBtn = createStyledButton("Add Student", new Color(63, 81, 181));
        JButton editBtn = createStyledButton("Edit", new Color(0, 150, 136));
        JButton deleteBtn = createStyledButton("Delete", new Color(244, 67, 54));
        JButton exportBtn = createStyledButton("Export Report", new Color(103, 58, 183));

        addBtn.addActionListener(e -> onAddStudent());
        editBtn.addActionListener(e -> onEditStudent());
        deleteBtn.addActionListener(e -> onDeleteStudent());
        exportBtn.addActionListener(e -> onExportReport());

        actionsPanel.add(addBtn);
        actionsPanel.add(editBtn);
        actionsPanel.add(deleteBtn);
        actionsPanel.add(exportBtn);

        // Search & Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterPanel.setOpaque(false);

        searchTxt = new JTextField(15);
        searchTxt.setToolTipText("Search by Name, Roll No, Email...");
        searchTxt.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });

        String[] depts = {"All Departments", "Computer Science", "Information Technology", "Electronics & Comm", "Electrical", "Mechanical", "Civil", "Data Science"};
        deptFilterCombo = new JComboBox<>(depts);
        deptFilterCombo.addActionListener(e -> applyFilters());

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchTxt);
        filterPanel.add(new JLabel("Department:"));
        filterPanel.add(deptFilterCombo);

        topPanel.add(actionsPanel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);

        // Center Tabbed View: Records Table & Analytics Panel
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Table Panel
        tableModel = new StudentTableModel();
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(26);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        studentTable.getTableHeader().setBackground(new Color(230, 235, 240));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        tabbedPane.addTab("Student Records", scrollPane);

        // Analytics Panel
        analyticsPanel = new AnalyticsPanel();
        tabbedPane.addTab("Grade Analytics", analyticsPanel);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                analyticsPanel.updateData(studentManager.getAllStudents());
            }
        });

        // Bottom Status Bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));

        statusLabel = new JLabel("System Ready.");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshData() {
        applyFilters();
    }

    private void applyFilters() {
        String q = searchTxt.getText().trim();
        String dept = (String) deptFilterCombo.getSelectedItem();
        if ("All Departments".equals(dept)) dept = "All";

        List<Student> list;
        if (!q.isEmpty()) {
            list = studentManager.searchStudents(q);
        } else {
            list = studentManager.filterByDepartment(dept);
        }

        tableModel.setStudents(list);
        analyticsPanel.updateData(studentManager.getAllStudents());
        statusLabel.setText("Showing " + list.size() + " student record(s). Total Enrolled: " + studentManager.getAllStudents().size());
    }

    private void onAddStudent() {
        AddEditStudentDialog dialog = new AddEditStudentDialog(this, null);
        dialog.setVisible(true);

        if (dialog.isSucceeded() && dialog.getResultStudent() != null) {
            try {
                studentManager.addStudent(dialog.getResultStudent());
                refreshData();
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (InvalidDataException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Adding Student", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = tableModel.getStudentAt(selectedRow);
        if (student == null) return;

        AddEditStudentDialog dialog = new AddEditStudentDialog(this, student);
        dialog.setVisible(true);

        if (dialog.isSucceeded() && dialog.getResultStudent() != null) {
            try {
                studentManager.updateStudent(student.getRollNumber(), dialog.getResultStudent());
                refreshData();
                JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (StudentNotFoundException | InvalidDataException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Updating Student", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDeleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = tableModel.getStudentAt(selectedRow);
        if (student == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student: " + student.getName() + " (" + student.getRollNumber() + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentManager.deleteStudent(student.getRollNumber());
                refreshData();
                JOptionPane.showMessageDialog(this, "Student deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            } catch (StudentNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Deleting Student", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onExportReport() {
        List<Student> students = studentManager.getAllStudents();
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No student records available to export.", "Export Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Summary Report");
        fileChooser.setSelectedFile(new java.io.File("Vityarthi_Academic_Report.txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                String path = DataExporter.exportSummaryReport(students, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Report exported successfully to:\n" + path, "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to export report: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
