package ui.components.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Calendar;
import java.util.Date;

public class CustomCalendar extends JPanel {
    private RoundedButton calendarButton;
    private JDialog calendarDialog;
    private Calendar currentCalendar;
    private JComboBox<String> monthCombo;
    private JSpinner yearSpinner;
    private RoundedButton[] dayButtons;

    private Color purpleTheme = new Color(140, 120, 255);
    private Color lightPurple = new Color(230, 220, 255);
    private Color hoverColor = new Color(200, 180, 255); // Hiệu ứng hover

    // Giá trị ngày được chọn; nếu null => chưa chọn
    private Date selectedDate = null;
    // Cờ cho biết đây là lần khởi tạo đầu tiên
    private boolean isFirstInit = true;

    public CustomCalendar() {
        setLayout(new BorderLayout());
        currentCalendar = Calendar.getInstance();
        setOpaque(false);

        initializeComponents();
        setupLayout();
        addEventListeners();
    }

    private void initializeComponents() {
        // Khởi tạo button với text dựa theo updateButtonText()
        calendarButton = new RoundedButton();
        calendarButton.setPreferredSize(new Dimension(150, 30));
        calendarButton.setBackground(lightPurple);
        calendarButton.setForeground(purpleTheme);
        calendarButton.setBorderColor(purpleTheme);
        calendarButton.setArcSize(15);
        calendarButton.setFocusPainted(false);
        updateButtonText();
    }

    private void initializeCalendarDialog() {
        // Giữ kích thước dialog nhỏ gọn
        calendarDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Date", true);
        calendarDialog.setUndecorated(true);
        calendarDialog.setSize(320, 270);
        calendarDialog.setLayout(new BorderLayout());

        try {
            calendarDialog.setShape(new RoundRectangle2D.Double(0, 0, 320, 270, 15, 15));
        } catch (UnsupportedOperationException ex) {
            // Nếu không được hỗ trợ
        }

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(purpleTheme);
        titleBar.setPreferredSize(new Dimension(320, 30));
        JLabel titleLabel = new JLabel("Select Date", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleBar.add(titleLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("X");
        closeButton.setBackground(purpleTheme);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> calendarDialog.setVisible(false));
        titleBar.add(closeButton, BorderLayout.EAST);

        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel selectionPanel = new JPanel(new BorderLayout(10, 0));
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(currentCalendar.get(Calendar.MONTH));
        monthCombo.setBackground(lightPurple);
        monthCombo.setForeground(purpleTheme);
        monthCombo.setPreferredSize(new Dimension(100, 25));

        SpinnerNumberModel yearModel = new SpinnerNumberModel(
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.YEAR) - 100,
                currentCalendar.get(Calendar.YEAR) + 100,
                1);
        yearSpinner = new JSpinner(yearModel);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearSpinner, "#");
        yearSpinner.setEditor(editor);
        yearSpinner.setPreferredSize(new Dimension(70, 25));

        JPanel monthYearPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        monthYearPanel.add(monthCombo);
        monthYearPanel.add(yearSpinner);
        selectionPanel.add(monthYearPanel, BorderLayout.CENTER);

        // Panel days với kích thước nhỏ gọn đủ cho 7 cột
        JPanel daysPanel = new JPanel(new GridLayout(7, 7, 3, 3));
        String[] daysOfWeek = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            dayLabel.setForeground(purpleTheme);
            daysPanel.add(dayLabel);
        }

        dayButtons = new RoundedButton[42];
        for (int i = 0; i < dayButtons.length; i++) {
            dayButtons[i] = new RoundedButton();
            dayButtons[i].setPreferredSize(new Dimension(50, 50));
            dayButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 10));
            dayButtons[i].setFocusPainted(false);
            dayButtons[i].setBackground(Color.WHITE);
            dayButtons[i].setForeground(purpleTheme);
            dayButtons[i].setHorizontalAlignment(SwingConstants.CENTER);
            dayButtons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            dayButtons[i].setMargin(new Insets(0, 0, 0, 0));

            final int idx = i;
            dayButtons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (dayButtons[idx].isEnabled()) {
                        dayButtons[idx].setBackground(hoverColor);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (dayButtons[idx].isEnabled()) {
                        dayButtons[idx].setBackground(Color.WHITE);
                    }
                }
            });
            daysPanel.add(dayButtons[i]);
        }

        calendarPanel.add(selectionPanel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);

        JPanel container = new JPanel(new BorderLayout());
        container.add(titleBar, BorderLayout.NORTH);
        container.add(calendarPanel, BorderLayout.CENTER);
        calendarDialog.add(container);

        updateCalendarDisplay();

        monthCombo.addActionListener(e -> {
            currentCalendar.set(Calendar.MONTH, monthCombo.getSelectedIndex());
            updateCalendarDisplay();
        });
        yearSpinner.addChangeListener(e -> {
            currentCalendar.set(Calendar.YEAR, (Integer) yearSpinner.getValue());
            updateCalendarDisplay();
        });
    }

    private void updateCalendarDisplay() {
        // Reset tất cả các nút ngày
        for (RoundedButton dayButton : dayButtons) {
            dayButton.setText("");
            dayButton.setEnabled(false);
            dayButton.setBackground(Color.WHITE);
            dayButton.setForeground(purpleTheme);
            for (ActionListener al : dayButton.getActionListeners()) {
                dayButton.removeActionListener(al);
            }
        }

        Calendar tempCalendar = (Calendar) currentCalendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < daysInMonth; i++) {
            int dayValue = i + 1;
            RoundedButton button = dayButtons[firstDayOfWeek + i];
            button.setText(String.valueOf(dayValue));
            button.setEnabled(true);

            // Hiển thị màu cho ngày đã chọn (nếu selectedDate không null)
            if (selectedDate != null && dayValue == currentDay &&
                    currentCalendar.get(Calendar.MONTH) == tempCalendar.get(Calendar.MONTH) &&
                    currentCalendar.get(Calendar.YEAR) == tempCalendar.get(Calendar.YEAR)) {
                button.setBackground(purpleTheme);
                button.setForeground(Color.WHITE);
            }

            final int finalDay = dayValue;
            button.addActionListener(e -> {
                currentCalendar.set(Calendar.DAY_OF_MONTH, finalDay);
                // Khi chọn ngày, ta cập nhật selectedDate thay vì sử dụng currentCalendar.getTime() mặc định
                selectedDate = currentCalendar.getTime();
                updateButtonText();
                updateCalendarDisplay();
                calendarDialog.setVisible(false);
            });
        }
    }

    private void updateButtonText() {
        if (selectedDate == null) {
            // Nếu chưa có ngày nào được chọn
            if (isFirstInit) {
                calendarButton.setText("Chọn ngày");
            } else {
                calendarButton.setText("Chọn này");
            }
        } else {
            // Khi đã có ngày được chọn, hiển thị theo định dạng dd/MM/yyyy
            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDate);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            calendarButton.setText(String.format("%02d/%02d/%d", day, month, year));
        }
    }

    private void setupLayout() {
        add(calendarButton, BorderLayout.CENTER);
    }

    private void addEventListeners() {
        calendarButton.addActionListener(e -> {
            if (calendarDialog == null) {
                initializeCalendarDialog();
            }
            Point btnLocation = calendarButton.getLocationOnScreen();
            int x = btnLocation.x;
            int y = btnLocation.y + calendarButton.getHeight();
            calendarDialog.setLocation(x, y);
            calendarDialog.setVisible(true);
        });
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date date) {
        selectedDate = date;
        if (date != null) {
            currentCalendar.setTime(date);
        }
        updateButtonText();
    }

    // RoundedButton với hiệu ứng bo góc
    private class RoundedButton extends JButton {
        private Color borderColor;
        private int arcSize = 10;

        public RoundedButton() {
            this("");
        }

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
        }

        public void setBorderColor(Color color) {
            this.borderColor = color;
        }

        public void setArcSize(int size) {
            this.arcSize = size;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcSize, arcSize);
            g2.dispose();
            super.paintComponent(g);
            if (borderColor != null) {
                Graphics2D g2b = (Graphics2D) g.create();
                g2b.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2b.setColor(borderColor);
                g2b.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);
                g2b.dispose();
            }
        }
    }
}
