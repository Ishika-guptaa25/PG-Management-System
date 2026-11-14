
package pg.student.dao;

import java.util.ArrayList;
import pg.student.model.Student;

public class StudentDAO {

    private static ArrayList<Student> students = new ArrayList<Student>();

    private static int nextId = 1;

    // Add a new student
    public void addStudent(Student student) {
        students.add(student);
    }

    // View all students
    public ArrayList<Student> getAllStudents() {
        return students;
    }

    // Search student using ID
    public Student getStudentById(int studentId) {

        for (Student student : students) {

            if (student.getStudentId() == studentId) {
                return student;
            }
        }

        return null;
    }

    // Generate student ID
    public int generateStudentId() {
        return nextId++;
    }

    // Update student details
    public boolean updateStudent(Student updatedStudent) {

        Student student = getStudentById(updatedStudent.getStudentId());

        if (student != null) {

            student.setName(updatedStudent.getName());
            student.setPhone(updatedStudent.getPhone());
            student.setEmail(updatedStudent.getEmail());
            student.setAddress(updatedStudent.getAddress());

            return true;
        }

        return false;
    }

    // Delete student
    public boolean deleteStudent(int studentId) {

        Student student = getStudentById(studentId);

        if (student != null) {
            students.remove(student);
            return true;
        }

        return false;
    }
}
