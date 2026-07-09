package dao;

import entity.Course;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for Course entity.
 * Implements CRUD operations for system courses using JDBC.
 */
public class CourseDAO {

    /**
     * Checks if a course ID exists.
     */
    public boolean exists(String courseId) throws SQLException {
        String sql = "SELECT course_id FROM Course WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Adds a new course to the database.
     */
    public boolean addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO Course (course_id, course_name, duration, fees) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getDuration());
            pstmt.setDouble(4, course.getFees());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates an existing course.
     */
    public boolean updateCourse(Course course) throws SQLException {
        String sql = "UPDATE Course SET course_name = ?, duration = ?, fees = ? WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getDuration());
            pstmt.setDouble(3, course.getFees());
            pstmt.setString(4, course.getCourseId());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a course from the database.
     */
    public boolean deleteCourse(String courseId) throws SQLException {
        String sql = "DELETE FROM Course WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves a Course by ID.
     */
    public Course getCourseById(String courseId) throws SQLException {
        String sql = "SELECT * FROM Course WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getString("course_id"),
                            rs.getString("course_name"),
                            rs.getString("duration"),
                            rs.getDouble("fees")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all course records.
     */
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getString("duration"),
                        rs.getDouble("fees")
                ));
            }
        }
        return courses;
    }
}
