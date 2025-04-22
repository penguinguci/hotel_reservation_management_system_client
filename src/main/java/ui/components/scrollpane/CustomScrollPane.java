package ui.components.scrollpane;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollPane extends JScrollPane {
    private Color thumbColor = new Color(136, 130, 246); // Màu tím đồng bộ với header table
    private Color trackColor = new Color(245, 245, 255); // Màu nền thanh cuộn
    private Color borderColor = new Color(220, 220, 220); // Màu viền
    private int thumbSize = 8; // Kích thước của thanh cuộn (độ rộng)
    private int arcSize = 8; // Độ bo tròn của thanh cuộn

    public CustomScrollPane() {
        this(null);
    }

    public CustomScrollPane(Component view) {
        super(view);
        initialize();
    }

    public CustomScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        initialize();
    }

    public CustomScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        initialize();
    }

    private void initialize() {
        // Loại bỏ viền mặc định của ScrollPane
        setBorder(BorderFactory.createLineBorder(borderColor, 1));

        // Cài đặt UI cho thanh cuộn dọc
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setUI(new ModernScrollBarUI());
        verticalScrollBar.setUnitIncrement(16); // Tăng độ cuộn mỗi lần click nhỏ
        verticalScrollBar.setBlockIncrement(64); // Tăng độ cuộn mỗi lần click lớn (page up/down)
        verticalScrollBar.setPreferredSize(new Dimension(thumbSize, 0)); // Đặt chiều rộng thanh cuộn

        // Cài đặt UI cho thanh cuộn ngang
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setUI(new ModernScrollBarUI());
        horizontalScrollBar.setUnitIncrement(16);
        horizontalScrollBar.setBlockIncrement(64);
        horizontalScrollBar.setPreferredSize(new Dimension(0, thumbSize)); // Đặt chiều cao thanh cuộn

        // Loại bỏ các nút mũi tên ở góc
        setCorner(JScrollPane.LOWER_RIGHT_CORNER, createCornerPanel());

        // Thêm viewport border - null để không có viền ngăn cách với component bên trong
        setViewportBorder(null);

        // Đặt màu nền cho viewport
        getViewport().setBackground(Color.WHITE);

        // Bật scrolling mượt
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);
    }

    // Panel góc cho scrollpane trống rỗng
    private JPanel createCornerPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(trackColor);
        return panel;
    }

    // Các phương thức setter để tùy chỉnh
    public void setThumbColor(Color color) {
        this.thumbColor = color;
        getVerticalScrollBar().repaint();
        getHorizontalScrollBar().repaint();
    }

    public void setTrackColor(Color color) {
        this.trackColor = color;
        getVerticalScrollBar().repaint();
        getHorizontalScrollBar().repaint();
    }

    public void setThumbSize(int size) {
        this.thumbSize = size;
        getVerticalScrollBar().setPreferredSize(new Dimension(size, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, size));
    }

    public void setArcSize(int size) {
        this.arcSize = size;
        getVerticalScrollBar().repaint();
        getHorizontalScrollBar().repaint();
    }

    // UI tùy chỉnh cho thanh cuộn
    private class ModernScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = CustomScrollPane.this.thumbColor;
            this.trackColor = CustomScrollPane.this.trackColor;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createEmptyButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createEmptyButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(trackColor);
            g2.fill(trackBounds);
            g2.dispose();
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);

            // Đảm bảo không vẽ khi thanh cuộn quá nhỏ
            if (thumbBounds.width < 5 || thumbBounds.height < 5) {
                g2.dispose();
                return;
            }

            // Hiệu ứng hover
            if (isDragging || isThumbRollover()) {
                g2.setColor(thumbColor.brighter());
            }

            // Vẽ thumb với bo tròn, tùy thuộc vào hướng thanh cuộn
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                // Điều chỉnh kích thước thumb theo chiều dọc
                int adjustedWidth = Math.min(thumbBounds.width - 2, thumbSize);
                int x = thumbBounds.x + (thumbBounds.width - adjustedWidth) / 2;
                g2.fillRoundRect(x, thumbBounds.y, adjustedWidth, thumbBounds.height, arcSize, arcSize);
            } else {
                // Điều chỉnh kích thước thumb theo chiều ngang
                int adjustedHeight = Math.min(thumbBounds.height - 2, thumbSize);
                int y = thumbBounds.y + (thumbBounds.height - adjustedHeight) / 2;
                g2.fillRoundRect(thumbBounds.x, y, thumbBounds.width, adjustedHeight, arcSize, arcSize);
            }

            g2.dispose();
        }

        private JButton createEmptyButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            button.setBorder(null);
            return button;
        }
    }

    // Demo để kiểm tra scrollpane với table
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Tạo một frame để test
            JFrame frame = new JFrame("Custom ScrollPane Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            // Tạo dữ liệu mẫu cho table
            Object[][] data = new Object[100][5];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    data[i][j] = "Cell " + i + "," + j;
                }
            }
            String[] columnNames = {"Column 1", "Column 2", "Column 3", "Column 4", "Column 5"};

            // Tạo model cho table
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, columnNames);

            // Giả sử CustomTable đã được import
            JTable customTable = new JTable(model); // Thay thế bằng CustomTable khi sử dụng thực tế
            customTable.setFillsViewportHeight(true);

            // Tạo custom scroll pane
            CustomScrollPane scrollPane = new CustomScrollPane(customTable);

            // Đặt vào frame
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}