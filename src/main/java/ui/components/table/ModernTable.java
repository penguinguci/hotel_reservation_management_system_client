package ui.components.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ModernTable extends JTable {
    public ModernTable() {
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setRowMargin(0);
        setFillsViewportHeight(true);

        // Custom header
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(70, 130, 180));
                setForeground(Color.WHITE);
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });

        // Custom cell renderer
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(noFocusBorder);
                if (row % 2 == 0) {
                    setBackground(new Color(250, 250, 250));
                } else {
                    setBackground(Color.WHITE);
                }
                return this;
            }
        });
    }

    public void setHeaderFont(Font font) {
        getTableHeader().setFont(font);
    }
}
