package dao;

import entity.Marks;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) class for Marks entity.
 * Operates on student subjects (Java, DBMS, OS, CN), calculating totals, grades,
 * and performing complex group queries for topper, performance, and pass/fail reports.
 */
public class MarksDAO {

    /**
     * Checks if a mark record exists for a student.
     */
    public boolean exists(int studentId) throws SQLException {
        String sql = "SELECT mark_id FROM Marks WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Records/Inserts marks for a student.
     */
    public boolean addMarks(Marks marks) throws SQLException {
        String sql = "INSERT INTO Marks (student_id, java_marks, dbms_marks, os_marks, cn_marks, percentage, grade) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, marks.getStudentId());
            pstmt.setDouble(2, marks.getJavaMarks());
            pstmt.setDouble(3, marks.getDbms_marks());
            pstmt.setDouble(4, marks.getOsMarks());
            pstmt.setDouble(5, marks.getCnMarks());
            pstmt.setDouble(6, marks.getPercentage());
            pstmt.setString(7, marks.getGrade());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates an existing marks record.
     */
    public boolean updateMarks(Marks marks) throws SQLException {
        String sql = "UPDATE Marks SET java_marks = ?, dbms_marks = ?, os_marks = ?, cn_marks = ?, percentage = ?, grade = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, marks.getJavaMarks());
            pstmt.setDouble(2, marks.getDbms_marks());
            pstmt.setDouble(3, marks.getOsMarks());
            pstmt.setDouble(4, marks.getCnMarks());
            pstmt.setDouble(5, marks.getPercentage());
            pstmt.setString(6, marks.getGrade());
            pstmt.setInt(7, marks.getStudentId());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves marks for a specific student ID.
     */
    public Marks getMarksByStudentId(int studentId) throws SQLException {
        String sql = "SELECT * FROM Marks WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Marks m = new Marks();
                    m.setMarkId(rs.getInt("mark_id"));
                    m.setStudentId(rs.getInt("student_id"));
                    m.setJavaMarks(rs.getDouble("java_marks"));
                    m.setDbms_marks(rs.getDouble("dbms_marks"));
                    m.setOsMarks(rs.getDouble("os_marks"));
                    m.setCnMarks(rs.getDouble("cn_marks"));
                    m.setPercentage(rs.getDouble("percentage"));
                    m.setGrade(rs.getString("grade"));
                    return m;
                }
            }
        }
        return null;
    }

    /**
     * Report: Gets the top performing student (highest percentage).
     */
    public Map<String, Object> getTopper() throws SQLException {
        String sql = "SELECT s.student_id, s.name, m.percentage, m.grade, c.course_name " +
                     "FROM Student s " +
                     "JOIN Marks m ON s.student_id = m.student_id " +
                     "JOIN Course c ON s.course_id = c.course_id " +
                     "ORDER BY m.percentage DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                Map<String, Object> topper = new HashMap<>();
                topper.put("student_id", rs.getInt("student_id"));
                topper.put("name", rs.getString("name"));
                topper.put("percentage", rs.getDouble("percentage"));
                topper.put("grade", rs.getString("grade"));
                topper.put("course_name", rs.getString("course_name"));
                return topper;
            }
        }
        return null;
    }

    /**
     * Report: Gets details of passing and failing students (Pass: percentage >= 40 in average, and no individual subject < 35).
     */
    public List<Map<String, Object>> getPassFailReport() throws SQLException {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = "SELECT s.student_id, s.name, m.percentage, m.grade, " +
                     "CASE WHEN m.percentage >= 40 AND m.java_marks >= 35 AND m.dbms_marks >= 35 AND m.os_marks >= 35 AND m.cn_marks >= 35 THEN 'PASS' " +
                     "ELSE 'FAIL' END AS status " +
                     "FROM Student s " +
                     "JOIN Marks m ON s.student_id = m.student_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("student_id", rs.getInt("student_id"));
                row.put("name", rs.getString("name"));
                row.put("percentage", rs.getDouble("percentage"));
                row.put("grade", rs.getString("grade"));
                row.put("status", rs.getString("status"));
                report.add(row);
            }
        }
        return report;
    }

    /**
     * Report: Calculates average score per subject.
     */
    public Map<String, Double> getSubjectAverages() throws SQLException {
        String sql = "SELECT AVG(java_marks) as avg_java, AVG(dbms_marks) as avg_dbms, AVG(os_marks) as avg_os, AVG(cn_marks) as avg_cn FROM Marks";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                Map<String, Double> avgs = new HashMap<>();
                avgs.put("Java", rs.getDouble("avg_java"));
                avgs.put("DBMS", rs.getDouble("avg_dbms"));
                avgs.put("OS", rs.getDouble("avg_os"));
                avgs.put("CN", rs.getDouble("avg_cn"));
                return avgs;
            }
        }
        return null;
    }
}
