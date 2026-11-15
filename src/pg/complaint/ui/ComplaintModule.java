package pg.complaint.ui;
import pg.complaint.model.Complaint;
import pg.complaint.service.ComplaintService;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ComplaintModule extends JFrame {

    public ComplaintModule() {

        setTitle("PG Management System - Complaint Module");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("COMPLAINT MANAGEMENT", SwingConstants.CENTER);
        heading.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));

        add(heading, BorderLayout.NORTH);

        // Main panel
        JPanel mainPanel = new JPanel();
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.setLayout(new GridLayout(3, 2, 15, 15));

        JButton submitButton = new JButton("Submit Complaint");
        submitButton.addActionListener(e -> submitComplaint());

        JButton viewButton = new JButton("View All Complaints");
        viewButton.addActionListener(e -> viewAllComplaints());

        JButton searchButton = new JButton("Search Complaint");
        searchButton.addActionListener(e -> searchComplaint());

        JButton studentSearchButton = new JButton("Search by Student ID");
        studentSearchButton.addActionListener(e -> searchByStudentId());

        JButton updateButton = new JButton("Update Complaint Status");
        updateButton.addActionListener(e -> updateComplaintStatus());

        JButton deleteButton = new JButton("Delete Complaint");
        deleteButton.addActionListener(e -> deleteComplaint());

        mainPanel.add(submitButton);
        mainPanel.add(viewButton);
        mainPanel.add(searchButton);
        mainPanel.add(studentSearchButton);
        mainPanel.add(updateButton);
        mainPanel.add(deleteButton);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        setVisible(true);

    }
    private void submitComplaint() {

        String studentIdInput = JOptionPane.showInputDialog(
                this, "Enter Student ID:");

        if (studentIdInput == null) {
            return;
        }

        String category = JOptionPane.showInputDialog(
                this, "Enter Complaint Category:");

        if (category == null) {
            return;
        }

        String description = JOptionPane.showInputDialog(
                this, "Enter Complaint Description:");

        if (description == null) {
            return;
        }

        try {

            int studentId = Integer.parseInt(studentIdInput);

            Complaint complaint = new Complaint(
                    0,
                    studentId,
                    category,
                    description,
                    LocalDate.now().toString(),
                    "Pending"
            );

            complaintService.addComplaint(complaint);

            JOptionPane.showMessageDialog(
                    this,
                    "Complaint submitted successfully!\nComplaint ID: "
                            + complaint.getComplaintId()
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this, "Student ID must be a number!");

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }

    }
    private void viewAllComplaints() {

        ArrayList<Complaint> complaints = complaintService.getAllComplaints();

        String[] columns = {
                "Complaint ID",
                "Student ID",
                "Category",
                "Description",
                "Date",
                "Status"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Complaint c : complaints) {

            Object[] row = {
                    c.getComplaintId(),
                    c.getStudentId(),
                    c.getCategory(),
                    c.getDescription(),
                    c.getComplaintDate(),
                    c.getStatus()
            };

            model.addRow(row);
        }

        JTable table = new JTable(model);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(table),
                "All Complaints",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    private void searchComplaint() {

        String input = JOptionPane.showInputDialog(
                this, "Enter Complaint ID:");

        if (input == null) {
            return;
        }

        try {

            int complaintId = Integer.parseInt(input);

            Complaint complaint = complaintService.getComplaintById(complaintId);

            if (complaint == null) {

                JOptionPane.showMessageDialog(this, "Complaint not found!");

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint ID: " + complaint.getComplaintId()
                                + "\nStudent ID: " + complaint.getStudentId()
                                + "\nCategory: " + complaint.getCategory()
                                + "\nDescription: " + complaint.getDescription()
                                + "\nDate: " + complaint.getComplaintDate()
                                + "\nStatus: " + complaint.getStatus()
                );

            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID!");

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }
    }
    private void searchByStudentId() {

        String input = JOptionPane.showInputDialog(
                this, "Enter Student ID:");

        if (input == null) {
            return;
        }

        try {

            int studentId = Integer.parseInt(input);

            ArrayList<Complaint> complaints =
                    complaintService.getComplaintsByStudentId(studentId);

            if (complaints.isEmpty()) {

                JOptionPane.showMessageDialog(this, "No complaints found for this student!");

            } else {

                StringBuilder result = new StringBuilder();

                for (Complaint c : complaints) {

                    result.append("Complaint ID: ").append(c.getComplaintId())
                            .append("\nCategory: ").append(c.getCategory())
                            .append("\nDescription: ").append(c.getDescription())
                            .append("\nDate: ").append(c.getComplaintDate())
                            .append("\nStatus: ").append(c.getStatus())
                            .append("\n-------------------------\n");
                }

                JOptionPane.showMessageDialog(this, result.toString());
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this, "Student ID must be a number!");

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void updateComplaintStatus() {

        String input = JOptionPane.showInputDialog(
                this, "Enter Complaint ID:");

        if (input == null) return;

        String[] statuses = {"Pending", "In Progress", "Resolved"};

        String status = (String) JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Update Complaint Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                statuses[0]
        );

        if (status == null) return;

        try {
            int complaintId = Integer.parseInt(input);

            complaintService.updateComplaintStatus(complaintId, status);

            JOptionPane.showMessageDialog(
                    this, "Complaint status updated successfully!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteComplaint() {

        String input = JOptionPane.showInputDialog(
                this, "Enter Complaint ID:");

        if (input == null) return;

        try {
            int complaintId = Integer.parseInt(input);

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this complaint?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                complaintService.deleteComplaint(complaintId);

                JOptionPane.showMessageDialog(
                        this, "Complaint deleted successfully!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private ComplaintService complaintService = new ComplaintService();
    public static void main(String[] args) {

        new ComplaintModule();

    }

}