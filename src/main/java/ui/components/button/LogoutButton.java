package ui.components.button;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class LogoutButton extends JButton {
    private boolean isHovered = false;

    public LogoutButton() {
        init();
    }

    private void init() {
        setContentAreaFilled(false); // Để vẽ gradient custom
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm icon đăng xuất
        try {
            ImageIcon icon = new ImageIcon("src/main/java/images/logout_icon.png");
            Image scaledIcon = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledIcon));
            // Đặt kích thước button dựa trên icon
            setPreferredSize(new Dimension(50, 50)); // Tăng kích thước để có padding quanh icon
        } catch (Exception e) {
            System.err.println("Không tìm thấy icon đăng xuất: " + e.getMessage());
            setPreferredSize(new Dimension(50, 50)); // Kích thước mặc định nếu không có icon
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        GradientPaint gradient;
        // Thay đổi gradient dựa trên trạng thái hover
        if (isHovered) {
            // Gradient sáng hơn khi hover
            gradient = new GradientPaint(
                    0, 0, new Color(171, 130, 255), // Tím sáng hơn
                    0, getHeight(), new Color(204, 102, 255) // Tím nhạt hơn
            );
        } else {
            // Gradient mặc định
            gradient = new GradientPaint(
                    0, 0, new Color(147, 112, 219), // Medium Purple
                    0, getHeight(), new Color(186, 85, 211) // Medium Orchid
            );
        }

        g2.setPaint(gradient);
        int diameter = Math.min(getWidth(), getHeight());
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;
        g2.fillOval(x, y, diameter, diameter);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Không vẽ border mặc định
    }

    @Override
    public boolean contains(int x, int y) {
        // Chỉ nhận click trong vùng hình tròn
        int diameter = Math.min(getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = diameter / 2;
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return distance <= radius;
    }
}