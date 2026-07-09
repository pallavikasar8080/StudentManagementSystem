package entity;

/**
 * Entity class representing student Marks for key subjects.
 * Provides functions to calculate totals, percentage, and letter grade.
 */
public class Marks {
    private int markId;
    private int studentId;
    private double javaMarks;
    private double dbms_marks;
    private double osMarks;
    private double cnMarks;
    private double percentage;
    private String grade;

    // Default Constructor
    public Marks() {}

    // Parameterized Constructor
    public Marks(int markId, int studentId, double javaMarks, double dbms_marks, double osMarks, double cnMarks) {
        this.markId = markId;
        this.studentId = studentId;
        this.javaMarks = javaMarks;
        this.dbms_marks = dbms_marks;
        this.osMarks = osMarks;
        this.cnMarks = cnMarks;
        calculatePercentageAndGrade();
    }

    // Calculations
    public double getTotalMarks() {
        return javaMarks + dbms_marks + osMarks + cnMarks;
    }

    public void calculatePercentageAndGrade() {
        this.percentage = getTotalMarks() / 4.0;
        if (percentage >= 90) {
            this.grade = "A+";
        } else if (percentage >= 80) {
            this.grade = "A";
        } else if (percentage >= 70) {
            this.grade = "B";
        } else if (percentage >= 60) {
            this.grade = "C";
        } else if (percentage >= 50) {
            this.grade = "D";
        } else if (percentage >= 40) {
            this.grade = "E";
        } else {
            this.grade = "F";
        }
    }

    // Getters and Setters
    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public double getJavaMarks() {
        return javaMarks;
    }

    public void setJavaMarks(double javaMarks) {
        this.javaMarks = javaMarks;
        calculatePercentageAndGrade();
    }

    public double getDbms_marks() {
        return dbms_marks;
    }

    public void setDbms_marks(double dbms_marks) {
        this.dbms_marks = dbms_marks;
        calculatePercentageAndGrade();
    }

    public double getOsMarks() {
        return osMarks;
    }

    public void setOsMarks(double osMarks) {
        this.osMarks = osMarks;
        calculatePercentageAndGrade();
    }

    public double getCnMarks() {
        return cnMarks;
    }

    public void setCnMarks(double cnMarks) {
        this.cnMarks = cnMarks;
        calculatePercentageAndGrade();
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Marks{" +
                "Mark ID=" + markId +
                ", Student ID=" + studentId +
                ", Java=" + javaMarks +
                ", DBMS=" + dbms_marks +
                ", OS=" + osMarks +
                ", CN=" + cnMarks +
                ", Percentage=" + percentage +
                ", Grade='" + grade + '\'' +
                '}';
    }
}
