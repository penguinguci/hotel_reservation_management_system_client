package ui.components.time;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A custom TimePicker component for Swing
 * @author YourName
 */
public class TimePicker extends JPanel {

    private JSpinner timeSpinner;
    private JLabel timeLabel;
    private Date time;
    private SimpleDateFormat timeFormat;

    /**
     * Creates new TimePicker
     */
    public TimePicker() {
        initComponents();
    }

    /**
     * TimePicker with custom label
     * @param label The label text
     */
    public TimePicker(String label) {
        this();
        timeLabel.setText(label);
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 0));

        // Default format for time display
        timeFormat = new SimpleDateFormat("HH:mm:ss");

        // Initialize time to current time
        time = new Date();

        // Create spinner model using calendar
        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(time);

        // Create and configure the time spinner
        timeSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(editor);

        // Add change listener to update time value when spinner changes
        timeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                time = (Date) timeSpinner.getValue();
                firePropertyChange("time", null, time);
            }
        });

        // Create label
        timeLabel = new JLabel("Time:");

        // Add components to panel
        add(timeLabel, BorderLayout.WEST);
        add(timeSpinner, BorderLayout.CENTER);
    }

    /**
     * Get the current time value
     * @return Date object representing the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Set the time value
     * @param time The time to set
     */
    public void setTime(Date time) {
        Date oldTime = this.time;
        this.time = time;
        timeSpinner.setValue(time);
        firePropertyChange("time", oldTime, time);
    }

    /**
     * Get the time as formatted string
     * @return Time as string in current format
     */
    public String getTimeAsString() {
        return timeFormat.format(time);
    }

    /**
     * Set the time format pattern
     * @param pattern Format pattern for SimpleDateFormat
     */
    public void setTimeFormat(String pattern) {
        timeFormat = new SimpleDateFormat(pattern);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, pattern);
        timeSpinner.setEditor(editor);
    }

    /**
     * Get label text
     * @return The label text
     */
    public String getLabelText() {
        return timeLabel.getText();
    }

    /**
     * Set label text
     * @param text The text to set
     */
    public void setLabelText(String text) {
        timeLabel.setText(text);
    }

    /**
     * Set hours only
     * @param hours Hours (0-23)
     */
    public void setHours(int hours) {
        if (hours < 0 || hours > 23)
            throw new IllegalArgumentException("Hours must be between 0-23");

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        setTime(cal.getTime());
    }

    /**
     * Set minutes only
     * @param minutes Minutes (0-59)
     */
    public void setMinutes(int minutes) {
        if (minutes < 0 || minutes > 59)
            throw new IllegalArgumentException("Minutes must be between 0-59");

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.MINUTE, minutes);
        setTime(cal.getTime());
    }

    /**
     * Set seconds only
     * @param seconds Seconds (0-59)
     */
    public void setSeconds(int seconds) {
        if (seconds < 0 || seconds > 59)
            throw new IllegalArgumentException("Seconds must be between 0-59");

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.SECOND, seconds);
        setTime(cal.getTime());
    }

    /**
     * Get hours component
     * @return Hours (0-23)
     */
    public int getHours() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Get minutes component
     * @return Minutes (0-59)
     */
    public int getMinutes() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.MINUTE);
    }

    /**
     * Get seconds component
     * @return Seconds (0-59)
     */
    public int getSeconds() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.SECOND);
    }

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame("TimePicker Test");
        TimePicker timePicker = new TimePicker("Select Time:");
        timePicker.setTime(new Date());

        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.add(timePicker);
        frame.pack();
        frame.setVisible(true);
    }
}
