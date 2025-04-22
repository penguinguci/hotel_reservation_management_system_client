/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Form_HomePage extends javax.swing.JPanel {

    private AnalogClock analogClock;
    private JLabel lblDigitalTime;
    private JLabel lblPhongTrong;
    private JLabel lblDoanhThu;

    public Form_HomePage() {
        initComponents();

        initCustomComponents();
    }

    private void initCustomComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tạo đồng hồ analog
        analogClock = new AnalogClock();
        lblDigitalTime = createStyledLabel("00:00:00", 24, new Color(255, 255, 255));
//        lblPhongTrong = createStyledLabel("Phòng trống: 15", 24, new Color(255, 255, 255));
//        lblDoanhThu = createStyledLabel("Doanh thu hôm nay: 10,000,000₫", 24, new Color(255, 255, 255));

        // Thêm các component vào layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(analogClock, gbc);

        gbc.gridy = 1;
        add(lblDigitalTime, gbc);

//        gbc.gridy = 2;
//        add(lblPhongTrong, gbc);
//
//        gbc.gridy = 3;
//        add(lblDoanhThu, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient background
        Color color1 = new Color(149, 145, 239);
        Color color2 = new Color(113, 105, 246);
        GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private JLabel createStyledLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        label.setForeground(color);
        label.setOpaque(false);
        return label;
    }

    private class AnalogClock extends javax.swing.JPanel {
        private Timer timer;

        public AnalogClock() {
            setPreferredSize(new Dimension(250, 250));
            setOpaque(false);

            // Timer cập nhật cả analog và digital
            timer = new Timer(1000, e -> {
                repaint();
                updateDigitalTime();
            });
            timer.start();
        }

        private void updateDigitalTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            lblDigitalTime.setText(sdf.format(Calendar.getInstance().getTime()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR);
            int minute = now.get(Calendar.MINUTE);
            int second = now.get(Calendar.SECOND);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(centerX, centerY) - 15;

            // Vẽ mặt đồng hồ
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

            // Vẽ vạch số
            g2d.setColor(new Color(0, 0, 0, 150));
            for (int i = 0; i < 12; i++) {
                double angle = Math.toRadians(i * 30 - 90);
                int x1 = centerX + (int) (0.85 * radius * Math.cos(angle));
                int y1 = centerY + (int) (0.85 * radius * Math.sin(angle));
                int x2 = centerX + (int) (0.95 * radius * Math.cos(angle));
                int y2 = centerY + (int) (0.95 * radius * Math.sin(angle));
                g2d.drawLine(x1, y1, x2, y2);
            }

            // Vẽ các kim
            drawHand(g2d, Math.toRadians((hour * 30) + (minute * 0.5) - 90), radius * 0.5, 5, new Color(50, 50, 50));
            drawHand(g2d, Math.toRadians((minute * 6) - 90), radius * 0.7, 3, new Color(70, 130, 180));
            drawHand(g2d, Math.toRadians((second * 6) - 90), radius * 0.8, 2, new Color(178, 34, 34));

            // Tâm đồng hồ
            g2d.setColor(new Color(178, 34, 34));
            g2d.fillOval(centerX - 4, centerY - 4, 8, 8);
        }

        private void drawHand(Graphics2D g2d, double angle, double length, int thickness, Color color) {
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int x = centerX + (int) (length * Math.cos(angle));
            int y = centerY + (int) (length * Math.sin(angle));

            g2d.setColor(color);
            g2d.setStroke(new java.awt.BasicStroke(thickness, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
            g2d.drawLine(centerX, centerY, x, y);
        }
    }

    public void setPhongTrong(int soPhong) {
        lblPhongTrong.setText("Phòng trống: " + soPhong);
    }

    public void setDoanhThu(long doanhThu) {
        lblDoanhThu.setText(String.format("Doanh thu hôm nay: %,d₫", doanhThu));
    }

    // Generated code
    @SuppressWarnings("unchecked")
    private void initComponents() {
    }
    // Variables declaration
    private JLabel jLabel1;
}