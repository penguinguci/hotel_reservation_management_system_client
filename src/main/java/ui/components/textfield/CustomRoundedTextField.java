/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.components.textfield;

import java.awt.*;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author TRAN LONG VU
 */
public class CustomRoundedTextField extends JTextField{
    private int cornerRadius = 15;

    public CustomRoundedTextField() {
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10)); // Đặt khoảng cách trong
        setFont(new Font("Arial", Font.PLAIN, 16));
        
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(2, 2, 230 , 25 , cornerRadius, cornerRadius);
    }
}
