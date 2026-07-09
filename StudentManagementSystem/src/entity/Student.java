package entity;

/**
 * Entity class representing a Student.
 * Handles student demographic and course enrollment details.
 */
public class Student {
    private int studentId;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String mobile;
    private String courseId; // Foreign key referencing Course
    private String address;

    // Default Constructor
    public Student() {}

    // Parameterized Constructor
    public Student(int studentId, String name, String gender, int age, String email, String mobile, String courseId, String address) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.mobile = mobile;
        this.courseId = courseId;
        this.address = address;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID=" + studentId +
                ", Name='" + name + '\'' +
                ", Gender='" + gender + '\'' +
                ", Age=" + age +
                ", Email='" + email + '\'' +
                ", Mobile='" + mobile + '\'' +
                ", Course ID='" + courseId + '\'' +
                ", Address='" + address + '\'' +
                '}';
    }
}
