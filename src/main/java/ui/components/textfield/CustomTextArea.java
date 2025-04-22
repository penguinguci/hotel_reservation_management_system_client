package ui.components.textfield;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * A custom text area component with modern styling and scrollbars that match the table's theme
 */
public class CustomTextArea extends JTextArea {
    private Color borderColor = new Color(200, 200, 200);
    private Color focusBorderColor = new Color(136, 130, 246); // Purple theme matching the table
    private Color backgroundColor = Color.WHITE;
    private Color textColor = new Color(50, 50, 50);
    private Color placeholderColor = new Color(150, 150, 150);
    private Font textFont = new Font("Segoe UI", Font.PLAIN, 14);
    private int borderRadius = 8;
    private String placeholder = "";
    private boolean isPlaceholderShowing = false;
    private int horizontalPadding = 12;
    private int verticalPadding = 8;

    // Scrollbar styling
    private Color scrollbarThumbColor = new Color(136, 130, 246);
    private Color scrollbarThumbHoverColor = new Color(116, 110, 236);
    private Color scrollbarTrackColor = new Color(245, 245, 255);
    private int scrollbarWidth = 8;

    /**
     * Creates a new empty CustomTextArea with default styling
     */
    public CustomTextArea() {
        super();
        setupTextArea();
    }

    /**
     * Creates a new CustomTextArea with the specified text
     * @param text the initial text to be displayed
     */
    public CustomTextArea(String text) {
        super(text);
        setupTextArea();
    }

    /**
     * Creates a new CustomTextArea with the specified rows and columns
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public CustomTextArea(int rows, int columns) {
        super(rows, columns);
        setupTextArea();
    }

    /**
     * Creates a new CustomTextArea with the specified text, rows, and columns
     * @param text the initial text to be displayed
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public CustomTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
        setupTextArea();
    }

    /**
     * Creates a new CustomTextArea with the given document, text, rows, and columns
     * @param doc the text document to use
     * @param text the initial text to be displayed
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public CustomTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
        setupTextArea();
    }

    private void setupTextArea() {
        setFont(textFont);
        setForeground(textColor);
        setBackground(backgroundColor);
        setCaretColor(focusBorderColor);
        setSelectionColor(new Color(166, 161, 246, 100)); // Light purple selection
        setMargin(new Insets(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding));
        setLineWrap(true);
        setWrapStyleWord(true);

        // Add focus listeners for animated effect
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholderShowing) {
                    setText("");
                    setForeground(textColor);
                    isPlaceholderShowing = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty() && !placeholder.isEmpty()) {
                    setText(placeholder);
                    setForeground(placeholderColor);
                    isPlaceholderShowing = true;
                }
            }
        });
    }

    /**
     * Create a scroll pane for this text area with custom styling
     * @return a styled scroll pane containing this text area
     */
    public JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setBorder(createDefaultBorder());
        scrollPane.getViewport().setBackground(backgroundColor);

        // Customize scrollbars to match the table's theme
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        // Remove viewport border
        scrollPane.setViewportBorder(null);

        return scrollPane;
    }

    /**
     * Custom ScrollBarUI implementation for sleek purple scrollbars
     */
    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = scrollbarThumbColor;
            this.thumbDarkShadowColor = scrollbarThumbColor;
            this.thumbHighlightColor = scrollbarThumbColor;
            this.thumbLightShadowColor = scrollbarThumbColor;
            this.trackColor = scrollbarTrackColor;
            this.trackHighlightColor = scrollbarTrackColor;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createEmptyButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createEmptyButton();
        }

        private JButton createEmptyButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(scrollbarTrackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Change color when mouse hovers over scrollbar
            if (isDragging || isThumbRollover()) {
                g2.setColor(scrollbarThumbHoverColor);
            } else {
                g2.setColor(scrollbarThumbColor);
            }

            // Draw rounded rectangle for the thumb
            int arc = 8;
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                // Adjust thumb width to make it sleeker
                int newWidth = scrollbarWidth;
                int offset = (thumbBounds.width - newWidth) / 2;
                g2.fillRoundRect(thumbBounds.x + offset, thumbBounds.y, newWidth, thumbBounds.height, arc, arc);
            } else {
                // Adjust thumb height to make it sleeker
                int newHeight = scrollbarWidth;
                int offset = (thumbBounds.height - newHeight) / 2;
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y + offset, thumbBounds.width, newHeight, arc, arc);
            }
        }

        @Override
        protected Dimension getMinimumThumbSize() {
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                return new Dimension(scrollbarWidth, 40);
            } else {
                return new Dimension(40, scrollbarWidth);
            }
        }
    }

    /**
     * Create a border for the default (non-focused) state
     */
    private Border createDefaultBorder() {
        // Using a compound border for padding and the outer line
        return new CompoundBorder(
                new RoundedBorder(borderColor, borderRadius, 1),
                new EmptyBorder(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding)
        );
    }

    /**
     * Create a border for the focused state
     */
    private Border createFocusBorder() {
        return new CompoundBorder(
                new RoundedBorder(focusBorderColor, borderRadius, 2),
                new EmptyBorder(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding)
        );
    }

    /**
     * Set placeholder text that will be shown when the area is empty
     * @param placeholder the placeholder text
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (getText().isEmpty() && !hasFocus()) {
            setText(placeholder);
            setForeground(placeholderColor);
            isPlaceholderShowing = true;
        }
    }

    /**
     * Get the actual text, ignoring placeholder
     */
    @Override
    public String getText() {
        String text = super.getText();
        if (isPlaceholderShowing) {
            return "";
        }
        return text;
    }

    // Setter methods for customization
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setFocusBorderColor(Color focusBorderColor) {
        this.focusBorderColor = focusBorderColor;
        setCaretColor(focusBorderColor);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBackground(backgroundColor);
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        if (!isPlaceholderShowing) {
            setForeground(textColor);
        }
    }

    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
        if (isPlaceholderShowing) {
            setForeground(placeholderColor);
        }
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
        setFont(textFont);
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }

    public void setFieldPadding(int horizontalPadding, int verticalPadding) {
        this.horizontalPadding = horizontalPadding;
        this.verticalPadding = verticalPadding;
    }

    // Scrollbar customization methods
    public void setScrollbarThumbColor(Color color) {
        this.scrollbarThumbColor = color;
    }

    public void setScrollbarThumbHoverColor(Color color) {
        this.scrollbarThumbHoverColor = color;
    }

    public void setScrollbarTrackColor(Color color) {
        this.scrollbarTrackColor = color;
    }

    public void setScrollbarWidth(int width) {
        this.scrollbarWidth = width;
    }

    /**
     * Custom border implementation that draws rounded rectangles
     */
    private class RoundedBorder extends LineBorder {
        private int radius;

        RoundedBorder(Color color, int radius, int thickness) {
            super(color, thickness, true);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Text Area Example");
        CustomTextArea textArea = new CustomTextArea();
        textArea.setPlaceholder("Enter your text here...");
        textArea.setPreferredSize(new Dimension(300, 200));

        JScrollPane scrollPane = textArea.createScrollPane();
        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}