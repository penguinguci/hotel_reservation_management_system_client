package ui.components.textfield;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalButtonUI;

public class RoundedPasswordField extends JPasswordField {
    private int cornerRadius = 20;
    private boolean showPassword = false;
    private Image eyeIcon;
    private Image eyeSlashIcon;
    private JButton toggleButton;

    public RoundedPasswordField() {
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 40)); // Chừa khoảng cách bên phải cho nút
        setFont(new Font("Arial", Font.PLAIN, 16));

        // Load và resize biểu tượng mắt
        eyeIcon = getResizedIcon("eye.png", 22, 15);
        eyeSlashIcon = getResizedIcon("eye-slash.png", 22, 15);

        // Tạo nút toggle ẩn/hiện mật khẩu
        toggleButton = new JButton();
        toggleButton.setBorder(null);
        toggleButton.setContentAreaFilled(false); // Loại bỏ nền nút
        toggleButton.setFocusPainted(false); // Loại bỏ viền focus
        toggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Thêm con trỏ chuột

        toggleButton.setUI(new MetalButtonUI() {
            @Override
            protected Color getDisabledTextColor() {
                return Color.GRAY; // Màu nút khi bị vô hiệu hóa
            }
        });

        toggleButton.addActionListener(e -> togglePasswordVisibility());

        // Đặt nút vào TextField
        setLayout(null);
        add(toggleButton);
        setComponentZOrder(toggleButton, 0); // Đưa nút lên trên cùng
    }

    private Image getResizedIcon(String iconPath, int width, int height) {
        String path = "src/main/java/ui/components/icons/" + iconPath;
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath())
                    .getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH); // Khử răng cưa khi resize
        }
        return null;
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    private void togglePasswordVisibility() {
        showPassword = !showPassword;
        setEchoChar(showPassword ? (char) 0 : '*'); // Hiện hoặc ẩn mật khẩu
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        //g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g);

        // Cập nhật vị trí của nút toggle
        int buttonSize = Math.min(getHeight() - 10, 30); // Kích thước nút tối đa 30
        int buttonY = (getHeight() - buttonSize) / 2; // Căn giữa nút theo chiều dọc
        toggleButton.setBounds(getWidth() - buttonSize - 5, buttonY, buttonSize, buttonSize);

        // Căn giữa biểu tượng eye
        int iconY = (getHeight() - 15) / 2; // Căn giữa biểu tượng theo chiều dọc
        int iconX = getWidth() - 30; // Căn biểu tượng bên phải

        Image iconToDraw = showPassword ? eyeSlashIcon : eyeIcon;
        if (iconToDraw != null) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.drawImage(iconToDraw, iconX, iconY, this); // Vẽ ảnh với khử răng cưa
        }
    }


    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(2, 2, 314 , 42, cornerRadius, cornerRadius);
    }
}
