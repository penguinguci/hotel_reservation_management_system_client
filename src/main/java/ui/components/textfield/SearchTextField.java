package ui.components.textfield;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * TextField tìm kiếm tùy chỉnh với hiệu ứng shadow và nút tìm kiếm
 * @author Custom Components
 */
public class SearchTextField extends JPanel {
    private JTextField textField;
    private JButton searchButton;
    private String placeholder;
    private Color placeholderColor = new Color(150, 150, 150);
    private Color shadowColor = new Color(0, 0, 0, 50);
    private int shadowSize = 5;
    private int cornerRadius = 10;
    private Color borderColor = new Color(220, 220, 220);
    private Color focusBorderColor = new Color(0, 120, 215);
    private boolean focused = false;

    /**
     * Constructor mặc định
     */
    public SearchTextField() {
        this("Tìm kiếm...");
    }

    /**
     * Constructor với placeholder
     * @param placeholder Text hiển thị khi trường nhập trống
     */
    public SearchTextField(String placeholder) {
        this.placeholder = placeholder;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(shadowSize, shadowSize, shadowSize, shadowSize));
        setOpaque(false);

        // Panel chứa textField và searchButton
        JPanel innerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ nền
                g2.setColor(Color.WHITE);
                RoundRectangle2D.Double shape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
                g2.fill(shape);

                // Vẽ viền
                g2.setColor(focused ? focusBorderColor : borderColor);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(shape);

                g2.dispose();
            }
        };
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setOpaque(false);

        // Tạo textField
        textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Vẽ placeholder nếu cần
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(placeholderColor);

                    // Sử dụng font hiện tại của textField
                    g2.setFont(getFont());

                    // Tính toán vị trí để căn text theo chiều dọc
                    FontMetrics fm = g2.getFontMetrics();
                    int x = getInsets().left;
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                    g2.drawString(placeholder, x, y);
                    g2.dispose();
                }
            }
        };

        textField.setBorder(new EmptyBorder(8, 10, 8, 5));
        textField.setOpaque(false);

        // Focus listener
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                focused = true;
                innerPanel.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
                innerPanel.repaint();
            }
        });

        // Tạo nút tìm kiếm
        searchButton = new JButton();
        searchButton.setIcon(createSearchIcon());
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(40, 30));

        // Thêm các component vào panel
        innerPanel.add(textField, BorderLayout.CENTER);
        innerPanel.add(searchButton, BorderLayout.EAST);

        add(innerPanel, BorderLayout.CENTER);

        // Set preferred size
        setPreferredSize(new Dimension(250, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ shadow
        int width = getWidth() - (shadowSize * 2);
        int height = getHeight() - (shadowSize * 2);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo hình chữ nhật bo tròn cho phần shadow
        RoundRectangle2D.Double shape = new RoundRectangle2D.Double(shadowSize, shadowSize, width, height, cornerRadius, cornerRadius);

        // Vẽ shadow
        for (int i = 0; i < shadowSize; i++) {
            int alpha = 50 - (i * 10);
            if (alpha < 0) alpha = 0;

            g2.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), alpha));
            g2.setStroke(new BasicStroke(i + 1));
            g2.draw(shape);
        }

        g2.dispose();
    }

    // Tạo icon tìm kiếm
    private ImageIcon createSearchIcon() {
        // Tạo hình ảnh trống
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ icon tìm kiếm
        g2.setColor(new Color(46, 45, 45));
        g2.setStroke(new BasicStroke(2.0f));

        // Vẽ hình tròn
        g2.drawOval(2, 2, 8, 8);

        // Vẽ thanh ngang
        g2.drawLine(11, 11, 14, 14);

        g2.dispose();

        return new ImageIcon(image);
    }

    /**
     * Lấy giá trị text
     * @return Giá trị hiện tại của text field
     */
    public String getText() {
        return textField.getText();
    }

    /**
     * Thiết lập giá trị text
     * @param text Giá trị mới
     */
    public void setText(String text) {
        textField.setText(text);
    }

    /**
     * Thêm action listener cho nút tìm kiếm
     * @param listener ActionListener xử lý sự kiện khi nút được nhấn
     */
    public void addSearchButtonActionListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    /**
     * Thêm action listener cho textfield (khi nhấn Enter)
     * @param listener ActionListener xử lý sự kiện khi Enter được nhấn
     */
    public void addTextFieldActionListener(ActionListener listener) {
        textField.addActionListener(listener);
    }

    /**
     * Thiết lập placeholder text
     * @param placeholder Text hiển thị khi trường nhập trống
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        textField.repaint();
    }

    /**
     * Thiết lập màu của placeholder text
     * @param color Màu mới cho placeholder
     */
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        textField.repaint();
    }

    /**
     * Thiết lập màu shadow
     * @param color Màu shadow mới
     */
    public void setShadowColor(Color color) {
        this.shadowColor = color;
        repaint();
    }

    /**
     * Thiết lập kích thước shadow
     * @param size Kích thước shadow mới
     */
    public void setShadowSize(int size) {
        this.shadowSize = size;
        setBorder(new EmptyBorder(shadowSize, shadowSize, shadowSize, shadowSize));
        repaint();
    }

    /**
     * Thiết lập bán kính bo góc
     * @param radius Bán kính bo góc mới
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    /**
     * Thiết lập màu viền
     * @param color Màu viền mới
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    /**
     * Thiết lập màu viền khi focus
     * @param color Màu viền khi focus mới
     */
    public void setFocusBorderColor(Color color) {
        this.focusBorderColor = color;
        repaint();
    }

    /**
     * Thiết lập icon cho nút tìm kiếm
     * @param icon Icon mới
     */
    public void setSearchButtonIcon(Icon icon) {
        searchButton.setIcon(icon);
    }

    /**
     * Lấy TextField bên trong để có thể tùy chỉnh thêm
     * @return JTextField bên trong component
     */
    public JTextField getTextField() {
        return textField;
    }

    /**
     * Lấy nút tìm kiếm để có thể tùy chỉnh thêm
     * @return JButton tìm kiếm
     */
    public JButton getSearchButton() {
        return searchButton;
    }

    /**
     * Thiết lập font cho textfield
     * @param font Font mới
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (textField != null) {
            textField.setFont(font);
            textField.repaint();
        }
    }

    // Demo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Search TextField Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));

            SearchTextField searchField = new SearchTextField("Nhập từ khóa...");
            // Thiết lập font lớn hơn
            searchField.setFont(new Font("Arial", Font.PLAIN, 14));

            searchField.addSearchButtonActionListener(e -> {
                JOptionPane.showMessageDialog(frame, "Tìm kiếm: " + searchField.getText());
            });

            frame.add(searchField);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}