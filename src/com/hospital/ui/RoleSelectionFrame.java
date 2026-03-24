package com.hospital.ui;

import com.hospital.service.BedService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class RoleSelectionFrame extends JFrame {
    private BedService bedService;

    public RoleSelectionFrame() {
        this.bedService = new BedService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Hospital Management System — Select Role");
        setSize(920, 700);
        setMinimumSize(new Dimension(760, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── Header ───────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(LoginFrame.PRIMARY);
        headerPanel.setBorder(new EmptyBorder(28, 30, 28, 30));

        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Select your role to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(200, 210, 240));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // ── Role cards ───────────────────────────────────────────────────
        JPanel rolesPanel = new JPanel(new GridLayout(1, 3, 24, 0));
        rolesPanel.setBackground(LoginFrame.BG_OUTER);
        rolesPanel.setBorder(new EmptyBorder(50, 60, 50, 60));

        rolesPanel.add(createRoleCard("A", "Admin",
                "Full system access", "Manage all records",
                LoginFrame.PRIMARY));
        rolesPanel.add(createRoleCard("D", "Doctor",
                "Medical professional", "Appointments & patients",
                new Color(34, 153, 84)));
        rolesPanel.add(createRoleCard("P", "Patient",
                "View your records", "Track appointments",
                new Color(142, 68, 173)));

        add(rolesPanel, BorderLayout.CENTER);

        // ── Bed availability footer ───────────────────────────────────────
        JPanel bedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 18));
        bedPanel.setBackground(new Color(30, 39, 60));
        bedPanel.setBorder(new EmptyBorder(6, 20, 6, 20));

        Map<String, Long> availability = bedService.getBedAvailabilityStats();

        bedPanel.add(createBedStat("ICU Beds",       availability.getOrDefault("ICU", 0L),       new Color(231, 76, 60)));
        bedPanel.add(createBedStat("General Beds",   availability.getOrDefault("GENERAL", 0L),   new Color(52, 152, 219)));
        bedPanel.add(createBedStat("Emergency Beds",  availability.getOrDefault("EMERGENCY", 0L), new Color(230, 126, 34)));

        add(bedPanel, BorderLayout.SOUTH);
    }

    private JPanel createRoleCard(String initial, String role, String desc1, String desc2, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 235), 1),
                new EmptyBorder(32, 28, 32, 28)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Coloured top accent bar
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setMaximumSize(new Dimension(80, 5));
        accentBar.setPreferredSize(new Dimension(80, 5));
        accentBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(accentBar);
        card.add(Box.createVerticalStrut(20));

        JLabel emojiLabel = new JLabel(initial);
        emojiLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        emojiLabel.setForeground(accent);
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        roleLabel.setForeground(accent);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(120, 1));
        sep.setForeground(new Color(220, 225, 240));

        JLabel desc1Label = new JLabel(desc1);
        desc1Label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc1Label.setForeground(LoginFrame.TEXT_MUTED);
        desc1Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc2Label = new JLabel(desc2);
        desc2Label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc2Label.setForeground(LoginFrame.TEXT_MUTED);
        desc2Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(emojiLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(roleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(sep);
        card.add(Box.createVerticalStrut(12));
        card.add(desc1Label);
        card.add(Box.createVerticalStrut(4));
        card.add(desc2Label);
        card.add(Box.createVerticalStrut(20));

        // "Login →" button
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setBackground(accent);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(140, 36));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverColor = accent.darker();
        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { loginBtn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent e)  { loginBtn.setBackground(accent); }
        });
        loginBtn.addActionListener(e -> navigateToLogin());

        card.add(loginBtn);

        // Hover highlight on the whole card
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(248, 250, 255)); }
            public void mouseExited(MouseEvent e)  { card.setBackground(Color.WHITE); }
            public void mouseClicked(MouseEvent e) { navigateToLogin(); }
        });

        return card;
    }

    private JPanel createBedStat(String label, long count, Color countColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(30, 39, 60));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelText.setForeground(new Color(170, 180, 200));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countText = new JLabel(String.valueOf(count) + " available");
        countText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        countText.setForeground(countColor);
        countText.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(labelText);
        panel.add(Box.createVerticalStrut(4));
        panel.add(countText);
        return panel;
    }

    private void navigateToLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}

