package com.hospital.ui;

import com.hospital.exception.InvalidCredentialsException;
import com.hospital.model.User;
import com.hospital.service.AuthService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private AuthService authService;

    static final Color PRIMARY      = new Color(63,  81, 181);
    static final Color PRIMARY_DARK = new Color(48,  63, 159);
    static final Color ACCENT       = new Color(46, 125,  50);
    static final Color BG_OUTER     = new Color(245, 247, 251);
    static final Color BG_CARD      = Color.WHITE;
    static final Color TEXT_MAIN    = new Color(33,  33,  33);
    static final Color TEXT_MUTED   = new Color(97,  97,  97);
    static final Color ERROR_COLOR  = new Color(198, 40,  40);
    static final Color BORDER_COLOR = new Color(218, 220, 224);
    static final Color SUCCESS      = new Color(46, 125,  50);
    static final Color DANGER       = new Color(198, 40,  40);

    public LoginFrame() {
        this.authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Hospital Management System — Login");
        setSize(820, 620);
        setMinimumSize(new Dimension(600, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(BG_OUTER);
        outerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 235), 1),
                new EmptyBorder(40, 50, 40, 50)));
        card.setMaximumSize(new Dimension(420, 600));

        JLabel iconLabel = new JLabel("HMS");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        iconLabel.setForeground(PRIMARY);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(12));

        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Sign in to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(28));

        card.add(makeFieldLabel("Username"));
        usernameField = new JTextField(20);
        styleTextField(usernameField);
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));

        card.add(makeFieldLabel("Password"));
        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(10));

        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(14));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(BG_CARD);
        btnRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = makeAccentButton("Login", PRIMARY, PRIMARY_DARK);
        loginButton.addActionListener(e -> handleLogin());

        JButton roleButton = makeAccentButton("Select Role",
                new Color(120, 130, 150), new Color(90, 100, 120));
        roleButton.addActionListener(e -> showRoleSelection());

        btnRow.add(loginButton);
        btnRow.add(roleButton);
        card.add(btnRow);
        card.add(Box.createVerticalStrut(28));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(320, 1));
        sep.setForeground(new Color(210, 215, 230));
        card.add(sep);
        card.add(Box.createVerticalStrut(14));

        JLabel hintTitle = new JLabel("Default Credentials");
        hintTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hintTitle.setForeground(TEXT_MUTED);
        hintTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(hintTitle);
        card.add(Box.createVerticalStrut(6));

        addHintRow(card, "Admin:",   "admin / admin123");
        addHintRow(card, "Doctor:",  "doctor1 / doc123");
        addHintRow(card, "Patient:", "patient1 / pat123");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        outerPanel.add(card, gbc);
        add(outerPanel, BorderLayout.CENTER);

        passwordField.addActionListener(e -> handleLogin());
        usernameField.addActionListener(e -> passwordField.requestFocus());
    }

    private JLabel makeFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_MAIN);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(320, 36));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 200, 220), 1),
                new EmptyBorder(6, 10, 6, 10)));
    }

    private JButton makeAccentButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(130, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private void addHintRow(JPanel parent, String roleLabel, String creds) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        row.setBackground(BG_CARD);
        row.setMaximumSize(new Dimension(320, 22));
        JLabel lbl = new JLabel(roleLabel);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        JLabel val = new JLabel(creds);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        val.setForeground(TEXT_MUTED);
        row.add(lbl);
        row.add(val);
        parent.add(row);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password.");
            return;
        }

        try {
            User user = authService.login(username, password);

            switch (user.getRole()) {
                case "ADMIN":
                    new AdminDashboardFrame(authService).setVisible(true);
                    break;
                case "DOCTOR":
                    new DoctorDashboardFrame(authService).setVisible(true);
                    break;
                case "PATIENT":
                    new PatientDashboardFrame(authService).setVisible(true);
                    break;
                default:
                    showError("Unknown role: " + user.getRole());
                    return;
            }
            dispose();

        } catch (InvalidCredentialsException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            showError("Login failed. Please try again or restart the application.");
        }
    }

    private void showRoleSelection() {
        new RoleSelectionFrame().setVisible(true);
        dispose();
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
