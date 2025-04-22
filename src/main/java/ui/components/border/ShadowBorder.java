package ui.components.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.Border;

public class ShadowBorder implements Border {
    private final int shadowSize;
    private final Color shadowColor;

    public ShadowBorder(int shadowSize, Color shadowColor) {
        this.shadowSize = shadowSize;
        this.shadowColor = shadowColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ shadow
        for (int i = 0; i < shadowSize; i++) {
            float opacity = (float) (i + 1) / shadowSize;
            g2d.setColor(new Color(shadowColor.getRed()/255f, shadowColor.getGreen()/255f,
                    shadowColor.getBlue()/255f, opacity * 0.3f));
            g2d.drawRoundRect(x + i, y + i, width - 1 - 2*i, height - 1 - 2*i, 10, 10);
        }

        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
