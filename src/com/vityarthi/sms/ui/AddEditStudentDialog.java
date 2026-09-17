package com.vityarthi.sms.ui;

import com.vityarthi.sms.exception.InvalidDataException;
import com.vityarthi.sms.model.GraduateStudent;
import com.vityarthi.sms.model.Student;
import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class AddEditStudentDialog extends JDialog {

    private JTextField nameTxt;
    private JTextField emailTxt;
    private JTextField rollTxt;
    private JComboBox<String> deptCombo;
    private JSpinner yearSpinner;
    private JTextField cgpaTxt;
    private JCheckBox isGradCheckBox;
    private JTextField topicTxt;
    private JTextField advisorTxt;

    private boolean succeeded = false;
    private Student resultStudent = null;

    public AddEditStudentDialog(Frame owner, Student existingStudent) {
        super(owner, existingStudent == null ? "Add New Student" : "Edit Student Details", true);
        setLayout(new BorderLayout(10, 10));
        setSize(420, 480);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameTxt = new JTextField(20);
        emailTxt = new JTextField(20);
        rollTxt = new JTextField(20);

        String[] departments = {"Computer Science", "Information Technology", "Electronics & Comm", "Electrical", "Mechanical", "Civil", "Data Science"};
        deptCombo = new JComboBox<>(departments);

        yearSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        cgpaTxt = new JTextField(20);

        isGradCheckBox = new JCheckBox("Postgraduate / Research Student");
        topicTxt = new JTextField(20);
        advisorTxt = new JTextField(20);

        topicTxt.setEnabled(false);
        advisorTxt.setEnabled(false);

        isGradCheckBox.addActionListener(e -> {
            boolean sel = isGradCheckBox.isSelected();
            topicTxt.setEnabled(sel);
            advisorTxt.setEnabled(sel);
        });

        int row = 0;
        addFormRow(formPanel, gbc, row++, "Full Name:", nameTxt);
        addFormRow(formPanel, gbc, row++, "Email Address:", emailTxt);
        addFormRow(formPanel, gbc, row++, "Roll Number:", rollTxt);
        addFormRow(formPanel, gbc, row++, "Department:", deptCombo);
        addFormRow(formPanel, gbc, row++, "Academic Year:", yearSpinner);
        addFormRow(formPanel, gbc, row++, "CGPA (0.0 - 10.0):", cgpaTxt);

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(isGradCheckBox, gbc);

        gbc.gridwidth = 1;
        addFormRow(formPanel, gbc, row++, "Research Topic:", topicTxt);
        addFormRow(formPanel, gbc, row++, "Advisor Name:", advisorTxt);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton saveBtn = new JButton("Save Student");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.setBackground(new Color(63, 81, 181));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        saveBtn.addActionListener(e -> onSave(existingStudent));
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Pre-fill if editing
        if (existingStudent != null) {
            nameTxt.setText(existingStudent.getName());
            emailTxt.setText(existingStudent.getEmail());
            rollTxt.setText(existingStudent.getRollNumber());
            rollTxt.setEditable(false); // Roll number acts as key
            deptCombo.setSelectedItem(existingStudent.getDepartment());
            yearSpinner.setValue(existingStudent.getAcademicYear());
            cgpaTxt.setText(String.valueOf(existingStudent.getCGPA()));

            if (existingStudent instanceof GraduateStudent grad) {
                isGradCheckBox.setSelected(true);
                topicTxt.setEnabled(true);
                advisorTxt.setEnabled(true);
                topicTxt.setText(grad.getResearchTopic());
                advisorTxt.setText(grad.getAdvisorName());
            }
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private void onSave(Student existingStudent) {
        try {
            String name = nameTxt.getText().trim();
            String email = emailTxt.getText().trim();
            String roll = rollTxt.getText().trim();
            String dept = (String) deptCombo.getSelectedItem();
            int year = (Integer) yearSpinner.getValue();

            double cgpa = 0.0;
            try {
                cgpa = Double.parseDouble(cgpaTxt.getText().trim());
            } catch (NumberFormatException e) {
                throw new InvalidDataException("CGPA must be a valid decimal number.");
            }

            String id = existingStudent != null ? existingStudent.getId() : UUID.randomUUID().toString().substring(0, 8);

            if (isGradCheckBox.isSelected()) {
                String topic = topicTxt.getText().trim();
                String advisor = advisorTxt.getText().trim();
                resultStudent = new GraduateStudent(id, name, email, roll, dept, year, cgpa, topic, advisor);
            } else {
                resultStudent = new Student(id, name, email, roll, dept, year, cgpa);
            }

            succeeded = true;
            dispose();
        } catch (InvalidDataException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public Student getResultStudent() {
        return resultStudent;
    }
}
