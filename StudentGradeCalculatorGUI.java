import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentGradeCalculatorGUI extends JFrame implements ActionListener {

    private JTextField nameField;
    private JTextField[] markFields = new JTextField[5];

    private JButton calculateBtn, resetBtn;
    private JTextArea resultArea;

    public StudentGradeCalculatorGUI() {

        setTitle("🎓 Student Grade Calculator");
        setSize(700, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(44, 62, 80),
                        0, getHeight(), new Color(52, 152, 219));

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(null);

        JLabel heading = new JLabel("STUDENT GRADE CALCULATOR");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);
        heading.setBounds(120, 20, 500, 40);
        panel.add(heading);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);

        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(60, 90, 150, 30);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        nameField.setBounds(220, 90, 250, 35);
        nameField.setBorder(BorderFactory.createLineBorder(
                new Color(52, 152, 219), 2));
        panel.add(nameField);

        for (int i = 0; i < 5; i++) {

            JLabel label = new JLabel("Subject " + (i + 1) + " Marks:");
            label.setFont(labelFont);
            label.setForeground(Color.WHITE);
            label.setBounds(60, 150 + (i * 50), 150, 30);
            panel.add(label);

            markFields[i] = new JTextField();

            markFields[i].setFont(
                    new Font("Segoe UI", Font.PLAIN, 15));

            markFields[i].setBounds(
                    220, 150 + (i * 50), 120, 35);

            markFields[i].setBorder(
                    BorderFactory.createLineBorder(
                            new Color(52, 152, 219), 2));

            panel.add(markFields[i]);
        }

        calculateBtn = new JButton("CALCULATE");
        calculateBtn.setBounds(120, 430, 180, 45);
        calculateBtn.setFont(
                new Font("Segoe UI", Font.BOLD, 16));
        calculateBtn.setBackground(
                new Color(46, 204, 113));
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFocusPainted(false);
        calculateBtn.addActionListener(this);
        panel.add(calculateBtn);

        resetBtn = new JButton("RESET");
        resetBtn.setBounds(380, 430, 180, 45);
        resetBtn.setFont(
                new Font("Segoe UI", Font.BOLD, 16));
        resetBtn.setBackground(
                new Color(231, 76, 60));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFocusPainted(false);

        resetBtn.addActionListener(e -> resetFields());

        panel.add(resetBtn);

        resultArea = new JTextArea();

        resultArea.setEditable(false);
        resultArea.setFont(
                new Font("Consolas", Font.BOLD, 16));

        resultArea.setBackground(
                new Color(30, 30, 30));

        resultArea.setForeground(
                new Color(0, 255, 127));

        resultArea.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10));

        JScrollPane scrollPane =
                new JScrollPane(resultArea);

        scrollPane.setBounds(50, 500, 580, 90);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(52, 152, 219), 2));

        panel.add(scrollPane);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {

            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter student name.");
                return;
            }

            double total = 0;

            for (int i = 0; i < 5; i++) {

                double mark =
                        Double.parseDouble(
                                markFields[i].getText());

                if (mark < 0 || mark > 100) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Marks must be between 0 and 100",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                total += mark;
            }

            double percentage = total / 5;

            String grade;

            if (percentage >= 90)
                grade = "A+";
            else if (percentage >= 80)
                grade = "A";
            else if (percentage >= 70)
                grade = "B";
            else if (percentage >= 60)
                grade = "C";
            else if (percentage >= 40)
                grade = "D";
            else
                grade = "F";

            if (grade.equals("A+") || grade.equals("A")) {
                resultArea.setForeground(Color.GREEN);
            } else if (grade.equals("B")
                    || grade.equals("C")) {
                resultArea.setForeground(Color.ORANGE);
            } else {
                resultArea.setForeground(Color.RED);
            }

            resultArea.setText(
                    "════════ STUDENT REPORT ════════\n\n" +
                    "Student Name : " + name +
                    "\n\nTotal Marks  : " + total + " / 500" +
                    "\nPercentage   : " +
                    String.format("%.2f", percentage) + "%" +
                    "\nGrade        : " + grade +
                    "\nStatus       : " +
                    (percentage >= 40 ? "PASS ✓" : "FAIL ✗"));

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid marks only!",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFields() {

        nameField.setText("");

        for (JTextField field : markFields) {
            field.setText("");
        }

        resultArea.setText("");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new StudentGradeCalculatorGUI());
    }
}