
package pg.student.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

import pg.database.DBConnection;
import pg.student.model.Student;

public class StudentDAO {

    public boolean addStudent(Student student) {

        String sql = "INSERT INTO Student "
                + "(name, email, phone, gender, address, guardian_name, guardian_phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            try (PreparedStatement ps =
                    con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, student.getName());
                ps.setString(2, student.getEmail());
                ps.setString(3, student.getPhone());
                ps.setString(4, student.getGender());
                ps.setString(5, student.getAddress());
                ps.setString(6, student.getGuardianName());
                ps.setString(7, student.getGuardianPhone());

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            student.setStudentId(rs.getInt(1));
                        }
                    }
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Student> getAllStudents() {

        ArrayList<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM Student";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return students;

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Student student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("gender"),
                            rs.getString("address"),
                            rs.getString("guardian_name"),
                            rs.getString("guardian_phone")
                    );

                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public Student getStudentById(int id) {

        String sql = "SELECT * FROM Student WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return null;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        return new Student(
                                rs.getInt("student_id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("gender"),
                                rs.getString("address"),
                                rs.getString("guardian_name"),
                                rs.getString("guardian_phone")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateStudent(int id, Student student) {

        String sql = "UPDATE Student SET name=?, email=?, phone=?, "
                + "gender=?, address=?, guardian_name=?, guardian_phone=? "
                + "WHERE student_id=?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, student.getName());
                ps.setString(2, student.getEmail());
                ps.setString(3, student.getPhone());
                ps.setString(4, student.getGender());
                ps.setString(5, student.getAddress());
                ps.setString(6, student.getGuardianName());
                ps.setString(7, student.getGuardianPhone());
                ps.setInt(8, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteStudent(int id) {

        String sql = "DELETE FROM Student WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
