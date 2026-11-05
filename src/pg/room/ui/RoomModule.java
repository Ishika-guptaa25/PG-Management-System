
package pg.room.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import pg.room.model.Room;
import pg.room.model.RoomAllocation;
import pg.room.service.RoomService;

public class RoomModule extends JFrame {

    private RoomService service = new RoomService();

    public RoomModule() {

        setTitle("PG Management - Room Module");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JLabel heading = new JLabel("ROOM MANAGEMENT SYSTEM", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 12, 12));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JButton addRoom = new JButton("Add Room");
        JButton viewRoom = new JButton("View Rooms");
        JButton updateRoom = new JButton("Update Room");
        JButton deleteRoom = new JButton("Delete Room");
        JButton availability = new JButton("Room Availability");
        JButton searchRoom = new JButton("Search Room");
        JButton allocate = new JButton("Allocate Student");
        JButton vacate = new JButton("Vacate Room");
        JButton viewAllocation = new JButton("View Allocations");

        buttonPanel.add(addRoom);
        buttonPanel.add(viewRoom);
        buttonPanel.add(updateRoom);
        buttonPanel.add(deleteRoom);
        buttonPanel.add(availability);
        buttonPanel.add(searchRoom);
        buttonPanel.add(allocate);
        buttonPanel.add(vacate);
        buttonPanel.add(viewAllocation);

        add(heading, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        addRoom.addActionListener(e -> addRoom());
        viewRoom.addActionListener(e -> viewRooms());
        updateRoom.addActionListener(e -> updateRoom());
        deleteRoom.addActionListener(e -> deleteRoom());
        availability.addActionListener(e -> showAvailability());
        searchRoom.addActionListener(e -> searchRoom());
        allocate.addActionListener(e -> allocateStudent());
        vacate.addActionListener(e -> vacateRoom());
        viewAllocation.addActionListener(e -> viewAllocations());
    }

    private Room getRoomDetails(Room oldRoom) {

        JTextField number = new JTextField(
            oldRoom == null ? "" : oldRoom.getRoomNumber()
        );

        JComboBox<String> type = new JComboBox<>(
            new String[]{"Single", "Double", "Triple"}
        );

        JTextField rent = new JTextField(
            oldRoom == null ? "" : String.valueOf(oldRoom.getRent())
        );

        JTextField capacity = new JTextField(
            oldRoom == null ? "" : String.valueOf(oldRoom.getCapacity())
        );

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Room Number:"));
        panel.add(number);

        panel.add(new JLabel("Room Type:"));
        panel.add(type);

        panel.add(new JLabel("Monthly Rent:"));
        panel.add(rent);

        panel.add(new JLabel("Capacity:"));
        panel.add(capacity);

        if (oldRoom != null) {
            type.setSelectedItem(oldRoom.getRoomType());
        }

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Room Details",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {

            return new Room(
                0,
                number.getText().trim(),
                (String) type.getSelectedItem(),
                Double.parseDouble(rent.getText()),
                Integer.parseInt(capacity.getText()),
                "Available"
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                "Enter valid rent and capacity!");
            return null;
        }
    }

    private void addRoom() {

        Room room = getRoomDetails(null);

        if (room == null) {
            return;
        }

        if (service.addRoom(room)) {
            JOptionPane.showMessageDialog(this,
                "Room Added Successfully!");
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid details or duplicate room number!");
        }
    }

    private void viewRooms() {

        ArrayList<Room> rooms = service.getAllRooms();

        String[] columns = {
            "Room ID", "Room Number", "Room Type",
            "Rent", "Capacity", "Occupied", "Status"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Room room : rooms) {

            int occupied = service.getOccupiedCount(room.getRoomId());

            model.addRow(new Object[]{
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getRent(),
                room.getCapacity(),
                occupied,
                room.getStatus()
            });
        }

        showTable(model, "All Rooms");
    }

    private void updateRoom() {

        String input = JOptionPane.showInputDialog(
            this, "Enter Room ID:"
        );

        if (input == null) {
            return;
        }

        try {

            int id = Integer.parseInt(input);

            Room oldRoom = null;

            for (Room r : service.getAllRooms()) {
                if (r.getRoomId() == id) {
                    oldRoom = r;
                    break;
                }
            }

            if (oldRoom == null) {
                JOptionPane.showMessageDialog(this, "Room Not Found!");
                return;
            }

            Room updatedRoom = getRoomDetails(oldRoom);

            if (updatedRoom != null) {

                if (service.updateRoom(id, updatedRoom)) {
                    JOptionPane.showMessageDialog(this,
                        "Room Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Invalid details, duplicate number or capacity too low!");
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Enter a valid Room ID!");
        }
    }

    private void deleteRoom() {

        String input = JOptionPane.showInputDialog(
            this, "Enter Room ID:"
        );

        if (input == null) {
            return;
        }

        try {

            int id = Integer.parseInt(input);

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this room?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                if (service.deleteRoom(id)) {
                    JOptionPane.showMessageDialog(this,
                        "Room Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Room not found or students are still allocated!");
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Enter a valid Room ID!");
        }
    }

    private void showAvailability() {

        int total = service.getAllRooms().size();
        int available = service.getAvailableRooms();
        int full = service.getOccupiedRooms();

        JOptionPane.showMessageDialog(this,
            "Total Rooms: " + total +
            "\nAvailable Rooms: " + available +
            "\nFully Occupied Rooms: " + full,
            "Room Availability",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchRoom() {

        String input = JOptionPane.showInputDialog(
            this, "Enter Room ID or Room Number:"
        );

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        Room room = service.searchRoom(input.trim());

        if (room == null) {
            JOptionPane.showMessageDialog(this, "Room Not Found!");
        } else {

            int occupied = service.getOccupiedCount(room.getRoomId());

            JOptionPane.showMessageDialog(this,
                "Room ID: " + room.getRoomId() +
                "\nRoom Number: " + room.getRoomNumber() +
                "\nRoom Type: " + room.getRoomType() +
                "\nRent: " + room.getRent() +
                "\nCapacity: " + room.getCapacity() +
                "\nOccupied: " + occupied +
                "\nStatus: " + room.getStatus(),
                "Room Details",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void allocateStudent() {

        JTextField studentId = new JTextField();
        JTextField roomId = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        panel.add(new JLabel("Student ID:"));
        panel.add(studentId);

        panel.add(new JLabel("Room ID:"));
        panel.add(roomId);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Allocate Room",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            int sid = Integer.parseInt(studentId.getText());
            int rid = Integer.parseInt(roomId.getText());

            if (service.allocateRoom(sid, rid)) {
                JOptionPane.showMessageDialog(this,
                    "Room Allocated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Allocation Failed! Check student ID, room ID or room capacity.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Enter valid numeric IDs!");
        }
    }

    private void vacateRoom() {

        String input = JOptionPane.showInputDialog(
            this, "Enter Student ID:"
        );

        if (input == null) {
            return;
        }

        try {

            int studentId = Integer.parseInt(input);

            if (service.vacateRoom(studentId)) {
                JOptionPane.showMessageDialog(this,
                    "Room Vacated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Student Allocation Not Found!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Enter a valid Student ID!");
        }
    }

    private void viewAllocations() {

        ArrayList<RoomAllocation> allocations =
            service.getAllAllocations();

        String[] columns = {
            "Allocation ID", "Student ID", "Room ID"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (RoomAllocation a : allocations) {

            model.addRow(new Object[]{
                a.getAllocationId(),
                a.getStudentId(),
                a.getRoomId()
            });
        }

        showTable(model, "Student Room Allocations");
    }

    private void showTable(DefaultTableModel model, String title) {

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        JOptionPane.showMessageDialog(
            this, scrollPane, title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new RoomModule().setVisible(true);
        });
    }
}
