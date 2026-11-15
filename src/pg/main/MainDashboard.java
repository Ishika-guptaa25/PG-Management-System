
package pg.main;

import javax.swing.*;
import java.awt.*;

import pg.room.ui.RoomModule;
import pg.complaint.ui.ComplaintModule;

public class MainDashboard extends JFrame {

    public MainDashboard() {

        setTitle("PG Management System");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

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
        header.add(heading);
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(
                new GridLayout(1, 2, 20, 20)
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(50, 40, 50, 40)
        );

        JButton roomButton = new JButton("Room Management");
        JButton complaintButton = new JButton("Complaint Management");

        roomButton.setFont(new Font("Arial", Font.BOLD, 18));
        complaintButton.setFont(new Font("Arial", Font.BOLD, 18));

        buttonPanel.add(roomButton);
        buttonPanel.add(complaintButton);

        add(buttonPanel, BorderLayout.CENTER);

        roomButton.addActionListener(e -> {
            RoomModule roomModule = new RoomModule();
            roomModule.setVisible(true);
        });

        complaintButton.addActionListener(e -> {
            new ComplaintModule();
        });

        JLabel footer = new JLabel(
                "PG Management System | All Modules",
                SwingConstants.CENTER
        );

        add(footer, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }
}

