package dao;

import entity.Student;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for Student entity.
 * Implements CRUD operations, searching, and custom queries using JDBC PreparedStatement.
 */
public class StudentDAO {

    /**
     * Checks if a student ID already exists in the database.
     */
    public boolean exists(int studentId) throws SQLException {
        String sql = "SELECT student_id FROM Student WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Inserts a new student record into the database.
     */
    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Student (student_id, name, gender, age, email, mobile, course_id, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getGender());
            pstmt.setInt(4, student.getAge());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getMobile());
            pstmt.setString(7, student.getCourseId());
            pstmt.setString(8, student.getAddress());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates an existing student record.
     */
    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE Student SET name = ?, gender = ?, age = ?, email = ?, mobile = ?, course_id = ?, address = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getGender());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getMobile());
            pstmt.setString(6, student.getCourseId());
            pstmt.setString(7, student.getAddress());
            pstmt.setInt(8, student.getStudentId());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a student record by ID.
     */
    public boolean deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM Student WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Finds a student by ID.
     */
    public Student getStudentById(int studentId) throws SQLException {
        String sql = "SELECT * FROM Student WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractStudent(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all student records.
     */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                students.add(extractStudent(rs));
            }
        }
        return students;
    }

    /**
     * Searches students by Name (using wildcards).
     */
    public List<Student> searchStudentsByName(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student WHERE name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudent(rs));
                }
            }
        }
        return students;
    }

    /**
     * Searches students by enrolled Course ID or Name.
     */
    public List<Student> searchStudentsByCourse(String courseSearch) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.* FROM Student s " +
                     "JOIN Course c ON s.course_id = c.course_id " +
                     "WHERE s.course_id = ? OR c.course_name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseSearch);
            pstmt.setString(2, "%" + courseSearch + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudent(rs));
                }
            }
        }
        return students;
    }

    // Helper method to reconstruct Student entity from ResultSet
    private Student extractStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getString("name"),
                rs.getString("gender"),
                rs.getInt("age"),
                rs.getString("email"),
                rs.getString("mobile"),
                rs.getString("course_id"),
                rs.getString("address")
        );
    }
}
