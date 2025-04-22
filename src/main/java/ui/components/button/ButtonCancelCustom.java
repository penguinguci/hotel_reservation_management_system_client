package ui.components.button;

import ui.components.effect.RippleEffect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class ButtonCancelCustom extends JButton {
    private Color startColor = new Color(246, 71, 71);
    private Color endColor = new Color(251, 36, 54);
    private Color disabledColor = new Color(239, 78, 90);
    private boolean isHovered = false;
    private int round = 10;
    private final RippleEffect rippleEffect;

    public ButtonCancelCustom() {
        setContentAreaFilled(false); // Tắt nền mặc định
        setFocusPainted(false); // Bỏ khung focus
        setBorderPainted(false); // Bỏ viền
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Khởi tạo hiệu ứng Ripple
        rippleEffect = new RippleEffect(this);

        // add event hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) { // Chỉ hover nếu nút được bật
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) { // Chỉ cập nhật nếu nút được bật
                    isHovered = false;
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create(); // Tạo bản sao Graphics
        // Bật khử răng cưa
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo hiệu ứng gradient hoặc màu đơn tùy theo trạng thái
        if (isEnabled()) {
            // Nút được bật: Sử dụng gradient
            GradientPaint gradient = new GradientPaint(
                    0, 0, isHovered ? endColor : startColor, // Màu bắt đầu khi hover
                    0, getHeight(), isHovered ? startColor : endColor // Màu kết thúc khi hover
            );
            g2d.setPaint(gradient);
        } else {
            // Nút bị vô hiệu hóa: Sử dụng màu xám
            g2d.setPaint(disabledColor);
        }

        // Vẽ hình chữ nhật bo tròn
        Area area = new Area(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        g2d.fill(area);

        // Vẽ gợn sóng (chỉ khi nút được bật)
        if (isEnabled()) {
            rippleEffect.reder(g2d, area);
        }

        // Vẽ chữ
        super.paintComponent(g);

        g2d.dispose(); // Giải phóng tài nguyên
    }

    // Getter và Setter cho các thuộc tính màu (nếu cần tùy chỉnh)
    public Color getStartColor() {
        return startColor;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
        repaint();
    }

    public Color getEndColor() {
        return endColor;
    }

    public void setEndColor(Color endColor) {
        this.endColor = endColor;
        repaint();
    }

    public Color getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(Color disabledColor) {
        this.disabledColor = disabledColor;
        repaint();
    }
}
