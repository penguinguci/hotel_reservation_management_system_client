package ui.components.time;

import javax.swing.*;
import javax.swing.SpinnerNumberModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSpinnerPicker extends JPanel {
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JComboBox<String> amPmComboBox;

    public TimeSpinnerPicker() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Spinner cho giờ (1-12)
        hourSpinner = new JSpinner(new SpinnerNumberModel(12, 1, 12, 1));
        JSpinner.NumberEditor hourEditor = new JSpinner.NumberEditor(hourSpinner, "00");
        hourSpinner.setEditor(hourEditor);
        add(hourSpinner);

        add(new JLabel(":"));

        // Spinner cho phút (0-59)
        minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        JSpinner.NumberEditor minuteEditor = new JSpinner.NumberEditor(minuteSpinner, "00");
        minuteSpinner.setEditor(minuteEditor);
        add(minuteSpinner);

        // Combo box cho AM/PM
        amPmComboBox = new JComboBox<>(new String[]{"AM", "PM"});
        add(amPmComboBox);

        // Set thời gian mặc định là hiện tại
        setTime(new Date());
    }

    public void setTime(Date time) {
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        SimpleDateFormat amPmFormat = new SimpleDateFormat("a");

        hourSpinner.setValue(Integer.parseInt(hourFormat.format(time)));
        minuteSpinner.setValue(Integer.parseInt(minuteFormat.format(time)));
        amPmComboBox.setSelectedItem(amPmFormat.format(time));
    }

    public Date getTime() {
        try {
            String timeString = String.format("%02d", hourSpinner.getValue()) + ":" +
                    String.format("%02d", minuteSpinner.getValue()) + " " +
                    amPmComboBox.getSelectedItem();
            return new SimpleDateFormat("hh:mm a").parse(timeString);
        } catch (Exception e) {
            return new Date();
        }
    }

    public String getTimeAsString() {
        return String.format("%02d", hourSpinner.getValue()) + ":" +
                String.format("%02d", minuteSpinner.getValue()) + " " +
                amPmComboBox.getSelectedItem();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Time Spinner Picker");
        TimeSpinnerPicker timeSpinnerPicker = new TimeSpinnerPicker();
        frame.add(timeSpinnerPicker);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setVisible(true);
    }
}