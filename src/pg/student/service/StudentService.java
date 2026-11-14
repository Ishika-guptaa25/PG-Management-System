
package pg.student.service;

import java.util.ArrayList;

import pg.student.dao.StudentDAO;
import pg.student.model.Student;

public class StudentService {

    private StudentDAO studentDAO = new StudentDAO();

    // Add a new student
    public boolean addStudent(String name, String phone,
                              String email, String address) {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        int studentId = studentDAO.generateStudentId();

        Student student = new Student(studentId, name, phone, email, address);

        studentDAO.addStudent(student);

        return true;
    }

    // View all students
    public ArrayList<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    // Search student by ID
    public Student searchStudent(int studentId) {
        return studentDAO.getStudentById(studentId);
    }

    // Update student details
    public boolean updateStudent(int studentId, String name,
                                 String phone, String email,
                                 String address) {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        Student student = new Student(studentId, name, phone, email, address);

        return studentDAO.updateStudent(student);
    }

    // Delete student
    public boolean deleteStudent(int studentId) {
        return studentDAO.deleteStudent(studentId);
    }
}
