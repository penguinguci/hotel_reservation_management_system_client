package loader;

import ultilities.WindowTitle;

import javax.swing.*;
import java.awt.*;

/**
 * @author Lenovo
 */
public class SplashLoading extends JFrame {
    private String currentLoadDataString = "";
    private static final int TOTAL_PROGRESS = 100;
    private float opacity = 0f;
    private JLabel statusLabel;

    public SplashLoading() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        initComponents();
        setLocationRelativeTo(null);
        jLabel2.setText("Phiên bản: " + WindowTitle.VERSION);

        // Hiệu ứng fade-in
        Timer fadeInTimer = new Timer(30, e -> {
            opacity += 0.05f;
            if (opacity >= 1f) {
                opacity = 1f;
                ((Timer) e.getSource()).stop();
            }
            setOpacity(opacity);
        });
        fadeInTimer.start();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(139, 145, 239),
                        0, getHeight(), new Color(89, 95, 189));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title panel (top)
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        // Hotel logo and title
        jLabel1 = new JLabel("MELODY HOTEL", SwingConstants.CENTER);
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 36));
        jLabel1.setForeground(Color.WHITE);
        titlePanel.add(jLabel1, BorderLayout.CENTER);

        // Status label
        statusLabel = new JLabel("Đang khởi tạo ứng dụng...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        titlePanel.add(statusLabel, BorderLayout.SOUTH);

        // Bottom panel (version, copyright, progress bar)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Version label
        jLabel2 = new JLabel("Phiên bản: 1.0.0", SwingConstants.CENTER);
        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jLabel2.setForeground(new Color(220, 220, 220));
        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(jLabel2, gbc);

        // Progress bar
        jProgressBar1 = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRoundRect(0, 0, getWidth(), 5, 5, 5);

                // Progress
                int progressWidth = (int) ((getWidth() * getValue()) / 100.0);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, progressWidth, 5, 5, 5);

                g2d.dispose();
            }
        };
        jProgressBar1.setBorder(BorderFactory.createEmptyBorder());
        jProgressBar1.setPreferredSize(new Dimension(400, 8));
        gbc.gridy = 1;
        gbc.ipady = 5;
        bottomPanel.add(jProgressBar1, gbc);

        // Copyright label
        jLabel3 = new JLabel("© 2025 Melody Hotel Management System by penguinguci - ztnosleep", SwingConstants.CENTER);
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        jLabel3.setForeground(new Color(200, 200, 200));
        gbc.gridy = 2;
        gbc.ipady = 0;
        bottomPanel.add(jLabel3, gbc);

        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        setSize(600, 350);
    }

    public void processBarUpdate(int percent, String status) {
        SwingUtilities.invokeLater(() -> {
            jProgressBar1.setValue(percent);
            statusLabel.setText(status);
        });
    }

    public void processBarUpdate(int percent) {
        SwingUtilities.invokeLater(() -> {
            jProgressBar1.setValue(percent);
        });
    }

    // Variables declaration
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JProgressBar jProgressBar1;
}