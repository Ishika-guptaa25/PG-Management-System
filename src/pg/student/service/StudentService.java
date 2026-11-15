
package pg.student.service;

import java.util.ArrayList;

import pg.student.dao.StudentDAO;
import pg.student.model.Student;

public class StudentService {

    private StudentDAO dao = new StudentDAO();

    public boolean addStudent(Student student) {

        if (!validateStudent(student)) {
            return false;
        }

        for (Student s : dao.getAllStudents()) {

            if (s.getEmail().equalsIgnoreCase(student.getEmail())
                    || s.getPhone().equals(student.getPhone())) {
                return false;
            }
        }

        return dao.addStudent(student);
    }

    public ArrayList<Student> getAllStudents() {
        return dao.getAllStudents();
    }

    public Student getStudentById(int id) {
        return dao.getStudentById(id);
    }

    public boolean updateStudent(int id, Student updatedStudent) {

        if (!validateStudent(updatedStudent)) {
            return false;
        }

        if (dao.getStudentById(id) == null) {
            return false;
        }

        for (Student s : dao.getAllStudents()) {

            if (s.getStudentId() != id) {

                if (s.getEmail().equalsIgnoreCase(updatedStudent.getEmail())
                        || s.getPhone().equals(updatedStudent.getPhone())) {
                    return false;
                }
            }
        }

        return dao.updateStudent(id, updatedStudent);
    }

    public boolean deleteStudent(int id) {
        return dao.deleteStudent(id);
    }

    public Student searchStudent(String search) {

        for (Student student : dao.getAllStudents()) {

            if (String.valueOf(student.getStudentId()).equals(search)
                    || student.getName().equalsIgnoreCase(search)
                    || student.getPhone().equals(search)
                    || student.getEmail().equalsIgnoreCase(search)) {

                return student;
            }
        }

        return null;
    }

    public int getStudentCount() {
        return dao.getAllStudents().size();
    }

    public boolean validateStudent(Student student) {

        if (student.getName() == null
                || student.getName().trim().isEmpty()) {
            return false;
        }

        if (student.getEmail() == null
                || !student.getEmail().matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return false;
        }

        if (student.getPhone() == null
                || !student.getPhone().matches("[0-9]{10}")) {
            return false;
        }

        if (student.getGender() == null
                || student.getGender().isEmpty()) {
            return false;
        }

        if (student.getAddress() == null
                || student.getAddress().trim().isEmpty()) {
            return false;
        }

        if (student.getGuardianName() == null
                || student.getGuardianName().trim().isEmpty()) {
            return false;
        }

        if (student.getGuardianPhone() == null
                || !student.getGuardianPhone().matches("[0-9]{10}")) {
            return false;
        }

        return true;
    }
}
