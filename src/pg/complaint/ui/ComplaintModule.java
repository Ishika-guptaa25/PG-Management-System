
package pg.complaint.ui;

import pg.complaint.model.Complaint;
import pg.complaint.service.ComplaintService;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

public class ComplaintModule extends JFrame {

    private ComplaintService complaintService = new ComplaintService();

    public ComplaintModule() {

        setTitle("PG Management System - Complaint Module");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel(
                "COMPLAINT MANAGEMENT",
                SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 24));

        add(heading, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(
                new GridLayout(3, 2, 15, 15)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        JButton submitButton = new JButton("Submit Complaint");
        JButton viewButton = new JButton("View All Complaints");
        JButton searchButton = new JButton("Search Complaint");
        JButton studentSearchButton = new JButton("Search by Student ID");
        JButton updateButton = new JButton("Update Complaint Status");
        JButton deleteButton = new JButton("Delete Complaint");

        submitButton.addActionListener(e -> submitComplaint());
        viewButton.addActionListener(e -> viewAllComplaints());
        searchButton.addActionListener(e -> searchComplaint());
        studentSearchButton.addActionListener(e -> searchByStudentId());
        updateButton.addActionListener(e -> updateComplaintStatus());
        deleteButton.addActionListener(e -> deleteComplaint());

        mainPanel.add(submitButton);
        mainPanel.add(viewButton);
        mainPanel.add(searchButton);
        mainPanel.add(studentSearchButton);
        mainPanel.add(updateButton);
        mainPanel.add(deleteButton);

        add(mainPanel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(
                "PG Management System | Complaint Module",
                SwingConstants.CENTER
        );

        add(footer, BorderLayout.SOUTH);
    }

    // Submit Complaint
    private void submitComplaint() {

        String studentIdInput = JOptionPane.showInputDialog(
                this, "Enter Student ID:"
        );

        if (studentIdInput == null) {
            return;
        }

        String category = JOptionPane.showInputDialog(
                this, "Enter Complaint Category:"
        );

        if (category == null) {
            return;
        }

        String description = JOptionPane.showInputDialog(
                this, "Enter Complaint Description:"
        );

        if (description == null) {
            return;
        }

        try {

            int studentId = Integer.parseInt(studentIdInput.trim());

            Complaint complaint = new Complaint(
                    0,
                    studentId,
                    category.trim(),
                    description.trim(),
                    LocalDate.now().toString(),
                    "Pending"
            );

            boolean added = complaintService.addComplaint(complaint);

            if (added) {

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint submitted successfully!\nComplaint ID: "
                                + complaint.getComplaintId()
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint could not be saved in database!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Student ID must be a number!"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this, e.getMessage()
            );
        }
    }

    // View All Complaints
    private void viewAllComplaints() {

        ArrayList<Complaint> complaints =
                complaintService.getAllComplaints();

        String[] columns = {
                "Complaint ID",
                "Student ID",
                "Category",
                "Description",
                "Date",
                "Status"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0);

        for (Complaint c : complaints) {

            model.addRow(new Object[]{
                    c.getComplaintId(),
                    c.getStudentId(),
                    c.getCategory(),
                    c.getDescription(),
                    c.getComplaintDate(),
                    c.getStatus()
            });
        }

        showTable(model, "All Complaints");
    }

    // Search Complaint
    private void searchComplaint() {

        String input = JOptionPane.showInputDialog(
                this, "Enter Complaint ID:"
        );

        if (input == null) {
            return;
        }

        try {

            int complaintId = Integer.parseInt(input.trim());

            Complaint complaint =
                    complaintService.getComplaintById(complaintId);

            if (complaint == null) {

                JOptionPane.showMessageDialog(
                        this, "Complaint not found!"
                );

            } else {

                showComplaintDetails(complaint);
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid numeric ID!"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this, e.getMessage()
            );
        }
    }

    // Search by Student ID
    private void searchByStudentId() {

        String input = JOptionPane.showInputDialog(
                this, "Enter Student ID:"
        );

        if (input == null) {
            return;
        }

        try {

            int studentId = Integer.parseInt(input.trim());

            ArrayList<Complaint> complaints =
                    complaintService.getComplaintsByStudentId(studentId);

            if (complaints.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No complaints found for this student!"
                );

            } else {

                String[] columns = {
                        "Complaint ID",
                        "Student ID",
                        "Category",
                        "Description",
                        "Date",
                        "Status"
                };

                DefaultTableModel model =
                        new DefaultTableModel(columns, 0);

                for (Complaint c : complaints) {

                    model.addRow(new Object[]{
                            c.getComplaintId(),
                            c.getStudentId(),
                            c.getCategory(),
                            c.getDescription(),
                            c.getComplaintDate(),
                            c.getStatus()
                    });
                }

                showTable(model, "Student Complaints");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Student ID must be a number!"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this, e.getMessage()
            );
        }
    }

    // Update Complaint Status
    private void updateComplaintStatus() {

        String input = JOptionPane.showInputDialog(
                this, "Enter Complaint ID:"
        );

        if (input == null) {
            return;
        }

        String[] statuses = {
                "Pending",
                "In Progress",
                "Resolved"
        };

        String status = (String) JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Update Complaint Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                statuses[0]
        );

        if (status == null) {
            return;
        }

        try {

            int complaintId = Integer.parseInt(input.trim());

            boolean updated =
                    complaintService.updateComplaintStatus(
                            complaintId, status
                    );

            if (updated) {

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint status updated successfully!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint status could not be updated!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid numeric ID!"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this, e.getMessage()
            );
        }
    }

    // Delete Complaint
    private void deleteComplaint() {

        String input = JOptionPane.showInputDialog(
                this, "Enter Complaint ID:"
        );

        if (input == null) {
            return;
        }

        try {

            int complaintId = Integer.parseInt(input.trim());

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this complaint?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                boolean deleted =
                        complaintService.deleteComplaint(complaintId);

                if (deleted) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Complaint deleted successfully!"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Complaint could not be deleted!"
                    );
                }
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid numeric ID!"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this, e.getMessage()
            );
        }
    }

    // Display Complaint Details
    private void showComplaintDetails(Complaint complaint) {

        String details =
                "Complaint ID: " + complaint.getComplaintId()
                + "\nStudent ID: " + complaint.getStudentId()
                + "\nCategory: " + complaint.getCategory()
                + "\nDescription: " + complaint.getDescription()
                + "\nDate: " + complaint.getComplaintDate()
                + "\nStatus: " + complaint.getStatus();

        JOptionPane.showMessageDialog(
                this,
                details,
                "Complaint Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Display Table
    private void showTable(
            DefaultTableModel model,
            String title
    ) {

        JTable table = new JTable(model);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(
                new java.awt.Dimension(800, 350)
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            new ComplaintModule().setVisible(true);
        });
    }
}
