# Student Management System

A robust, production-quality, console-based **Student Management System (SMS)** engineered using **Core Java (JDK 17+)**, **JDBC**, and a persistent **MySQL Database**. This project adopts standard Object-Oriented Programming (OOP) principles, a clean Data Access Object (DAO) pattern, custom exception handling, and input validation to deliver a complete student records, marks, and course catalog workflow.

---

## 📂 Project Package Structure

Adheres to professional Java packaging conventions for separation of concerns:

```text
StudentManagementSystem/
│
├── database/
│   └── student.sql             # SQL Script for Table schemas, foreign keys, and seed data
│
├── src/
│   ├── entity/                 # Plain Old Java Objects (POJOs) representing DB models
│   │   ├── Admin.java          # Administrator Entity
│   │   ├── Course.java         # Course Catalog Entity
│   │   ├── Marks.java          # Academic Scores & Calculation Entity
│   │   └── Student.java        # Student Profile Entity
│   │
│   ├── dao/                    # Data Access Objects executing SQL via PreparedStatements
│   │   ├── CourseDAO.java      # CRUD queries for course configurations
│   │   ├── MarksDAO.java       # CRUD queries, Toppers, pass/fail and metrics reporting
│   │   └── StudentDAO.java     # CRUD, Wildcard Name search, Course join searches
│   │
│   ├── service/                # Business logic and input validation layer
│   │   └── StudentService.java # Mobile/Email/Age formats and duplication safeguards
│   │
│   ├── exception/              # Application-specific Custom Exceptions
│   │   ├── InvalidInputException.java
│   │   └── StudentNotFoundException.java
│   │
│   ├── util/                   # Connection pool & database configuration helpers
│   │   └── DBConnection.java   # MySQL JDBC Driver & Thread-safe Connection manager
│   │
│   └── main/                   # Command Line Interface (CLI) loop and user portal
│       └── Main.java           # Entrypoint class containing menu-driven console UI
│
├── README.md                   # This project installation and documentation guide
└── StudentManagementSystem.iml # IntelliJ IDEA Project Module descriptor
```

---

## ✨ Features Checklist

- **🔒 Secure Admin Portal**: Includes credential matching with custom validation safeguards.
- **🎓 Student Records (CRUD)**:
  - **Create**: Add new student entries. Includes validation against duplicate Student IDs, invalid email formats, and incorrect mobile phone numbers.
  - **Read**: Fetch all registered students or display a single detailed student profile.
  - **Update**: Incrementally modify student fields (Name, Gender, Email, Mobile, Course) while retaining old properties if left empty.
  - **Delete**: Purge specific student profiles safely (with active CASCADE constraints on marks).
- **📚 Course Catalog**: Define and decommission course codes (e.g. `CS101`), specifying durations and fees.
- **📊 Marks & Grade Book**:
  - Store subject marks for **Java, DBMS, OS, and CN** (0 - 100 check).
  - Automatically calculate **Total Score**, **Cumulative Percentage**, and letter grades (`A+` down to `F`).
- **🔍 Multi-Criteria Search Engine**: Search for students instantly by **ID**, **Full/Partial Name**, or **Enrolled Course Name**.
- **📈 Advanced Reporting Dashboard**:
  - Identify and award the **Class Topper** (highest cumulative score).
  - List pass/fail summaries based on custom course benchmarks.
  - View subject-by-subject average metrics to analyze course difficulty.

---

## 🛠️ Technology Stack

- **Language**: Java SE 17 / 21
- **RDBMS Engine**: MySQL 5.7 / 8.0
- **Database Driver**: MySQL Connector/J JDBC Driver (v8.x)
- **IDE**: IntelliJ IDEA (Ultimate or Community) / Eclipse IDE
- **Build Tool**: Maven (Optional) / Traditional Classpath imports

---

## 🚀 Step-by-Step Installation & Setup

### Prerequisite Checklist
1. **Java Development Kit (JDK) 17 or higher** installed.
2. **MySQL Server** installed and running on default port `3306`.
3. An IDE like **IntelliJ IDEA** or Eclipse installed.

---

### Step 1: Database Setup
1. Open your terminal or MySQL Workbench and log in:
   ```bash
   mysql -u root -p
   ```
2. Run the provided database creation and seeding script located in `database/student.sql`:
   ```sql
   SOURCE /path/to/StudentManagementSystem/database/student.sql;
   ```
This will automatically construct the database `student_management`, generate tables (`Admin`, `Course`, `Student`, `Marks`), configure key relations, and seed mock data.

---

### Step 2: Configure DB Connection
1. Locate `/src/util/DBConnection.java`.
2. Update the credentials in `DBConnection.java` to match your local MySQL configuration:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/student_management";
   private static final String USER = "root";
   private static final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";
   ```

---

### Step 3: Run the Console Application
1. **IntelliJ IDEA**:
   - Right-click the root directory `StudentManagementSystem/` and select **Open as Project**.
   - Navigate to `/src/main/Main.java`.
   - Click the green **Run** button or press `Shift + F10`.
2. **Command Line Compilation**:
   ```bash
   cd StudentManagementSystem/src
   # Compile all java files
   javac -d ../bin main/Main.java entity/*.java dao/*.java service/*.java exception/*.java util/*.java
   # Run with MySQL JDBC Driver on classpath
   java -cp ../bin:path/to/mysql-connector-j-8.x.jar main.Main
   ```

---

## 🖥️ Console Portal Screenshot Placeholders

### 1. Login Module
```text
==================================================
        STUDENT MANAGEMENT SYSTEM (CORE JAVA)     
==================================================

--- ADMINISTRATOR PORTAL LOGIN ---
Enter Username: admin
Enter Password: ••••••••

✓ Login successful! Welcome, Administrator.
```

### 2. Main Portal Menu
```text
=================================
           MAIN MENU             
=================================
1. Student Records Management (CRUD)
2. Course Catalog Management
3. Marks & Performance Management
4. Multi-criteria Search
5. Academic Analysis Reports
6. Logout & Exit
=================================
Select an option (1-6): 
```

### 3. Student Registration (Validation Demo)
```text
--- REGISTER NEW STUDENT ---
Enter Student ID (Number): 106
Enter Full Name: Sarah Connor
Enter Gender (Male/Female/Other): Female
Enter Age: 15
Enter Email Address: sarah@skynet
Enter Mobile (10-digit): 987654

✗ Input Error: Student registration failed: Age must be between 16 and 100.
```

### 4. Interactive Academic Report
```text
--- REPORTING & ANALYTICAL METRICS ---
1. View Class Topper Profile
2. Pass / Fail Status Summary
3. Subject-wise Benchmark Analysis
Select analytical report (1-3): 1

-------------------------------------------
           ACADEMIC TOPPER AWARD           
-------------------------------------------
Student ID  : 102
Name        : Priya Patel
Course      : B.Tech Computer Science
Percentage  : 92.50%
Grade Assigned: A+
-------------------------------------------
```

---

## 📝 Design Patterns & Concepts Applied

- **DAO (Data Access Object) Pattern**: decouples database persistence details from high-level service rules, enabling easier maintenance.
- **Encapsulation**: Keeps entity fields private with tailored accessors (getters/setters).
- **Method Overloading**: Applied to searching techniques (identifying records through string names or integer IDs).
- **Custom Exception Handlers**: Gracefully intercepts invalid student ranges (`StudentNotFoundException`) and invalid patterns (`InvalidInputException`) before hitting database drivers, increasing stability.
- **Java Try-With-Resources**: Assures database connections, statements, and result sets are auto-closed, blocking memory leaks.
