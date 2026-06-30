package database;

import model.GradedStudent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for interacting with the SQLite database via JDBC.
 */
public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:students.db";

    static {
        try {
            // Load SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: SQLite JDBC Driver not found in classpath!");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a connection to the database.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initializes the database, creating the students table if it doesn't already exist.
     */
    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS students (" +
                                "usn TEXT PRIMARY KEY, " +
                                "name TEXT NOT NULL, " +
                                "mark1 REAL, " +
                                "mark2 REAL, " +
                                "mark3 REAL, " +
                                "total REAL, " +
                                "average REAL, " +
                                "grade TEXT" +
                                ");";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks if a student with the given USN already exists in the database.
     * 
     * @param usn the Unique Student Number
     * @return true if the student exists, false otherwise
     * @throws SQLException if a database error occurs
     */
    public static boolean checkStudentExists(String usn) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE usn = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usn);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Adds a student to the database.
     * 
     * @param student GradedStudent to add
     * @throws SQLException if a database error occurs
     */
    public static void addStudent(GradedStudent student) throws SQLException {
        String sql = "INSERT INTO students(usn, name, mark1, mark2, mark3, total, average, grade) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getUsn());
            pstmt.setString(2, student.getName());
            pstmt.setDouble(3, student.getMark1());
            pstmt.setDouble(4, student.getMark2());
            pstmt.setDouble(5, student.getMark3());
            pstmt.setDouble(6, student.getTotal());
            pstmt.setDouble(7, student.getAverage());
            pstmt.setString(8, student.getGrade());
            pstmt.executeUpdate();
        }
    }

    /**
     * Updates an existing student record in the database.
     * 
     * @param student GradedStudent with updated details
     * @throws SQLException if a database error occurs
     */
    public static void updateStudent(GradedStudent student) throws SQLException {
        String sql = "UPDATE students SET name = ?, mark1 = ?, mark2 = ?, mark3 = ?, total = ?, average = ?, grade = ? WHERE usn = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setDouble(2, student.getMark1());
            pstmt.setDouble(3, student.getMark2());
            pstmt.setDouble(4, student.getMark3());
            pstmt.setDouble(5, student.getTotal());
            pstmt.setDouble(6, student.getAverage());
            pstmt.setString(7, student.getGrade());
            pstmt.setString(8, student.getUsn());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a student record from the database.
     * 
     * @param usn unique identifier of the student
     * @throws SQLException if a database error occurs
     */
    public static void deleteStudent(String usn) throws SQLException {
        String sql = "DELETE FROM students WHERE usn = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usn);
            pstmt.executeUpdate();
        }
    }

    /**
     * Searches for a student by USN.
     * 
     * @param usn unique identifier of the student
     * @return GradedStudent if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public static GradedStudent searchStudent(String usn) throws SQLException {
        String sql = "SELECT * FROM students WHERE usn = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    GradedStudent student = new GradedStudent(
                        rs.getString("usn"),
                        rs.getString("name"),
                        rs.getDouble("mark1"),
                        rs.getDouble("mark2"),
                        rs.getDouble("mark3")
                    );
                    student.setTotal(rs.getDouble("total"));
                    student.setAverage(rs.getDouble("average"));
                    student.setGrade(rs.getString("grade"));
                    return student;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all student records from the database.
     * 
     * @return List of GradedStudent objects
     * @throws SQLException if a database error occurs
     */
    public static List<GradedStudent> getAllStudents() throws SQLException {
        List<GradedStudent> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                GradedStudent student = new GradedStudent(
                    rs.getString("usn"),
                    rs.getString("name"),
                    rs.getDouble("mark1"),
                    rs.getDouble("mark2"),
                    rs.getDouble("mark3")
                );
                student.setTotal(rs.getDouble("total"));
                student.setAverage(rs.getDouble("average"));
                student.setGrade(rs.getString("grade"));
                list.add(student);
            }
        }
        return list;
    }
}
