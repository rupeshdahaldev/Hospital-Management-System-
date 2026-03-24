package com.hospital.ui;

import com.hospital.exception.EntityNotFoundException;
import com.hospital.model.*;
import com.hospital.service.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DoctorDashboardFrame extends JFrame {

    private AuthService authService;
    private AppointmentService appointmentService;
    private PatientService patientService;
    private NotificationService notificationService;
    private Doctor currentDoctor;

    private JTable appointmentTable, patientTable;
    private DefaultTableModel appointmentTableModel, patientTableModel;
    private JTextArea diagnosisArea, prescriptionArea, notesArea;
    private JList<String> notificationList;

    public DoctorDashboardFrame(AuthService authService) {
        this.authService        = authService;
        this.appointmentService = new AppointmentService();
        this.patientService     = new PatientService();
        this.notificationService = new NotificationService();
        loadDoctorInfo();
        initComponents();
        loadData();
    }

    private void initComponents() {
        String doctorName = (currentDoctor != null) ? currentDoctor.getFullName() : "Doctor";
        setTitle("Doctor Dashboard — " + doctorName);
        setSize(1200, 800);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LoginFrame.PRIMARY);
        headerPanel.setBorder(new EmptyBorder(14, 22, 14, 22));

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerLeft.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome, " + doctorName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerLeft.add(welcomeLabel);

        if (currentDoctor != null) {
            JLabel infoLabel = new JLabel("  ·  " + currentDoctor.getSpecialization() + " | " + currentDoctor.getDepartment());
            infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            infoLabel.setForeground(new Color(200, 210, 240));
            headerLeft.add(infoLabel);
        }

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerRight.setOpaque(false);

        JButton refreshButton = makeHeaderButton("Refresh", LoginFrame.ACCENT, LoginFrame.ACCENT.darker());
        refreshButton.addActionListener(e -> loadData());

        JButton logoutButton = makeHeaderButton("Logout", LoginFrame.DANGER, LoginFrame.DANGER.darker());
        logoutButton.addActionListener(e -> {
            authService.logout();
            new LoginFrame().setVisible(true);
            dispose();
        });

        headerRight.add(refreshButton);
        headerRight.add(logoutButton);
        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(headerRight, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.addTab("Appointments", createAppointmentsPanel());
        tabbedPane.addTab("Patients",     createPatientsPanel());
        tabbedPane.addTab("Notifications", createNotificationsPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = {"Appointment ID", "Patient ID", "Date", "Time Slot", "Status"};
        appointmentTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        appointmentTable = styledTable(appointmentTableModel);

        JPanel formPanel = new JPanel(new BorderLayout(0, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 190, 210), 1),
                "Complete Selected Appointment",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                LoginFrame.PRIMARY));
        formPanel.setBackground(Color.WHITE);

        JPanel textAreaPanel = new JPanel(new GridLayout(3, 1, 6, 6));
        textAreaPanel.setBackground(Color.WHITE);
        textAreaPanel.add(labeledArea("Diagnosis:",    diagnosisArea    = new JTextArea(3, 20)));
        textAreaPanel.add(labeledArea("Prescription:", prescriptionArea = new JTextArea(3, 20)));
        textAreaPanel.add(labeledArea("Notes:",        notesArea        = new JTextArea(2, 20)));

        JButton completeButton = makeActionButton("Mark as Completed", LoginFrame.ACCENT, LoginFrame.ACCENT.darker());
        completeButton.addActionListener(e -> handleCompleteAppointment());

        formPanel.add(textAreaPanel, BorderLayout.CENTER);
        formPanel.add(completeButton, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(appointmentTable), formPanel);
        split.setDividerLocation(350);
        split.setResizeWeight(0.6);
        panel.add(split);
        return panel;
    }

    private JPanel createPatientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] columns = {"Patient ID", "Full Name", "Age", "Blood Group"};
        patientTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        patientTable = styledTable(patientTableModel);
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createNotificationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        notificationList = new JList<>();
        notificationList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notificationList.setFixedCellHeight(28);
        panel.add(new JScrollPane(notificationList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel labeledArea(String label, JTextArea area) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(LoginFrame.TEXT_MAIN);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225), 1),
                new EmptyBorder(4, 6, 4, 6)));
        p.add(lbl, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setGridColor(new Color(225, 230, 240));
        table.setSelectionBackground(new Color(210, 220, 255));
        table.setSelectionForeground(LoginFrame.TEXT_MAIN);
        table.getTableHeader().setReorderingAllowed(false);

        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                lbl.setBackground(LoginFrame.PRIMARY);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.LEFT);
                lbl.setOpaque(true);
                lbl.setBorder(new EmptyBorder(0, 8, 0, 8));
                return lbl;
            }
        });

        return table;
    }

    private JButton makeHeaderButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(110, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private JButton makeActionButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(220, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private void loadDoctorInfo() {
        User u = authService.getCurrentUser();
        if (u instanceof Doctor) {
            currentDoctor = (Doctor) u;
        }
    }

    private void loadData() {
        loadAppointments();
        loadPatients();
        loadNotifications();
    }

    private void loadAppointments() {
        if (currentDoctor == null) return;
        appointmentTableModel.setRowCount(0);
        for (Appointment a : appointmentService.getAppointmentsByDoctor(currentDoctor.getDoctorId())) {
            appointmentTableModel.addRow(new Object[]{
                    a.getAppointmentId(), a.getPatientId(), a.getAppointmentDate(), a.getTimeSlot(), a.getStatus()});
        }
    }

    private void loadPatients() {
        if (currentDoctor == null) return;
        patientTableModel.setRowCount(0);
        for (Patient p : patientService.getPatientsByDoctor(currentDoctor.getDoctorId())) {
            patientTableModel.addRow(new Object[]{
                    p.getPatientId(), p.getFullName(), p.getAge(), p.getBloodGroup()});
        }
    }

    private void loadNotifications() {
        if (currentDoctor == null) return;
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Notification n : notificationService.getUnreadNotificationsForUser(currentDoctor.getUserId())) {
            model.addElement("• " + n.getTitle() + " — " + n.getMessage());
        }
        notificationList.setModel(model);
    }

    private void handleCompleteAppointment() {
        int row = appointmentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String id = (String) appointmentTableModel.getValueAt(row, 0);
            appointmentService.addDiagnosisAndPrescription(id,
                    diagnosisArea.getText(), prescriptionArea.getText(), notesArea.getText());
            JOptionPane.showMessageDialog(this, "Appointment marked as completed.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAppointments();
            diagnosisArea.setText("");
            prescriptionArea.setText("");
            notesArea.setText("");
        } catch (EntityNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Not Found", JOptionPane.ERROR_MESSAGE);
        }
    }
}
