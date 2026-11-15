
package pg.room.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pg.database.DBConnection;
import pg.room.model.RoomAllocation;

public class RoomAllocationDAO {

    public void addAllocation(int studentId, int roomId) {

        String sql = "INSERT INTO Room_Allocation (student_id, room_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, studentId);
            ps.setInt(2, roomId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Allocation ID: " + rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RoomAllocation> getAllAllocations() {

        ArrayList<RoomAllocation> allocations = new ArrayList<>();

        String sql = "SELECT * FROM Room_Allocation ORDER BY allocation_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                RoomAllocation allocation = new RoomAllocation(
                    rs.getInt("allocation_id"),
                    rs.getInt("student_id"),
                    rs.getInt("room_id")
                );

                allocations.add(allocation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allocations;
    }

    public boolean removeAllocation(int studentId) {

        String sql = "DELETE FROM Room_Allocation WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getOccupiedCount(int roomId) {

        String sql = "SELECT COUNT(*) FROM Room_Allocation WHERE room_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean isStudentAllocated(int studentId) {

        String sql = "SELECT COUNT(*) FROM Room_Allocation WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
