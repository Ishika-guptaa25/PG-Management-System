
package pg.student.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import pg.student.model.Student;
import pg.student.service.StudentService;

public class StudentModule extends JFrame {

    private StudentService service = new StudentService();

    public StudentModule() {

        setTitle("PG Management - Student Module");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JLabel heading = new JLabel("STUDENT MANAGEMENT SYSTEM");
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        add(heading, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View Students");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton searchButton = new JButton("Search Student");
        JButton countButton = new JButton("Student Count");

        panel.add(addButton);
        panel.add(viewButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(countButton);

        add(panel, BorderLayout.CENTER);

        addButton.addActionListener(e -> addStudent());
        viewButton.addActionListener(e -> viewStudents());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        searchButton.addActionListener(e -> searchStudent());
        countButton.addActionListener(e -> showCount());
    }

    private Student getStudentDetails(Student oldStudent) {

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> genderBox =
                new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField addressField = new JTextField();
        JTextField guardianField = new JTextField();
        JTextField guardianPhoneField = new JTextField();

        if (oldStudent != null) {
            nameField.setText(oldStudent.getName());
            emailField.setText(oldStudent.getEmail());
            phoneField.setText(oldStudent.getPhone());
            genderBox.setSelectedItem(oldStudent.getGender());
            addressField.setText(oldStudent.getAddress());
            guardianField.setText(oldStudent.getGuardianName());
            guardianPhoneField.setText(oldStudent.getGuardianPhone());
        }

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));

        form.add(new JLabel("Student Name:"));
        form.add(nameField);

        form.add(new JLabel("Email:"));
        form.add(emailField);

        form.add(new JLabel("Phone:"));
        form.add(phoneField);

        form.add(new JLabel("Gender:"));
        form.add(genderBox);

        form.add(new JLabel("Address:"));
        form.add(addressField);

        form.add(new JLabel("Guardian Name:"));
        form.add(guardianField);

        form.add(new JLabel("Guardian Phone:"));
        form.add(guardianPhoneField);

        int result = JOptionPane.showConfirmDialog(
                this, form, "Student Details",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        return new Student(
                0,
                nameField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                genderBox.getSelectedItem().toString(),
                addressField.getText().trim(),
                guardianField.getText().trim(),
                guardianPhoneField.getText().trim()
        );
    }

    private void addStudent() {

        Student student = getStudentDetails(null);

        if (student == null) {
            return;
        }

        if (service.addStudent(student)) {
            JOptionPane.showMessageDialog(this,
                    "Student added successfully!\nStudent ID: "
                            + student.getStudentId());
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid details or duplicate email/phone!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewStudents() {

        ArrayList<Student> students = service.getAllStudents();

        String[] columns = {
                "Student ID", "Name", "Email", "Phone",
                "Gender", "Address", "Guardian", "Guardian Phone"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Student s : students) {
            model.addRow(new Object[]{
                    s.getStudentId(),
                    s.getName(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getGender(),
                    s.getAddress(),
                    s.getGuardianName(),
                    s.getGuardianPhone()
            });
        }

        showTable(model, "All Students");
    }

    private void updateStudent() {

        String input = JOptionPane.showInputDialog(this,
                "Enter Student ID to update:");

        if (input == null) {
            return;
        }

        try {
            int id = Integer.parseInt(input.trim());
            Student oldStudent = service.getStudentById(id);

            if (oldStudent == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }

            Student updatedStudent = getStudentDetails(oldStudent);

            if (updatedStudent == null) {
                return;
            }

            if (service.updateStudent(id, updatedStudent)) {
                JOptionPane.showMessageDialog(this,
                        "Student updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid details or duplicate email/phone!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid Student ID!");
        }
    }

    private void deleteStudent() {

        String input = JOptionPane.showInputDialog(this,
                "Enter Student ID to delete:");

        if (input == null) {
            return;
        }

        try {
            int id = Integer.parseInt(input.trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this student?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                if (service.deleteStudent(id)) {
                    JOptionPane.showMessageDialog(this,
                            "Student deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Student not found!");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid Student ID!");
        }
    }

    private void searchStudent() {

        String search = JOptionPane.showInputDialog(this,
                "Enter Student ID, Name, Email or Phone:");

        if (search == null || search.trim().isEmpty()) {
            return;
        }

        Student student = service.searchStudent(search.trim());

        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }

        String details =
                "Student ID: " + student.getStudentId()
                + "\nName: " + student.getName()
                + "\nEmail: " + student.getEmail()
                + "\nPhone: " + student.getPhone()
                + "\nGender: " + student.getGender()
                + "\nAddress: " + student.getAddress()
                + "\nGuardian: " + student.getGuardianName()
                + "\nGuardian Phone: " + student.getGuardianPhone();

        JOptionPane.showMessageDialog(this, details,
                "Student Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showCount() {

        int count = service.getStudentCount();

        JOptionPane.showMessageDialog(this,
                "Total Registered Students: " + count);
    }

    private void showTable(DefaultTableModel model, String title) {

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new java.awt.Dimension(750, 350));

        JOptionPane.showMessageDialog(this, scrollPane,
                title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new StudentModule().setVisible(true);
        });
    }
}
