
package pg.room.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pg.database.DBConnection;
import pg.room.model.Room;

public class RoomDAO {

    public void addRoom(Room room) {

        String sql = "INSERT INTO Room (room_number, room_type, rent, capacity, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getRent());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    room.setRoomId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Room> getAllRooms() {

        ArrayList<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM Room ORDER BY room_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Room room = new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("rent"),
                    rs.getInt("capacity"),
                    rs.getString("status")
                );

                rooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public Room getRoomById(int id) {

        String sql = "SELECT * FROM Room WHERE room_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("rent"),
                        rs.getInt("capacity"),
                        rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateRoom(int id, Room updatedRoom) {

        String sql = "UPDATE Room SET room_number=?, room_type=?, rent=?, capacity=?, status=? WHERE room_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, updatedRoom.getRoomNumber());
            ps.setString(2, updatedRoom.getRoomType());
            ps.setDouble(3, updatedRoom.getRent());
            ps.setInt(4, updatedRoom.getCapacity());
            ps.setString(5, updatedRoom.getStatus());
            ps.setInt(6, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRoom(int id) {

        String sql = "DELETE FROM Room WHERE room_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
