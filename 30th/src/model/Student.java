package model;

/**
 * Base class representing a Student.
 * Implements encapsulation for student properties.
 */
public class Student {
    private String usn;
    private String name;

    /**
     * Constructs a student with USN and Name.
     * @param usn  the Unique Student Number
     * @param name the student's name
     */
    public Student(String usn, String name) {
        this.usn = usn;
        this.name = name;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
