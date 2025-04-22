package ui.components.popup;

import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PopupSearch implements Serializable {
    private static final long serialVersionUID = 1L;

    private JPopupMenu popupMenu;
    private JList<String> suggestionList;
    private DefaultListModel<String> listModel;
    private List<String> suggestions;
    private Color suggestionBackground = Color.WHITE;
    private Color suggestionForeground = Color.BLACK;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private SuggestionListener suggestionListener;
    private JComponent focusTarget;

    public PopupSearch() {
        suggestions = new ArrayList<>();
        initialize();
    }

    public PopupSearch(List<String> suggestions) {
        this.suggestions = new ArrayList<>(suggestions);
        initialize();
    }

    private void initialize() {
        popupMenu = new JPopupMenu();
        popupMenu.setFocusable(false);

        listModel = new DefaultListModel<>();
        suggestionList = new JList<>(listModel);
        suggestionList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        suggestionList.setBackground(suggestionBackground);
        suggestionList.setForeground(suggestionForeground);
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionList.setFocusable(false);

        JScrollPane scrollPane = new JScrollPane(suggestionList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(460, 150));
        popupMenu.add(scrollPane);

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && suggestionList.getSelectedIndex() >= 0) {
                    selectSuggestion(suggestionList.getSelectedValue());
                    ensureFocus();
                }
            }
        });

        popupMenu.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                ensureFocus();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                ensureFocus();
            }
        });

        popupMenu.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                ensureFocus();
            }
        });
    }

    private void ensureFocus() {
        if (focusTarget == null || focusTarget.hasFocus()) return;

        // Gọi ngay lập tức
        focusTarget.requestFocusInWindow();

        // Sử dụng Timer để kiểm tra lại focus nhiều lần nếu cần
        Timer focusTimer = new Timer(50, new ActionListener() {
            int retries = 0;
            final int maxRetries = 5;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusTarget.hasFocus() || retries >= maxRetries) {
                    ((Timer) e.getSource()).stop();
                    return;
                }
                focusTarget.requestFocusInWindow();
                retries++;
            }
        });
        focusTimer.setRepeats(true);
        focusTimer.start();
    }

    @SneakyThrows
    public void selectSuggestion(String suggestion) {
        popupMenu.setVisible(false);
        if (suggestionListener != null) {
            suggestionListener.onSuggestionSelected(suggestion);
        }
    }

    public void showPopup(JComponent component) {
        this.focusTarget = component;
        updateSuggestions();
        if (!listModel.isEmpty()) {
            Container parent = component.getParent();
            Point location = component.getLocation();
            int x = location.x;
            int y = location.y + component.getHeight();
            popupMenu.show(parent, x, y);
        } else {
            System.out.println("Popup not shown: no suggestions");
        }
    }

    public void hidePopup() {
        System.out.println("Hiding popup");
        popupMenu.setVisible(false);
        ensureFocus();
    }

    public boolean isPopupFocused() {
        return suggestionList.hasFocus();
    }

    private void updateSuggestions() {
        listModel.clear();
        for (String suggestion : suggestions) {
            listModel.addElement(suggestion);
        }
    }

    public void setSuggestions(List<String> suggestions) {
        List<String> oldSuggestions = this.suggestions;
        this.suggestions = new ArrayList<>(suggestions);
        pcs.firePropertyChange("suggestions", oldSuggestions, suggestions);
        updateSuggestions();
    }

    public List<String> getSuggestions() {
        return new ArrayList<>(suggestions);
    }

    public void setSuggestionBackground(Color color) {
        Color oldColor = this.suggestionBackground;
        this.suggestionBackground = color;
        suggestionList.setBackground(color);
        pcs.firePropertyChange("suggestionBackground", oldColor, color);
    }

    public void setSuggestionForeground(Color color) {
        Color oldColor = this.suggestionForeground;
        this.suggestionForeground = color;
        suggestionList.setForeground(color);
        pcs.firePropertyChange("suggestionForeground", oldColor, color);
    }

    public void setSuggestionListener(SuggestionListener listener) {
        this.suggestionListener = listener;
    }

    public void setSelectedIndex(int index) {
        if (index >= 0 && index < listModel.getSize()) {
            suggestionList.setSelectedIndex(index);
            suggestionList.ensureIndexIsVisible(index);
        } else {
            suggestionList.clearSelection();
        }
    }

    public interface SuggestionListener {
        void onSuggestionSelected(String suggestion) throws RemoteException;
    }
}