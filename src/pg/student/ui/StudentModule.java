
package pg.student.ui;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import pg.student.model.Student;
import pg.student.service.StudentService;

public class StudentModule extends JFrame implements ActionListener {

    private JTextField idField;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;

    private JTextArea outputArea;

    private JButton addButton;
    private JButton viewButton;
    private JButton searchButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    private StudentService studentService = new StudentService();

    public StudentModule() {

        setTitle("Student Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        // Create buttons
        addButton = new JButton("Add Student");
        viewButton = new JButton("View Students");
        searchButton = new JButton("Search Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");
        clearButton = new JButton("Clear Fields");

        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        searchButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Output area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add panels to window
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Handle button clicks
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addButton) {

            boolean result = studentService.addStudent(
                nameField.getText(),
                phoneField.getText(),
                emailField.getText(),
                addressField.getText()
            );

            if (result) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                viewStudents();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Name and phone are required!");
            }
        }

        else if (e.getSource() == viewButton) {
            viewStudents();
        }

        else if (e.getSource() == searchButton) {

            try {
                int id = Integer.parseInt(idField.getText());

                Student student = studentService.searchStudent(id);

                if (student != null) {

                    outputArea.setText(
                        "Student ID: " + student.getStudentId() +
                        "\nName: " + student.getName() +
                        "\nPhone: " + student.getPhone() +
                        "\nEmail: " + student.getEmail() +
                        "\nAddress: " + student.getAddress()
                    );

                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Student ID!");
            }
        }

        else if (e.getSource() == updateButton) {

            try {
                int id = Integer.parseInt(idField.getText());

                boolean result = studentService.updateStudent(
                    id,
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText()
                );

                if (result) {
                    JOptionPane.showMessageDialog(this, "Student updated successfully!");
                    viewStudents();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found or invalid details!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Student ID!");
            }
        }

        else if (e.getSource() == deleteButton) {

            try {
                int id = Integer.parseInt(idField.getText());

                boolean result = studentService.deleteStudent(id);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                    viewStudents();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Student ID!");
            }
        }

        else if (e.getSource() == clearButton) {
            clearFields();
        }
    }

    // Display all students
    private void viewStudents() {

        ArrayList<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            outputArea.setText("No students found.");
            return;
        }

        String result = "";

        for (Student student : students) {

            result += "Student ID: " + student.getStudentId() +
                      "\nName: " + student.getName() +
                      "\nPhone: " + student.getPhone() +
                      "\nEmail: " + student.getEmail() +
                      "\nAddress: " + student.getAddress() +
                      "\n----------------------------\n";
        }

        outputArea.setText(result);
    }

    // Clear all input fields
    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");

    }

    // Main method
    public static void main(String[] args) {

        new StudentModule();

    }
}

