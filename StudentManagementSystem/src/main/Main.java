package main;

import dao.CourseDAO;
import dao.MarksDAO;
import dao.StudentDAO;
import entity.Course;
import entity.Marks;
import entity.Student;
import exception.InvalidInputException;
import exception.StudentNotFoundException;
import service.StudentService;
import util.DBConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Main application class featuring a comprehensive console-based UI.
 * Handles administrator authentication and menu navigation for CRUD, Search, and Reporting operations.
 */
public class Main {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final MarksDAO marksDAO = new MarksDAO();
    private static final StudentService studentService = new StudentService();

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("        STUDENT MANAGEMENT SYSTEM (CORE JAVA)     ");
        System.out.println("==================================================");

        try {
            boolean authenticated = handleLogin();
            if (!authenticated) {
                System.out.println("Authentication failed. Exiting system.");
                return;
            }

            boolean exit = false;
            while (!exit) {
                printMainMenu();
                String choice = readInput("Select an option (1-6): ");
                switch (choice) {
                    case "1":
                        handleStudentMenu();
                        break;
                    case "2":
                        handleCourseMenu();
                        break;
                    case "3":
                        handleMarksMenu();
                        break;
                    case "4":
                        handleSearchMenu();
                        break;
                    case "5":
                        handleReportsMenu();
                        break;
                    case "6":
                        System.out.println("\nThank you for using the Student Management System. Goodbye!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal system error encountered: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    private static boolean handleLogin() throws Exception {
        System.out.println("\n--- ADMINISTRATOR PORTAL LOGIN ---");
        int attempts = 3;
        while (attempts > 0) {
            String username = readInput("Enter Username: ");
            String password = readInput("Enter Password: ");

            // Simple admin check (matching db credentials)
            if ("admin".equals(username) && "admin123".equals(password)) {
                System.out.println("\n✓ Login successful! Welcome, Administrator.");
                return true;
            } else {
                attempts--;
                System.out.println("✗ Invalid credentials! Attempts remaining: " + attempts);
            }
        }
        return false;
    }

    private static void printMainMenu() {
        System.out.println("\n=================================");
        System.out.println("           MAIN MENU             ");
        System.out.println("=================================");
        System.out.println("1. Student Records Management (CRUD)");
        System.out.println("2. Course Catalog Management");
        System.out.println("3. Marks & Performance Management");
        System.out.println("4. Multi-criteria Search");
        System.out.println("5. Academic Analysis Reports");
        System.out.println("6. Logout & Exit");
        System.out.println("=================================");
    }

    private static void handleStudentMenu() throws Exception {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- STUDENT MANAGEMENT MENU ---");
            System.out.println("1. Register New Student (Add)");
            System.out.println("2. Update Existing Student");
            System.out.println("3. De-register Student (Delete)");
            System.out.println("4. View All Registered Students");
            System.out.println("5. View Student Details by ID");
            System.out.println("6. Back to Main Menu");

            String choice = readInput("Select an option (1-6): ");
            switch (choice) {
                case "1":
                    registerNewStudent();
                    break;
                case "2":
                    updateStudent();
                    break;
                case "3":
                    deleteStudent();
                    break;
                case "4":
                    viewAllStudents();
                    break;
                case "5":
                    viewStudentById();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    private static void registerNewStudent() {
        try {
            System.out.println("\n--- REGISTER NEW STUDENT ---");
            int id = Integer.parseInt(readInput("Enter Student ID (Number): "));
            String name = readInput("Enter Full Name: ");
            String gender = readInput("Enter Gender (Male/Female/Other): ");
            int age = Integer.parseInt(readInput("Enter Age: "));
            String email = readInput("Enter Email Address: ");
            String mobile = readInput("Enter Mobile (10-digit): ");
            String courseId = readInput("Enter Course ID (e.g., CS101): ");
            String address = readInput("Enter Address: ");

            Student student = new Student(id, name, gender, age, email, mobile, courseId, address);
            if (studentService.registerStudent(student)) {
                System.out.println("\n✓ Student registered successfully!");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Student ID and Age must be numeric.");
        } catch (InvalidInputException e) {
            System.out.println("✗ Input Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("✗ Database Error: Fail to register student due to SQL exception. " + e.getMessage());
        }
    }

    private static void updateStudent() {
        try {
            System.out.println("\n--- UPDATE STUDENT DETAILS ---");
            int id = Integer.parseInt(readInput("Enter Student ID to update: "));

            Student old = studentDAO.getStudentById(id);
            if (old == null) {
                System.out.println("✗ Error: Student not found with ID " + id);
                return;
            }

            System.out.println("Found current details. Press Enter to keep existing values:");
            String name = readInput("Name (" + old.getName() + "): ");
            name = name.trim().isEmpty() ? old.getName() : name;

            String gender = readInput("Gender (" + old.getGender() + "): ");
            gender = gender.trim().isEmpty() ? old.getGender() : gender;

            String ageInput = readInput("Age (" + old.getAge() + "): ");
            int age = ageInput.trim().isEmpty() ? old.getAge() : Integer.parseInt(ageInput);

            String email = readInput("Email (" + old.getEmail() + "): ");
            email = email.trim().isEmpty() ? old.getEmail() : email;

            String mobile = readInput("Mobile (" + old.getMobile() + "): ");
            mobile = mobile.trim().isEmpty() ? old.getMobile() : mobile;

            String courseId = readInput("Course ID (" + old.getCourseId() + "): ");
            courseId = courseId.trim().isEmpty() ? old.getCourseId() : courseId;

            String address = readInput("Address (" + old.getAddress() + "): ");
            address = address.trim().isEmpty() ? old.getAddress() : address;

            Student student = new Student(id, name, gender, age, email, mobile, courseId, address);
            if (studentService.updateStudentDetails(student)) {
                System.out.println("\n✓ Student records updated successfully!");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Input fields were of invalid numeric type.");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        try {
            System.out.println("\n--- DELETE STUDENT RECORD ---");
            int id = Integer.parseInt(readInput("Enter Student ID to delete: "));
            String confirmation = readInput("Are you sure you want to delete Student ID " + id + "? (yes/no): ");
            if ("yes".equalsIgnoreCase(confirmation)) {
                if (studentDAO.deleteStudent(id)) {
                    System.out.println("\n✓ Student record purged successfully.");
                } else {
                    System.out.println("✗ Student not found or couldn't be deleted.");
                }
            } else {
                System.out.println("De-registration cancelled.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void viewAllStudents() {
        try {
            System.out.println("\n--- REGISTERED STUDENTS LIST ---");
            List<Student> list = studentDAO.getAllStudents();
            if (list.isEmpty()) {
                System.out.println("No student records exist in the database.");
                return;
            }
            printStudentHeader();
            for (Student s : list) {
                printStudentRow(s);
            }
        } catch (SQLException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void viewStudentById() {
        try {
            int id = Integer.parseInt(readInput("\nEnter Student ID to view details: "));
            Student s = studentDAO.getStudentById(id);
            if (s != null) {
                System.out.println("\n-----------------------------");
                System.out.println("STUDENT DETAILS PROFILE");
                System.out.println("-----------------------------");
                System.out.println("ID        : " + s.getStudentId());
                System.out.println("Name      : " + s.getName());
                System.out.println("Gender    : " + s.getGender());
                System.out.println("Age       : " + s.getAge());
                System.out.println("Email     : " + s.getEmail());
                System.out.println("Mobile    : " + s.getMobile());
                System.out.println("Course ID : " + s.getCourseId());
                System.out.println("Address   : " + s.getAddress());
                System.out.println("-----------------------------");

                Marks m = marksDAO.getMarksByStudentId(id);
                if (m != null) {
                    System.out.println("ACADEMIC PERFORMANCE");
                    System.out.println("Java: " + m.getJavaMarks() + " | DBMS: " + m.getDbms_marks() + " | OS: " + m.getOsMarks() + " | CN: " + m.getCnMarks());
                    System.out.println("Average/Percentage: " + m.getPercentage() + "% | Grade: " + m.getGrade());
                } else {
                    System.out.println("Academic scores not recorded yet.");
                }
            } else {
                System.out.println("✗ Student not found.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void handleCourseMenu() throws Exception {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- COURSE CATALOG MENU ---");
            System.out.println("1. Create New Course (Add)");
            System.out.println("2. Update Course Offering");
            System.out.println("3. Decommission Course (Delete)");
            System.out.println("4. View Course Catalog");
            System.out.println("5. Back to Main Menu");

            String choice = readInput("Select an option (1-5): ");
            switch (choice) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    updateCourse();
                    break;
                case "3":
                    deleteCourse();
                    break;
                case "4":
                    viewAllCourses();
                    break;
                case "5":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    private static void addCourse() {
        try {
            System.out.println("\n--- CREATE NEW COURSE ---");
            String id = readInput("Enter Course ID (e.g., CS101): ");
            if (courseDAO.exists(id)) {
                System.out.println("✗ Error: Course ID already exists.");
                return;
            }
            String name = readInput("Enter Course Name: ");
            String duration = readInput("Enter Duration (e.g., 6 Months, 4 Years): ");
            double fees = Double.parseDouble(readInput("Enter Semester Fees ($): "));

            Course course = new Course(id, name, duration, fees);
            if (courseDAO.addCourse(course)) {
                System.out.println("\n✓ Course successfully cataloged!");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void updateCourse() {
        try {
            System.out.println("\n--- UPDATE COURSE OFFERING ---");
            String id = readInput("Enter Course ID to update: ");
            Course old = courseDAO.getCourseById(id);
            if (old == null) {
                System.out.println("✗ Course ID not found.");
                return;
            }

            String name = readInput("Course Name (" + old.getCourseName() + "): ");
            name = name.trim().isEmpty() ? old.getCourseName() : name;

            String duration = readInput("Duration (" + old.getDuration() + "): ");
            duration = duration.trim().isEmpty() ? old.getDuration() : duration;

            String feesInput = readInput("Fees (" + old.getFees() + "): ");
            double fees = feesInput.trim().isEmpty() ? old.getFees() : Double.parseDouble(feesInput);

            Course updated = new Course(id, name, duration, fees);
            if (courseDAO.updateCourse(updated)) {
                System.out.println("\n✓ Course records updated successfully!");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void deleteCourse() {
        try {
            System.out.println("\n--- DECOMMISSION COURSE OFFERING ---");
            String id = readInput("Enter Course ID to decommission: ");
            if (courseDAO.deleteCourse(id)) {
                System.out.println("\n✓ Course successfully purged from catalog.");
            } else {
                System.out.println("✗ Course not found or has registered student references.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void viewAllCourses() {
        try {
            System.out.println("\n--- CATALOGED COURSE OFFERINGS ---");
            List<Course> list = courseDAO.getAllCourses();
            if (list.isEmpty()) {
                System.out.println("No course entries exist.");
                return;
            }
            System.out.format("%-12s | %-30s | %-15s | %-12s\n", "Course ID", "Course Name", "Duration", "Fees");
            System.out.println("-----------------------------------------------------------------------------");
            for (Course c : list) {
                System.out.format("%-12s | %-30s | %-15s | $%-11.2f\n", c.getCourseId(), c.getCourseName(), c.getDuration(), c.getFees());
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void handleMarksMenu() {
        try {
            System.out.println("\n--- MARKS MANAGEMENT SYSTEM ---");
            int studentId = Integer.parseInt(readInput("Enter Student ID to log scores: "));
            if (!studentDAO.exists(studentId)) {
                System.out.println("✗ Error: Student ID does not exist.");
                return;
            }

            double java = Double.parseDouble(readInput("Enter Java Programming Marks (0-100): "));
            double dbms = Double.parseDouble(readInput("Enter DBMS Marks (0-100): "));
            double os = Double.parseDouble(readInput("Enter Operating Systems Marks (0-100): "));
            double cn = Double.parseDouble(readInput("Enter Computer Networks Marks (0-100): "));

            Marks m = new Marks(0, studentId, java, dbms, os, cn);
            if (studentService.enterStudentMarks(m)) {
                System.out.println("\n✓ Student academic records saved! Cumulative %: " + String.format("%.2f", m.getPercentage()) + "% | Grade: " + m.getGrade());
            }
        } catch (Exception e) {
            System.out.println("✗ Fail to enter marks: " + e.getMessage());
        }
    }

    private static void handleSearchMenu() {
        try {
            System.out.println("\n--- SEARCH ENGINE ---");
            System.out.println("1. Search by Student ID");
            System.out.println("2. Search by Student Name");
            System.out.println("3. Search by Assigned Course ID / Course Name");
            String criteria = readInput("Select criteria (1-3): ");

            switch (criteria) {
                case "1":
                    int id = Integer.parseInt(readInput("Enter Student ID: "));
                    Student s = studentDAO.getStudentById(id);
                    if (s != null) {
                        printStudentHeader();
                        printStudentRow(s);
                    } else {
                        System.out.println("No matching student records found.");
                    }
                    break;
                case "2":
                    String name = readInput("Enter part or full Student Name: ");
                    List<Student> byName = studentDAO.searchStudentsByName(name);
                    displaySearchList(byName);
                    break;
                case "3":
                    String course = readInput("Enter Course ID or Course Name: ");
                    List<Student> byCourse = studentDAO.searchStudentsByCourse(course);
                    displaySearchList(byCourse);
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("✗ Search failed: " + e.getMessage());
        }
    }

    private static void displaySearchList(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("No matching records found.");
        } else {
            printStudentHeader();
            for (Student s : list) {
                printStudentRow(s);
            }
        }
    }

    private static void handleReportsMenu() {
        try {
            System.out.println("\n--- REPORTING & ANALYTICAL METRICS ---");
            System.out.println("1. View Class Topper Profile");
            System.out.println("2. Pass / Fail Status Summary");
            System.out.println("3. Subject-wise Benchmark Analysis");

            String choice = readInput("Select analytical report (1-3): ");
            switch (choice) {
                case "1":
                    Map<String, Object> topper = marksDAO.getTopper();
                    if (topper != null) {
                        System.out.println("\n-------------------------------------------");
                        System.out.println("           ACADEMIC TOPPER AWARD           ");
                        System.out.println("-------------------------------------------");
                        System.out.println("Student ID  : " + topper.get("student_id"));
                        System.out.println("Name        : " + topper.get("name"));
                        System.out.println("Course      : " + topper.get("course_name"));
                        System.out.println("Percentage  : " + String.format("%.2f", topper.get("percentage")) + "%");
                        System.out.println("Grade Assigned: " + topper.get("grade"));
                        System.out.println("-------------------------------------------");
                    } else {
                        System.out.println("No academic scores registered to compute toppers.");
                    }
                    break;
                case "2":
                    List<Map<String, Object>> reports = marksDAO.getPassFailReport();
                    if (reports.isEmpty()) {
                        System.out.println("No student academic records recorded.");
                        return;
                    }
                    System.out.println("\n--- PASS/FAIL PROGRESS STATUS ---");
                    System.out.format("%-10s | %-22s | %-12s | %-6s | %-6s\n", "ID", "Name", "Percentage", "Grade", "Status");
                    System.out.println("---------------------------------------------------------------");
                    for (Map<String, Object> row : reports) {
                        System.out.format("%-10d | %-22s | %-11.2f%% | %-6s | %-6s\n",
                                (int) row.get("student_id"),
                                row.get("name"),
                                (double) row.get("percentage"),
                                row.get("grade"),
                                row.get("status"));
                    }
                    break;
                case "3":
                    Map<String, Double> averages = marksDAO.getSubjectAverages();
                    if (averages != null && averages.get("Java") != null) {
                        System.out.println("\n--- SUBJECT-WISE BENCHMARK AVERAGES ---");
                        System.out.format("%-25s | %-15s\n", "Course Module Name", "Average Score");
                        System.out.println("-------------------------------------------");
                        System.out.format("%-25s | %-14.2f%%\n", "Java Programming Core", averages.get("Java"));
                        System.out.format("%-25s | %-14.2f%%\n", "Database Management (DBMS)", averages.get("DBMS"));
                        System.out.format("%-25s | %-14.2f%%\n", "Operating Systems (OS)", averages.get("OS"));
                        System.out.format("%-25s | %-14.2f%%\n", "Computer Networks (CN)", averages.get("CN"));
                        System.out.println("-------------------------------------------");
                    } else {
                        System.out.println("No score entries available to compute subject-wise metrics.");
                    }
                    break;
                default:
                    System.out.println("Invalid reporting option.");
            }
        } catch (Exception e) {
            System.out.println("✗ Report compilation failed: " + e.getMessage());
        }
    }

    private static String readInput(String prompt) {
        System.out.print(prompt);
        try {
            return reader.readLine();
        } catch (Exception e) {
            return "";
        }
    }

    private static void printStudentHeader() {
        System.out.format("%-6s | %-22s | %-6s | %-3s | %-25s | %-10s | %-10s\n", "ID", "Name", "Gender", "Age", "Email", "Mobile", "Course ID");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }

    private static void printStudentRow(Student s) {
        System.out.format("%-6d | %-22s | %-6s | %-3d | %-25s | %-10s | %-10s\n",
                s.getStudentId(), s.getName(), s.getGender(), s.getAge(), s.getEmail(), s.getMobile(), s.getCourseId());
    }
}
