/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.components.label;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;

/**
 *
 * @author TRAN LONG VU
 */
public class LabelImage extends JLabel {
    private BufferedImage image;
    private int borderWidth = 2; // Độ rộng viền

    // Constructor mặc định
    public LabelImage() {
        this.setPreferredSize(new Dimension(150, 150)); // Kích thước mặc định
    }

    // Constructor nhận ảnh
    public LabelImage(BufferedImage image) {
        this.image = image;
        this.setPreferredSize(new Dimension(150, 150));
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            int diameter = Math.min(getWidth(), getHeight());
            BufferedImage croppedImage = cropToCircle(image, diameter);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ viền tím
            g2d.setColor(new Color(196, 75, 248)); // Màu tím
            g2d.fill(new Ellipse2D.Double(0, 0, diameter, diameter));

            // Vẽ ảnh bên trong viền
            g2d.setClip(new Ellipse2D.Double(borderWidth, borderWidth, diameter - 2 * borderWidth, diameter - 2 * borderWidth));
            g2d.drawImage(croppedImage, borderWidth, borderWidth, diameter - 2 * borderWidth, diameter - 2 * borderWidth, null);
        }
    }

    private BufferedImage cropToCircle(BufferedImage src, int diameter) {
        BufferedImage output = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = output.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setClip(new Ellipse2D.Double(0, 0, diameter, diameter));
        g2d.drawImage(src, 0, 0, diameter, diameter, null);
        g2d.dispose();
        return output;
    }
}