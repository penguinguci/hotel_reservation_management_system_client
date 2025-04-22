package ui.components.table;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CustomTable extends JTable {
    private Color headerBackground = new Color(136, 130, 246); // SteelBlue - màu header mới
    private Color headerForeground = Color.WHITE;
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
    private Color evenRowColor = new Color(240, 248, 255); // AliceBlue - màu hàng chẵn
    private Color oddRowColor = Color.WHITE;
    private Color selectionColor = new Color(166, 161, 246); // CornflowerBlue - màu khi chọn
    private Color gridColor = new Color(220, 220, 220); // Màu đường kẻ mờ
    private int rowHeight = 30; // Chiều cao hàng

    // Purple theme scrollbar colors
    private Color scrollbarThumbColor = new Color(136, 130, 246);
    private Color scrollbarThumbHoverColor = new Color(116, 110, 236);
    private Color scrollbarTrackColor = new Color(245, 245, 255);
    private int scrollbarWidth = 8; // Scrollbar width for a sleeker look

    public CustomTable() {
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setGridColor(gridColor);
        setIntercellSpacing(new Dimension(0, 0));
        setRowMargin(0);
        setFillsViewportHeight(true);
        setRowHeight(rowHeight);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Đặt font mặc định cho nội dung bảng
        setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Custom header
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(headerBackground);
                setForeground(headerForeground);
                setFont(headerFont);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 90, 140)), // Viền dưới header
                        BorderFactory.createEmptyBorder(0, 10, 0, 0) // Padding trái
                ));
                return this;
            }
        });

        // Custom cell renderer
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Đặt màu nền
                if (isSelected) {
                    setBackground(selectionColor);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? evenRowColor : oddRowColor);
                    setForeground(Color.BLACK);
                }

                // Đặt border và padding
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Padding trái
                setHorizontalAlignment(SwingConstants.LEFT);

                return this;
            }
        });
    }

    /**
     * Create a custom scrollpane for this table with purple-themed scrollbars
     * @return JScrollPane with custom UI
     */
    public JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Customize scrollbar appearance
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        // Remove borders
        scrollPane.setViewportBorder(null);

        return scrollPane;
    }

    // Custom ScrollBarUI implementation for sleek purple scrollbars
    private class ModernScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = scrollbarThumbColor;
            this.thumbDarkShadowColor = scrollbarThumbColor;
            this.thumbHighlightColor = scrollbarThumbColor;
            this.thumbLightShadowColor = scrollbarThumbColor;
            this.trackColor = scrollbarTrackColor;
            this.trackHighlightColor = scrollbarTrackColor;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createEmptyButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createEmptyButton();
        }

        private JButton createEmptyButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(scrollbarTrackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Change color when mouse hovers over scrollbar
            if (isDragging || isThumbRollover()) {
                g2.setColor(scrollbarThumbHoverColor);
            } else {
                g2.setColor(scrollbarThumbColor);
            }

            // Draw rounded rectangle for the thumb
            int arc = 8;
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                // Adjust thumb width to make it sleeker
                int newWidth = scrollbarWidth;
                int offset = (thumbBounds.width - newWidth) / 2;
                g2.fillRoundRect(thumbBounds.x + offset, thumbBounds.y, newWidth, thumbBounds.height, arc, arc);
            } else {
                // Adjust thumb height to make it sleeker
                int newHeight = scrollbarWidth;
                int offset = (thumbBounds.height - newHeight) / 2;
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y + offset, thumbBounds.width, newHeight, arc, arc);
            }
        }

        @Override
        protected Dimension getMinimumThumbSize() {
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                return new Dimension(scrollbarWidth, 40);
            } else {
                return new Dimension(40, scrollbarWidth);
            }
        }
    }

    // Các phương thức setter để tùy chỉnh
    public void setHeaderBackground(Color color) {
        this.headerBackground = color;
        getTableHeader().repaint();
    }

    public void setHeaderForeground(Color color) {
        this.headerForeground = color;
        getTableHeader().repaint();
    }

    public void setHeaderFont(Font font) {
        this.headerFont = font;
        getTableHeader().setFont(font);
    }

    public void setEvenRowColor(Color color) {
        this.evenRowColor = color;
        repaint();
    }

    public void setOddRowColor(Color color) {
        this.oddRowColor = color;
        repaint();
    }

    public void setSelectionColor(Color color) {
        this.selectionColor = color;
        repaint();
    }

    public void setGridColor(Color color) {
        this.gridColor = color;
        super.setGridColor(color); // Sử dụng super để gọi phương thức của lớp cha
    }

    public void setTableRowHeight(int height) {
        this.rowHeight = height;
        setRowHeight(height);
    }

    // New setters for scrollbar customization
    public void setScrollbarThumbColor(Color color) {
        this.scrollbarThumbColor = color;
    }

    public void setScrollbarThumbHoverColor(Color color) {
        this.scrollbarThumbHoverColor = color;
    }

    public void setScrollbarTrackColor(Color color) {
        this.scrollbarTrackColor = color;
    }

    public void setScrollbarWidth(int width) {
        this.scrollbarWidth = width;
    }
}