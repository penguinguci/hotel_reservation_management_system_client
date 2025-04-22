package ui.components.button;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

// Renderer cho nút trong table
public class ButtonRenderer extends DefaultTableCellRenderer {
    private JButton button;
    private String text;

    public ButtonRenderer(String text) {
        this.text = text;
        button = new JButton(text);
        button.setOpaque(true);
        button.setBackground(new Color(103, 58, 183));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            button.setBackground(new Color(156, 39, 176));
        } else {
            button.setBackground(new Color(103, 58, 183));
        }
        return button;
    }
}

