package ui.components.time;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * A modern, stylish TimePicker component for Swing
 * @author CustomComponent
 */
public class ModernTimePicker extends JPanel {

    // Main components
    private JPanel displayPanel;
    private JLabel timeLabel;
    private JLabel amPmLabel;
    private JPopupMenu popup;
    private TimePickerPanel pickerPanel;

    // Time properties
    private Date time;
    private SimpleDateFormat timeFormat;
    private boolean use24HourFormat = false;
    private boolean showSeconds = true;

    // Visual properties
    private Color accentColor = new Color(25, 118, 210); // Material blue
    private Color backgroundColor = Color.WHITE;
    private Color textColor = new Color(60, 60, 60);
    private Color buttonHoverColor = new Color(240, 240, 240);
    private int cornerRadius = 8;
    private Font timeFont = new Font("Segoe UI", Font.BOLD, 18);
    private Font amPmFont = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Creates new ModernTimePicker
     */
    public ModernTimePicker() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(backgroundColor);

        // Initialize time to current time
        time = new Date();

        // Default format for time display
        timeFormat = new SimpleDateFormat("HH:mm:ss");

        // Create main display panel
        displayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(backgroundColor);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

                // Draw border
                g2.setColor(new Color(220, 220, 220));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius));
                g2.dispose();
            }
        };
        displayPanel.setOpaque(false);
        displayPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Time label
        timeLabel = new JLabel(formatTime());
        timeLabel.setFont(timeFont);
        timeLabel.setForeground(textColor);

        // AM/PM label
        amPmLabel = new JLabel(getAmPm());
        amPmLabel.setFont(amPmFont);
        amPmLabel.setForeground(textColor);

        // Add components to display panel
        displayPanel.add(timeLabel);
        displayPanel.add(amPmLabel);

        // Add click listener to display panel
        displayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTimePickerPopup();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                displayPanel.setBackground(buttonHoverColor);
                displayPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                displayPanel.setBackground(backgroundColor);
                displayPanel.repaint();
            }
        });

        // Create time picker popup
        popup = new JPopupMenu();
        popup.setBackground(backgroundColor);
        popup.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Create time picker panel
        pickerPanel = new TimePickerPanel();
        popup.add(pickerPanel);

        // Add display panel to the main component
        add(displayPanel, BorderLayout.CENTER);

        // Set preferred size
        setPreferredSize(new Dimension(120, 35));

        // Start timer to update time every second if showing current time
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDisplay();
            }
        });
        timer.start();
    }

    /**
     * Show the time picker popup
     */
    private void showTimePickerPopup() {
        popup.show(displayPanel, 0, displayPanel.getHeight());
    }

    /**
     * Update the display with current time values
     */
    private void updateDisplay() {
        timeLabel.setText(formatTime());
        amPmLabel.setText(getAmPm());

        // Hide AM/PM label if using 24-hour format
        amPmLabel.setVisible(!use24HourFormat);
    }

    /**
     * Format the time according to current settings
     * @return Formatted time string
     */
    private String formatTime() {
        SimpleDateFormat format;
        if (use24HourFormat) {
            format = showSeconds ? new SimpleDateFormat("HH:mm:ss") : new SimpleDateFormat("HH:mm");
        } else {
            format = showSeconds ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("hh:mm");
        }
        return format.format(time);
    }

    /**
     * Get AM/PM text
     * @return AM or PM string
     */
    private String getAmPm() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
    }

    /**
     * Inner class for the time picker panel
     */
    private class TimePickerPanel extends JPanel {
        private JPanel hoursPanel;
        private JPanel minutesPanel;
        private JPanel secondsPanel;
        private JPanel amPmPanel;
        private JPanel buttonsPanel;

        private NumberSpinner hoursSpinner;
        private NumberSpinner minutesSpinner;
        private NumberSpinner secondsSpinner;
        private AmPmSelector amPmSelector;

        private JButton cancelButton;
        private JButton okButton;

        public TimePickerPanel() {
            setLayout(new GridBagLayout());
            setBackground(backgroundColor);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(5, 5, 5, 5);

            // Hours panel
            hoursPanel = createLabeledPanel("Hours");
            hoursSpinner = createHoursSpinner();
            hoursPanel.add(hoursSpinner, BorderLayout.CENTER);

            // Minutes panel
            minutesPanel = createLabeledPanel("Minutes");
            minutesSpinner = createMinutesSpinner();
            minutesPanel.add(minutesSpinner, BorderLayout.CENTER);

            // Seconds panel
            secondsPanel = createLabeledPanel("Seconds");
            secondsSpinner = createSecondsSpinner();
            secondsPanel.add(secondsSpinner, BorderLayout.CENTER);

            // AM/PM panel
            amPmPanel = createLabeledPanel("AM/PM");
            amPmSelector = new AmPmSelector();
            amPmPanel.add(amPmSelector, BorderLayout.CENTER);

            // Buttons panel
            buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonsPanel.setOpaque(false);

            cancelButton = createButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    popup.setVisible(false);
                }
            });

            okButton = createButton("OK");
            okButton.setBackground(accentColor);
            okButton.setForeground(Color.WHITE);
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    applyTimeChanges();
                    popup.setVisible(false);
                }
            });

            buttonsPanel.add(cancelButton);
            buttonsPanel.add(okButton);

            // Add components to layout
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(hoursPanel, gbc);

            gbc.gridx = 1;
            add(minutesPanel, gbc);

            if (showSeconds) {
                gbc.gridx = 2;
                add(secondsPanel, gbc);
            }

            if (!use24HourFormat) {
                gbc.gridx = 3;
                add(amPmPanel, gbc);
            }

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            add(buttonsPanel, gbc);

            // Set preferred size
            setPreferredSize(new Dimension(showSeconds ? 400 : 300, 150));
        }

        private JPanel createLabeledPanel(String labelText) {
            JPanel panel = new JPanel(new BorderLayout(0, 5));
            panel.setOpaque(false);

            JLabel label = new JLabel(labelText);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setForeground(new Color(120, 120, 120));
            label.setHorizontalAlignment(SwingConstants.CENTER);

            panel.add(label, BorderLayout.NORTH);
            return panel;
        }

        private JButton createButton(String text) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 6, 6));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setPreferredSize(new Dimension(70, 28));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBackground(new Color(245, 245, 245));
            button.setForeground(textColor);

            return button;
        }

        private NumberSpinner createHoursSpinner() {
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (!use24HourFormat) {
                hour = cal.get(Calendar.HOUR);
                if (hour == 0) hour = 12;
            }

            return new NumberSpinner(hour, use24HourFormat ? 0 : 1, use24HourFormat ? 23 : 12);
        }

        private NumberSpinner createMinutesSpinner() {
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            return new NumberSpinner(cal.get(Calendar.MINUTE), 0, 59);
        }

        private NumberSpinner createSecondsSpinner() {
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            return new NumberSpinner(cal.get(Calendar.SECOND), 0, 59);
        }

        private void applyTimeChanges() {
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);

            // Set hour
            if (use24HourFormat) {
                cal.set(Calendar.HOUR_OF_DAY, hoursSpinner.getValue());
            } else {
                int hour = hoursSpinner.getValue();
                if (hour == 12) hour = 0;
                if (amPmSelector.isAm()) {
                    cal.set(Calendar.AM_PM, Calendar.AM);
                } else {
                    cal.set(Calendar.AM_PM, Calendar.PM);
                }
                cal.set(Calendar.HOUR, hour);
            }

            // Set minute and second
            cal.set(Calendar.MINUTE, minutesSpinner.getValue());
            if (showSeconds) {
                cal.set(Calendar.SECOND, secondsSpinner.getValue());
            } else {
                cal.set(Calendar.SECOND, 0);
            }

            // Update time
            Date oldTime = time;
            time = cal.getTime();
            updateDisplay();

            // Fire property change event
            firePropertyChange("time", oldTime, time);
        }
    }

    /**
     * Inner class for number spinners (hours, minutes, seconds)
     */
    private class NumberSpinner extends JPanel {
        private JLabel valueLabel;
        private JButton upButton;
        private JButton downButton;
        private int value;
        private int min;
        private int max;

        public NumberSpinner(int initialValue, int min, int max) {
            this.value = initialValue;
            this.min = min;
            this.max = max;

            setLayout(new GridBagLayout());
            setOpaque(false);

            // Create components
            valueLabel = new JLabel(String.format("%02d", value));
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            valueLabel.setForeground(textColor);
            valueLabel.setPreferredSize(new Dimension(50, 30));

            upButton = createArrowButton("\u25B2");
            upButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    incrementValue();
                }
            });

            downButton = createArrowButton("\u25BC");
            downButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    decrementValue();
                }
            });

            // Layout components
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            add(upButton, gbc);

            gbc.gridy = 1;
            add(valueLabel, gbc);

            gbc.gridy = 2;
            add(downButton, gbc);

            // Set preferred size
            setPreferredSize(new Dimension(60, 90));
        }

        private JButton createArrowButton(String text) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 4, 4));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 8));
            button.setPreferredSize(new Dimension(50, 20));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setForeground(accentColor);
            button.setBackground(new Color(245, 245, 245));
            button.setBorder(new EmptyBorder(2, 0, 2, 0));

            return button;
        }

        public void incrementValue() {
            value++;
            if (value > max) value = min;
            updateDisplay();
        }

        public void decrementValue() {
            value--;
            if (value < min) value = max;
            updateDisplay();
        }

        private void updateDisplay() {
            valueLabel.setText(String.format("%02d", value));
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            if (value >= min && value <= max) {
                this.value = value;
                updateDisplay();
            }
        }
    }

    /**
     * Inner class for AM/PM selector
     */
    private class AmPmSelector extends JPanel {
        private JButton amButton;
        private JButton pmButton;
        private boolean isAm;

        public AmPmSelector() {
            setLayout(new GridBagLayout());
            setOpaque(false);

            // Determine initial AM/PM state
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            isAm = cal.get(Calendar.AM_PM) == Calendar.AM;

            // Create buttons
            amButton = createAmPmButton("AM");
            pmButton = createAmPmButton("PM");

            // Add action listeners
            amButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setAm(true);
                }
            });

            pmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setAm(false);
                }
            });

            // Layout components
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            add(amButton, gbc);

            gbc.gridy = 1;
            add(pmButton, gbc);

            // Update button states
            updateButtonStates();

            // Set preferred size
            setPreferredSize(new Dimension(60, 60));
        }

        private JButton createAmPmButton(String text) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 4, 4));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setPreferredSize(new Dimension(50, 25));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            return button;
        }

        public boolean isAm() {
            return isAm;
        }

        public void setAm(boolean isAm) {
            this.isAm = isAm;
            updateButtonStates();
        }

        private void updateButtonStates() {
            if (isAm) {
                amButton.setBackground(accentColor);
                amButton.setForeground(Color.WHITE);
                pmButton.setBackground(new Color(245, 245, 245));
                pmButton.setForeground(textColor);
            } else {
                pmButton.setBackground(accentColor);
                pmButton.setForeground(Color.WHITE);
                amButton.setBackground(new Color(245, 245, 245));
                amButton.setForeground(textColor);
            }
        }
    }

    // ---- Getters and Setters ----

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
        updateDisplay();
        firePropertyChange("time", oldTime, time);
    }

    /**
     * Get the time as formatted string
     * @return Time as string in current format
     */
    public String getTimeAsString() {
        return formatTime();
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
     * Get minutes component
     * @return Minutes (0-59)
     */
    public int getMinutes() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.MINUTE);
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
     * Get seconds component
     * @return Seconds (0-59)
     */
    public int getSeconds() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.SECOND);
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
     * Check if using 24-hour format
     * @return true if using 24-hour format, false for 12-hour
     */
    public boolean isUse24HourFormat() {
        return use24HourFormat;
    }

    /**
     * Set time format to 24-hour or 12-hour
     * @param use24HourFormat true for 24-hour, false for 12-hour
     */
    public void setUse24HourFormat(boolean use24HourFormat) {
        this.use24HourFormat = use24HourFormat;
        updateDisplay();
    }

    /**
     * Check if showing seconds
     * @return true if showing seconds
     */
    public boolean isShowSeconds() {
        return showSeconds;
    }

    /**
     * Set whether to show seconds
     * @param showSeconds true to show seconds
     */
    public void setShowSeconds(boolean showSeconds) {
        this.showSeconds = showSeconds;
        updateDisplay();
    }

    /**
     * Get accent color
     * @return The accent color
     */
    public Color getAccentColor() {
        return accentColor;
    }

    /**
     * Set accent color
     * @param accentColor The color to set
     */
    public void setAccentColor(Color accentColor) {
        this.accentColor = accentColor;
        repaint();
    }

    /**
     * Get background color
     * @return The background color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Set background color
     * @param backgroundColor The color to set
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBackground(backgroundColor);
        repaint();
    }

    /**
     * Get text color
     * @return The text color
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Set text color
     * @param textColor The color to set
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        timeLabel.setForeground(textColor);
        amPmLabel.setForeground(textColor);
    }

    /**
     * Get corner radius
     * @return The corner radius
     */
    public int getCornerRadius() {
        return cornerRadius;
    }

    /**
     * Set corner radius
     * @param cornerRadius The radius to set
     */
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                javax.swing.JFrame frame = new javax.swing.JFrame("Modern Time Picker");
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 200);
                frame.setLayout(new FlowLayout());

                ModernTimePicker timePicker = new ModernTimePicker();
                timePicker.setUse24HourFormat(false);
                timePicker.setShowSeconds(true);

                frame.add(timePicker);
                frame.setVisible(true);
            }
        });
    }
}
