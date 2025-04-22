package ui.components.tab;

import ui.components.border.ShadowBorder;
import ui.components.table.CustomTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;

public class CustomTabPanel extends JPanel {
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color CARD_BACKGROUND = Color.WHITE;
    private DefaultTableModel tableModel;
    private CustomTable table;
    private TableRowSorter<DefaultTableModel> sorter;

    public CustomTabPanel(String[] columnNames, JPanel formPanel, String searchLabel) {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add Form Panel
        formPanel.setBackground(CARD_BACKGROUND);
        formPanel.setBorder(new ShadowBorder(5, new Color(0, 0, 0, 50)));
        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(CARD_BACKGROUND);
        tablePanel.setBorder(new ShadowBorder(5, new Color(0, 0, 0, 50)));
        tablePanel.setName("tablePanel");

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setOpaque(false);
        JLabel lblSearch = new JLabel(searchLabel);
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(lblSearch);

        JTextField txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(300, 35));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        searchPanel.add(txtSearch);

        tablePanel.add(searchPanel, BorderLayout.NORTH);

        // Table Setup
        tableModel = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không có cột nào chỉnh sửa được
            }
        };
        table = new CustomTable();
        table.setHeaderBackground(new Color(103, 58, 183)); // Áp dụng màu purple từ Form_EmployeeManagement
        table.setModel(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Search functionality
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public CustomTable getTable() {
        return table;
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    private void filterTable() {
        JTextField txtSearch = (JTextField) ((JPanel) ((JPanel) getComponent(1)).getComponent(0)).getComponent(1);
        String text = txtSearch.getText().trim().toLowerCase();
        sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
    }
}