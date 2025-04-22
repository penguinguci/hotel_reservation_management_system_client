package ui.components.button;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class GradientButton extends JButton {
    private Color startColor;
    private Color endColor;
    private Color hoverStartColor;
    private Color hoverEndColor;
    private boolean hovered;

    public GradientButton(String text, Color startColor, Color endColor) {
        super(text);
        this.startColor = startColor;
        this.endColor = endColor;
        this.hoverStartColor = endColor;
        this.hoverEndColor = startColor;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hovered = true;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color sColor = hovered ? hoverStartColor : startColor;
        Color eColor = hovered ? hoverEndColor : endColor;

        GradientPaint gp = new GradientPaint(
                0, 0, sColor,
                getWidth(), getHeight(), eColor);

        g2d.setPaint(gp);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        super.paintComponent(g);
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 40);
    }

    public void setGradientColors(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        repaint();
    }

    public void setHoverGradientColors(Color hoverStartColor, Color hoverEndColor) {
        this.hoverStartColor = hoverStartColor;
        this.hoverEndColor = hoverEndColor;
        repaint();
    }
}