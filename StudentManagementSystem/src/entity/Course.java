package entity;

/**
 * Entity class representing a Course.
 */
public class Course {
    private String courseId;
    private String courseName;
    private String duration; // e.g., "6 Months", "4 Years"
    private double fees;

    // Default Constructor
    public Course() {}

    // Parameterized Constructor
    public Course(String courseId, String courseName, String duration, double fees) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.duration = duration;
        this.fees = fees;
    }

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    @Override
    public String toString() {
        return "Course{" +
                "ID='" + courseId + '\'' +
                ", Name='" + courseName + '\'' +
                ", Duration='" + duration + '\'' +
                ", Fees=" + fees +
                '}';
    }
}
