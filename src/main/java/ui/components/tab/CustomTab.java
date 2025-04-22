package ui.components.tab;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomTab - A reusable tab component that can be used in GUI designers
 * @author TRAN LONG VU (Modified)
 */
public class CustomTab extends JPanel {
    private JPanel tabButtonPanel;      // Panel chứa các tab button
    private JPanel contentPanel;        // Panel chứa nội dung các tab (sử dụng CardLayout)
    private CardLayout cardLayout;
    private List<TabButton> tabButtons; // Danh sách các tab button để quản lý trạng thái selected

    // Màu của đường bottom line khi tab được chọn
    private Color selectedLineColor = new Color(113, 105, 246); // Màu xanh dương

    // Properties for design-time support
    private int tabHeight = 35;
    private int tabWidth = 150;
    private boolean initialized = false;

    /**
     * Default constructor for drag and drop support in GUI designers
     */
    public CustomTab() {
        initComponents();
    }

    /**
     * Initialize the component - called by constructors and can be called
     * by GUI designers during initialization
     */
    public void initComponents() {
        if (initialized) return;

        setLayout(new BorderLayout());

        // Panel chứa tab button (nền trắng)
        tabButtonPanel = new JPanel();
        tabButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabButtonPanel.setBackground(Color.WHITE);

        // Panel chứa nội dung tab với CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        tabButtons = new ArrayList<>();

        add(tabButtonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        initialized = true;
    }

    /**
     * Thêm một tab mới.
     *
     * @param title Tiêu đề của tab.
     * @param comp  Component được hiển thị khi tab được chọn.
     * @return Component được thêm vào tab
     */
    public Component addTab(String title, Component comp) {
        if (!initialized) {
            initComponents();
        }

        // Tạo TabButton với title
        TabButton tabButton = new TabButton(title);
        tabButton.setBackground(Color.WHITE);
        tabButton.setForeground(Color.BLACK);
        tabButton.setPreferredSize(new Dimension(tabWidth, tabHeight));

        // Khi click, cập nhật trạng thái các tab và hiển thị nội dung tương ứng
        tabButton.addActionListener(e -> {
            selectTab(tabButton);
        });
        tabButtons.add(tabButton);
        tabButtonPanel.add(tabButton);

        // Thêm nội dung vào contentPanel với key là title
        contentPanel.add(comp, title);

        // Nếu đây là tab đầu tiên, tự động chọn nó
        if (tabButtons.size() == 1) {
            selectTab(tabButton);
        }

        // Cập nhật lại giao diện
        revalidate();
        repaint();

        return comp;
    }

    /**
     * Thêm một JPanel mới vào tab
     *
     * @param title Tiêu đề của tab
     * @param panel Panel được hiển thị khi tab được chọn
     * @return Panel được thêm vào tab
     */
    public JPanel addTabPanel(String title, JPanel panel) {
        return (JPanel)addTab(title, panel);
    }

    /**
     * Tạo một panel trống mới và thêm vào tab
     *
     * @param title Tiêu đề của tab
     * @return Panel trống mới được thêm vào tab
     */
    public JPanel createTabPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        return addTabPanel(title, panel);
    }

    /**
     * Chọn tab với tiêu đề cụ thể
     *
     * @param title Tiêu đề của tab cần chọn
     */
    public void selectTabByTitle(String title) {
        for (TabButton tb : tabButtons) {
            if (tb.getText().equals(title)) {
                selectTab(tb);
                return;
            }
        }
    }

    /**
     * Chọn tab theo index
     *
     * @param index Index của tab cần chọn (0-based)
     */
    public void selectTabByIndex(int index) {
        if (index >= 0 && index < tabButtons.size()) {
            selectTab(tabButtons.get(index));
        }
    }

    // Phương thức chọn tab: cập nhật trạng thái của tất cả các tab và hiển thị đúng nội dung
    private void selectTab(TabButton selectedTab) {
        for (TabButton tb : tabButtons) {
            tb.setSelected(tb == selectedTab);
        }
        cardLayout.show(contentPanel, selectedTab.getText());
        tabButtonPanel.repaint();
    }

    /**
     * Xóa tất cả các tab
     */
    public void removeAllTabs() {
        tabButtonPanel.removeAll();
        contentPanel.removeAll();
        tabButtons.clear();
        revalidate();
        repaint();
    }

    /**
     * Xóa tab tại vị trí index
     *
     * @param index Index của tab cần xóa
     */
    public void removeTabAt(int index) {
        if (index >= 0 && index < tabButtons.size()) {
            TabButton tb = tabButtons.get(index);
            tabButtonPanel.remove(tb);
            contentPanel.remove(index);
            tabButtons.remove(index);

            // Nếu xóa tab đang chọn, chọn tab khác
            if (tabButtons.size() > 0) {
                selectTab(tabButtons.get(0));
            }

            revalidate();
            repaint();
        }
    }

    // Inner class định nghĩa các tab button với giao diện tùy chỉnh
    private class TabButton extends JButton {
        private boolean selected = false;

        public TabButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            // Sử dụng BasicButtonUI để không bị thay đổi bởi LAF
            setUI(new BasicButtonUI());
            setFocusPainted(false);
            // Căn giữa text
            setHorizontalAlignment(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.PLAIN, 15));
            // Bỏ margin nếu cần
            setMargin(new Insets(0, 5, 0, 5));
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Vẽ nền trắng
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Vẽ text của nút (sử dụng font, alignment mặc định của JButton)
            super.paintComponent(g);

            // Nếu nút được chọn, vẽ đường bottom line (có chiều dày có thể tùy chỉnh)
            if (selected) {
                g2.setColor(selectedLineColor);
                // Vẽ đường line dày 2 pixel ở dưới cùng
                int lineHeight = 2;
                g2.fillRect(0, getHeight() - lineHeight, getWidth(), lineHeight);
            }
            g2.dispose();
        }

        // Override getPreferredSize() để đảm bảo kích thước cố định
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(tabWidth, tabHeight);
        }
    }

    // Getter and Setter để sử dụng trong property editor

    /**
     * Thiết lập màu đường viền khi tab được chọn
     *
     * @param color Màu cần thiết lập
     */
    public void setSelectedLineColor(Color color) {
        this.selectedLineColor = color;
        tabButtonPanel.repaint();
    }

    /**
     * Lấy màu đường viền khi tab được chọn
     *
     * @return Màu hiện tại của đường viền
     */
    public Color getSelectedLineColor() {
        return selectedLineColor;
    }

    /**
     * Thiết lập chiều cao của tab
     *
     * @param height Chiều cao mới
     */
    public void setTabHeight(int height) {
        this.tabHeight = height;
        for (TabButton button : tabButtons) {
            button.setPreferredSize(new Dimension(tabWidth, tabHeight));
        }
        revalidate();
        repaint();
    }

    /**
     * Lấy chiều cao hiện tại của tab
     *
     * @return Chiều cao hiện tại
     */
    public int getTabHeight() {
        return tabHeight;
    }

    /**
     * Thiết lập chiều rộng của tab
     *
     * @param width Chiều rộng mới
     */
    public void setTabWidth(int width) {
        this.tabWidth = width;
        for (TabButton button : tabButtons) {
            button.setPreferredSize(new Dimension(tabWidth, tabHeight));
        }
        revalidate();
        repaint();
    }

    /**
     * Lấy chiều rộng hiện tại của tab
     *
     * @return Chiều rộng hiện tại
     */
    public int getTabWidth() {
        return tabWidth;
    }

    /**
     * Lấy component hiện đang được hiển thị
     *
     * @return Component đang hiển thị
     */
    public Component getSelectedComponent() {
        for (Component comp : contentPanel.getComponents()) {
            if (comp.isVisible()) {
                return comp;
            }
        }
        return null;
    }

    /**
     * Lấy index của tab đang được chọn
     *
     * @return Index của tab đang chọn, hoặc -1 nếu không có tab nào được chọn
     */
    public int getSelectedIndex() {
        for (int i = 0; i < tabButtons.size(); i++) {
            TabButton button = tabButtons.get(i);
            if (button.selected) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Lấy số lượng tab hiện có
     *
     * @return Số lượng tab
     */
    public int getTabCount() {
        return tabButtons.size();
    }

    // Phương thức tạo panel mẫu để hiển thị nội dung của tab
    private static JPanel createSamplePanel(String text) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240));
        panel.add(new JLabel(text));
        return panel;
    }

    // Ví dụ sử dụng CustomTab
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Custom Tab Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setSize(500, 400);

            CustomTab customTab = new CustomTab();

            // Thêm vài tab ví dụ:
            customTab.addTab("Home", createSamplePanel("Home Content"));
            customTab.addTab("Profile", createSamplePanel("Profile Content"));

            // Tạo panel mới cho tab Settings
            JPanel settingsPanel = customTab.createTabPanel("Settings");
            settingsPanel.setLayout(new FlowLayout());
            settingsPanel.add(new JButton("Save Settings"));
            settingsPanel.add(new JCheckBox("Enable notifications"));

            frame.add(customTab, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}