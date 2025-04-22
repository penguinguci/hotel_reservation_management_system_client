package ui.components.spinner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * CustomSpinner - A modern, elegant JSpinner with enhanced numeric value support
 */
public class CustomSpinner extends JSpinner {

    // Flat modern color scheme
    private Color backgroundColor = new Color(250, 250, 250);
    private Color foregroundColor = new Color(60, 60, 60);
    private Color buttonColor = new Color(240, 240, 240);
    private Color buttonHoverColor = new Color(230, 230, 230);
    private Color buttonPressedColor = new Color(220, 220, 220);
    private Color borderColor = new Color(119, 118, 118);
    private Color textColor = new Color(43, 43, 43);
    private int arcSize = 6;

    // Default step size for numeric spinner
    private double stepSize = 1.0;

    public CustomSpinner() {
        initialize();
    }

    public CustomSpinner(SpinnerModel model) {
        super(model);
        initialize();

        // If using a number model, extract the step size
        if (model instanceof SpinnerNumberModel) {
            Object step = ((SpinnerNumberModel)model).getStepSize();
            if (step instanceof Number) {
                stepSize = ((Number)step).doubleValue();
            }
        }
    }

    /**
     * Create a spinner specifically for numbers with custom format
     *
     * @param min Minimum value
     * @param max Maximum value
     * @param initialValue Starting value
     * @param step Step increment/decrement size
     * @param format Number format pattern (e.g., "#,##0.##")
     * @return A configured CustomSpinner
     */
    public static CustomSpinner createNumberSpinner(double min, double max, double initialValue, double step, String format) {
        SpinnerNumberModel model = new SpinnerNumberModel(initialValue, min, max, step);
        CustomSpinner spinner = new CustomSpinner(model);

        // Set up custom number editor with the specified format
        NumberEditor editor = new NumberEditor(spinner, format);
        spinner.setEditor(editor);

        // Customize text field
        JFormattedTextField textField = editor.getTextField();
        textField.setForeground(spinner.getTextColor());
        textField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        return spinner;
    }

    /**
     * Convenience method to create an integer spinner
     */
    public static CustomSpinner createIntegerSpinner(int min, int max, int initialValue, int step) {
        return createNumberSpinner(min, max, initialValue, step, "#,##0");
    }

    /**
     * Convenience method to create a decimal spinner with 2 decimal places
     */
    public static CustomSpinner createDecimalSpinner(double min, double max, double initialValue, double step) {
        return createNumberSpinner(min, max, initialValue, step, "#,##0.00");
    }

    private void initialize() {
        setUI(new CustomSpinnerUI());
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Set up number format for numeric spinners
        if (getModel() instanceof SpinnerNumberModel) {
            NumberEditor editor = new NumberEditor(
                    this, "#,##0.##");
            setEditor(editor);

            // Customize editor field
            JFormattedTextField textField = editor.getTextField();
            textField.setForeground(textColor);
            textField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
            textField.setFont(new Font("SansSerif", Font.PLAIN, 14));

            // Add keyboard shortcuts for increment/decrement
            addKeyboardShortcuts(textField);
        }
    }

    /**
     * Get the next value from the spinner model
     * @return The next value, or null if not available
     */
    public Object getNext() {
        return getModel().getNextValue();
    }

    /**
     * Get the previous value from the spinner model
     * @return The previous value, or null if not available
     */
    public Object getPrevious() {
        return getModel().getPreviousValue();
    }

    /**
     * Add keyboard shortcut support for incrementing/decrementing
     */
    private void addKeyboardShortcuts(JFormattedTextField textField) {
        // Using Key Bindings for better component-focused behavior
        InputMap inputMap = textField.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = textField.getActionMap();

        // Up arrow to increment
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "increment");
        actionMap.put("increment", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Force commit of any edited text before incrementing
                    textField.commitEdit();
                    Object next = getNext();
                    if (next != null) {
                        setValue(next);
                    }
                } catch (ParseException ex) {
                    // If parsing fails, revert to the previous value
                    JFormattedTextField field = (JFormattedTextField)e.getSource();
                    field.setValue(CustomSpinner.this.getValue()); // Fixed: use CustomSpinner's getValue
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        // Down arrow to decrement
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "decrement");
        actionMap.put("decrement", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Force commit of any edited text before decrementing
                    textField.commitEdit();
                    Object previous = getPrevious();
                    if (previous != null) {
                        setValue(previous);
                    }
                } catch (ParseException ex) {
                    // If parsing fails, revert to the previous value
                    JFormattedTextField field = (JFormattedTextField)e.getSource();
                    field.setValue(CustomSpinner.this.getValue()); // Fixed: use CustomSpinner's getValue
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }

    /**
     * Get the current value as a double
     * @return The current value as a double, or 0 if not a number
     */
    public double getDoubleValue() {
        Object value = getValue();
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        return 0.0;
    }

    /**
     * Get the current value as an integer
     * @return The current value as an integer, or 0 if not a number
     */
    public int getIntValue() {
        Object value = getValue();
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        return 0;
    }

    /**
     * Set the step size for manual incrementing/decrementing
     * @param step The step size
     */
    public void setStepSize(double step) {
        this.stepSize = step;
        if (getModel() instanceof SpinnerNumberModel) {
            ((SpinnerNumberModel)getModel()).setStepSize(step);
        }
    }

    /**
     * Get the current step size
     * @return The step size
     */
    public double getStepSize() {
        return this.stepSize;
    }

    /**
     * Manually increment the spinner value by the step size
     */
    public void increment() {
        try {
            // Ensure any pending edits are committed
            JComponent editor = getEditor();
            if (editor instanceof DefaultEditor) {
                JFormattedTextField textField = ((DefaultEditor)editor).getTextField();
                textField.commitEdit();
            }

            // Use the model's next value
            Object next = getNext();
            if (next != null) {
                setValue(next);
            }
        } catch (ParseException e) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Manually decrement the spinner value by the step size
     */
    public void decrement() {
        try {
            // Ensure any pending edits are committed
            JComponent editor = getEditor();
            if (editor instanceof DefaultEditor) {
                JFormattedTextField textField = ((DefaultEditor)editor).getTextField();
                textField.commitEdit();
            }

            // Use the model's previous value
            Object previous = getPrevious();
            if (previous != null) {
                setValue(previous);
            }
        } catch (ParseException e) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // Getter and setter methods for customization
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        repaint();
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        repaint();
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(Color buttonColor) {
        this.buttonColor = buttonColor;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;

        // Update editor text color if applicable
        JComponent editor = getEditor();
        if (editor instanceof DefaultEditor) {
            JFormattedTextField textField = ((DefaultEditor) editor).getTextField();
            textField.setForeground(textColor);
        }

        repaint();
    }

    public int getArcSize() {
        return arcSize;
    }

    public void setArcSize(int arcSize) {
        this.arcSize = arcSize;
        repaint();
    }

    // Custom UI implementation
    class CustomSpinnerUI extends BasicSpinnerUI {
        private boolean upButtonHover = false;
        private boolean downButtonHover = false;
        private boolean upButtonPressed = false;
        private boolean downButtonPressed = false;

        // Explicitly declare these fields to reference the ones from BasicSpinnerUI
        protected JComponent editor;
        protected JButton nextButton;
        protected JButton previousButton;

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.setOpaque(false);
        }

        @Override
        protected JComponent createEditor() {
            // Get the editor from super class implementation
            editor = super.createEditor();

            // Apply custom styling
            if (editor instanceof DefaultEditor) {
                JFormattedTextField textField = ((DefaultEditor) editor).getTextField();
                textField.setForeground(textColor);
                textField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            }

            return editor;
        }

        @Override
        protected Component createPreviousButton() {
            previousButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Paint button background - subtle and flat
                    Color btnColor = buttonColor;
                    if (downButtonPressed) {
                        btnColor = buttonPressedColor;
                    } else if (downButtonHover) {
                        btnColor = buttonHoverColor;
                    }

                    g2.setColor(btnColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Draw minimalist arrow
                    g2.setColor(foregroundColor);
                    int arrowWidth = 7;
                    int arrowHeight = 3;
                    int x = (getWidth() - arrowWidth) / 2;
                    int y = (getHeight() - arrowHeight) / 2 + 1;

                    for (int i = 0; i < arrowHeight; i++) {
                        g2.drawLine(x + i, y + i, x + arrowWidth - 1 - i, y + i);
                    }

                    g2.dispose();
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(20, 12);
                }
            };

            previousButton.setFocusPainted(false);
            previousButton.setBorderPainted(false);
            previousButton.setContentAreaFilled(false);

            // Fix: Add a proper action listener to handle decrement
            previousButton.addActionListener(e -> {
                CustomSpinner.this.decrement();
            });

            previousButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    downButtonHover = true;
                    previousButton.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    downButtonHover = false;
                    downButtonPressed = false;
                    previousButton.repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    downButtonPressed = true;
                    previousButton.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    downButtonPressed = false;
                    previousButton.repaint();
                }
            });

            return previousButton;
        }

        @Override
        protected Component createNextButton() {
            nextButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Paint button background - subtle and flat
                    Color btnColor = buttonColor;
                    if (upButtonPressed) {
                        btnColor = buttonPressedColor;
                    } else if (upButtonHover) {
                        btnColor = buttonHoverColor;
                    }

                    g2.setColor(btnColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Draw minimalist arrow
                    g2.setColor(foregroundColor);
                    int arrowWidth = 7;
                    int arrowHeight = 3;
                    int x = (getWidth() - arrowWidth) / 2;
                    int y = (getHeight() - arrowHeight) / 2 - 1;

                    for (int i = 0; i < arrowHeight; i++) {
                        g2.drawLine(x + i, y + (arrowHeight - 1 - i), x + arrowWidth - 1 - i, y + (arrowHeight - 1 - i));
                    }

                    g2.dispose();
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(20, 12);
                }
            };

            nextButton.setFocusPainted(false);
            nextButton.setBorderPainted(false);
            nextButton.setContentAreaFilled(false);

            // Fix: Add a proper action listener to handle increment
            nextButton.addActionListener(e -> {
                CustomSpinner.this.increment();
            });

            nextButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    upButtonHover = true;
                    nextButton.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    upButtonHover = false;
                    upButtonPressed = false;
                    nextButton.repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    upButtonPressed = true;
                    nextButton.repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    upButtonPressed = false;
                    nextButton.repaint();
                }
            });

            return nextButton;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = c.getWidth();
            int height = c.getHeight();

            // Draw clean background with subtle rounded corners
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, width, height, arcSize, arcSize);

            // Draw thin, subtle border
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(0, 0, width - 1, height - 1, arcSize, arcSize);

            // Draw subtle separator between buttons
            int buttonDividerY = height / 2;
            g2.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 150));
            g2.drawLine(width - 20, buttonDividerY, width - 1, buttonDividerY);

            // Draw subtle vertical separator between text and buttons
            g2.drawLine(width - 21, 1, width - 21, height - 2);

            g2.dispose();

            super.paint(g, c);
        }

        @Override
        protected LayoutManager createLayout() {
            return new SpinnerLayout();
        }

        // Improved layout for better proportions
        private class SpinnerLayout implements LayoutManager {
            public void addLayoutComponent(String name, Component c) {}
            public void removeLayoutComponent(Component c) {}

            public Dimension preferredLayoutSize(Container parent) {
                Dimension editorSize = editor.getPreferredSize();
                Dimension nextButtonSize = nextButton.getPreferredSize();
                Dimension previousButtonSize = previousButton.getPreferredSize();

                return new Dimension(
                        editorSize.width + nextButtonSize.width,
                        Math.max(editorSize.height, nextButtonSize.height + previousButtonSize.height)
                );
            }

            public Dimension minimumLayoutSize(Container parent) {
                return preferredLayoutSize(parent);
            }

            public void layoutContainer(Container parent) {
                int width = parent.getWidth();
                int height = parent.getHeight();

                // Layout with fixed button width
                int buttonWidth = 20;

                // Position editor
                editor.setBounds(0, 0, width - buttonWidth, height);

                // Position buttons
                nextButton.setBounds(width - buttonWidth, 0, buttonWidth, height / 2);
                previousButton.setBounds(width - buttonWidth, height / 2, buttonWidth, height / 2);
            }
        }
    }

    // Demo method with multiple theme options and enhanced number handling
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Enhanced Custom Spinner Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(new Color(245, 245, 245));
            frame.setLayout(new GridLayout(8, 2, 15, 15));
            ((GridLayout)frame.getLayout()).setHgap(20);
            ((GridLayout)frame.getLayout()).setVgap(20);
            frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Default integer spinner
            CustomSpinner intSpinner = createIntegerSpinner(0, 100, 42, 1);

            // Decimal spinner
            CustomSpinner decimalSpinner = createDecimalSpinner(0.0, 100.0, 42.5, 0.5);

            // Large step spinner
            CustomSpinner largeStepSpinner = createIntegerSpinner(0, 1000, 50, 10);

            // Percentage spinner with custom format
            CustomSpinner percentSpinner = createNumberSpinner(0, 100, 75, 5, "#0'%'");

            // Blue theme spinner
            CustomSpinner blueSpinner = createDecimalSpinner(0.0, 100.0, 75.0, 0.25);
            blueSpinner.setBackgroundColor(new Color(240, 248, 255));
            blueSpinner.setBorderColor(new Color(176, 196, 222));
            blueSpinner.setButtonColor(new Color(230, 240, 250));
            blueSpinner.setForegroundColor(new Color(70, 130, 180));

            // Currency spinner
            CustomSpinner currencySpinner = createNumberSpinner(0, 10000, 1299.99, 100, "$#,##0.00");
            currencySpinner.setBackgroundColor(new Color(240, 255, 240));
            currencySpinner.setBorderColor(new Color(144, 238, 144));
            currencySpinner.setButtonColor(new Color(230, 250, 230));
            currencySpinner.setForegroundColor(new Color(60, 179, 113));

            // Date spinner
            CustomSpinner dateSpinner = new CustomSpinner(new SpinnerDateModel());
            DateEditor dateEditor = new DateEditor(
                    dateSpinner, "dd/MM/yyyy");
            dateSpinner.setEditor(dateEditor);
            dateSpinner.setBackgroundColor(new Color(253, 245, 230));
            dateSpinner.setBorderColor(new Color(210, 180, 140));
            dateSpinner.setButtonColor(new Color(250, 240, 230));
            dateSpinner.setForegroundColor(new Color(160, 82, 45));

            // List spinner
            String[] items = {"Apple", "Banana", "Cherry", "Durian", "Elderberry"};
            CustomSpinner listSpinner = new CustomSpinner(
                    new SpinnerListModel(items));
            listSpinner.setBackgroundColor(new Color(245, 245, 255));
            listSpinner.setBorderColor(new Color(230, 230, 250));
            listSpinner.setButtonColor(new Color(240, 240, 245));
            listSpinner.setForegroundColor(new Color(106, 90, 205));

            // Add components with labels
            JLabel intLabel = new JLabel("Integer Spinner:");
            intLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel decimalLabel = new JLabel("Decimal Spinner (0.5 steps):");
            decimalLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel largeStepLabel = new JLabel("Large Step Spinner (10):");
            largeStepLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel percentLabel = new JLabel("Percentage Spinner:");
            percentLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel blueLabel = new JLabel("Blue Theme (0.25 steps):");
            blueLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel currencyLabel = new JLabel("Currency Spinner:");
            currencyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel dateLabel = new JLabel("Date Spinner:");
            dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel listLabel = new JLabel("List Spinner:");
            listLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            frame.add(intLabel);
            frame.add(intSpinner);
            frame.add(decimalLabel);
            frame.add(decimalSpinner);
            frame.add(largeStepLabel);
            frame.add(largeStepSpinner);
            frame.add(percentLabel);
            frame.add(percentSpinner);
            frame.add(blueLabel);
            frame.add(blueSpinner);
            frame.add(currencyLabel);
            frame.add(currencySpinner);
            frame.add(dateLabel);
            frame.add(dateSpinner);
            frame.add(listLabel);
            frame.add(listSpinner);

            // Add a panel with control buttons for demonstration
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton getValueBtn = new JButton("Get Current Value");
            getValueBtn.addActionListener(e ->
                    JOptionPane.showMessageDialog(frame,
                            "Current Integer Value: " + intSpinner.getIntValue() +
                                    "\nCurrent Decimal Value: " + decimalSpinner.getDoubleValue(),
                            "Spinner Values", JOptionPane.INFORMATION_MESSAGE));

            controlPanel.add(getValueBtn);

            // Add control panel spanning both columns
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            frame.setSize(550, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}