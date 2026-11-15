
package pg.main;

import javax.swing.*;
import java.awt.*;

import pg.room.ui.RoomModule;
import pg.complaint.ui.ComplaintModule;
import pg.student.ui.StudentModule;

public class MainDashboard extends JFrame {

    public MainDashboard() {

        setTitle("PG Management System");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // Heading
        JLabel heading = new JLabel(
                "PG MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
                "Central Management Dashboard",
                SwingConstants.CENTER
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 10, 10)
        );

        header.add(heading);
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        // Module Buttons
        JPanel buttonPanel = new JPanel(
                new GridLayout(1, 3, 20, 20)
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(50, 30, 50, 30)
        );

        JButton roomButton = new JButton("Room Management");
        JButton complaintButton = new JButton("Complaint Management");
        JButton studentButton = new JButton("Student Management");

        roomButton.setFont(new Font("Arial", Font.BOLD, 18));
        complaintButton.setFont(new Font("Arial", Font.BOLD, 18));
        studentButton.setFont(new Font("Arial", Font.BOLD, 18));

        buttonPanel.add(roomButton);
        buttonPanel.add(complaintButton);
        buttonPanel.add(studentButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Room Module
        roomButton.addActionListener(e -> {
            RoomModule roomModule = new RoomModule();
            roomModule.setVisible(true);
        });

        // Complaint Module
        complaintButton.addActionListener(e -> {
            ComplaintModule complaintModule = new ComplaintModule();
            complaintModule.setVisible(true);
        });

        // Student Module
        studentButton.addActionListener(e -> {
            StudentModule studentModule = new StudentModule();
            studentModule.setVisible(true);
        });

        // Footer
        JLabel footer = new JLabel(
                "PG Management System | All Modules",
                SwingConstants.CENTER
        );

        footer.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 15, 10)
        );

        add(footer, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }
}
