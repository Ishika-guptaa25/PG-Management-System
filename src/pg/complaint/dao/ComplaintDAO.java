
package pg.complaint.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

import pg.database.DBConnection;
import pg.complaint.model.Complaint;

public class ComplaintDAO {

    public boolean addComplaint(Complaint complaint) {

        String sql = "INSERT INTO Complaint "
                + "(student_id, category, description, complaint_date, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            try (PreparedStatement ps =
                    con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, complaint.getStudentId());
                ps.setString(2, complaint.getCategory());
                ps.setString(3, complaint.getDescription());
                ps.setDate(4, java.sql.Date.valueOf(complaint.getComplaintDate()));
                ps.setString(5, complaint.getStatus());

                if (ps.executeUpdate() > 0) {

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            complaint.setComplaintId(rs.getInt(1));
                        }
                    }

                    return true;
                }
            }

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Complaint> getAllComplaints() {

        ArrayList<Complaint> complaints = new ArrayList<>();

        String sql = "SELECT * FROM Complaint";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return complaints;

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    complaints.add(new Complaint(
                            rs.getInt("complaint_id"),
                            rs.getInt("student_id"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getDate("complaint_date").toString(),
                            rs.getString("status")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return complaints;
    }

    public Complaint getComplaintById(int complaintId) {

        String sql = "SELECT * FROM Complaint WHERE complaint_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return null;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, complaintId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        return new Complaint(
                                rs.getInt("complaint_id"),
                                rs.getInt("student_id"),
                                rs.getString("category"),
                                rs.getString("description"),
                                rs.getDate("complaint_date").toString(),
                                rs.getString("status")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateComplaint(int complaintId, Complaint complaint) {

        String sql = "UPDATE Complaint SET student_id=?, category=?, "
                + "description=?, complaint_date=?, status=? "
                + "WHERE complaint_id=?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, complaint.getStudentId());
                ps.setString(2, complaint.getCategory());
                ps.setString(3, complaint.getDescription());
                ps.setDate(4, java.sql.Date.valueOf(complaint.getComplaintDate()));
                ps.setString(5, complaint.getStatus());
                ps.setInt(6, complaintId);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteComplaint(int complaintId) {

        String sql = "DELETE FROM Complaint WHERE complaint_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, complaintId);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Complaint> getComplaintsByStudentId(int studentId) {

        ArrayList<Complaint> complaints = new ArrayList<>();

        String sql = "SELECT * FROM Complaint WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return complaints;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, studentId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        complaints.add(new Complaint(
                                rs.getInt("complaint_id"),
                                rs.getInt("student_id"),
                                rs.getString("category"),
                                rs.getString("description"),
                                rs.getDate("complaint_date").toString(),
                                rs.getString("status")
                        ));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return complaints;
    }
}
