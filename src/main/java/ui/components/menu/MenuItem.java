package ui.components.menu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import ui.components.effect.RippleEffect;
import ui.components.swing.shadow.ShadowRenderer;

public class MenuItem extends JButton {
    private RippleEffect rippleEffect;
    private BufferedImage shadow;
    private int shadowWidth;
    private int shadowSize = 10;
    private int index;
    private boolean subMenuAble;
    private float animate;
    private int subMenuIndex;
    private int length;
    private boolean isHovered = false;
    private boolean selected = false;

    public MenuItem(String name, int index, boolean subMenuAble) {
        super(name);
        this.index = index;
        this.subMenuAble = subMenuAble;
        setContentAreaFilled(false);
        setForeground(new Color(230, 230, 230));
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(new EmptyBorder(9, 10, 9, 10));
        setFont(new Font("Segoe UI", Font.PLAIN, 18));
        setIconTextGap(10);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        rippleEffect = new RippleEffect(this);
        rippleEffect.setRippleColor(new Color(220, 220, 220));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    private void createShadowImage() {
        int width = getWidth();
        int height = getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fill(new Rectangle2D.Double(0, 0, width, height));
        shadow = new ShadowRenderer(shadowSize, 0.1f, Color.BLACK).createShadow(img);
        g2.dispose();
    }

    public void initSubMenu(int subMenuIndex, int length) {
        this.subMenuIndex = subMenuIndex;
        this.length = length;
        setBorder(new EmptyBorder(9, 33, 9, 10));
        setOpaque(true);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        animate = selected ? 1f : 0f;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền dựa trên trạng thái
        if (length > 0) { // Submenu items
            if (selected) {
                // Trạng thái active cho submenu
                GradientPaint activeGradient = new GradientPaint(
                        0, 0, new Color(140, 120, 255),  // Tím đậm
                        0, getHeight(), new Color(170, 150, 255)
                );
                g2.setPaint(activeGradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                setForeground(Color.WHITE);
            } else if (isHovered) {
                // Trạng thái hover cho submenu
                GradientPaint hoverGradient = new GradientPaint(
                        0, 0, new Color(160, 145, 255),  // Tím trung gian
                        0, getHeight(), new Color(190, 175, 255)
                );
                g2.setPaint(hoverGradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                setForeground(Color.WHITE);
            } else {
                // Trạng thái mặc định cho submenu
                GradientPaint defaultGradient = new GradientPaint(
                        0, 0, new Color(180, 165, 255),  // Tím nhạt
                        0, getHeight(), new Color(140, 120, 255)
                );
                g2.setPaint(defaultGradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                setForeground(new Color(255, 255, 255));
                setBackground(new Color(180, 165, 255));
            }
        } else { // Main menu items
            if (selected) {
                g2.setColor(new Color(130, 120, 255));
                g2.fillRect(0, 0, getWidth(), getHeight());
                setForeground(Color.WHITE);
            } else if (isHovered) {
                g2.setColor(new Color(100, 90, 200, 100));
                g2.fillRect(0, 0, getWidth(), getHeight());
                setForeground(new Color(240, 240, 240));
            } else {
                g2.setColor(new Color(149, 145, 239));
                g2.fillRect(0, 0, getWidth(), getHeight());
                setForeground(new Color(230, 230, 230));
            }
        }

        // Vẽ cây menu cho submenu items
        if (length != 0) {
            g2.setColor(new Color(71, 70, 70, 180)); // Màu xám đậm cho cây menu rõ nét
            g2.setStroke(new BasicStroke(1.5f));

            if (subMenuIndex == 1) {
                g2.drawImage(shadow, -shadowSize, -20, null);
                g2.drawLine(18, 0, 18, getHeight());
                g2.drawLine(18, getHeight() / 2, 26, getHeight() / 2);
            } else if (subMenuIndex == length - 1) {
                g2.drawImage(shadow, -shadowSize, getHeight() - 6, null);
                g2.drawLine(18, 0, 18, getHeight() / 2);
                g2.drawLine(18, getHeight() / 2, 26, getHeight() / 2);
            } else {
                g2.drawLine(18, 0, 18, getHeight());
                g2.drawLine(18, getHeight() / 2, 26, getHeight() / 2);
            }
        } else if (subMenuAble) {
            g2.setColor(getForeground());
            int arrowWidth = 8;
            int arrowHeight = 4;
            Path2D p = new Path2D.Double();
            p.moveTo(0, arrowHeight * animate);
            p.lineTo(arrowWidth / 2, (1f - animate) * arrowHeight);
            p.lineTo(arrowWidth, arrowHeight * animate);
            g2.translate(getWidth() - arrowWidth - 15, (getHeight() - arrowHeight) / 2);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.draw(p);
        }

        g2.dispose();
        rippleEffect.reder(g, new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        super.paintComponent(g);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        createShadowImage();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSubMenuAble() {
        return subMenuAble;
    }

    public void setSubMenuAble(boolean subMenuAble) {
        this.subMenuAble = subMenuAble;
    }

    public int getSubMenuIndex() {
        return subMenuIndex;
    }

    public void setSubMenuIndex(int subMenuIndex) {
        this.subMenuIndex = subMenuIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public float getAnimate() {
        return animate;
    }

    public void setAnimate(float animate) {
        this.animate = animate;
    }
}