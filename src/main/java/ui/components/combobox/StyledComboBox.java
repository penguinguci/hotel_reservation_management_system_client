package ui.components.combobox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class StyledComboBox<E> extends JComboBox<E> {
    private Color borderColor = new Color(255, 255, 255);

    public StyledComboBox() {
        setRenderer(new CustomComboBoxRenderer());
        setBackground(Color.WHITE);
        setBorder(null);
    }

    public StyledComboBox(E[] items) {
        super(items);
        setRenderer(new CustomComboBoxRenderer());
        setBackground(Color.WHITE);
        setBorder(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);

        super.paintComponent(g);

        // Vẽ border
        g2d.setColor(borderColor);
        g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 5, 5);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 35);
    }

    private class CustomComboBoxRenderer extends BasicComboBoxRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setBorder(new EmptyBorder(5, 10, 5, 10));

            if (isSelected) {
                setBackground(new Color(126, 77, 213));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }

            return this;
        }
    }
}