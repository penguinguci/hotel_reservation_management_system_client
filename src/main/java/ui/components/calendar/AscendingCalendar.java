package ui.components.calendar;

import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AscendingCalendar extends JPanel {
    private JTextField dateField;
    private JButton calendarButton;

    public AscendingCalendar() {
        setLayout(new BorderLayout());
        dateField = new JTextField();
        dateField.setEditable(false);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        calendarButton = new JButton("📅");
        calendarButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        calendarButton.setFocusPainted(false);
        calendarButton.addActionListener(e -> showCalendarPopup());

        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
    }

    private void showCalendarPopup() {
        JPopupMenu popup = new JPopupMenu();
        JCalendar calendar = new JCalendar();
        calendar.addPropertyChangeListener("calendar", evt -> {
            Date selectedDate = calendar.getDate();
            dateField.setText(new SimpleDateFormat("dd/MM/yyyy").format(selectedDate));
            popup.setVisible(false);
        });
        popup.add(calendar);
        popup.show(this, 0, getHeight());
    }

    public Date getDate() {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(dateField.getText());
        } catch (Exception e) {
            return null;
        }
    }

    public void setDate(Date date) {
        if (date != null) {
            dateField.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));
        } else {
            dateField.setText("");
        }
    }
}