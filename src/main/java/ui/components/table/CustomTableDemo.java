package ui.components.table;


import ui.components.button.ButtonCustom;
import ui.components.table.CustomTableButton;
import  ui.components.table.CustomTableButton.ButtonType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Lớp demo minh họa cách sử dụng CustomTable
 */
public class CustomTableDemo extends JFrame {

    private CustomTableButton customTable;

    public CustomTableDemo() {
        setTitle("Demo Bảng Tùy Chỉnh");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Tạo bảng tùy chỉnh
        String[] columnNames = {"Mã Phòng", "Loại Phòng", "Giá", "Sức Chứa", "Trạng Thái", "Thao Tác", "Dịch Vụ"};
        customTable = new CustomTableButton(columnNames);

        // Chỉnh màu header
        customTable.setHeaderBackgroundColor(new Color(41, 128, 185));
        customTable.setHeaderForegroundColor(Color.WHITE);
        customTable.setHeaderHeight(40);

        // Chỉnh kích thước ScrollPane
        customTable.setScrollPaneSize(760, 300);

        // Chỉnh chiều cao hàng
        customTable.setRowHeight(40);

        // Thêm dữ liệu và nút vào bảng
        addSampleData();

        // Thiết lập listener cho sự kiện click nút
        customTable.setButtonClickListener(new CustomTableButton.ButtonClickListener() {
            @Override
            public void onButtonClick(ButtonType buttonType, int row, int column) {
                Object[] rowData = customTable.getTableModel().getRowData(row);
                String roomId = (String) rowData[0];

                switch (buttonType) {
                    case EDIT:
                        JOptionPane.showMessageDialog(CustomTableDemo.this,
                                "Đã nhấn nút Sửa cho phòng: " + roomId);
                        break;
                    case DELETE:
                        int confirm = JOptionPane.showConfirmDialog(CustomTableDemo.this,
                                "Bạn có chắc muốn xóa phòng " + roomId + "?",
                                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            customTable.getTableModel().removeRow(row);
                        }
                        break;
                    case VIEW:
                        JOptionPane.showMessageDialog(CustomTableDemo.this,
                                "Đã nhấn nút Xem cho phòng: " + roomId);
                        break;
                    case ADD:
                        JOptionPane.showMessageDialog(CustomTableDemo.this,
                                "Đã nhấn nút Thêm dịch vụ cho phòng: " + roomId);
                        showAddServiceDialog(roomId);
                        break;
                }
            }
        });

        // Tạo panel chứa các nút điều khiển
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Thêm Phòng Mới", new Color(46, 204, 113));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewRoom();
            }
        });

        JButton refreshButton = createStyledButton("Làm Mới Bảng", new Color(52, 152, 219));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customTable.clearTable();
                addSampleData();
            }
        });

        JButton themeButton = createStyledButton("Đổi Màu Header", new Color(155, 89, 182));
        themeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chọn màu ngẫu nhiên cho header
                Color randomColor = new Color(
                        (int)(Math.random() * 255),
                        (int)(Math.random() * 255),
                        (int)(Math.random() * 255)
                );
                customTable.setHeaderBackgroundColor(randomColor);
            }
        });

        controlPanel.add(addButton);
        controlPanel.add(refreshButton);
        controlPanel.add(themeButton);

        // Thêm các component vào panel chính
        JLabel titleLabel = new JLabel("QUẢN LÝ DANH SÁCH PHÒNG");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(customTable, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addSampleData() {
        // Thêm dữ liệu mẫu với các nút tương ứng
        Object[] row1 = {"P101", "Phòng Đơn", "500.000 VND", "2", "Trống", null, null};
        ButtonType[] buttons1 = {null, null, null, null, null, ButtonType.EDIT, ButtonType.ADD};
        customTable.addRow(row1, buttons1);

        Object[] row2 = {"P102", "Phòng Đôi", "800.000 VND", "4", "Đã đặt", null, null};
        ButtonType[] buttons2 = {null, null, null, null, null, ButtonType.VIEW, ButtonType.ADD};
        customTable.addRow(row2, buttons2);

        Object[] row3 = {"P103", "Phòng VIP", "1.200.000 VND", "2", "Trống", null, null};
        ButtonType[] buttons3 = {null, null, null, null, null, ButtonType.DELETE, ButtonType.ADD};
        customTable.addRow(row3, buttons3);

        Object[] row4 = {"P104", "Phòng Gia Đình", "1.500.000 VND", "6", "Bảo trì", null, null};
        ButtonType[] buttons4 = {null, null, null, null, null, ButtonType.EDIT, ButtonType.ADD};
        customTable.addRow(row4, buttons4);

        Object[] row5 = {"P105", "Phòng Đơn", "550.000 VND", "2", "Trống", null, null};
        ButtonType[] buttons5 = {null, null, null, null, null, ButtonType.EDIT, ButtonType.ADD};
        customTable.addRow(row5, buttons5);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thiết lập kích thước nút
        button.setPreferredSize(new Dimension(150, 35));

        return button;
    }

    private void addNewRoom() {
        // Tạo dialog để nhập thông tin phòng mới
        JDialog dialog = new JDialog(this, "Thêm Phòng Mới", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField roomIdField = new JTextField();
        JComboBox<String> roomTypeCombo = new JComboBox<>(
                new String[]{"Phòng Đơn", "Phòng Đôi", "Phòng VIP", "Phòng Gia Đình"});
        JTextField priceField = new JTextField();
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        JComboBox<String> statusCombo = new JComboBox<>(
                new String[]{"Trống", "Đã đặt", "Bảo trì"});

        formPanel.add(new JLabel("Mã Phòng:"));
        formPanel.add(roomIdField);
        formPanel.add(new JLabel("Loại Phòng:"));
        formPanel.add(roomTypeCombo);
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Sức Chứa:"));
        formPanel.add(capacitySpinner);
        formPanel.add(new JLabel("Trạng Thái:"));
        formPanel.add(statusCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createStyledButton("Lưu", new Color(46, 204, 113));
        JButton cancelButton = createStyledButton("Hủy", new Color(231, 76, 60));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (roomIdField.getText().isEmpty() || priceField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Vui lòng nhập đầy đủ thông tin",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Object[] newRow = {
                        roomIdField.getText(),
                        roomTypeCombo.getSelectedItem(),
                        priceField.getText() + " VND",
                        capacitySpinner.getValue().toString(),
                        statusCombo.getSelectedItem(),
                        null,
                        null
                };

                ButtonType[] buttons = {null, null, null, null, null, ButtonType.EDIT, ButtonType.ADD};
                customTable.addRow(newRow, buttons);
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(new JLabel("  Nhập thông tin phòng mới", JLabel.LEFT), BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showAddServiceDialog(String roomId) {
        // Tạo dialog để thêm dịch vụ cho phòng
        JDialog dialog = new JDialog(this, "Thêm Dịch Vụ Cho Phòng " + roomId, true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Tạo bảng dịch vụ
        String[] columnNames = {"Chọn", "Mã Dịch Vụ", "Tên Dịch Vụ", "Giá", "Số Lượng"};
        JPanel servicePanel = new JPanel(new BorderLayout());

        // Tạo model cho bảng dịch vụ
        DefaultTableModel serviceModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                } else if (columnIndex == 4) {
                    return Integer.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 4;
            }
        };

        JTable serviceTable = new JTable(serviceModel);
        serviceTable.setRowHeight(30);

        // Thêm dữ liệu mẫu cho bảng dịch vụ
        Object[] service1 = {false, "SV001", "Dịch vụ giặt là", "100.000 VND", 1};
        Object[] service2 = {false, "SV002", "Ăn sáng", "150.000 VND", 1};
        Object[] service3 = {false, "SV003", "Spa", "300.000 VND", 1};
        Object[] service4 = {false, "SV004", "Đưa đón sân bay", "250.000 VND", 1};

        serviceModel.addRow(service1);
        serviceModel.addRow(service2);
        serviceModel.addRow(service3);
        serviceModel.addRow(service4);

        // Tùy chỉnh cột số lượng với spinner
        TableColumn quantityColumn = serviceTable.getColumnModel().getColumn(4);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantityColumn.setCellEditor(new DefaultCellEditor(new JTextField()) {
            private JSpinner spinner;

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                spinner = new JSpinner(new SpinnerNumberModel(
                        value == null ? 1 : (Integer)value, 1, 10, 1));
                return spinner;
            }

            @Override
            public Object getCellEditorValue() {
                return spinner.getValue();
            }
        });

        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);
        serviceScrollPane.setBorder(BorderFactory.createEmptyBorder());

        servicePanel.add(serviceScrollPane, BorderLayout.CENTER);

        // Tạo panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = createStyledButton("Xác Nhận", new Color(46, 204, 113));
        JButton cancelButton = createStyledButton("Hủy", new Color(231, 76, 60));

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder selectedServices = new StringBuilder();

                for (int i = 0; i < serviceTable.getRowCount(); i++) {
                    Boolean isSelected = (Boolean) serviceTable.getValueAt(i, 0);
                    if (isSelected) {
                        String serviceName = (String) serviceTable.getValueAt(i, 2);
                        selectedServices.append(serviceName).append(", ");
                    }
                }

                if (selectedServices.length() > 0) {
                    selectedServices.setLength(selectedServices.length() - 2); // Xóa dấu ", " cuối cùng
                    JOptionPane.showMessageDialog(dialog,
                            "Các dịch vụ đã chọn cho phòng " + roomId + ":\n" + selectedServices,
                            "Xác nhận", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Không có dịch vụ nào được chọn.",
                            "Thông báo", JOptionPane.WARNING_MESSAGE);
                }

                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        dialog.add(servicePanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new CustomTableDemo();
    }
}

