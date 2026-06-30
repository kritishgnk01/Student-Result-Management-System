package ui;

import database.DatabaseHelper;
import model.GradedStudent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Main UI Class for the Student Result Management System.
 * Extends JFrame to provide a responsive Swing application.
 */
public class StudentResultSystem extends JFrame {

    private JTextField usnField;
    private JTextField nameField;
    private JTextField mark1Field;
    private JTextField mark2Field;
    private JTextField mark3Field;
    private JTextField searchField;

    private JLabel totalValLabel;
    private JLabel averageValLabel;
    private JLabel gradeValLabel;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    // Palette Colors
    private static final Color COLOR_BG = new Color(15, 23, 42);         // Slate 900
    private static final Color COLOR_CARD = new Color(30, 41, 59);       // Slate 800
    private static final Color COLOR_CARD_DARK = new Color(24, 32, 48);
    private static final Color COLOR_BORDER = new Color(51, 65, 85);     // Slate 700
    private static final Color COLOR_TEXT_PRI = new Color(248, 250, 252); // Slate 50
    private static final Color COLOR_TEXT_SEC = new Color(148, 163, 184); // Slate 400
    private static final Color COLOR_ACCENT = new Color(99, 102, 241);    // Indigo 500
    private static final Color COLOR_SUCCESS = new Color(16, 185, 129);   // Emerald 500
    private static final Color COLOR_DANGER = new Color(239, 68, 68);     // Rose 500

    /**
     * Custom JPanel that paints rounded corners.
     */
    static class RoundPanel extends JPanel {
        private final int cornerRadius;
        private final Color bgColor;

        public RoundPanel(LayoutManager layout, int radius, Color bgColor) {
            super(layout);
            this.cornerRadius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
        }
    }

    public StudentResultSystem() {
        super("Student Result Management System");
        initializeUI();
        refreshTableData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // 1. Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_CARD);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JLabel titleLabel = new JLabel("STUDENT RESULT PORTAL");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(COLOR_ACCENT);

        JLabel subtitleLabel = new JLabel("Academic Performance Repository & Analytical Dashboard");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(COLOR_TEXT_SEC);

        JPanel titleTextPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        titleTextPanel.setOpaque(false);
        titleTextPanel.add(titleLabel);
        titleTextPanel.add(subtitleLabel);

        headerPanel.add(titleTextPanel, BorderLayout.WEST);

        // 2. Main Dashboard Container
        JPanel mainContainer = new JPanel(new BorderLayout(20, 20));
        mainContainer.setBackground(COLOR_BG);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // --- LEFT PANEL (Inputs, Summary, Actions) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(COLOR_BG);
        leftPanel.setPreferredSize(new Dimension(420, 0));

        // Form Card
        RoundPanel formCard = new RoundPanel(new GridBagLayout(), 16, COLOR_CARD);
        formCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Section Title
        JLabel formTitle = new JLabel("Student Information & Scores");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        formTitle.setForeground(COLOR_TEXT_PRI);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        formCard.add(formTitle, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(6, 5, 6, 5);

        // USN Field
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("USN / Roll No:", new Font("Segoe UI", Font.BOLD, 12), COLOR_TEXT_SEC), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        usnField = createStyledTextField();
        formCard.add(usnField, gbc);

        // Name Field
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Student Name:", new Font("Segoe UI", Font.BOLD, 12), COLOR_TEXT_SEC), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        nameField = createStyledTextField();
        formCard.add(nameField, gbc);

        // Subject 1
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Subject 1 Marks:", new Font("Segoe UI", Font.BOLD, 12), COLOR_TEXT_SEC), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        mark1Field = createStyledTextField();
        formCard.add(mark1Field, gbc);

        // Subject 2
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Subject 2 Marks:", new Font("Segoe UI", Font.BOLD, 12), COLOR_TEXT_SEC), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        mark2Field = createStyledTextField();
        formCard.add(mark2Field, gbc);

        // Subject 3
        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Subject 3 Marks:", new Font("Segoe UI", Font.BOLD, 12), COLOR_TEXT_SEC), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        mark3Field = createStyledTextField();
        formCard.add(mark3Field, gbc);

        // Result Summary Card
        RoundPanel summaryCard = new RoundPanel(new BorderLayout(10, 10), 16, COLOR_CARD);
        summaryCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel summaryTitle = new JLabel("Performance Indicator");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        summaryTitle.setForeground(COLOR_TEXT_PRI);
        summaryCard.add(summaryTitle, BorderLayout.NORTH);

        JPanel cardsContainer = new JPanel(new GridLayout(1, 3, 10, 0));
        cardsContainer.setOpaque(false);

        // Total Indicator Card
        RoundPanel totalCard = new RoundPanel(new GridBagLayout(), 12, COLOR_BG);
        totalCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints cGbc = new GridBagConstraints();
        cGbc.gridx = 0; cGbc.gridy = 0; cGbc.anchor = GridBagConstraints.CENTER;
        totalCard.add(createStyledLabel("TOTAL", new Font("Segoe UI", Font.BOLD, 10), COLOR_TEXT_SEC), cGbc);
        cGbc.gridy = 1; cGbc.insets = new Insets(5, 0, 0, 0);
        totalValLabel = createStyledLabel("0.0 / 300", new Font("Segoe UI", Font.BOLD, 14), COLOR_ACCENT);
        totalCard.add(totalValLabel, cGbc);

        // Average Indicator Card
        RoundPanel averageCard = new RoundPanel(new GridBagLayout(), 12, COLOR_BG);
        averageCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cGbc.gridy = 0; cGbc.insets = new Insets(0, 0, 0, 0);
        averageCard.add(createStyledLabel("AVERAGE", new Font("Segoe UI", Font.BOLD, 10), COLOR_TEXT_SEC), cGbc);
        cGbc.gridy = 1; cGbc.insets = new Insets(5, 0, 0, 0);
        averageValLabel = createStyledLabel("0.00%", new Font("Segoe UI", Font.BOLD, 14), COLOR_ACCENT);
        averageCard.add(averageValLabel, cGbc);

        // Grade Indicator Card
        RoundPanel gradeCard = new RoundPanel(new GridBagLayout(), 12, COLOR_BG);
        gradeCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cGbc.gridy = 0; cGbc.insets = new Insets(0, 0, 0, 0);
        gradeCard.add(createStyledLabel("GRADE", new Font("Segoe UI", Font.BOLD, 10), COLOR_TEXT_SEC), cGbc);
        cGbc.gridy = 1; cGbc.insets = new Insets(3, 0, 0, 0);
        gradeValLabel = createStyledLabel("-", new Font("Segoe UI", Font.BOLD, 22), COLOR_TEXT_SEC);
        gradeCard.add(gradeValLabel, cGbc);

        cardsContainer.add(totalCard);
        cardsContainer.add(averageCard);
        cardsContainer.add(gradeCard);
        summaryCard.add(cardsContainer, BorderLayout.CENTER);

        // Actions Button Card
        RoundPanel buttonsCard = new RoundPanel(new GridLayout(2, 2, 12, 12), 16, COLOR_CARD);
        buttonsCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JButton btnCalculate = createStyledButton("Calculate Preview", COLOR_ACCENT, Color.WHITE, new Color(79, 70, 229));
        JButton btnClear = createStyledButton("Clear Fields", new Color(71, 85, 105), COLOR_TEXT_PRI, new Color(51, 65, 85));
        JButton btnAdd = createStyledButton("Save / Update", COLOR_SUCCESS, Color.WHITE, new Color(5, 150, 105));
        JButton btnDelete = createStyledButton("Delete Record", COLOR_DANGER, Color.WHITE, new Color(220, 38, 38));

        buttonsCard.add(btnCalculate);
        buttonsCard.add(btnClear);
        buttonsCard.add(btnAdd);
        buttonsCard.add(btnDelete);

        // Add action listeners
        btnCalculate.addActionListener(e -> performPreview());
        btnClear.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> addOrUpdateStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        leftPanel.add(formCard);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(summaryCard);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(buttonsCard);

        // --- RIGHT PANEL (Search & Table View) ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setBackground(COLOR_BG);

        // Search Bar Card
        RoundPanel searchCard = new RoundPanel(new GridBagLayout(), 16, COLOR_CARD);
        searchCard.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        GridBagConstraints sGbc = new GridBagConstraints();
        sGbc.fill = GridBagConstraints.HORIZONTAL;
        sGbc.insets = new Insets(0, 5, 0, 5);

        sGbc.gridx = 0; sGbc.weightx = 0.0;
        searchCard.add(createStyledLabel("Search USN:", new Font("Segoe UI", Font.BOLD, 12), COLOR_TEXT_SEC), sGbc);

        sGbc.gridx = 1; sGbc.weightx = 1.0;
        searchField = createStyledTextField();
        searchCard.add(searchField, sGbc);

        sGbc.gridx = 2; sGbc.weightx = 0.0;
        JButton btnSearch = createStyledButton("Search", COLOR_ACCENT, Color.WHITE, new Color(79, 70, 229));
        searchCard.add(btnSearch, sGbc);

        sGbc.gridx = 3; sGbc.weightx = 0.0;
        JButton btnReset = createStyledButton("Reset", new Color(71, 85, 105), COLOR_TEXT_PRI, new Color(51, 65, 85));
        searchCard.add(btnReset, sGbc);

        btnSearch.addActionListener(e -> searchStudent());
        btnReset.addActionListener(e -> {
            searchField.setText("");
            refreshTableData();
        });

        // Records Table Card
        RoundPanel tableCard = new RoundPanel(new BorderLayout(10, 10), 16, COLOR_CARD);
        tableCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel tableTitle = new JLabel("Students Repository");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableTitle.setForeground(COLOR_TEXT_PRI);
        tableCard.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"USN / Roll No", "Student Name", "Sub 1", "Sub 2", "Sub 3", "Total", "Average", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(28);
        studentTable.setShowGrid(true);
        studentTable.setGridColor(COLOR_BORDER);
        studentTable.setBackground(COLOR_CARD);
        studentTable.setForeground(COLOR_TEXT_PRI);
        studentTable.setSelectionBackground(COLOR_ACCENT);
        studentTable.setSelectionForeground(Color.WHITE);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Apply Custom Table Cell Renderer
        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(COLOR_ACCENT);
                    c.setForeground(Color.WHITE);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(COLOR_CARD);
                    } else {
                        c.setBackground(COLOR_CARD_DARK);
                    }
                    c.setForeground(COLOR_TEXT_PRI);
                }

                // Alignment and formatting
                if (column >= 2) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                // Set grade specific text coloring in JTable
                if (column == 7 && !isSelected) {
                    String val = String.valueOf(value);
                    if ("F".equals(val)) {
                        c.setForeground(COLOR_DANGER);
                    } else {
                        c.setForeground(COLOR_SUCCESS);
                    }
                }
                return c;
            }
        });

        // Styling table header
        JTableHeader tableHeader = studentTable.getTableHeader();
        tableHeader.setBackground(COLOR_BG);
        tableHeader.setForeground(COLOR_TEXT_PRI);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        tableHeader.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(COLOR_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Selection Listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    String usn = (String) studentTable.getValueAt(selectedRow, 0);
                    String name = (String) studentTable.getValueAt(selectedRow, 1);
                    double m1 = (double) studentTable.getValueAt(selectedRow, 2);
                    double m2 = (double) studentTable.getValueAt(selectedRow, 3);
                    double m3 = (double) studentTable.getValueAt(selectedRow, 4);
                    double total = (double) studentTable.getValueAt(selectedRow, 5);
                    double avg = (double) studentTable.getValueAt(selectedRow, 6);
                    String grade = (String) studentTable.getValueAt(selectedRow, 7);

                    usnField.setText(usn);
                    nameField.setText(name);
                    mark1Field.setText(String.valueOf(m1));
                    mark2Field.setText(String.valueOf(m2));
                    mark3Field.setText(String.valueOf(m3));

                    totalValLabel.setText(String.format("%.1f / 300", total));
                    averageValLabel.setText(String.format("%.2f%%", avg));
                    gradeValLabel.setText(grade);
                    if ("F".equals(grade)) {
                        gradeValLabel.setForeground(COLOR_DANGER);
                    } else {
                        gradeValLabel.setForeground(COLOR_SUCCESS);
                    }
                }
            }
        });

        rightPanel.add(searchCard, BorderLayout.NORTH);
        rightPanel.add(tableCard, BorderLayout.CENTER);

        mainContainer.add(leftPanel, BorderLayout.WEST);
        mainContainer.add(rightPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);

        // Window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
    }

    // --- Core Action Implementations ---

    private void performPreview() {
        try {
            String m1Str = mark1Field.getText().trim();
            String m2Str = mark2Field.getText().trim();
            String m3Str = mark3Field.getText().trim();
            if (m1Str.isEmpty() || m2Str.isEmpty() || m3Str.isEmpty()) {
                showError("Please enter marks for all three subjects to preview calculations.");
                return;
            }
            double m1 = Double.parseDouble(m1Str);
            double m2 = Double.parseDouble(m2Str);
            double m3 = Double.parseDouble(m3Str);
            if (m1 < 0 || m1 > 100 || m2 < 0 || m2 > 100 || m3 < 0 || m3 > 100) {
                showError("Marks must be between 0 and 100.");
                return;
            }
            GradedStudent temp = new GradedStudent("temp", "temp", m1, m2, m3);
            updateSummaryLabels(temp);
        } catch (NumberFormatException ex) {
            showError("Marks must be valid numerical values.");
        }
    }

    private void addOrUpdateStudent() {
        if (!validateFields()) {
            return;
        }
        String usn = usnField.getText().trim();
        String name = nameField.getText().trim();
        double m1 = Double.parseDouble(mark1Field.getText().trim());
        double m2 = Double.parseDouble(mark2Field.getText().trim());
        double m3 = Double.parseDouble(mark3Field.getText().trim());

        GradedStudent student = new GradedStudent(usn, name, m1, m2, m3);

        try {
            boolean exists = DatabaseHelper.checkStudentExists(usn);
            if (exists) {
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "Student with USN " + usn + " already exists. Do you want to update their record?",
                        "Confirm Update",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (option == JOptionPane.YES_OPTION) {
                    DatabaseHelper.updateStudent(student);
                    showInfo("Record updated successfully!");
                    clearForm();
                    refreshTableData();
                }
            } else {
                DatabaseHelper.addStudent(student);
                showInfo("Record saved successfully!");
                clearForm();
                refreshTableData();
            }
        } catch (SQLException e) {
            showError("Database operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        String usn = usnField.getText().trim();
        if (usn.isEmpty()) {
            showError("Please select a student record or specify a USN to delete.");
            return;
        }
        try {
            boolean exists = DatabaseHelper.checkStudentExists(usn);
            if (!exists) {
                showError("No record found with USN " + usn);
                return;
            }
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to permanently delete USN: " + usn + "?",
                    "Confirm Record Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (option == JOptionPane.YES_OPTION) {
                DatabaseHelper.deleteStudent(usn);
                showInfo("Record deleted successfully.");
                clearForm();
                refreshTableData();
            }
        } catch (SQLException e) {
            showError("Database operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchStudent() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showError("Please enter a USN to search.");
            return;
        }
        try {
            GradedStudent student = DatabaseHelper.searchStudent(query);
            if (student == null) {
                showInfo("No record found with USN: " + query);
            } else {
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                        student.getUsn(),
                        student.getName(),
                        student.getMark1(),
                        student.getMark2(),
                        student.getMark3(),
                        student.getTotal(),
                        student.getAverage(),
                        student.getGrade()
                });
                
                // Select first row in filtered table to trigger selection listener
                studentTable.setRowSelectionInterval(0, 0);
            }
        } catch (SQLException e) {
            showError("Database query failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshTableData() {
        try {
            List<GradedStudent> list = DatabaseHelper.getAllStudents();
            tableModel.setRowCount(0);
            for (GradedStudent s : list) {
                tableModel.addRow(new Object[]{
                        s.getUsn(),
                        s.getName(),
                        s.getMark1(),
                        s.getMark2(),
                        s.getMark3(),
                        s.getTotal(),
                        s.getAverage(),
                        s.getGrade()
                });
            }
        } catch (SQLException e) {
            showError("Failed to retrieve student repository: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        usnField.setText("");
        nameField.setText("");
        mark1Field.setText("");
        mark2Field.setText("");
        mark3Field.setText("");

        totalValLabel.setText("0.0 / 300");
        totalValLabel.setForeground(COLOR_ACCENT);
        averageValLabel.setText("0.00%");
        averageValLabel.setForeground(COLOR_ACCENT);
        gradeValLabel.setText("-");
        gradeValLabel.setForeground(COLOR_TEXT_SEC);

        studentTable.clearSelection();
    }

    private void updateSummaryLabels(GradedStudent student) {
        totalValLabel.setText(String.format("%.1f / 300", student.getTotal()));
        averageValLabel.setText(String.format("%.2f%%", student.getAverage()));

        String grade = student.getGrade();
        gradeValLabel.setText(grade);
        if ("F".equals(grade)) {
            gradeValLabel.setForeground(COLOR_DANGER);
        } else {
            gradeValLabel.setForeground(COLOR_SUCCESS);
        }
    }

    private boolean validateFields() {
        String usn = usnField.getText().trim();
        String name = nameField.getText().trim();

        if (usn.isEmpty()) {
            showError("USN / Roll Number field cannot be empty.");
            usnField.requestFocus();
            return false;
        }
        if (name.isEmpty()) {
            showError("Student Name field cannot be empty.");
            nameField.requestFocus();
            return false;
        }

        double m1, m2, m3;
        try {
            m1 = Double.parseDouble(mark1Field.getText().trim());
            if (m1 < 0 || m1 > 100) {
                showError("Subject 1 Marks must be between 0.0 and 100.0.");
                mark1Field.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Subject 1 Marks must be a valid numerical value.");
            mark1Field.requestFocus();
            return false;
        }

        try {
            m2 = Double.parseDouble(mark2Field.getText().trim());
            if (m2 < 0 || m2 > 100) {
                showError("Subject 2 Marks must be between 0.0 and 100.0.");
                mark2Field.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Subject 2 Marks must be a valid numerical value.");
            mark2Field.requestFocus();
            return false;
        }

        try {
            m3 = Double.parseDouble(mark3Field.getText().trim());
            if (m3 < 0 || m3 > 100) {
                showError("Subject 3 Marks must be between 0.0 and 100.0.");
                mark3Field.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Subject 3 Marks must be a valid numerical value.");
            mark3Field.requestFocus();
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- Component Styling Helpers ---

    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(COLOR_BG);
        textField.setForeground(COLOR_TEXT_PRI);
        textField.setCaretColor(COLOR_TEXT_PRI);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return textField;
    }

    private JButton createStyledButton(String text, Color bg, Color fg, Color hoverBg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        // Initialize SQLite Database Tables
        DatabaseHelper.initializeDatabase();

        // Use standard Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // fallback
        }

        // Tweak basic UIManager UI defaults
        UIManager.put("ScrollBar.background", COLOR_BG);
        UIManager.put("ScrollBar.thumb", COLOR_BORDER);
        UIManager.put("OptionPane.background", COLOR_CARD);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXT_PRI);
        UIManager.put("Panel.background", COLOR_CARD);

        SwingUtilities.invokeLater(() -> {
            StudentResultSystem system = new StudentResultSystem();
            system.setVisible(true);
        });
    }
}
