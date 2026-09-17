package com.vityarthi.sms.ui;

import com.vityarthi.sms.model.Student;
import com.vityarthi.sms.service.GradeAnalyzer;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AnalyticsPanel extends JPanel {

    private List<Student> students;

    private JLabel avgGpaLabel;
    private JLabel totalStudentsLabel;
    private JLabel topPerformerLabel;
    private JLabel passRateLabel;
    private ChartCanvas chartCanvas;

    public AnalyticsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));

        // Header / Summary Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);

        avgGpaLabel = createStatCard("Class Average CGPA", "0.00", new Color(63, 81, 181), cardsPanel);
        totalStudentsLabel = createStatCard("Total Enrolled", "0", new Color(0, 150, 136), cardsPanel);
        passRateLabel = createStatCard("Pass Rate (CGPA >= 5)", "0%", new Color(76, 175, 80), cardsPanel);
        topPerformerLabel = createStatCard("Highest CGPA", "0.00", new Color(255, 152, 0), cardsPanel);

        add(cardsPanel, BorderLayout.NORTH);

        // Chart Canvas
        chartCanvas = new ChartCanvas();
        chartCanvas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        add(chartCanvas, BorderLayout.CENTER);
    }

    private JLabel createStatCard(String title, String value, Color themeColor, JPanel parent) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, themeColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 234, 240)),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12))));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLbl.setForeground(new Color(100, 110, 120));

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        valLbl.setForeground(new Color(30, 40, 50));

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLbl, BorderLayout.CENTER);
        parent.add(card);

        return valLbl;
    }

    public void updateData(List<Student> students) {
        this.students = students;

        if (students == null || students.isEmpty()) {
            avgGpaLabel.setText("0.00");
            totalStudentsLabel.setText("0");
            passRateLabel.setText("0%");
            topPerformerLabel.setText("0.00");
        } else {
            double avg = GradeAnalyzer.calculateClassAverageCGPA(students);
            double highest = GradeAnalyzer.getHighestCGPA(students);
            int passCount = GradeAnalyzer.getPassCount(students, 5.0);
            int passPct = (int) Math.round(((double) passCount / students.size()) * 100.0);

            avgGpaLabel.setText(String.format("%.2f", avg));
            totalStudentsLabel.setText(String.valueOf(students.size()));
            passRateLabel.setText(passPct + "%");
            topPerformerLabel.setText(String.format("%.2f", highest));
        }

        chartCanvas.setStudents(students);
        chartCanvas.repaint();
    }

    // Custom Graphics2D Bar Chart Component
    private static class ChartCanvas extends JPanel {
        private List<Student> students;

        public ChartCanvas() {
            setBackground(Color.WHITE);
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Title
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.setColor(new Color(40, 50, 60));
            g2.drawString("Grade Distribution Breakdown", 20, 30);

            if (students == null || students.isEmpty()) {
                g2.setFont(new Font("SansSerif", Font.ITALIC, 13));
                g2.setColor(Color.GRAY);
                g2.drawString("No student records available to render analytics.", width / 3, height / 2);
                return;
            }

            Map<String, Integer> dist = GradeAnalyzer.getGradeDistribution(students);
            String[] brackets = {"9.0 - 10.0", "8.0 - 8.9", "7.0 - 7.9", "6.0 - 6.9", "< 6.0"};
            Color[] barColors = {
                new Color(76, 175, 80),   // Green
                new Color(33, 150, 243),  // Blue
                new Color(0, 188, 212),   // Cyan
                new Color(255, 152, 0),  // Orange
                new Color(244, 67, 54)    // Red
            };

            int maxCount = 1;
            for (int count : dist.values()) {
                if (count > maxCount) maxCount = count;
            }

            int chartX = 60;
            int chartY = 60;
            int chartW = width - 100;
            int chartH = height - 110;

            // Draw Y-Axis line & grid
            g2.setColor(new Color(230, 235, 240));
            g2.drawLine(chartX, chartY, chartX, chartY + chartH);
            g2.drawLine(chartX, chartY + chartH, chartX + chartW, chartY + chartH);

            int numBars = brackets.length;
            int barWidth = (chartW / numBars) - 30;
            int gap = 30;

            for (int i = 0; i < numBars; i++) {
                String bracket = brackets[i];
                int count = dist.getOrDefault(bracket, 0);

                int barHeight = (int) (((double) count / maxCount) * (chartH - 30));
                int x = chartX + gap / 2 + i * (barWidth + gap);
                int y = chartY + chartH - barHeight;

                // Draw Bar
                g2.setColor(barColors[i % barColors.length]);
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

                // Draw Count label on top of bar
                g2.setColor(new Color(50, 50, 50));
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString(String.valueOf(count), x + barWidth / 2 - 4, y - 5);

                // Draw X-Axis Bracket label
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.setColor(new Color(100, 100, 100));
                g2.drawString(bracket, x + (barWidth / 2) - 25, chartY + chartH + 20);
            }
        }
    }
}
