package model;

/**
 * Subclass representing a graded student.
 * Inherits from Student and implements the Gradable interface.
 */
public class GradedStudent extends Student implements Gradable {
    private double mark1;
    private double mark2;
    private double mark3;
    private double total;
    private double average;
    private String grade;

    /**
     * Constructs a GradedStudent with base details and marks,
     * automatically calculating the total, average, and grade.
     * 
     * @param usn   the Unique Student Number
     * @param name  the student's name
     * @param mark1 marks for subject 1
     * @param mark2 marks for subject 2
     * @param mark3 marks for subject 3
     */
    public GradedStudent(String usn, String name, double mark1, double mark2, double mark3) {
        super(usn, name);
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
        recalculate();
    }

    /**
     * Recalculates total, average, and grade based on current marks.
     */
    public final void recalculate() {
        this.total = calculateTotal();
        this.average = calculateAverage();
        this.grade = calculateGrade();
    }

    @Override
    public double calculateTotal() {
        return mark1 + mark2 + mark3;
    }

    @Override
    public double calculateAverage() {
        return calculateTotal() / 3.0;
    }

    @Override
    public String calculateGrade() {
        // Individual subject check: failing any subject (mark < 35) yields a fail grade (F)
        if (mark1 < 35 || mark2 < 35 || mark3 < 35) {
            return "F";
        }
        
        double avg = calculateAverage();
        if (avg >= 90) {
            return "A+";
        } else if (avg >= 80) {
            return "A";
        } else if (avg >= 70) {
            return "B";
        } else if (avg >= 60) {
            return "C";
        } else if (avg >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    // Getters and setters
    public double getMark1() {
        return mark1;
    }

    public void setMark1(double mark1) {
        this.mark1 = mark1;
        recalculate();
    }

    public double getMark2() {
        return mark2;
    }

    public void setMark2(double mark2) {
        this.mark2 = mark2;
        recalculate();
    }

    public double getMark3() {
        return mark3;
    }

    public void setMark3(double mark3) {
        this.mark3 = mark3;
        recalculate();
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
