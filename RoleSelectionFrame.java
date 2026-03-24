package com.hospital.ui;

import com.hospital.exception.*;
import com.hospital.model.*;
import com.hospital.service.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class AdminDashboardFrame extends JFrame {

    private AuthService authService;
    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private BedService bedService;

    private JTabbedPane tabbedPane;
    private JLabel totalPatientsLabel, totalDoctorsLabel, totalAppointmentsLabel, availableBedsLabel;

    private JTable patientTable, doctorTable, appointmentTable, bedTable;
    private DefaultTableModel patientTableModel, doctorTableModel,
            appointmentTableModel, bedTableModel;

    private JTextField patUsernameField, patPasswordField, patNameField,
            patEmailField, patPhoneField, patAgeField,
            patAddressField, patEmergencyContactField;
    private JComboBox<String> patGenderCombo, patBloodGroupCombo;

    private JTextField docUsernameField, docPasswordField, docNameField,
            docEmailField, docPhoneField, docSpecializationField,
            docQualificationField, docExperienceField;
    private JComboBox<String> docDepartmentCombo;

    private JTextField aptPatientIdField, aptDoctorIdField;
    private JComboBox<String> aptTimeSlotCombo;
    private JSpinner aptDateSpinner;

    private JTextField bedIdField, bedChargeField;
    private JComboBox<String> bedTypeCombo;

    private static final String[] GENDERS = {"Male", "Female"};
    private static final String[] BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    private static final String[] DEPARTMENTS = {
        "Cardiology", "Neurology", "Pediatrics", "Orthopedics",
        "Oncology", "Radiology", "Emergency", "General Surgery",
        "Obstetrics & Gynecology", "Psychiatry", "Dermatology", "Other"
    };
    private static final String[] TIME_SLOTS = {
        "09:00-10:00", "10:00-11:00", "11:00-12:00", "14:00-15:00", "15:00-16:00"
    };

    public AdminDashboardFrame(AuthService authService) {
        this.authService        = authService;
        this.patientService     = new PatientService();
        this.doctorService      = new DoctorService();
        this.appointmentService = new AppointmentService();
        this.bedService         = new BedService();
        initComponents();
        loadAllData();
        updateAnalytics();
    }

    private void initComponents() {
        setTitle("Admin Dashboard — Hospital Management System");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LoginFrame.PRIMARY);
        header.setBorder(new EmptyBorder(14, 22, 14, 22));

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JPanel hLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hLeft.setOpaque(false);
        hLeft.add(title);

        JPanel hRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        hRight.setOpaque(false);

        JButton refresh = hdrBtn("Refresh All", LoginFrame.SUCCESS, LoginFrame.SUCCESS.darker());
        refresh.addActionListener(e -> {
            loadAllData();
            updateAnalytics();
            JOptionPane.showMessageDialog(this, "Data refreshed.", "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton logout = hdrBtn("Logout", LoginFrame.DANGER, LoginFrame.DANGER.darker());
        logout.addActionListener(e -> {
            authService.logout();
            new LoginFrame().setVisible(true);
            dispose();
        });

        hRight.add(refresh);
        hRight.add(logout);
        header.add(hLeft, BorderLayout.WEST);
        header.add(hRight, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.addTab("Analytics",    createAnalyticsPanel());
        tabbedPane.addTab("Patients",     createPatientPanel());
        tabbedPane.addTab("Doctors",      createDoctorPanel());
        tabbedPane.addTab("Appointments", createAppointmentPanel());
        tabbedPane.addTab("Beds",         createBedPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAnalyticsPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LoginFrame.BG_OUTER);

        JPanel row = new JPanel(new GridLayout(1, 4, 20, 0));
        row.setBackground(LoginFrame.BG_OUTER);
        row.setBorder(new EmptyBorder(40, 40, 40, 40));

        row.add(statCard("Total Patients",   totalPatientsLabel     = new JLabel("0"), LoginFrame.PRIMARY));
        row.add(statCard("Total Doctors",    totalDoctorsLabel      = new JLabel("0"), LoginFrame.SUCCESS));
        row.add(statCard("Appointments",     totalAppointmentsLabel = new JLabel("0"), new Color(245, 124, 0)));
        row.add(statCard("Available Beds",   availableBedsLabel     = new JLabel("0"), new Color(142, 36, 170)));

        wrapper.add(row, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel statCard(String label, JLabel val, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LoginFrame.BORDER_COLOR, 1),
                new EmptyBorder(24, 24, 24, 24)));

        JPanel bar = new JPanel();
        bar.setBackground(accent);
        bar.setMaximumSize(new Dimension(60, 4));
        bar.setPreferredSize(new Dimension(60, 4));
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);

        val.setFont(new Font("Segoe UI", Font.BOLD, 40));
        val.setForeground(accent);
        val.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(LoginFrame.TEXT_MUTED);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(bar);
        card.add(Box.createVerticalStrut(14));
        card.add(val);
        card.add(Box.createVerticalStrut(8));
        card.add(lbl);
        return card;
    }

    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(LoginFrame.BG_OUTER);

        patientTableModel = new DefaultTableModel(
                new String[]{"Patient ID", "Full Name", "Age", "Gender", "Blood Group", "Phone"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        patientTable = buildTable(patientTableModel);
        rightAlign(patientTable, 2);
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populatePatientForm();
        });

        JPanel form = formContainer("Patient Details");
        JPanel fields = new JPanel(new GridLayout(0, 2, 8, 8));
        fields.setBackground(Color.WHITE);

        fields.add(secHdr("Basic Information")); fields.add(new JLabel());
        fields.add(fLbl("Username *"));
        patUsernameField = tf("Unique login ID (4–20 chars)");
        patUsernameField.setToolTipText("4–20 characters: letters, digits, underscores only.");
        fields.add(patUsernameField);
        fields.add(fLbl("Password *"));   patPasswordField = tf("Min. 6 characters");  fields.add(patPasswordField);
        fields.add(fLbl("Full Name *"));  patNameField     = tf("Enter full name");     fields.add(patNameField);

        fields.add(secHdr("Contact Information")); fields.add(new JLabel());
        fields.add(fLbl("Email *"));                       patEmailField            = tf("example@email.com");       fields.add(patEmailField);
        fields.add(fLbl("Phone * (10 digits)"));           patPhoneField            = numField(10, "10-digit number"); fields.add(patPhoneField);
        fields.add(fLbl("Emergency Contact * (10 digits)")); patEmergencyContactField = numField(10, "10-digit number"); fields.add(patEmergencyContactField);
        fields.add(fLbl("Address *"));                     patAddressField          = tf("Street, City");            fields.add(patAddressField);

        fields.add(secHdr("Medical Information")); fields.add(new JLabel());
        fields.add(fLbl("Age *"));         patAgeField        = numField(3, "0–150");  fields.add(patAgeField);
        fields.add(fLbl("Gender *"));      patGenderCombo     = combo(GENDERS);        fields.add(patGenderCombo);
        fields.add(fLbl("Blood Group *")); patBloodGroupCombo = combo(BLOOD_GROUPS);  fields.add(patBloodGroupCombo);

        form.add(fields, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btns.setBackground(Color.WHITE);
        btns.add(actBtn("Add Patient",    LoginFrame.SUCCESS, LoginFrame.SUCCESS.darker(), e -> handleAddPatient()));
        btns.add(actBtn("Delete Patient", LoginFrame.DANGER,  LoginFrame.DANGER.darker(),  e -> handleDeletePatient()));
        btns.add(actBtn("Clear Form",     new Color(96, 96, 96), new Color(60, 60, 60),    e -> clearPatientForm()));
        form.add(btns, BorderLayout.SOUTH);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(patientTable), form);
        sp.setDividerLocation(640);
        sp.setResizeWeight(0.6);
        panel.add(sp);
        return panel;
    }

    private JPanel createDoctorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(LoginFrame.BG_OUTER);

        doctorTableModel = new DefaultTableModel(
                new String[]{"Doctor ID", "Full Name", "Specialization", "Qualification", "Exp. (Yrs)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        doctorTable = buildTable(doctorTableModel);
        rightAlign(doctorTable, 4);
        doctorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateDoctorForm();
        });

        JPanel form = formContainer("Doctor Details");
        JPanel fields = new JPanel(new GridLayout(0, 2, 8, 8));
        fields.setBackground(Color.WHITE);

        fields.add(secHdr("Basic Information")); fields.add(new JLabel());
        fields.add(fLbl("Username *"));
        docUsernameField = tf("Unique login ID (4–20 chars)");
        docUsernameField.setToolTipText("4–20 characters: letters, digits, underscores only.");
        fields.add(docUsernameField);
        fields.add(fLbl("Password *"));  docPasswordField = tf("Min. 6 characters"); fields.add(docPasswordField);
        fields.add(fLbl("Full Name *")); docNameField     = tf("Dr. First Last");    fields.add(docNameField);

        fields.add(secHdr("Contact Information")); fields.add(new JLabel());
        fields.add(fLbl("Email *"));             docEmailField = tf("example@hospital.com");      fields.add(docEmailField);
        fields.add(fLbl("Phone * (10 digits)")); docPhoneField = numField(10, "10-digit number"); fields.add(docPhoneField);

        fields.add(secHdr("Professional Information")); fields.add(new JLabel());
        fields.add(fLbl("Specialization *"));   docSpecializationField = tf("e.g. Cardiology"); fields.add(docSpecializationField);
        fields.add(fLbl("Qualification *"));    docQualificationField  = tf("e.g. MD, FACC");   fields.add(docQualificationField);
        fields.add(fLbl("Experience (Yrs) *")); docExperienceField     = numField(2, "0–60");    fields.add(docExperienceField);
        fields.add(fLbl("Department *"));       docDepartmentCombo     = combo(DEPARTMENTS);     fields.add(docDepartmentCombo);

        form.add(fields, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btns.setBackground(Color.WHITE);
        btns.add(actBtn("Add Doctor",    LoginFrame.SUCCESS, LoginFrame.SUCCESS.darker(), e -> handleAddDoctor()));
        btns.add(actBtn("Delete Doctor", LoginFrame.DANGER,  LoginFrame.DANGER.darker(),  e -> handleDeleteDoctor()));
        btns.add(actBtn("Clear Form",    new Color(96, 96, 96), new Color(60, 60, 60),    e -> clearDoctorForm()));
        form.add(btns, BorderLayout.SOUTH);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(doctorTable), form);
        sp.setDividerLocation(640);
        sp.setResizeWeight(0.6);
        panel.add(sp);
        return panel;
    }

    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(LoginFrame.BG_OUTER);

        appointmentTableModel = new DefaultTableModel(
                new String[]{"Appointment ID", "Patient ID", "Doctor ID", "Date", "Time Slot", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        appointmentTable = buildTable(appointmentTableModel);

        JPanel form = formContainer("Schedule Appointment");
        JPanel fields = new JPanel(new GridLayout(0, 2, 8, 8));
        fields.setBackground(Color.WHITE);

        fields.add(fLbl("Patient ID *")); aptPatientIdField = tf("e.g. PAT-000001"); fields.add(aptPatientIdField);
        fields.add(fLbl("Doctor ID *"));  aptDoctorIdField  = tf("e.g. DOC-000001"); fields.add(aptDoctorIdField);

        fields.add(fLbl("Date *"));
        aptDateSpinner = new JSpinner(new SpinnerDateModel());
        aptDateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aptDateSpinner.setBorder(BorderFactory.createLineBorder(LoginFrame.BORDER_COLOR, 1));
        aptDateSpinner.setPreferredSize(new Dimension(0, 32));
        if (aptDateSpinner.getEditor() instanceof JSpinner.DefaultEditor de)
            de.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fields.add(aptDateSpinner);

        fields.add(fLbl("Time Slot *")); aptTimeSlotCombo = combo(TIME_SLOTS); fields.add(aptTimeSlotCombo);
        form.add(fields, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btns.setBackground(Color.WHITE);
        btns.add(actBtn("Schedule",     LoginFrame.SUCCESS, LoginFrame.SUCCESS.darker(), e -> handleScheduleAppointment()));
        btns.add(actBtn("Cancel Appt.", LoginFrame.DANGER,  LoginFrame.DANGER.darker(),  e -> handleCancelAppointment()));
        form.add(btns, BorderLayout.SOUTH);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(appointmentTable), form);
        sp.setDividerLocation(640);
        sp.setResizeWeight(0.6);
        panel.add(sp);
        return panel;
    }

    private JPanel createBedPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(LoginFrame.BG_OUTER);

        bedTableModel = new DefaultTableModel(
                new String[]{"Bed ID", "Ward Type", "Status", "Assigned Patient", "Daily Charge ($)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bedTable = buildTable(bedTableModel);
        rightAlign(bedTable, 4);

        JPanel form = formContainer("Add Bed");
        JPanel fields = new JPanel(new GridLayout(0, 2, 8, 8));
        fields.setBackground(Color.WHITE);

        fields.add(fLbl("Bed ID *"));           bedIdField     = tf("e.g. ICU-01");   fields.add(bedIdField);
        fields.add(fLbl("Ward Type *"));        bedTypeCombo   = combo(new String[]{"ICU", "GENERAL", "EMERGENCY"}); fields.add(bedTypeCombo);
        fields.add(fLbl("Daily Charge ($) *")); bedChargeField = tf("Amount per day"); fields.add(bedChargeField);
        form.add(fields, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btns.setBackground(Color.WHITE);
        btns.add(actBtn("Add Bed",    LoginFrame.SUCCESS, LoginFrame.SUCCESS.darker(), e -> handleAddBed()));
        btns.add(actBtn("Delete Bed", LoginFrame.DANGER,  LoginFrame.DANGER.darker(),  e -> handleDeleteBed()));
        form.add(btns, BorderLayout.SOUTH);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(bedTable), form);
        sp.setDividerLocation(640);
        sp.setResizeWeight(0.6);
        panel.add(sp);
        return panel;
    }

    private void populatePatientForm() {
        int r = patientTable.getSelectedRow();
        if (r == -1) return;
        patNameField.setText((String) patientTableModel.getValueAt(r, 1));
        patAgeField.setText(String.valueOf(patientTableModel.getValueAt(r, 2)));
        patGenderCombo.setSelectedItem(patientTableModel.getValueAt(r, 3));
        patBloodGroupCombo.setSelectedItem(patientTableModel.getValueAt(r, 4));
        patPhoneField.setText((String) patientTableModel.getValueAt(r, 5));
    }

    private void populateDoctorForm() {
        int r = doctorTable.getSelectedRow();
        if (r == -1) return;
        docNameField.setText((String) doctorTableModel.getValueAt(r, 1));
        docSpecializationField.setText((String) doctorTableModel.getValueAt(r, 2));
        docQualificationField.setText((String) doctorTableModel.getValueAt(r, 3));
        docExperienceField.setText(String.valueOf(doctorTableModel.getValueAt(r, 4)));
    }

    private void clearPatientForm() {
        for (JTextField f : new JTextField[]{patUsernameField, patPasswordField, patNameField,
                patEmailField, patPhoneField, patAgeField, patAddressField, patEmergencyContactField})
            f.setText("");
        patGenderCombo.setSelectedIndex(0);
        patBloodGroupCombo.setSelectedIndex(0);
        patientTable.clearSelection();
    }

    private void clearDoctorForm() {
        for (JTextField f : new JTextField[]{docUsernameField, docPasswordField, docNameField,
                docEmailField, docPhoneField, docSpecializationField, docQualificationField, docExperienceField})
            f.setText("");
        docDepartmentCombo.setSelectedIndex(0);
        doctorTable.clearSelection();
    }

    private boolean validatePatientForm() {
        String un = patUsernameField.getText().trim();
        String pw = patPasswordField.getText().trim();
        String nm = patNameField.getText().trim();
        String em = patEmailField.getText().trim();
        String ph = patPhoneField.getText().trim();
        String ag = patAgeField.getText().trim();
        String ad = patAddressField.getText().trim();
        String ec = patEmergencyContactField.getText().trim();

        if (un.isEmpty() || !un.matches("[a-zA-Z0-9_]{4,20}"))
            return verr("Username must be 4–20 characters (letters, digits, underscores only).");
        if (pw.length() < 6)
            return verr("Password must be at least 6 characters.");
        if (nm.isEmpty())
            return verr("Full name is required.");
        if (!em.matches("^[A-Za-z0-9+_.-]+@.+\\..+$"))
            return verr("Enter a valid email address.");
        if (!ph.matches("[0-9]{10}"))
            return verr("Phone must be exactly 10 digits.");
        if (!ec.matches("[0-9]{10}"))
            return verr("Emergency contact must be exactly 10 digits.");
        if (ad.isEmpty())
            return verr("Address is required.");
        if (ag.isEmpty())
            return verr("Age is required.");
        try {
            int a = Integer.parseInt(ag);
            if (a < 0 || a > 150) return verr("Age must be between 0 and 150.");
        } catch (NumberFormatException e) {
            return verr("Age must be a valid number.");
        }
        return true;
    }

    private boolean validateDoctorForm() {
        String un = docUsernameField.getText().trim();
        String pw = docPasswordField.getText().trim();
        String nm = docNameField.getText().trim();
        String em = docEmailField.getText().trim();
        String ph = docPhoneField.getText().trim();
        String sp = docSpecializationField.getText().trim();
        String ql = docQualificationField.getText().trim();
        String ex = docExperienceField.getText().trim();

        if (un.isEmpty() || !un.matches("[a-zA-Z0-9_]{4,20}"))
            return verr("Username must be 4–20 characters (letters, digits, underscores only).");
        if (pw.length() < 6)
            return verr("Password must be at least 6 characters.");
        if (nm.isEmpty())
            return verr("Full name is required.");
        if (!em.matches("^[A-Za-z0-9+_.-]+@.+\\..+$"))
            return verr("Enter a valid email address.");
        if (!ph.matches("[0-9]{10}"))
            return verr("Phone must be exactly 10 digits.");
        if (sp.isEmpty())
            return verr("Specialization is required.");
        if (ql.isEmpty())
            return verr("Qualification is required.");
        if (ex.isEmpty())
            return verr("Experience is required.");
        try {
            int e = Integer.parseInt(ex);
            if (e < 0 || e > 60) return verr("Experience must be between 0 and 60 years.");
        } catch (NumberFormatException e) {
            return verr("Experience must be a valid number.");
        }
        return true;
    }

    private boolean verr(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    private void handleAddPatient() {
        if (!validatePatientForm()) return;
        try {
            patientService.addPatient(
                    patUsernameField.getText().trim(), patPasswordField.getText().trim(),
                    patNameField.getText().trim(), patEmailField.getText().trim(),
                    patPhoneField.getText().trim(), Integer.parseInt(patAgeField.getText().trim()),
                    (String) patGenderCombo.getSelectedItem(), (String) patBloodGroupCombo.getSelectedItem(),
                    patAddressField.getText().trim(), patEmergencyContactField.getText().trim());
            JOptionPane.showMessageDialog(this, "Patient added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearPatientForm();
            loadPatients();
            updateAnalytics();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeletePatient() {
        int r = patientTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Please select a patient to delete."); return; }
        if (JOptionPane.showConfirmDialog(this,
                "Delete patient \"" + patientTableModel.getValueAt(r, 1) + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        try {
            patientService.deletePatient((String) patientTableModel.getValueAt(r, 0));
            JOptionPane.showMessageDialog(this, "Patient deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearPatientForm();
            loadPatients();
            updateAnalytics();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddDoctor() {
        if (!validateDoctorForm()) return;
        try {
            doctorService.addDoctor(
                    docUsernameField.getText().trim(), docPasswordField.getText().trim(),
                    docNameField.getText().trim(), docEmailField.getText().trim(),
                    docPhoneField.getText().trim(), docSpecializationField.getText().trim(),
                    docQualificationField.getText().trim(), Integer.parseInt(docExperienceField.getText().trim()),
                    (String) docDepartmentCombo.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Doctor added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearDoctorForm();
            loadDoctors();
            updateAnalytics();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteDoctor() {
        int r = doctorTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Please select a doctor to delete."); return; }
        if (JOptionPane.showConfirmDialog(this,
                "Delete doctor \"" + doctorTableModel.getValueAt(r, 1) + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        try {
            doctorService.deleteDoctor((String) doctorTableModel.getValueAt(r, 0));
            JOptionPane.showMessageDialog(this, "Doctor deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearDoctorForm();
            loadDoctors();
            updateAnalytics();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleScheduleAppointment() {
        String pid = aptPatientIdField.getText().trim();
        String did = aptDoctorIdField.getText().trim();
        if (pid.isEmpty() || did.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID and Doctor ID are required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            java.util.Date d = (java.util.Date) aptDateSpinner.getValue();
            LocalDate ld = new java.sql.Date(d.getTime()).toLocalDate();
            appointmentService.scheduleAppointment(pid, did, ld, (String) aptTimeSlotCombo.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Appointment scheduled.", "Success", JOptionPane.INFORMATION_MESSAGE);
            aptPatientIdField.setText("");
            aptDoctorIdField.setText("");
            loadAppointments();
            updateAnalytics();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancelAppointment() {
        int r = appointmentTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Please select an appointment to cancel."); return; }
        if (JOptionPane.showConfirmDialog(this, "Cancel this appointment?", "Confirm Cancel",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        try {
            appointmentService.cancelAppointment((String) appointmentTableModel.getValueAt(r, 0));
            JOptionPane.showMessageDialog(this, "Appointment cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAppointments();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddBed() {
        String id = bedIdField.getText().trim();
        String ch = bedChargeField.getText().trim();
        if (id.isEmpty() || ch.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bed ID and Daily Charge are required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double charge = Double.parseDouble(ch);
            if (charge < 0) throw new NumberFormatException("negative");
            bedService.addBed(id, (String) bedTypeCombo.getSelectedItem(), charge);
            JOptionPane.showMessageDialog(this, "Bed added.", "Success", JOptionPane.INFORMATION_MESSAGE);
            bedIdField.setText("");
            bedChargeField.setText("");
            loadBeds();
            updateAnalytics();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Daily Charge must be a valid non-negative number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteBed() {
        int r = bedTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Please select a bed to delete."); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete bed \"" + bedTableModel.getValueAt(r, 0) + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        try {
            bedService.deleteBed((String) bedTableModel.getValueAt(r, 0));
            JOptionPane.showMessageDialog(this, "Bed deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBeds();
            updateAnalytics();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllData() { loadPatients(); loadDoctors(); loadAppointments(); loadBeds(); }

    private void loadPatients() {
        patientTableModel.setRowCount(0);
        for (Patient p : patientService.getAllPatients())
            patientTableModel.addRow(new Object[]{
                    p.getPatientId(), p.getFullName(), p.getAge(), p.getGender(), p.getBloodGroup(), p.getPhone()});
    }

    private void loadDoctors() {
        doctorTableModel.setRowCount(0);
        for (Doctor d : doctorService.getDoctors())
            doctorTableModel.addRow(new Object[]{
                    d.getDoctorId(), d.getFullName(), d.getSpecialization(), d.getQualification(), d.getExperienceYears()});
    }

    private void loadAppointments() {
        appointmentTableModel.setRowCount(0);
        for (Appointment a : appointmentService.getAllAppointments())
            appointmentTableModel.addRow(new Object[]{
                    a.getAppointmentId(), a.getPatientId(), a.getDoctorId(),
                    a.getAppointmentDate(), a.getTimeSlot(), a.getStatus()});
    }

    private void loadBeds() {
        bedTableModel.setRowCount(0);
        for (Bed b : bedService.getAllBeds())
            bedTableModel.addRow(new Object[]{
                    b.getBedId(), b.getWardType(), b.getStatus(), b.getAssignedPatientId(), b.getDailyCharge()});
    }

    private void updateAnalytics() {
        totalPatientsLabel.setText(String.valueOf(patientService.getAllPatients().size()));
        totalDoctorsLabel.setText(String.valueOf(doctorService.getDoctors().size()));
        totalAppointmentsLabel.setText(String.valueOf(appointmentService.getAllAppointments().size()));
        availableBedsLabel.setText(String.valueOf(bedService.getAvailableBeds().size()));
    }

    /**
     * Builds a consistently styled JTable and applies a custom header renderer so
     * that the column headers stay visible regardless of the system look-and-feel.
     * Some L&Fs (especially on Windows) override JTableHeader's background unless
     * a custom renderer forces it.
     */
    private JTable buildTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(28);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setGridColor(LoginFrame.BORDER_COLOR);
        t.setBackground(Color.WHITE);
        t.setSelectionBackground(new Color(232, 234, 246));
        t.setSelectionForeground(LoginFrame.TEXT_MAIN);
        t.getTableHeader().setReorderingAllowed(false);

        // Custom renderer keeps column headers painted correctly on all platforms
        t.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(LoginFrame.PRIMARY);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setHorizontalAlignment(JLabel.LEFT);
                lbl.setOpaque(true);
                lbl.setBorder(new EmptyBorder(0, 8, 0, 8));
                return lbl;
            }
        });

        DefaultTableCellRenderer leftRend = new DefaultTableCellRenderer();
        leftRend.setHorizontalAlignment(JLabel.LEFT);
        for (int i = 0; i < model.getColumnCount(); i++)
            t.getColumnModel().getColumn(i).setCellRenderer(leftRend);

        return t;
    }

    private void rightAlign(JTable t, int col) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.RIGHT);
        t.getColumnModel().getColumn(col).setCellRenderer(r);
    }

    private JPanel formContainer(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(LoginFrame.BORDER_COLOR, 1), title,
                        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13), LoginFrame.PRIMARY),
                new EmptyBorder(10, 10, 10, 10)));
        return p;
    }

    private JLabel fLbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(LoginFrame.TEXT_MAIN);
        return l;
    }

    private JLabel secHdr(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(LoginFrame.TEXT_MUTED);
        l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LoginFrame.BORDER_COLOR));
        return l;
    }

    private PlaceholderTextField tf(String hint) {
        PlaceholderTextField f = new PlaceholderTextField(hint);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(LoginFrame.TEXT_MAIN);
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LoginFrame.BORDER_COLOR, 1),
                new EmptyBorder(5, 9, 5, 9)));
        f.setPreferredSize(new Dimension(0, 32));
        return f;
    }

    private PlaceholderTextField numField(int maxDigits, String hint) {
        PlaceholderTextField f = tf(hint);
        ((AbstractDocument) f.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(FilterBypass fb, int off, String s, AttributeSet a) throws BadLocationException {
                if (s == null) return;
                if (fb.getDocument().getLength() + s.length() <= maxDigits && s.matches("[0-9]+"))
                    super.insertString(fb, off, s, a);
            }
            public void replace(FilterBypass fb, int off, int len, String s, AttributeSet a) throws BadLocationException {
                if (s == null) return;
                if (fb.getDocument().getLength() - len + s.length() <= maxDigits && s.matches("[0-9]*"))
                    super.replace(fb, off, len, s, a);
            }
        });
        return f;
    }

    private <T> JComboBox<T> combo(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
        cb.setForeground(LoginFrame.TEXT_MAIN);
        cb.setPreferredSize(new Dimension(0, 32));
        cb.setBorder(BorderFactory.createLineBorder(LoginFrame.BORDER_COLOR, 1));
        return cb;
    }

    private JButton hdrBtn(String text, Color bg, Color hover) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(130, 32));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        return b;
    }

    private JButton actBtn(String text, Color bg, Color hover, java.awt.event.ActionListener al) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(140, 34));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        b.addActionListener(al);
        return b;
    }
}
