package model;

/**
 * Interface defining standard methods for student result calculations.
 */
public interface Gradable {
    /**
     * Calculates the total marks of the student.
     * @return the total marks
     */
    double calculateTotal();

    /**
     * Calculates the average marks of the student.
     * @return the average marks
     */
    double calculateAverage();

    /**
     * Computes the grade for the student based on marks and status.
     * @return the grade string
     */
    String calculateGrade();
}
