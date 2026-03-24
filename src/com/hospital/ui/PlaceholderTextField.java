package com.hospital.ui;

import javax.swing.*;
import java.awt.*;

public class PlaceholderTextField extends JTextField {

    private String placeholder = "";

    public PlaceholderTextField() {
        this("");
    }

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        applyStyle();
    }

    public PlaceholderTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        applyStyle();
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    private void applyStyle() {
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(LoginFrame.TEXT_MAIN);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LoginFrame.BORDER_COLOR, 1),
                new javax.swing.border.EmptyBorder(5, 9, 5, 9)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(new Color(170, 174, 186));
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            Insets ins = getInsets();
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, ins.left, y);
            g2.dispose();
        }
    }
}
