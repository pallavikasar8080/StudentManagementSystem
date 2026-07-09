package service;

import dao.StudentDAO;
import dao.CourseDAO;
import dao.MarksDAO;
import entity.Student;
import entity.Course;
import entity.Marks;
import exception.StudentNotFoundException;
import exception.InvalidInputException;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service layer coordinating validation logic and database CRUD requests.
 * Ensures data integrity (duplicate check, email validation, mobile validation, age check).
 */
public class StudentService {
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final MarksDAO marksDAO;

    // Regex pattern definitions
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[0-9]{10}$"); // Simple 10-digit check

    public StudentService() {
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();
        this.marksDAO = new MarksDAO();
    }

    /**
     * Adds a student after running strict validation rules.
     */
    public boolean registerStudent(Student student) throws SQLException, InvalidInputException {
        // 1. Duplicate check
        if (studentDAO.exists(student.getStudentId())) {
            throw new InvalidInputException("Student registration failed: ID " + student.getStudentId() + " already exists!");
        }

        // 2. Name check
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new InvalidInputException("Student registration failed: Name cannot be empty.");
        }

        // 3. Age validation (e.g. must be between 16 and 100)
        if (student.getAge() < 16 || student.getAge() > 100) {
            throw new InvalidInputException("Student registration failed: Age must be between 16 and 100.");
        }

        // 4. Email validation
        if (student.getEmail() == null || !EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            throw new InvalidInputException("Student registration failed: Invalid email format (e.g., example@domain.com).");
        }

        // 5. Mobile validation (10 digits)
        if (student.getMobile() == null || !MOBILE_PATTERN.matcher(student.getMobile()).matches()) {
            throw new InvalidInputException("Student registration failed: Mobile number must be exactly 10 digits.");
        }

        // 6. Enrolled course check
        if (student.getCourseId() == null || !courseDAO.exists(student.getCourseId())) {
            throw new InvalidInputException("Student registration failed: Assigned Course ID '" + student.getCourseId() + "' does not exist.");
        }

        return studentDAO.addStudent(student);
    }

    /**
     * Updates student details, with validation.
     */
    public boolean updateStudentDetails(Student student) throws SQLException, InvalidInputException, StudentNotFoundException {
        if (!studentDAO.exists(student.getStudentId())) {
            throw new StudentNotFoundException("Cannot update: Student with ID " + student.getStudentId() + " not found.");
        }

        // Name check
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new InvalidInputException("Update failed: Name cannot be empty.");
        }

        // Age check
        if (student.getAge() < 16 || student.getAge() > 100) {
            throw new InvalidInputException("Update failed: Age must be between 16 and 100.");
        }

        // Email check
        if (student.getEmail() == null || !EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
            throw new InvalidInputException("Update failed: Invalid email format.");
        }

        // Mobile check
        if (student.getMobile() == null || !MOBILE_PATTERN.matcher(student.getMobile()).matches()) {
            throw new InvalidInputException("Update failed: Mobile number must be exactly 10 digits.");
        }

        // Enrolled course check
        if (student.getCourseId() == null || !courseDAO.exists(student.getCourseId())) {
            throw new InvalidInputException("Update failed: Assigned Course ID '" + student.getCourseId() + "' does not exist.");
        }

        return studentDAO.updateStudent(student);
    }

    /**
     * Enters marks for an existing student.
     */
    public boolean enterStudentMarks(Marks marks) throws SQLException, StudentNotFoundException, InvalidInputException {
        if (!studentDAO.exists(marks.getStudentId())) {
            throw new StudentNotFoundException("Cannot add marks: Student with ID " + marks.getStudentId() + " not found.");
        }

        // Marks bounds check (0 - 100)
        validateSubjectMarks("Java", marks.getJavaMarks());
        validateSubjectMarks("DBMS", marks.getDbms_marks());
        validateSubjectMarks("OS", marks.getOsMarks());
        validateSubjectMarks("CN", marks.getCnMarks());

        // Recalculate and persist
        marks.calculatePercentageAndGrade();

        if (marksDAO.exists(marks.getStudentId())) {
            return marksDAO.updateMarks(marks);
        } else {
            return marksDAO.addMarks(marks);
        }
    }

    private void validateSubjectMarks(String subject, double val) throws InvalidInputException {
        if (val < 0 || val > 100) {
            throw new InvalidInputException("Invalid marks: " + subject + " score must be between 0 and 100.");
        }
    }
}
