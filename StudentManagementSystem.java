import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Student implements Serializable {

    private String name;
    private String rollNumber;
    private String department;
    private String email;
    private double grade;

    public Student(String name, String rollNumber,
                   String department, String email,
                   double grade) {

        this.name = name;
        this.rollNumber = rollNumber;
        this.department = department;
        this.email = email;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public double getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}

public class StudentManagementSystem extends JFrame {

    private JTextField txtName;
    private JTextField txtRoll;
    private JTextField txtDepartment;
    private JTextField txtEmail;
    private JTextField txtGrade;
    private JTextField txtSearch;

    private JLabel totalStudentsLabel;
    private JLabel avgGradeLabel;
    private JLabel highestGradeLabel;

    private JTable table;
    private DefaultTableModel model;

    private ArrayList<Student> students = new ArrayList<>();

    private final String FILE_NAME = "students.dat";

    public StudentManagementSystem() {

        setTitle("Student Record Management System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel header = new JLabel(
                "Student Management Dashboard",
                SwingConstants.CENTER);

        header.setFont(new Font("Calibri", Font.BOLD, 30));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(new Color(0, 102, 204));
        header.setPreferredSize(new Dimension(100, 70));

        mainPanel.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(13, 1, 8, 8));
        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Student Information"));

        txtName = new JTextField();
        txtRoll = new JTextField();
        txtDepartment = new JTextField();
        txtEmail = new JTextField();
        txtGrade = new JTextField();

        formPanel.add(new JLabel("Student Name"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Roll Number"));
        formPanel.add(txtRoll);

        formPanel.add(new JLabel("Department"));
        formPanel.add(txtDepartment);

        formPanel.add(new JLabel("Email"));
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Grade"));
        formPanel.add(txtGrade);

        JButton addBtn = new JButton("Add Student");
        JButton updateBtn = new JButton("Update Record");
        JButton deleteBtn = new JButton("Delete Record");

        addBtn.setBackground(new Color(46,125,50));
        updateBtn.setBackground(new Color(255,143,0));
        deleteBtn.setBackground(new Color(198,40,40));

        addBtn.setForeground(Color.WHITE);
        updateBtn.setForeground(Color.WHITE);
        deleteBtn.setForeground(Color.WHITE);

        formPanel.add(addBtn);
        formPanel.add(updateBtn);
        formPanel.add(deleteBtn);

        model = new DefaultTableModel();

        model.addColumn("Name");
        model.addColumn("Roll No");
        model.addColumn("Department");
        model.addColumn("Email");
        model.addColumn("Grade");

        table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(new Font("Calibri", Font.PLAIN, 15));

        JScrollPane scrollPane =
                new JScrollPane(table);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(8,1,10,10));

        totalStudentsLabel =
                new JLabel("Total Students : 0");

        avgGradeLabel =
                new JLabel("Average Grade : 0");

        highestGradeLabel =
                new JLabel("Highest Grade : 0");

        txtSearch = new JTextField();

        JButton searchBtn =
                new JButton("Search Student");

        JButton displayBtn =
                new JButton("Display All");

        rightPanel.add(
                new JLabel("Student Statistics"));

        rightPanel.add(totalStudentsLabel);
        rightPanel.add(avgGradeLabel);
        rightPanel.add(highestGradeLabel);

        rightPanel.add(
                new JLabel("Search Roll Number"));

        rightPanel.add(txtSearch);
        rightPanel.add(searchBtn);
        rightPanel.add(displayBtn);

        JLabel footer = new JLabel(
                "© 2026 Student Management System",
                SwingConstants.CENTER);

        footer.setOpaque(true);
        footer.setForeground(Color.WHITE);
        footer.setBackground(
                new Color(0,102,204));

        mainPanel.add(formPanel,
                BorderLayout.WEST);

        mainPanel.add(scrollPane,
                BorderLayout.CENTER);

        mainPanel.add(rightPanel,
                BorderLayout.EAST);

        mainPanel.add(footer,
                BorderLayout.SOUTH);

        add(mainPanel);

        loadStudents();
        updateTable();
        updateStats();
                addBtn.addActionListener(e -> addStudent());

        updateBtn.addActionListener(e -> updateStudent());

        deleteBtn.addActionListener(e -> deleteStudent());

        searchBtn.addActionListener(e -> searchStudent());

        displayBtn.addActionListener(e -> updateTable());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int row = table.getSelectedRow();

                txtName.setText(
                        model.getValueAt(row, 0).toString());

                txtRoll.setText(
                        model.getValueAt(row, 1).toString());

                txtDepartment.setText(
                        model.getValueAt(row, 2).toString());

                txtEmail.setText(
                        model.getValueAt(row, 3).toString());

                txtGrade.setText(
                        model.getValueAt(row, 4).toString());
            }
        });

        setVisible(true);
    }

    private void addStudent() {

        if (txtName.getText().trim().isEmpty() ||
            txtRoll.getText().trim().isEmpty() ||
            txtDepartment.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtGrade.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill all fields.");
            return;
        }

        try {

            double grade =
                    Double.parseDouble(txtGrade.getText());

            Student student = new Student(
                    txtName.getText(),
                    txtRoll.getText(),
                    txtDepartment.getText(),
                    txtEmail.getText(),
                    grade);

            students.add(student);

            saveStudents();
            updateTable();
            updateStats();
            clearFields();

            JOptionPane.showMessageDialog(this,
                    "Student added successfully.");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Grade must be numeric.");
        }
    }

    private void updateStudent() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(this,
                    "Select a student first.");
            return;
        }

        try {

            students.get(row).setName(
                    txtName.getText());

            students.get(row).setDepartment(
                    txtDepartment.getText());

            students.get(row).setEmail(
                    txtEmail.getText());

            students.get(row).setGrade(
                    Double.parseDouble(
                            txtGrade.getText()));

            saveStudents();
            updateTable();
            updateStats();

            JOptionPane.showMessageDialog(this,
                    "Record updated successfully.");

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Invalid data.");
        }
    }

    private void deleteStudent() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(this,
                    "Select a student first.");
            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete selected student?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            students.remove(row);

            saveStudents();
            updateTable();
            updateStats();

            clearFields();

            JOptionPane.showMessageDialog(this,
                    "Record deleted.");
        }
    }

    private void searchStudent() {

        String roll =
                txtSearch.getText().trim();

        model.setRowCount(0);

        for (Student s : students) {

            if (s.getRollNumber()
                    .equalsIgnoreCase(roll)) {

                model.addRow(new Object[]{
                        s.getName(),
                        s.getRollNumber(),
                        s.getDepartment(),
                        s.getEmail(),
                        s.getGrade()
                });
            }
        }
    }

    private void updateTable() {

        model.setRowCount(0);

        for (Student s : students) {

            model.addRow(new Object[]{
                    s.getName(),
                    s.getRollNumber(),
                    s.getDepartment(),
                    s.getEmail(),
                    s.getGrade()
            });
        }
    }

    private void updateStats() {

        totalStudentsLabel.setText(
                "Total Students : " + students.size());

        double total = 0;
        double highest = 0;

        for (Student s : students) {

            total += s.getGrade();

            if (s.getGrade() > highest) {
                highest = s.getGrade();
            }
        }

        double average = 0;

        if (!students.isEmpty()) {
            average = total / students.size();
        }

        avgGradeLabel.setText(
                String.format(
                        "Average Grade : %.2f",
                        average));

        highestGradeLabel.setText(
                "Highest Grade : " + highest);
    }

    private void clearFields() {

        txtName.setText("");
        txtRoll.setText("");
        txtDepartment.setText("");
        txtEmail.setText("");
        txtGrade.setText("");
    }

    private void saveStudents() {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(FILE_NAME));

            out.writeObject(students);

            out.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadStudents() {

        try {

            ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(FILE_NAME));

            students =
                    (ArrayList<Student>) in.readObject();

            in.close();

        } catch (Exception e) {

            students = new ArrayList<>();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new StudentManagementSystem());
    }
}