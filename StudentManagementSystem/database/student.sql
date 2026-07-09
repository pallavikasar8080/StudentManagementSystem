-- ==========================================================
-- SQL Script for Student Management System Database Creation
-- Target Database Engine: MySQL (v5.7+ / v8.0+)
-- ==========================================================

-- 1. Create and select Database
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- 2. Drop existing tables if they exist to start fresh (ordered due to foreign key constraints)
DROP TABLE IF EXISTS Marks;
DROP TABLE IF EXISTS Student;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS Admin;

-- 3. Create Course Table
CREATE TABLE Course (
    course_id VARCHAR(10) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    duration VARCHAR(50) NOT NULL,
    fees DECIMAL(10, 2) NOT NULL
);

-- 4. Create Student Table
CREATE TABLE Student (
    student_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(15) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mobile VARCHAR(15) UNIQUE NOT NULL,
    course_id VARCHAR(10),
    address TEXT,
    FOREIGN KEY (course_id) REFERENCES Course(course_id) ON DELETE SET NULL
);

-- 5. Create Marks Table
CREATE TABLE Marks (
    mark_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT UNIQUE NOT NULL,
    java_marks DECIMAL(5, 2) DEFAULT 0.00,
    dbms_marks DECIMAL(5, 2) DEFAULT 0.00,
    os_marks DECIMAL(5, 2) DEFAULT 0.00,
    cn_marks DECIMAL(5, 2) DEFAULT 0.00,
    percentage DECIMAL(5, 2) DEFAULT 0.00,
    grade VARCHAR(5) DEFAULT 'F',
    FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE
);

-- 6. Create Admin Table (For admin login validation)
CREATE TABLE Admin (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

-- ==========================================================
-- 7. Populate Database with Initial/Seed Data
-- ==========================================================

-- Seed Course Data
INSERT INTO Course (course_id, course_name, duration, fees) VALUES
('CS101', 'B.Tech Computer Science', '4 Years', 50000.00),
('CS102', 'M.Tech Computer Science', '2 Years', 65000.00),
('CS201', 'B.Sc Data Science', '3 Years', 35000.00),
('CS202', 'Web Development BootCamp', '6 Months', 15000.00);

-- Seed Admin Records
INSERT INTO Admin (username, password) VALUES
('admin', 'admin123');

-- Seed Student Records
INSERT INTO Student (student_id, name, gender, age, email, mobile, course_id, address) VALUES
(101, 'Rahul Sharma', 'Male', 21, 'rahul.sharma@example.com', '9876543210', 'CS101', '123, MG Road, Bangalore'),
(102, 'Priya Patel', 'Female', 20, 'priya.patel@example.com', '9823456781', 'CS101', '45, Satellite Rd, Ahmedabad'),
(103, 'Aravind Swamy', 'Male', 23, 'aravind.swamy@example.com', '9123456780', 'CS102', '78, Anna Salai, Chennai'),
(104, 'Ananya Sen', 'Female', 19, 'ananya.sen@example.com', '9432109876', 'CS201', '12, Salt Lake, Kolkata'),
(105, 'Amit Verma', 'Male', 22, 'amit.verma@example.com', '8877665544', 'CS202', '56, Vikas Marg, Delhi');

-- Seed Marks Records
-- Percentage = (Java + DBMS + OS + CN) / 4
INSERT INTO Marks (student_id, java_marks, dbms_marks, os_marks, cn_marks, percentage, grade) VALUES
(101, 85.00, 78.50, 90.00, 88.00, 85.38, 'A'),
(102, 92.00, 95.00, 89.00, 94.00, 92.50, 'A+'),
(103, 70.00, 68.00, 72.00, 65.00, 68.75, 'C'),
(104, 55.00, 60.00, 58.00, 62.00, 58.75, 'D'),
(105, 45.00, 38.00, 42.00, 40.00, 41.25, 'E');

-- Verification Queries
-- SELECT * FROM Course;
-- SELECT * FROM Student;
-- SELECT * FROM Marks;
-- SELECT * FROM Admin;
