package ui.components.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import ui.components.button.LogoutButton;

public class Menu extends JComponent {
    private MenuEvent event;
    private MigLayout layout;
    private String[][] menuItems = new String[][] {
            {"Trang chủ"},
            {"Đặt phòng"}, {"Phòng và Dịch vụ"},
            {"Khách hàng"},
            {"Nhân viên"}, {"Thống kê"}
    };
    private LogoutButton logoutButton;
    private JPanel menuPanel;
    private MenuItem selectedMenuItem;

    public Menu() {
        init();
    }

    public MenuEvent getEvent() {
        return event;
    }

    public void setEvent(MenuEvent event) {
        this.event = event;
    }

    public void init() {
        layout = new MigLayout("wrap 1, fillx, gapy 0, inset 2", "[fill]", "[][grow][]");
        setLayout(layout);
        setOpaque(true);

        menuPanel = new JPanel(new MigLayout("wrap 1, fillx, gapy 0", "fill"));
        menuPanel.setOpaque(false);
        for (int i = 0; i < menuItems.length; i++) {
            addMenu(menuPanel, menuItems[i][0], i);
        }
        add(menuPanel, "grow");

        addLogoutButton();
    }

    private void addLogoutButton() {
        logoutButton = new LogoutButton();
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (event != null) {
                    try {
                        event.selected(-1, -1);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        JPanel logoutPanel = new JPanel(new MigLayout("insets 0", "[left]"));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutButton, "gapleft 10, gapbottom 10");
        add(logoutPanel, "south");
    }

    public void setLogoutAction(ActionListener listener) {
        logoutButton.removeActionListener(logoutButton.getActionListeners()[0]);
        logoutButton.addActionListener(listener);
    }

    private Icon getIcon(int index) {
        String path = "src/main/java/ui/components/menu/" + index + ".png";
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        } else {
            return null;
        }
    }

    private void addMenu(JPanel panel, String menuName, int index) {
        int length = menuItems[index].length;
        MenuItem item = new MenuItem(menuName, index, length > 1);
        Icon icon = getIcon(index);
        if (icon != null) {
            item.setIcon(icon);
        }

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!item.isSelected()) {
                    item.repaint();
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!item.isSelected()) {
                    item.repaint();
                }
            }
        });

        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (length > 1) {
                    closeOtherSubMenus(item);
                    if (!item.isSelected()) {
                        item.setSelected(true);
                        addSubMenu(item, index, length);
                    } else {
                        closeSubMenu(item);
                        item.setSelected(false);
                    }
                } else {
                    closeAllSubMenus();
                    if (event != null) {
                        try {
                            event.selected(index, 0);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

                selectedMenuItem = item.isSelected() ? item : null;
            }
        });
        panel.add(item);
    }

    private void addSubMenu(MenuItem item, int index, int length) {
        for (Component com : menuPanel.getComponents()) {
            if (com instanceof JPanel && com.getName() != null && com.getName().equals(index + "")) {
                com.setVisible(true);
                MenuAnimation.showMenu((JPanel) com, item, (MigLayout) menuPanel.getLayout());
                return;
            }
        }

        // Tạo submenu mới
        JPanel panel = new JPanel(new MigLayout("wrap 1, fillx, inset 0, gapy 0", "fill"));
        panel.setName(index + "");
        panel.setOpaque(false); // Làm trong suốt để không che màu nền của MenuItem

        for (int i = 1; i < length; i++) {
            MenuItem subItem = new MenuItem(menuItems[index][i], i, false);
            subItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (event != null) {
                        try {
                            event.selected(index, subItem.getIndex());
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });
            subItem.initSubMenu(i, length);
            panel.add(subItem);
        }

        int itemIndex = -1;
        Component[] components = menuPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == item) {
                itemIndex = i;
                break;
            }
        }

        if (itemIndex != -1) {
            String constraints = "cell 0 " + (itemIndex + 1) + ",growx,h 0!";
            menuPanel.add(panel, constraints, itemIndex + 1);
            MenuAnimation.showMenu(panel, item, (MigLayout) menuPanel.getLayout());
        }
    }

    private void closeSubMenu(MenuItem item) {
        for (Component com : menuPanel.getComponents()) {
            if (com instanceof JPanel && com.getName() != null && com.getName().equals(item.getIndex() + "")) {
                MenuAnimation.hideMenu((JPanel) com, item, (MigLayout) menuPanel.getLayout(), () -> {
                    menuPanel.remove(com);
                    menuPanel.revalidate();
                    menuPanel.repaint();
                });
                break;
            }
        }
    }

    private void closeOtherSubMenus(MenuItem exceptItem) {
        for (Component com : menuPanel.getComponents()) {
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item != exceptItem && item.isSelected()) {
                    item.setSelected(false);
                    closeSubMenu(item);
                }
            }
        }
    }

    private void closeAllSubMenus() {
        for (Component com : menuPanel.getComponents()) {
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item.isSelected()) {
                    item.setSelected(false);
                    closeSubMenu(item);
                }
            }
        }
        selectedMenuItem = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(149, 145, 239));
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        super.paintComponent(g);
    }
}